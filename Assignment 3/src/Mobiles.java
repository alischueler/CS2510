import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import javalib.worldcanvas.WorldCanvas;

import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Red, Green, Yellow, Blue, Black, White)



interface IMobile {

  //computes the total weight of this IMobile
  int totalWeight();

  //computes the total height of this IMobile
  int totalHeight();

  //determines if this IMobile is balanced
  boolean isBalanced();

  //builds a balanced IMobile using this IMobile and the given IMobile
  IMobile buildMobile(IMobile that, int string, int strut);

  //calculates the total width of this IMobile
  int curWidth();

  //calculates the total width of the rightside of this IMobile
  int curWidthRight();

  //calculates the total width of the leftside of this IMobile
  int curWidthLeft();

  //produces an image of this IMobile
  WorldImage drawMobile();
}

class Simple implements IMobile { //represents a Simple Mobile
  int length;
  int weight;
  Color color;

  Simple(int length, int weight, Color color) {
    this.length = length;
    this.weight = weight;
    this.color = color;
  }

  /*TEMPLATE:
   * FIELDS:
   * ...this.length...  int
   * ...this.weight...  int
   * ...this.color...  Color
   *
   * METHODS:
   * ...this.totalWeight()...  int
   * ...this.totalHeight()...  int
   * ...this.isBalanced()...  boolean
   * ...this.buildMobile(IMobile that, int string, int strut)...  IMobile
   * ...this.curWidth()...  int
   * ...this.curWidthRight()...  int
   * ...this.curWidthLeft()...  int
   * ...this.drawMobile()...  WorldImage
   */

  //computes the total weight of this Simple
  public int totalWeight() {
    return this.weight;
  }

  //computes the total height of this Simple
  public int totalHeight() {
    return this.weight / 10 + this.length;
  }

  //determines if this Simple is balanced
  public boolean isBalanced() {
    return true;
  }

  //creates a new, balanced IMobile, using this Simple and the given IMobile
  public IMobile buildMobile(IMobile that, int string, int strut) {
    int rights = (this.weight * strut) / (that.totalWeight() + this.weight);
    int lefts = strut - rights;
    return new Complex(string, lefts, rights, this, that);
  }

  //calculates the total width of this Simple
  public int curWidth() {
    if (this.weight % 10 == 0) {
      return this.weight / 10;
    }
    else {
      return (this.weight / 10) + 1;
    }
  }

  //calculates the right-width of this Simple
  public int curWidthRight() {
    return this.curWidth() / 2;
  }

  //calculates the left-width of this Simple
  public int curWidthLeft() {
    return this.curWidth() / 2;
  }

  //produces the image of this Simple
  public WorldImage drawMobile() {
    int width = this.curWidth();
    int height = this.totalHeight();
    WorldImage newRect = new RectangleImage(width, height, OutlineMode.SOLID, this.color)
            .movePinhole(0, -(height / 2));
    WorldImage vertLine = new RectangleImage(1, this.length, OutlineMode.SOLID, Color.BLACK)
            .movePinhole(0, (this.length / 2));

    return new OverlayImage(vertLine, newRect).movePinhole(0, -(height / 2));
  }

}

class Complex implements IMobile { //represents a Complex Mobile
  int length;
  int leftside;
  int rightside;
  IMobile left;
  IMobile right;

  Complex(int length, int leftside, int rightside, IMobile left, IMobile right) {
    this.length = length;
    this.leftside = leftside;
    this.rightside = rightside;
    this.left = left;
    this.right = right;
  }

  /*TEMPLATE:
   * FIELDS:
   * ...this.length...  int
   * ...this.leftside...  int
   * ...this.rightside...  int
   * ...this.left...  IMobile
   * ...this.right...  IMobile
   *
   * METHODS:
   * ...this.totalWeight()...  int
   * ...this.totalHeight()...  int
   * ...this.isBalanced()...  boolean
   * ...this.buildMobile(IMobile that, int string, int strut)...  IMobile
   * ...this.curWidth()...  int
   * ...this.curWidthRight()...  int
   * ...this.curWidthLeft()...  int
   * ...this.drawMobile()...  WorldImage
   *
   * METHODS OF FIELDS:
   * ...this.right.totalWeight()...  int
   * ...this.right.totalHeight()...  int
   * ...this.right.isBalanced()...  boolean
   * ...this.right.buildMobile(IMobile that, int string, int strut)...  IMobile
   * ...this.right.curWidth()...  int
   * ...this.right.curWidthRight()...  int
   * ...this.right.curWidthLeft()...  int
   * ...this.right.drawMobile()...  WorldImage
   * ...this.left.totalWeight()...  int
   * ...this.left.totalHeight()...  int
   * ...this.left.isBalanced()...  boolean
   * ...this.left.buildMobile(IMobile that, int string, int strut)...  IMobile
   * ...this.left.curWidth()...  int
   * ...this.left.curWidthRight()...  int
   * ...this.left.curWidthLeft()...  int
   * ...this.left.drawMobile()...  WorldImage
   */

  //computes the total weight of this Complex
  public int totalWeight() {
    return this.right.totalWeight() + this.left.totalWeight();
  }

  //computes the total height of this Complex
  public int totalHeight() {
    if (this.right.totalWeight() > this.left.totalWeight()) {
      return this.right.totalHeight() + this.length;
    }
    else {
      return this.left.totalHeight() + this.length;
    }
  }

  //determines if this Complex is balanced
  public boolean isBalanced() {
    return (this.right.totalWeight() * this.rightside) ==
            (this.left.totalWeight() * this.leftside);
  }

  //creates a new, balanced IMobile, using this Complex, and the given IMobile
  public IMobile buildMobile(IMobile that, int string, int strut) {
    int rights = (this.totalWeight() * strut) / (that.totalWeight() + this.totalWeight());
    int lefts = strut - rights;
    return new Complex(string, lefts, rights, this, that);
  }

  //calculates the total width of this complex
  public int curWidth() {
    return this.curWidthRight() + this.curWidthLeft();
  }

  //calculates the length of the strut of the rightside of this Complex
  public int curWidthRight() {
    return Math.max(this.rightside + this.right.curWidthRight(),
            this.left.curWidthRight() - this.leftside);
  }

  //calculates the length of the strut of the leftside of this Complex
  public int curWidthLeft() {
    return Math.max(this.leftside + this.left.curWidthLeft(),
            this.right.curWidthLeft() - this.rightside);
  }

  //produces the image of this Complex
  public WorldImage drawMobile() {
    WorldImage rights =
            this.right.drawMobile();
    WorldImage lefts =
            this.left.drawMobile();
    WorldImage rLine =
            new RectangleImage(this.rightside, 1, OutlineMode.SOLID, Color.BLACK)
                    .movePinhole(-(this.rightside / 2), 0);
    WorldImage rrLine =
            new OverlayImage(rLine, rights)
                    .movePinhole((((this.rightside / 2) + (this.right.curWidthRight())) * 2),
                            -(this.right.totalHeight() / 2));
    WorldImage lLine =
            new RectangleImage(this.leftside, 1, OutlineMode.SOLID, Color.BLACK)
                    .movePinhole((this.leftside / 2), 0);
    WorldImage llLine =
            new OverlayImage(lLine, lefts)
                    .movePinhole((-((this.leftside / 2) + (this.left.curWidthLeft())) * 2),
                            this.left.totalHeight() / 2);
    WorldImage rlLine =
            new OverlayImage(rrLine, llLine)
                    .movePinhole((this.leftside) / (2 * (this.rightside)), -(this.totalHeight()));
    WorldImage string =
            new RectangleImage(1, this.length, OutlineMode.SOLID, Color.BLACK)
                    .movePinhole(0, -(this.length / 2));
    WorldImage rlLineString = new OverlayImage(rlLine, string);
    return rlLineString;
  }
}

class ExamplesMobiles {
  IMobile exampleSimple = new Simple(2, 20, Color.BLUE);
  IMobile exampleSimple2 = new Simple(1, 36, Color.BLUE);
  IMobile exampleSimple3 = new Simple(1, 12, Color.RED);
  IMobile exampleSimple4 = new Simple(2, 36, Color.RED);
  IMobile exampleSimple5 = new Simple(1, 60, Color.GREEN);
  IMobile exampleComplex1 = new Complex(2, 5, 3, this.exampleSimple4, this.exampleSimple5);
  IMobile exampleComplex2 = new Complex(2, 8, 1, this.exampleSimple3, this.exampleComplex1);
  IMobile exampleComplex = new Complex(1, 9, 3, this.exampleSimple2, this.exampleComplex2);
  IMobile example3 = new Complex(3, 2, 4, this.exampleComplex, this.exampleComplex2);

  //tests for totalWeight()
  boolean testtotalWeight(Tester t) {
    return
            t.checkExpect(this.exampleSimple.totalWeight(), 20) &&
                    t.checkExpect(this.exampleSimple3.totalWeight(), 12) &&
                    t.checkExpect(this.exampleComplex.totalWeight(), 144) &&
                    t.checkExpect(this.example3.totalWeight(), 252);
  }

  //tests for totalHeight()
  boolean testtotalHeight(Tester t) {
    return
            t.checkExpect(this.exampleSimple.totalHeight(), 4) &&
                    t.checkExpect(this.exampleSimple3.totalHeight(), 2) &&
                    t.checkExpect(this.exampleComplex.totalHeight(), 12) &&
                    t.checkExpect(this.example3.totalHeight(), 15);
  }

  //tests for isBalanced()
  boolean testisBalanced(Tester t) {
    return
            t.checkExpect(this.exampleSimple.isBalanced(), true) &&
                    t.checkExpect(this.exampleSimple3.isBalanced(), true) &&
                    t.checkExpect(this.exampleComplex.isBalanced(), true) &&
                    t.checkExpect(this.example3.isBalanced(), false);
  }

  //tests for curWidth()
  boolean testcurWidth(Tester t) {
    return
            t.checkExpect(this.exampleSimple.curWidth(), 2) &&
                    t.checkExpect(this.exampleSimple3.curWidth(), 2) &&
                    t.checkExpect(this.exampleComplex.curWidth(), 21) &&
                    t.checkExpect(this.example3.curWidth(), 24);
  }

  //tests for curWidthRight()
  boolean testcurWidthRight(Tester t) {
    return
            t.checkExpect(this.exampleSimple.curWidthRight(), 1) &&
                    t.checkExpect(this.exampleSimple3.curWidthRight(), 1) &&
                    t.checkExpect(this.exampleComplex.curWidthRight(), 10) &&
                    t.checkExpect(this.example3.curWidthRight(), 11);
  }

  //tests for curWidthLeft()
  boolean testcurWidthLeft(Tester t) {
    return
            t.checkExpect(this.exampleSimple.curWidthLeft(), 1) &&
                    t.checkExpect(this.exampleSimple3.curWidthLeft(), 1) &&
                    t.checkExpect(this.exampleComplex.curWidthLeft(), 11) &&
                    t.checkExpect(this.example3.curWidthLeft(), 13);
  }

  //tests for buildMobile(IMobile that, int string, int strut)
  boolean testbuildMobile(Tester t) {
    return
            t.checkExpect(this.exampleSimple.buildMobile(this.exampleSimple, 5, 4),
                    new Complex(5, 2, 2, this.exampleSimple, this.exampleSimple)) &&
                    t.checkExpect(this.exampleSimple3.buildMobile(exampleSimple2, 10, 2),
                            new Complex(10, 2, 0, this.exampleSimple3, this.exampleSimple2)) &&
                    t.checkExpect(this.exampleSimple3.buildMobile(exampleComplex1, 10, 2),
                            new Complex(10, 2, 0, this.exampleSimple3, this.exampleComplex1)) &&
                    t.checkExpect(this.exampleComplex.buildMobile(this.exampleSimple, 10, 10),
                            new Complex(10, 2, 8, this.exampleComplex, this.exampleSimple)) &&
                    t.checkExpect(this.exampleComplex.buildMobile(this.exampleSimple3, 5, 5),
                            new Complex(5, 1, 4, this.exampleComplex, this.exampleSimple3)) &&
                    t.checkExpect(this.exampleComplex2.buildMobile(exampleComplex1, 1, 5),
                            new Complex(1, 3, 2, this.exampleComplex2, this.exampleComplex1));
  }

  //shows image at the center of an equally-sized canvas,
  // and the text at the top of the frame is given by description
  boolean showImage(WorldImage image, String description) {
    int width = (int) Math.ceil(image.getWidth());
    int height = (int) Math.ceil(image.getHeight());
    WorldCanvas canvas = new WorldCanvas(500, 500, description);
    WorldScene scene = new WorldScene(500, 500);
    return canvas.drawScene(scene.placeImageXY(image, width, height))
            && canvas.show();
  }

  //tests for drawMobile()
  boolean testdrawMobile(Tester t) {
    return
            showImage(
                    new ScaleImage(this.exampleSimple.drawMobile(), 3), "Simple") &&
                    showImage(
                            new ScaleImage(this.exampleSimple3.drawMobile(), 3), "simple") &&
                    showImage(
                            new ScaleImage(this.exampleComplex.drawMobile(), 3), "hi") &&
                    showImage(
                            new ScaleImage(new Complex(4, 5, 7, this.exampleSimple,
                                    this.exampleSimple).drawMobile(), 3), "alright");
  }
}