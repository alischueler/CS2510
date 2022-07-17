import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// bridge it game v2
class BridgIt2 extends World {
    static final Color neutralCol = Color.WHITE;
    static final Color p1Col = Color.MAGENTA;
    static final Color p2Col = Color.PINK;

    int boardSize;
    WorldScene bg;
    ArrayList<ArrayList<ACell>> cells;
    ArrayList<ACell> p1edges;
    ArrayList<ACell> p2edges;
    boolean gameOver;
    Color winner;  // null until someone wins
    boolean p1Turn;
    boolean p2Turn;

    BridgIt2(int boardSize) {
        this.boardSize = new Utils().verify(boardSize);
        this.bg = new WorldScene(boardSize * ACell.SIZE, boardSize * ACell.SIZE);
        this.cells = new ArrayList<ArrayList<ACell>>();
        this.p1edges = new ArrayList<ACell>();
        this.p2edges = new ArrayList<ACell>();
        this.gameOver = false;
        this.p1Turn = true;
        this.p2Turn = false;
        init();
    }

    // Convenience constructor
    BridgIt2(int boardSize, WorldScene bg, ArrayList<ArrayList<ACell>> cells,
             ArrayList<ACell> p1edges, ArrayList<ACell> p2edges,
             boolean gameOver, boolean p1Turn, boolean p2Turn) {
        this.boardSize = boardSize;
        this.bg = bg;
        this.cells = cells;
        this.p1edges = p1edges;
        this.p2edges = p2edges;
        this.gameOver = gameOver;
        this.p1Turn = p1Turn;
        this.p2Turn = p2Turn;
    }

    /* TMPLT
     * Fields:
     *
     *   neutralCol  - Color.WHITE
     *   p1Col       - Color.MAGENTA
     *   p2Col       - Color.PINK
     *
     *   boardSize     - int
     *   bg            - WorldScene
     *   cells         - ArrayList<ArrayList<Cell>>
     *   p1edges       - ArrayList<Cell>
     *   p2edges       - ArrayList<Cell>
     *   gameOver      - boolean
     *   winner        - Color
     *   p1Turn        - boolean
     *   p2Turn        - boolean
     *
     * Methods:
     *
     *   makeScene()          - WorldScene
     *   init()               - void
     *   onTick()             - void
     *   onMouseClicked(Posn) - void
     *   endGame()            - void
     */

    // initial board set up
    // EFFECTS:
    //   1. creates board (cells and cell colors)
    //   2. links cell neighbors
    //   3. adds edge cells to worklist

    void init() {
        // adds cells

        // blank board
        for (int i = 0; i < boardSize; i ++) {
            cells.add(new ArrayList<ACell>());
        }

        // adds left and top edge
        // top row
        for (int col = 0; col < boardSize; col++) {
            if (col % 2 == 0) {
                Edge ne = new Edge(0 * ACell.SIZE + (ACell.SIZE / 2),
                        col * ACell.SIZE + (ACell.SIZE / 2),
                        neutralCol);
                cells.get(0).add(ne);
            }
            else {
                Edge pe = new Edge(0 * ACell.SIZE + (ACell.SIZE / 2),
                        col * ACell.SIZE + (ACell.SIZE / 2),
                        p1Col);
                cells.get(0).add(pe);
            }
        }

        // left column
        for (int row = 1; row < boardSize - 1; row ++) {
            if (row % 2 == 0) {
                Edge ne = new Edge(row * ACell.SIZE + (ACell.SIZE / 2),
                        0 * ACell.SIZE + (ACell.SIZE / 2),
                        neutralCol);
                cells.get(row).add(ne);
            }
            else {
                Edge pe = new Edge(row * ACell.SIZE + (ACell.SIZE / 2),
                        0 * ACell.SIZE + (ACell.SIZE / 2),
                        p2Col);
                cells.get(row).add(pe);
            }
        }


        // middle of board
        for (int row = 1; row < boardSize - 1; row ++) {
            for (int col = 1; col < boardSize - 1; col ++) {
                if (row % 2 == 0 && col % 2 == 1) {
                    // horizontal player's cells
                    cells.get(row).add(new Mid(row * ACell.SIZE + (ACell.SIZE / 2),
                            col * ACell.SIZE + (ACell.SIZE / 2),
                            p1Col));
                }
                else if (row % 2 == 1 && col % 2 == 0) {
                    // vertical player's cells
                    cells.get(row).add(new Mid(row * ACell.SIZE + (ACell.SIZE / 2),
                            col * ACell.SIZE + (ACell.SIZE / 2),
                            p2Col));
                }
                else {
                    // neutral white cells
                    cells.get(row).add(new Mid(row * ACell.SIZE + (ACell.SIZE / 2),
                            col * ACell.SIZE + (ACell.SIZE / 2),
                            neutralCol));
                }
            }
        }
        // adds right and bottom edge
        // bottom row
        for (int col = 0; col < boardSize; col++) {
            if (col % 2 == 0) {
                Edge ne = new Edge((boardSize - 1) * ACell.SIZE + (ACell.SIZE / 2),
                        col * ACell.SIZE + (ACell.SIZE / 2),
                        neutralCol);
                cells.get(boardSize - 1).add(ne);
            }
            else {
                Edge pe = new Edge((boardSize - 1) * ACell.SIZE + (ACell.SIZE / 2),
                        col * ACell.SIZE + (ACell.SIZE / 2),
                        p1Col);
                cells.get(boardSize - 1).add(pe);
            }
        }

        // right column
        for (int row = 1; row < boardSize - 1; row ++) {
            if (row % 2 == 0) {
                Edge ne = new Edge(row * ACell.SIZE + (ACell.SIZE / 2),
                        (boardSize - 1) * ACell.SIZE + (ACell.SIZE / 2),
                        neutralCol);
                cells.get(row).add(ne);
            }
            else {
                Edge pe = new Edge(row * ACell.SIZE + (ACell.SIZE / 2),
                        (boardSize - 1) * ACell.SIZE + (ACell.SIZE / 2),
                        p2Col);
                cells.get(row).add(pe);
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

        // adds edges to list of edges
        for (int i = 0; i < boardSize; i ++) {
            if (i % 2 == 1) {
                p1edges.add(cells.get(0).get(i));
                p1edges.add(cells.get(boardSize - 1).get(i));
                p2edges.add(cells.get(i).get(0));
                p2edges.add(cells.get(i).get(boardSize - 1));
            }
        }

    }

    // draws the scene
    public WorldScene makeScene() {
        if (this.gameOver) {
            int placement = boardSize * ACell.SIZE / 2;
            String winnerMsg;
            if (winner == p1Col) {
                winnerMsg = "Player 1 Wins!";
            }
            else {
                winnerMsg = "Player 2 Wins!";
            }
            bg.placeImageXY(new TextImage(winnerMsg, ACell.SIZE, Color.BLACK), placement, placement);
        }

        else {
            for (int row = 0; row < boardSize; row ++) {
                for (int col = 0; col < boardSize; col ++) {
                    cells.get(row).get(col).renderCell(bg);
                }
            }
        }
        return bg;
    }

    // handles on tick, defaults to endGame
    // EFFECT: 1. modifies who has one the game and if a path exists from edge to edge
    //         2. modifies who's turn it is
    public void onTick() {
        for (ACell p1e : p1edges) {
            if (p1e.hasPathToEdge(p1Col, winner, this.boardSize)) {
                winner = p1Col;
                this.gameOver = true;
                this.p2Turn = false;
            }
        }

        for (ACell p2e : p2edges) {
            if (p2e.hasPathToEdge(p2Col, winner, this.boardSize)) {
                winner = p2Col;
                this.gameOver = true;
                this.p1Turn = false;
            }
        }
    }


    // handles mouse presses
    // EFFECT: 1. Changes the cell clicked to the appropriate player's color
    //         2. Alternates player's turns
    //         3. checks if game is over
    public void onMouseClicked(Posn p) {

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col ++) {
                ACell cell = cells.get(row).get(col);
                if (cell.contains(p) && cell.color == Color.WHITE) {
                    if (p1Turn) {
                        cell.claim(p1Col);
                    }
                    if (p2Turn) {
                        cell.claim(p2Col);
                    }
                    p1Turn = !p1Turn;
                    p2Turn = !p2Turn;
                }
            }
        }

    }
}


// represents a cell in the board
abstract class ACell {
    static final int SIZE = 50;
    int x; // middle x coord
    int y; // middle y coord
    Color color;
    Deque<ACell> workList;

    // neighbors
    ACell above;
    ACell below;
    ACell left;
    ACell right;

    ACell(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.above = null;
        this.below = null;
        this.left = null;
        this.right = null;
        this.workList = new ArrayDeque<ACell>();
    }

    // convenience constructor
    ACell(int x, int y, Color color, ACell above, ACell below, ACell left, ACell right) {
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
     *   ACell.SIZE       - 20
     *   this.x          - int
     *   this.y          - int
     *   this.color      - Color
     *   this.workList        - Deque<ACell>
     *   this.above      - Cell
     *   this.below      - Cell
     *   this.left       - Cell
     *   this.right      - Cell
     *
     * Methods:
     *   this.renderCell(WorldScene)   - void
     *   this.contains(Posn)           - boolean
     *   this.claim(Color)             - void
     *   this.checkBoard(Deque<ACell>) - void
     *   this.hasPathToEdge(Color, Color, int)   - boolean
     */


    // EFFECT: renders this cell on given background
    void renderCell(WorldScene bg) {
        RectangleImage cell = new RectangleImage(SIZE, SIZE, "solid", color);
        bg.placeImageXY(cell, x, y);
    }

    // does this cell contain the posn
    boolean contains(Posn p) {
        return p.x > this.x - (SIZE / 2) && p.x <= this.x + (SIZE / 2)
                && p.y > this.y - (SIZE / 2) && p.y <= this.y + (SIZE / 2);
    }


    // EFFECT: claims this cell as the players, and updates change color to given
    abstract void claim(Color p);

    // EFFECT: checks if there is a path that exists from edge to edge
    //EFFECT: updates the workList with all cells in the board, connected to this one that are
    //the same color
    void checkBoard(Deque<ACell> workList) {

        if (this.above != null && this.color == this.above.color && !workList.contains(this.above)) {
            this.workList.addFirst(this.above);
            this.above.checkBoard(workList);
        }
        if (this.below != null && this.color == this.below.color && !workList.contains(this.below)) {
            workList.addFirst(this.below);
            this.below.checkBoard(workList);
        }
        if (this.left != null && this.color == this.left.color && !workList.contains(this.left)) {
            workList.addFirst(this.left);
            this.left.checkBoard(workList);
        }
        if (this.right != null && this.color == this.right.color && !workList.contains(this.right)) {
            workList.addFirst(this.right);
            this.right.checkBoard(workList);
        }
    }


    // is the path of this color starts at one end and ends at the other
    //end of the board
    abstract boolean hasPathToEdge(Color player, Color winner, int boardSize);

}


// cell in middle of board
class Mid extends ACell {

    // constructor
    Mid(int x, int y, Color color) {
        super(x, y, color);
    }

    // convenience constructor
    Mid(int x, int y, Color color, ACell above, ACell below, ACell left, ACell right) {
        super(x, y, color, above, below, left, right);
    }

    /* TMPLT
     * Fields:
     *
     * Methods:
     *   this.claim(Color)                       - void
     *   this.hasPathToEdge(Color, Color, int)   - boolean
     *
     * @see ACell
     */

    //EFFECT: claims this middle cell as the players, and updates change color to given
    void claim(Color p) {
        this.color = p;
    }

    // determines if the path of this color starts at one end and ends at the other
    //end of the board
    boolean hasPathToEdge(Color player, Color winner, int boardSize) {
        return false;
    }
}

// cell on edge of board
class Edge extends ACell {

    // constructor
    Edge(int x, int y, Color color) {
        super(x, y, color);
    }

    //convenience constructor
    Edge(int x, int y, Color color, ACell above, ACell below, ACell left, ACell right) {
        super(x, y, color, above, below, left, right);
    }

    /* TMPLT
     * Fields:
     *
     * Methods:
     *   this.claim(Color)                       - void
     *   this.hasPathToEdge(Color, Color, int)   - boolean
     *
     * @see ACell
     */

    // EFFECT: does nothing to this edge cell because the player either already owns or
    //cannot claim edge piece
    void claim(Color p) {
        return;
    }

    // does this edge have a path to another edge
    boolean hasPathToEdge(Color player, Color winner, int boardSize) {
        Deque<ACell> workList = new ArrayDeque<ACell>();
        if (player == BridgIt2.p1Col) {
            this.checkBoard(workList);
            boolean left = false;
            boolean right = false;
            for (ACell cell : workList) {
                if (cell.x == ACell.SIZE + (ACell.SIZE / 2)) {
                    left = true;
                }
                if (cell.x == (boardSize - 1) * ACell.SIZE + (ACell.SIZE / 2)) {
                    right = true;
                }
            }
            winner = player;
            return left && right;
        }
        else if (player == BridgIt2.p2Col) {
            this.checkBoard(workList);
            boolean top = false;
            boolean bottom = false;
            for (ACell cell : workList) {
                if (cell.y == ACell.SIZE + (ACell.SIZE / 2)) {
                    top = true;
                }
                if (cell.y == (boardSize - 1) * ACell.SIZE + (ACell.SIZE / 2)) {
                    bottom = true;
                }
            }
            winner = player;
            return top && bottom;
        }
        else {
            return false;
        }
    }
}

//utilities
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
class ExamplesBridgIt2 {
    ACell mid;
    ACell edge;
    ArrayList<ArrayList<ACell>> noCells;
    ArrayList<ACell> noEdges1;
    ArrayList<ACell> noEdges2;
    BridgIt2 smallGame3;
    BridgIt2 smallGame5;

    WorldScene bg;

    // inits examples
    void initEx() {
        mid = new Mid(10, 10, Color.WHITE);
        edge = new Edge(10, 10, Color.MAGENTA);
        noCells = new ArrayList<ArrayList<ACell>>();
        noEdges1 = new ArrayList<ACell>();
        noEdges2 = new ArrayList<ACell>();
        bg = new WorldScene(60, 60);
        smallGame3 = new BridgIt2(3, bg, noCells, noEdges1, noEdges2, false, true, false);
        smallGame5 = new BridgIt2(5);
    }

    // test big bang
    void testBigBang(Tester t) {
        initEx();
        int boardSize = 11;
        BridgIt2 game = new BridgIt2(boardSize);
        int w = boardSize * ACell.SIZE;
        int h = boardSize * ACell.SIZE;
        double speed = 1;

        game.bigBang(w, h, speed);
    }


    // tests constructors
    void testBridgItConstructorException(Tester t) {
        initEx();
        IllegalArgumentException e = new IllegalArgumentException("Size "
                + "must be greater than or equal to 3 and odd");
        t.checkConstructorException(e, "BridgIt2", 2);
        t.checkConstructorException(e, "BridgIt2", 1);
        ArrayList<ArrayList<ACell>> expectedBoard = new ArrayList<ArrayList<ACell>>();
        expectedBoard.add(new ArrayList<ACell>());
        expectedBoard.add(new ArrayList<ACell>());
        expectedBoard.add(new ArrayList<ACell>());

        expectedBoard.get(0).add(new Edge(ACell.SIZE / 2, ACell.SIZE / 2, Color.WHITE));
        expectedBoard.get(0).add(new Mid(ACell.SIZE / 2 + ACell.SIZE,
                ACell.SIZE / 2, Color.MAGENTA));
        expectedBoard.get(0).add(new Edge(ACell.SIZE / 2 + (ACell.SIZE * 2),
                ACell.SIZE / 2, Color.WHITE));
        expectedBoard.get(1).add(new Edge(ACell.SIZE / 2, ACell.SIZE / 2 +
                ACell.SIZE, Color.PINK));
        expectedBoard.get(1).add(new Mid(ACell.SIZE / 2 + ACell.SIZE,
                ACell.SIZE / 2 + ACell.SIZE, Color.WHITE));
        expectedBoard.get(1).add(new Edge(ACell.SIZE / 2 + (ACell.SIZE * 2),
                ACell.SIZE / 2 + ACell.SIZE, Color.PINK));
        expectedBoard.get(2).add(new Edge(ACell.SIZE / 2, ACell.SIZE / 2 + (2 * ACell.SIZE),
                Color.WHITE));
        expectedBoard.get(2).add(new Mid(ACell.SIZE / 2 + ACell.SIZE,
                ACell.SIZE / 2 + ACell.SIZE, Color.MAGENTA));
        expectedBoard.get(2).add(new Edge(ACell.SIZE / 2 + (2 * ACell.SIZE),
                ACell.SIZE / 2 + (2 * ACell.SIZE), Color.WHITE));
    }


    // tests init
    void testInit(Tester t) {
        initEx();
        smallGame3.init();

        // check cell colors
        t.checkExpect(smallGame3.cells.get(0).get(0).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(0).get(1).color, Color.MAGENTA);
        t.checkExpect(smallGame3.cells.get(0).get(2).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(1).get(0).color, Color.PINK);
        t.checkExpect(smallGame3.cells.get(1).get(1).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(1).get(2).color, Color.PINK);
        t.checkExpect(smallGame3.cells.get(2).get(0).color, Color.WHITE);
        t.checkExpect(smallGame3.cells.get(2).get(1).color, Color.MAGENTA);
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
        WorldScene ex = new WorldScene(250, 250);
        for (int row = 0; row < smallGame5.boardSize; row ++) {
            for (int col = 0; col < smallGame5.boardSize; col ++) {
                ex.placeImageXY(new RectangleImage(ACell.SIZE, ACell.SIZE,
                                "solid",
                                smallGame5.cells.get(row).get(col).color),
                        smallGame5.cells.get(row).get(col).x,
                        smallGame5.cells.get(row).get(col).y);
            }
        }

        smallGame5.makeScene();

        t.checkExpect(smallGame5.bg, ex);
    }

    // tests onTick
    void testOnTick(Tester t) {
        initEx();
        t.checkExpect(smallGame5.gameOver, false);

        t.checkExpect(smallGame5.cells.get(0).get(1).color, Color.MAGENTA); // cell 1 (edge)

        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.WHITE); // cell 2 (mid)
        smallGame5.cells.get(1).get(1).color = Color.MAGENTA;
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.MAGENTA);

        t.checkExpect(smallGame5.cells.get(2).get(1).color, Color.MAGENTA); // cell 3 (mid, autoset)

        t.checkExpect(smallGame5.cells.get(3).get(1).color, Color.WHITE); // cell 4
        smallGame5.cells.get(3).get(1).color = Color.MAGENTA;
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.MAGENTA);

        t.checkExpect(smallGame5.cells.get(4).get(1).color, Color.MAGENTA); // cell 5 edge

        smallGame5.onTick();
        t.checkExpect(smallGame5.gameOver, true);
        t.checkExpect(smallGame5.p2Turn, false);
    }

    // test onMouseClick
    void testOnMouseClicked(Tester t) {
        this.initEx();
        t.checkExpect(this.smallGame5.p1Turn, true);
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.WHITE);
        this.smallGame5.onMouseClicked(new Posn(125, 75));
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.WHITE);
        this.smallGame5.onMouseClicked(new Posn(75, 75));
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.MAGENTA);
        t.checkExpect(this.smallGame5.p1Turn, false);
        t.checkExpect(this.smallGame5.p2Turn, true);
        this.smallGame5.onMouseClicked(new Posn(75, 75));
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.MAGENTA);
        t.checkExpect(smallGame5.cells.get(3).get(1).color, Color.white);
        this.smallGame5.onMouseClicked(new Posn(175, 75));
        t.checkExpect(smallGame5.cells.get(3).get(1).color, Color.PINK);
        t.checkExpect(this.smallGame5.p1Turn, true);
        t.checkExpect(this.smallGame5.p2Turn, false);
        t.checkExpect(smallGame5.cells.get(4).get(2).color, Color.white);
        this.smallGame5.onMouseClicked(new Posn(225, 125));
        t.checkExpect(smallGame5.cells.get(4).get(2).color, Color.white);
        t.checkExpect(this.smallGame5.p1Turn, false);
        t.checkExpect(this.smallGame5.p2Turn, true);
        t.checkExpect(smallGame5.cells.get(0).get(1).color, Color.MAGENTA);
        this.smallGame5.onMouseClicked(new Posn(25, 75));
        t.checkExpect(smallGame5.cells.get(0).get(1).color, Color.MAGENTA);
        t.checkExpect(this.smallGame5.p1Turn, false);
        t.checkExpect(this.smallGame5.p2Turn, true);
        t.checkExpect(smallGame5.cells.get(1).get(0).color, Color.PINK);
        this.smallGame5.onMouseClicked(new Posn(75, 25));
        t.checkExpect(smallGame5.cells.get(1).get(0).color, Color.PINK);
        t.checkExpect(this.smallGame5.p1Turn, false);
        t.checkExpect(this.smallGame5.p2Turn, true);
    }

    // tests renderCell
    void testRenderCell(Tester t) {
        initEx();
        WorldScene bg = new WorldScene(100, 100);
        WorldScene expect = new WorldScene(100, 100);
        expect.placeImageXY(new RectangleImage(ACell.SIZE,
                        ACell.SIZE,
                        "solid",
                        mid.color),
                mid.x, mid.y);

        mid.renderCell(bg);
        t.checkExpect(bg, expect);
    }


    //tests checkBoard()
    void testCheckBoard(Tester t) {
        this.initEx();
        Deque<ACell> test = new ArrayDeque<ACell>();
        Deque<ACell> test2 = new ArrayDeque<ACell>();

        this.edge.checkBoard(test);
        t.checkExpect(test, test2);

        this.mid.checkBoard(test);
        t.checkExpect(test, test2);

        this.smallGame5.cells.get(0).get(1).checkBoard(test);
        t.checkExpect(test, test2);

        t.checkExpect(smallGame5.cells.get(0).get(1).color, Color.MAGENTA);
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.WHITE);

        this.smallGame5.onMouseClicked(new Posn(75, 75));
        this.smallGame5.cells.get(0).get(1).checkBoard(test);
        test2.addFirst(smallGame5.cells.get(1).get(1));
        test2.addFirst(smallGame5.cells.get(2).get(1));
        t.checkExpect(test, test2);
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.MAGENTA);
        t.checkExpect(smallGame5.cells.get(2).get(1).color, Color.MAGENTA);
    }


    // tests contains
    void testContains(Tester t) {
        initEx();
        t.checkExpect(mid.contains(new Posn(10, 10)), true); // middle of cell
        t.checkExpect(edge.contains(new Posn(10, 10)), true);
        t.checkExpect(mid.contains(new Posn(-15, -15)), false); // bottom left corner out of range
        t.checkExpect(edge.contains(new Posn(35, 35)), true); // top right
        t.checkExpect(mid.contains(new Posn(-14, -15)), false); // in range bottom
    }

    // tests claim
    void testClaim(Tester t) {
        initEx();
        t.checkExpect(edge.color, Color.MAGENTA);
        edge.claim(Color.BLACK);
        t.checkExpect(edge.color, Color.MAGENTA); // do nothing if trying to claim edge

        t.checkExpect(mid.color, Color.WHITE);
        mid.claim(Color.BLUE);
        t.checkExpect(mid.color, Color.BLUE);
    }

    // tests hasPathToEdge
    void testHasPathToEdge(Tester t) {
        initEx();
        t.checkExpect(mid.hasPathToEdge(Color.PINK, Color.WHITE, 5), false); // all mids false

        t.checkExpect(smallGame5.cells.get(0).get(1).hasPathToEdge(Color.MAGENTA, Color.WHITE, 5),
                false);
        // creates path to edge for p2
        t.checkExpect(smallGame5.gameOver, false);

        t.checkExpect(smallGame5.cells.get(0).get(1).color, Color.MAGENTA); // cell 1 (edge)

        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.WHITE); // cell 2 (mid)
        smallGame5.cells.get(1).get(1).color = Color.MAGENTA;
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.MAGENTA);

        t.checkExpect(smallGame5.cells.get(2).get(1).color, Color.MAGENTA); // cell 3 (mid, autoset)

        t.checkExpect(smallGame5.cells.get(3).get(1).color, Color.WHITE); // cell 4
        smallGame5.cells.get(3).get(1).color = Color.MAGENTA;
        t.checkExpect(smallGame5.cells.get(1).get(1).color, Color.MAGENTA);

        t.checkExpect(smallGame5.cells.get(4).get(1).color, Color.MAGENTA); // cell 5 edge

        // checks if there is a path
        t.checkExpect(smallGame5.cells.get(0).get(1).hasPathToEdge(Color.MAGENTA, Color.WHITE, 5),
                true);
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
