import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// bridgIt game
class BridgIt extends World {
    static final Color neutralCol = Color.WHITE;
    static final Color pVertCol = Color.MAGENTA;
    static final Color pHorCol = Color.PINK;

    int boardSize;
    WorldScene bg;
    ArrayList<ArrayList<Cell>> cells;
    boolean gameOver;
    boolean p1Turn;
    boolean p2Turn;

    BridgIt(int boardSize) {
        this.boardSize = new Utils().verify(boardSize);
        this.bg = new WorldScene(boardSize * Cell.SIZE, boardSize * Cell.SIZE);
        this.cells = new ArrayList<ArrayList<Cell>>();
        this.gameOver = false;
        this.p1Turn = true;
        this.p2Turn = false;
        init();
    }

    // Convenience constructor
    BridgIt(int boardSize, WorldScene bg, ArrayList<ArrayList<Cell>> cells,
            boolean gameOver, boolean p1Turn, boolean p2Turn) {
        this.boardSize = boardSize;
        this.bg = bg;
        this.cells = cells;
        this.gameOver = gameOver;
        this.p1Turn = p1Turn;
        this.p2Turn = p2Turn;
    }

    /* TMPLT
     * Fields:
     *
     *   neutralCells  - Color.WHITE
     *   pVertCells       - Color.BLACK
     *   pHorCells       - Color.GRAY
     *
     *   boardSize     - int
     *   bg            - WorldScene
     *   cells         - ArrayList<Cell>
     *   gameOver      - boolean
     *
     * Methods:
     *
     *   makeScene()     - WorldScene
     *   init()          - void
     */

    // initial board set up
    // EFFECTS:
    //   1. creates board (cells and cell colors)
    //   2. links cell neighbors
    void init() {
        // adds cells
        for (int row = 0; row < boardSize; row ++) {
            cells.add(new ArrayList<Cell>());
            for (int col = 0; col < boardSize; col ++) {
                if (row % 2 == 0 && col % 2 == 1) {
                    // horizontal player's cells
                    cells.get(row).add(new Cell(row * Cell.SIZE + (Cell.SIZE / 2),
                            col * Cell.SIZE + (Cell.SIZE / 2),
                            pHorCol));
                }
                else if (row % 2 == 1 && col % 2 == 0) {
                    // vertical player's cells
                    cells.get(row).add(new Cell(row * Cell.SIZE + (Cell.SIZE / 2),
                            col * Cell.SIZE + (Cell.SIZE / 2),
                            pVertCol));
                }
                else {
                    // neutral white cells
                    cells.get(row).add(new Cell(row * Cell.SIZE + (Cell.SIZE / 2),
                            col * Cell.SIZE + (Cell.SIZE / 2),
                            neutralCol));
                }
            }
        }

        // updates cell links
        for (int row = 0; row < boardSize; row ++) {
            for (int col = 0; col < boardSize; col ++) {
                if (row > 0) {
                    cells.get(row).get(col).above = cells.get(row - 1).get(col);
                }
                if (row < boardSize - 1) {
                    cells.get(row).get(col).below = cells.get(row + 1).get(col);
                }
                if (col > 0) {
                    cells.get(row).get(col).left = cells.get(row).get(col - 1);
                }
                if (col < boardSize - 1) {
                    cells.get(row).get(col).right = cells.get(row).get(col + 1);
                }
            }
        }
    }

    // makes a scene
    public WorldScene makeScene() {
        for (int row = 0; row < boardSize; row ++) {
            for (int col = 0; col < boardSize; col ++) {
                cells.get(row).get(col).renderCell(bg);
            }
        }
        return bg;
    }

}


// one cell in the game
class Cell {
    static final int SIZE = 20;
    int x; // middle x coord
    int y; // middle y coord
    Color color;

    // neighbors
    Cell above;
    Cell below;
    Cell left;
    Cell right;

    Cell(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.above = null;
        this.below = null;
        this.left = null;
        this.right = null;
    }

    // convenience constructor
    Cell(int x, int y, Color color, Cell above, Cell below, Cell left, Cell right) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.above = above;
        this.below = below;
        this.left = left;
        this.right = right;
    }

    /* TMPLT
     * Fields:
     *   this.SIZE       - 20
     *   this.x          - int
     *   this.y          - int
     *   this.color      - Color
     *   this.above      - Cell
     *   this.below      - Cell
     *   this.left       - Cell
     *   this.right      - Cell
     *
     * Methods:
     *   this.renderCell(WorldScene)   - void
     */

    // EFFECT: renders this cell on given background
    void renderCell(WorldScene bg) {
        RectangleImage cell = new RectangleImage(SIZE, SIZE, "solid", color);
        bg.placeImageXY(cell, x, y);
    }
}

// utilities
class Utils {
    // verify size is greater than or equal to 3
    int verify(int size) {
        if (size >= 3 && size % 2 == 1) {
            return size;
        }
        else {
            throw new IllegalArgumentException("Size must be greater than or equal to 3 and odd");
        }
    }
}

// examples and tests
class ExamplesBridgIt {
    Cell cell;
    ArrayList<ArrayList<Cell>> noCells;

    BridgIt smallGame3;
    BridgIt smallGame5;

    WorldScene bg;

    // inits examples
    void initEx() {
        cell = new Cell(10, 10, Color.WHITE);
        noCells = new ArrayList<ArrayList<Cell>>();
        bg = new WorldScene(60, 60);
        smallGame3 = new BridgIt(3, bg, noCells, false, true, false);
        smallGame5 = new BridgIt(5);
    }

    // test big bang
    void testBigBang(Tester t) {
        initEx();
        int boardSize = 11;
        BridgIt game = new BridgIt(boardSize);
        int w = boardSize * Cell.SIZE;
        int h = boardSize * Cell.SIZE;
        double speed = 1;

        game.bigBang(w, h, speed);
        return;
    }

    // tests constructor
    void testBridgItConstructorException(Tester t) {
        initEx();
        IllegalArgumentException e = new IllegalArgumentException("Size "
                + "must be greater than or equal to 3 and odd");
        t.checkConstructorException(e, "BridgIt", 2);
        t.checkConstructorException(e, "BridgIt", 1);
        return;
    }

    // tests init
    void testInit(Tester t) {
        initEx();
        smallGame3.init();

        // check cell colors
        t.checkExpect(smallGame3.cells.get(0).get(0).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(0).get(1).color, Color.PINK);
        t.checkExpect(smallGame3.cells.get(0).get(2).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(1).get(0).color, Color.MAGENTA);
        t.checkExpect(smallGame3.cells.get(1).get(1).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(1).get(2).color, Color.MAGENTA);
        t.checkExpect(smallGame3.cells.get(2).get(0).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(2).get(1).color, Color.PINK);
        t.checkExpect(smallGame3.cells.get(2).get(2).color, Color.WHITE);

        // check links
        //top left corner
        t.checkExpect(smallGame3.cells.get(0).get(0).above, null);
        t.checkExpect(smallGame3.cells.get(0).get(0).below, smallGame3.cells.get(1).get(0));
        t.checkExpect(smallGame3.cells.get(0).get(0).left, null);
        t.checkExpect(smallGame3.cells.get(0).get(0).right, smallGame3.cells.get(0).get(1));

        // top right corner
        t.checkExpect(smallGame3.cells.get(0).get(2).above, null);
        t.checkExpect(smallGame3.cells.get(0).get(2).below, smallGame3.cells.get(1).get(2));
        t.checkExpect(smallGame3.cells.get(0).get(2).left, smallGame3.cells.get(0).get(1));
        t.checkExpect(smallGame3.cells.get(0).get(2).right, null);

        // bottom left corner
        t.checkExpect(smallGame3.cells.get(2).get(0).above, smallGame3.cells.get(1).get(0));
        t.checkExpect(smallGame3.cells.get(2).get(0).below, null);
        t.checkExpect(smallGame3.cells.get(2).get(0).left, null);
        t.checkExpect(smallGame3.cells.get(2).get(0).right, smallGame3.cells.get(2).get(1));

        // bottom right corner
        t.checkExpect(smallGame3.cells.get(2).get(2).above, smallGame3.cells.get(1).get(2));
        t.checkExpect(smallGame3.cells.get(2).get(2).below, null);
        t.checkExpect(smallGame3.cells.get(2).get(2).left, smallGame3.cells.get(2).get(1));
        t.checkExpect(smallGame3.cells.get(2).get(2).right, null);

        // middle
        t.checkExpect(smallGame3.cells.get(1).get(1).above, smallGame3.cells.get(0).get(1));
        t.checkExpect(smallGame3.cells.get(1).get(1).below, smallGame3.cells.get(2).get(1));
        t.checkExpect(smallGame3.cells.get(1).get(1).left, smallGame3.cells.get(1).get(0));
        t.checkExpect(smallGame3.cells.get(1).get(1).right, smallGame3.cells.get(1).get(2));

    }

    // tests makeScene
    void testMakeScene(Tester t) {
        initEx();
        WorldScene ex = new WorldScene(100, 100);
        for (int row = 0; row < smallGame5.boardSize; row ++) {
            for (int col = 0; col < smallGame5.boardSize; col ++) {
                smallGame5.cells.get(row).get(col).renderCell(ex);
            }
        }

        smallGame5.makeScene();

        t.checkExpect(smallGame5.bg, ex);
        return;
    }

    // tests renderCell
    void testRenderCell(Tester t) {
        initEx();
        WorldScene bg = new WorldScene(100, 100);
        WorldScene expect = new WorldScene(100, 100);
        expect.placeImageXY(new RectangleImage(Cell.SIZE,
                        Cell.SIZE,
                        "solid",
                        cell.color),
                cell.x, cell.y);

        cell.renderCell(bg);
        t.checkExpect(bg, expect);
    }

    // tests utils
    void testUtil(Tester t) {
        initEx();
        Utils util = new Utils();
        IllegalArgumentException e = new IllegalArgumentException("Size "
                + "must be greater than or equal to 3 and odd");
        t.checkException(e, util, "verify", 1);
        t.checkException(e, util, "verify", 2);
        t.checkException(e, util, "verify", 4);
        t.checkExpect(util.verify(3), 3);
        t.checkExpect(util.verify(5), 5);
    }
}