import java.util.ArrayList;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;


// represents a single square of the game area

class Cell {
    static int CELL_SIZE = 20;
    // coords of the cell, origin is top left of the screen
    int x;
    int y;
    Color color;
    boolean flooded;

    // the four cells adjacent to this one
    Cell left;
    Cell top;
    Cell right;
    Cell bottom;

    // start game constructor
    Cell(int x, int y, Color color) {
        this(x, y, color, false, null, null, null, null);
    }

    // convenience constructor
    Cell(int x, int y, Color color, boolean flooded,
         Cell left, Cell top, Cell right, Cell bottom) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.flooded = flooded;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /* TMPLT
     *
     * Fields:
     *   Cell.CELL_SIZE  20
     *   this.x        - int
     *   this.y        - int
     *   this.color    - Color
     *   this.flooded  - boolean
     *   this.left     - Cell
     *   this.right    - Cell
     *   this.top      - Cell
     *   this.bottom   - Cell
     *
     * Methods:
     *   this.renderCell(WorldScene)                   void
     *   this.updateCellLinks(Cell, Cell, Cell, Cell)  void
     */

    // updates scene
    // EFFECT: draws this cell onto the given WorldScene
    void renderCell(WorldScene scene) {
        RectangleImage cell = new RectangleImage(CELL_SIZE, CELL_SIZE, "solid", this.color);
        scene.placeImageXY(cell, this.x, this.y);
    }

    //EFFECT: updates links of this cell
    void updateCellLinks(Cell left, Cell top, Cell right, Cell bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}

// flood it game
class FloodItWorld extends World {
    static int BOARD_SIZE = 22; // size of the board

    ArrayList<Cell> board;      // all cells in board
    ArrayList<Color> colors;    // all possible colors
    WorldScene scene;

    int numAllowedClicks;
    int numClicks;
    boolean lost;
    Color colorClicked;
    boolean acceptClicks;

    Random rand;

    // constructor for game
    FloodItWorld() {
        this.board = new ArrayList<Cell>();

        this.colors = new ArrayList<Color>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.ORANGE);
        colors.add(Color.MAGENTA);

        this.scene = new WorldScene(FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE,
                FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE);
        this.numAllowedClicks = 39;   // will change for later implementation
        this.numClicks = 0;
        this.lost = false;
        this.colorClicked = null;
        this.acceptClicks = true;
        this.rand = new Random();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Color color = colors.get(rand.nextInt(colors.size()));
                Cell temp = new Cell(j * Cell.CELL_SIZE + (Cell.CELL_SIZE / 2),
                        i * Cell.CELL_SIZE + (Cell.CELL_SIZE / 2), color, false, null, null, null, null);
                board.add(temp);
            }
        }

        this.updateLinks();
    }

    // Convenience constructor for tests
    FloodItWorld(ArrayList<Cell> board, ArrayList<Color> colors, WorldScene scene,
                 int numAllowedClicks, int numClicks, boolean lost, Color colorClicked,
                 boolean acceptClicks, Random rand) {
        this.board = board;
        this.colors = colors;
        this.scene = scene;
        this.numAllowedClicks = numAllowedClicks;
        this.numClicks = numClicks;
        this.lost = lost;
        this.colorClicked = colorClicked;
        this.acceptClicks = acceptClicks;
        this.rand = rand;
    }


    /* TMPLT
     *
     * Fields:
     *   FloodItWorld.BOARD_SIZE         22
     *   this.board              ArrayList<Cell>
     *   this.colors             ArrayList<Color>
     *   this.scene              WorldScene
     *   this.numAllowedClicks   int
     *   this.numClicks          int
     *   this.lost               boolean
     *   this.colorClicked       Color
     *   this.acceptClicks       boolean
     *   this.rand               Random
     *
     * Methods:
     *   this.updateLinks()     void
     *   this.makeScene()       WorldScene
     *   this.onTick()          void
     *   this.onMouseClick()    void
     *   this.onKeyEvent()      void
     *   this.worldEnds()       WorldEnds
     *   this.finalScene()      FinalScene
     */

    // EFFECT: modifies the left, right, top, and bottom of this cell's links
    public void updateLinks() {
        Cell left;
        Cell right;
        Cell top;
        Cell bottom;
        for (int i = 0; i < board.size(); i++) {
            // check bottom is not null
            if (i + BOARD_SIZE >= board.size()) {
                bottom = null;
            }
            else {
                bottom = board.get(i + BOARD_SIZE);
            }
            // check top is not null
            if (i - BOARD_SIZE < 0) {
                top = null;
            }
            else {
                top = board.get(i - BOARD_SIZE);
            }
            // check left is not null
            if (i % BOARD_SIZE == 0) {
                left = null;
            }
            else {
                left = board.get(i - 1);
            }
            // check right is not null
            if (i % BOARD_SIZE == BOARD_SIZE - 1) {
                right = null;
            }
            else {
                right = board.get(i + 1);
            }

            board.get(i).updateCellLinks(left, top, right, bottom);
        }
    }

    // draws scene
    public WorldScene makeScene() {
        for (int i = 0; i < board.size(); i++) {
            board.get(i).renderCell(scene);
        }
        return scene;
    }

    // ticks through game
    // EFFECT: Modifies the game by changing the color of the board when prompted
    // NOTE: will be implemented for part 2
    public void onTick() {
        return;
    }

    // on mouse click
    // EFFECT: Modifies the game by updating the board with the clicked Cell
    // NOTE: will be implemented for part 2
    public void onMouseClick() {
        return;

    }

    // on key click (r to reset)
    //EFFECT: modifies the game by restarting
    // NOTE: will be implemented for part 2
    public void onKeyEvent() {
        return;

    }

    // on world end
    public WorldEnd worldEnds() {
        if (lost) {
            return new WorldEnd(true, this.finalScene());
        }
        else {
            return new WorldEnd(false, this.makeScene());
        }
    }

    // final scene
    public WorldScene finalScene() {
        scene.placeImageXY(new TextImage("You lose", Color.BLACK), 100, 100);
        return scene;
    }
}

// examples
class ExamplesFlood {
    Cell c1;
    Cell c2;
    Cell c3;
    Cell c4;
    Cell c5;

    ArrayList<Cell> cells;
    ArrayList<Color> colors;
    ArrayList<Cell> cells2;

    WorldScene scene;
    Random rand;

    FloodItWorld example;
    FloodItWorld example2;
    FloodItWorld example3;

    // initializes test cases
    void initCond() {
        c1 = new Cell(10, 10, Color.RED);
        c2 = new Cell(30, 10, Color.BLUE);
        c3 = new Cell(10, 30, Color.GREEN);
        c4 = new Cell(30, 30, Color.PINK);
        c5 = new Cell(15, 15, Color.CYAN);

        cells = new ArrayList<Cell>();
        cells2 = new ArrayList<Cell>();
        colors = new ArrayList<Color>();

        cells.add(c1);
        cells.add(c2);
        cells.add(c3);

        cells2.add(c4);

        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.BLACK);

        scene = new WorldScene(440, 440);
        rand = new Random(0);

        example = new FloodItWorld();
        example2 = new FloodItWorld(cells, colors, scene, 10, 0, true, Color.BLACK, true, rand);
        example3 = new FloodItWorld(cells2,
                colors,
                new WorldScene(440, 440),
                10,
                0,
                true,
                Color.BLUE,
                true,
                rand);
    }

    //big bang
    void testBigBang(Tester t) {
        FloodItWorld w = new FloodItWorld();
        int worldWidth = FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE;
        int worldHeight = FloodItWorld.BOARD_SIZE * Cell.CELL_SIZE;
        double tickRate = 1.0;
        w.bigBang(worldWidth, worldHeight, tickRate);
    }

    //tests updateLinks for board
    void testUpdateLinks(Tester t) {
        initCond();
        // updateLinks called in the constructor for example
        // top left corner
        t.checkExpect(example.board.get(0).bottom, example.board.get(22));
        t.checkExpect(example.board.get(0).top, null);
        t.checkExpect(example.board.get(0).left, null);
        t.checkExpect(example.board.get(0).right, example.board.get(1));
        t.checkExpect(example.board.get(0).x, 10);
        t.checkExpect(example.board.get(0).y, 10);

        // top right corner
        t.checkExpect(example.board.get(21).bottom, example.board.get(43));
        t.checkExpect(example.board.get(21).top, null);
        t.checkExpect(example.board.get(21).left, example.board.get(20));
        t.checkExpect(example.board.get(21).right, null);
        t.checkExpect(example.board.get(21).x, 430);
        t.checkExpect(example.board.get(21).y, 10);

        // bottom left corner
        t.checkExpect(example.board.get(462).bottom, null);
        t.checkExpect(example.board.get(462).top, example.board.get(440));
        t.checkExpect(example.board.get(462).left, null);
        t.checkExpect(example.board.get(462).right, example.board.get(463));
        t.checkExpect(example.board.get(462).x, 10);
        t.checkExpect(example.board.get(462).y, 430);

        // bottom right
        t.checkExpect(example.board.get(483).bottom, null);
        t.checkExpect(example.board.get(483).top, example.board.get(461));
        t.checkExpect(example.board.get(483).left, example.board.get(482));
        t.checkExpect(example.board.get(483).right, null);
        t.checkExpect(example.board.get(483).x, 430);
        t.checkExpect(example.board.get(483).y, 430);

        // middle
        t.checkExpect(example.board.get(121).bottom, example.board.get(143));
        t.checkExpect(example.board.get(121).top, example.board.get(99));
        t.checkExpect(example.board.get(121).left, example.board.get(120));
        t.checkExpect(example.board.get(121).right, example.board.get(122));
        t.checkExpect(example.board.get(121).x, 230);
        t.checkExpect(example.board.get(121).y, 110);
    }

    //tests updateCellLinks() for cells
    void testUpdateCellLinks(Tester t) {
        initCond();
        c5.updateCellLinks(c1, c2, c3, c4);
        t.checkExpect(c5.left, c1);
        t.checkExpect(c5.top, c2);
        t.checkExpect(c5.right, c3);
        t.checkExpect(c5.bottom, c4);
    }

    //tests makeScene()
    void testmakeScene(Tester t) {
        initCond();
        // expected scene
        WorldScene expectedScene = new WorldScene(440, 440);
        expectedScene.placeImageXY(new RectangleImage(Cell.CELL_SIZE,
                        Cell.CELL_SIZE,
                        "solid",
                        Color.RED),
                10, 10);
        expectedScene.placeImageXY(new RectangleImage(Cell.CELL_SIZE,
                        Cell.CELL_SIZE,
                        "solid",
                        Color.BLUE),
                30, 10);
        expectedScene.placeImageXY(new RectangleImage(Cell.CELL_SIZE,
                        Cell.CELL_SIZE,
                        "solid",
                        Color.GREEN),
                10, 30);

        WorldScene expectedScene2 = new WorldScene(440, 440);
        expectedScene2.placeImageXY(new RectangleImage(Cell.CELL_SIZE,
                        Cell.CELL_SIZE,
                        "solid",
                        Color.PINK),
                30, 30);
        example2.makeScene();
        example3.makeScene();
        t.checkExpect(example2.scene, expectedScene);
        t.checkExpect(example3.makeScene(), expectedScene2);
    }

    //tests renderCell()
    void testRenderCell(Tester t) {
        initCond();
        // expected scene
        WorldScene expectedScene = new WorldScene(440, 440);
        expectedScene.placeImageXY(new RectangleImage(Cell.CELL_SIZE,
                        Cell.CELL_SIZE,
                        "solid",
                        Color.RED),
                10, 10);
        c1.renderCell(scene);
        t.checkExpect(scene, expectedScene);
        expectedScene.placeImageXY(new RectangleImage(Cell.CELL_SIZE,
                        Cell.CELL_SIZE,
                        "solid",
                        Color.BLUE),
                30, 10);
        c2.renderCell(scene);
        t.checkExpect(scene, expectedScene);
    }


    // tests onTick  NOTE: empty because will be implemented for part 2
    void testOnTick(Tester t) {
        return;
    }


    // tests onMouseClick   NOTE: empty because will be implemented for part 2
    void testOnMouseClick(Tester t) {
        return;
    }


    // tests onKeyEvent   NOTE: empty because will be implemented for part 2
    void testOnKeyEvent(Tester t) {
        return;
    }


    //tests worldEnds
    void testWorldEnds(Tester t) {
        initCond();
        //expected end screen
        WorldScene endScreen =  new WorldScene(440, 440);
        endScreen.placeImageXY(new TextImage("You lose", Color.BLACK), 100, 100);


        t.checkExpect(this.example2.lost, true); // lost
        t.checkExpect(this.example.lost, false); // still playing

        t.checkExpect(example2.worldEnds(), new WorldEnd(true, endScreen));
        t.checkExpect(example.worldEnds(), new WorldEnd(false, example.makeScene()));
    }


    //tests finalScene
    void testFinalScene(Tester t) {
        initCond();
        // initial screen
        t.checkExpect(this.example2.scene, new WorldScene(440, 440));

        //expected end screen
        WorldScene endScreen =  new WorldScene(440, 440);
        endScreen.placeImageXY(new TextImage("You lose", Color.BLACK), 100, 100);

        t.checkExpect(this.example2.finalScene(), endScreen);
    }

}