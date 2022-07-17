import tester.Tester;
import java.awt.Color;

interface IPaint {

    //computes the final color of this Paint
    Color getFinalColor();

    //computes the number of Solid Paints involved in
    //producing the final color of this Paint
    int countPaints();

    //computes the number of times this paint was mixed in some way
    int countMixes();

    //computes how deeply mixtures are nested in the formula for this Paint
    int formulaDepth();

    //produces the paint as is, except all Darken mixtures should become
    //Brighten mixtures and vice versa
    IPaint invert();

    //takes an integer depth, and produces a  String representing the contents of this paint,
    //where the formula for the paint has been expanded only depth times
    String mixingFormula(int depth);

}

class Solid implements IPaint { //represents a solid Paint Color
    String name;
    Color color;

    Solid(String name, Color color) {
        this.name = name;
        this.color = color;
    }
    /*TEMPLATE
     * Fields:
     * this.name  String
     * this.color Color
     * Methods:
     * this.getFinalColor() Color
     * this.getRedValue() int
     * this.getGreenValue() int
     * this.getBlueValue()  int
     * this.countPaints() int
     * this.countMixes()  int
     * this.formulaDepth()  int
     * this.invert() IPaint
     * this.mixingFormula(int depth)  String
     * Methods of Fields:
     * this.color.darker()  Color
     * this.color.brighter()  Color
     */

    //computes the final color of this Solid
    public Color getFinalColor() {
        return this.color;
    }

    //determines if this Solid has any solid Paints in it
    //this solid  has one solid paint in it, so adds one
    public int countPaints() {
        return 1;
    }

    //determines if this Solid is mixes in any way
    //this Solid is never mixed, so returns 0
    public int countMixes() {
        return 0;
    }

    //determines how deeply nested the mixtures are in this Solid's formula
    //this Solid only has one color to it, add 0
    public int formulaDepth() {
        return 0;
    }

    //produces this Paint as is because this Solid does not have any
    //darkens or lightens
    public IPaint invert() {
        return this;
    }

    //produces a String representing the contents of this Solid,
    //where the formula for the paint has been expanded only depth times
    public String mixingFormula(int depth) {
        return this.name;
    }

}

class Combo implements IPaint { //represents a non-Solid Paint Color
    String name;
    IMixture operation;

    Combo(String name, IMixture operation) {
        this.name = name;
        this.operation = operation;
    }

    /*TEMPLATE
     * Fields:
     * this.name  String
     * this.operation IMixture
     * Methods:
     * this.getFinalColor() Color
     * this.countPaints() int
     * this.countMixes()  int
     * this.formulaDepth()  int
     * this.invert() IPaint
     * this.mixingFormula(int depth)  String
     * Methods of Fields:
     * this.operation.findColors()  Color
     * this.operation.findSolids()  int
     * this.operation.howManyMixes()  int
     * this.operation.calculateDepth()  int
     * this.operation.switchOperations()  IPaint
     * this.operation.writeFormula(int depth) String
     */

    //computes the final color of this Combo
    public Color getFinalColor() {
        return this.operation.findColors();
    }

    //this Combo has no Solids in the fields so must look to IMixture field
    public int countPaints() {
        return this.operation.findSolids();
    }

    //this Combo is mixed, but must delegate to IMixture to find out how
    //many times
    public int countMixes() {
        return this.operation.howManyMixes();
    }

    //this Combo must delegate to the IMixture to calculate
    //the depth of the formula
    public int formulaDepth() {
        return this.operation.calculateDepth();
    }

    //this Combo does must delegate to the IMixture to find out which colors have been lightened
    //and which have been darkened, and swap
    public IPaint invert() {
        return new Combo(this.name, this.operation.switchOperation());
    }

    //produces a String representing the contents of this Combo,
    //where the formula for the paint has been expanded only depth times
    public String mixingFormula(int depth) {
        if (depth == 0) {
            return this.name;
        }
        else {
            return this.operation.writeFormula(depth);
        }
    }
}


interface IMixture {

    //finds the final Color of this IMixture
    Color findColors();

    //counts the number of Solids in this IMixture
    int findSolids();

    //counts how many times this IMixture has been mixed
    int howManyMixes();

    //calculates the depth of the mixtures in this IMixture formula
    int calculateDepth();

    //switches the Brighten and Darken mixtures for the opposite
    //in this IMixture
    IMixture switchOperation();

    //produces a String representing the contents of this IMixture,
    //where the formula for the paint has been expanded only depth times
    String writeFormula(int depth);
}

class Darken implements IMixture { //darkens a color
    IPaint paint;

    Darken(IPaint paint) {
        this.paint = paint;
    }

    /*TEMPLATE
     * Fields:
     * this.paint IPaint
     * Methods:
     * this.findColors()  Color
     * this.findSolids()  int
     * this.howManyMixes()  int
     * this.calculateDepth()  int
     * this.switchOperations()  IMixture
     * this.writeFormula(int depth) String
     * Methods of Fields:
     * this.paint.getFinalColor() Color
     * this.paint.countPaints() int
     * this.paint.countMixes()  int
     * this.paint.formulaDepth()  int
     * this.paint.invert() IPaint
     * this.paint.mixingFormula(int depth)  String
     */


    //determines what colors are used in this Darken
    public Color findColors() {
        return this.paint.getFinalColor().darker();
    }

    //this Darken adds Solid to it, but must also look to see if there are
    //underlying solids in the Paint
    public int findSolids() {
        return 1 + this.paint.countPaints();
    }

    //this Darken is a mixture but also must see is there are underlying mixes
    //in the Paint
    public int howManyMixes() {
        return 1 + this.paint.countMixes();
    }

    //this Darken is adds depth to the formula but must see if there
    //are any underlying formulas
    public int calculateDepth() {
        return 1 + this.paint.formulaDepth();
    }

    //this mixture is a Darken, so we must switch it for a Brighten mixture
    public IMixture switchOperation() {
        return new Brighten(this.paint.invert());
    }

    //produces a String representing the contents of this Darken,
    //where the formula for the paint has been expanded only depth times
    public String writeFormula(int depth) {
        if (depth == 0) {
            return "darken(" + this.paint.mixingFormula(0) + ")";
        }
        else {
            return "darken(" + this.paint.mixingFormula(depth - 1) + ")";
        }
    }

}

class Brighten implements IMixture { //lightens a color
    IPaint paint;

    Brighten(IPaint paint) {
        this.paint = paint;
    }

    /*TEMPLATE
     * Fields:
     * this.paint IPaint
     * Methods:
     * this.findColors()  Color
     * this.findSolids()  int
     * this.howManyMixes()  int
     * this.calculateDepth()  int
     * this.switchOperations()  IMixture
     * this.writeFormula(int depth) String
     * Methods of Fields:
     * this.paint.getFinalColor() Color
     * this.paint.countPaints() int
     * this.paint.countMixes()  int
     * this.paint.formulaDepth()  int
     * this.paint.invert() IPaint
     * this.paint.mixingFormula(int depth)  String
     */

    //determines what Colors are used in this Brighten
    public Color findColors() {
        return this.paint.getFinalColor().brighter();
    }

    //this Brighten adds solid to it, but must also look to see if there are
    //underlying Solids in the paint
    public int findSolids() {
        return 1 + this.paint.countPaints();
    }

    //this Brighten is a mix but also must see is there are underlying mixes
    //in the paint
    public int howManyMixes() {
        return 1 + this.paint.countMixes();
    }

    //this Brighten adds one to the formula depth but must see if there
    //are any underlying formulas
    public int calculateDepth() {
        return 1 + this.paint.formulaDepth();
    }

    //this mixture is a Lighten, so we must switch it for a Darken IMixture
    public IMixture switchOperation() {
        return new Darken(this.paint.invert());
    }

    //produces a String representing the contents of this Lighten,
    //where the formula for the paint has been expanded only depth times
    public String writeFormula(int depth) {
        if (depth == 0) {
            return "brighten(" + this.paint.mixingFormula(0) + ")";
        }
        else {
            return "brighten(" + this.paint.mixingFormula(depth - 1) + ")";
        }
    }

}

class Blend implements IMixture { //blends two paints together
    IPaint paint1;
    IPaint paint2;

    Blend(IPaint paint1, IPaint paint2) {
        this.paint1 = paint1;
        this.paint2 = paint2;
    }

    /*TEMPLATE
     * Fields:
     * this.paint1  IPaint
     * this.paint2  IPaint
     * Methods:
     * this.findColors()  Color
     * this.findSolids()  int
     * this.howManyMixes()  int
     * this.calculateDepth()  int
     * this.switchOperations()  IMixture
     * this.writeFormula(int depth) String
     * Methods of Fields:
     * this.paint1.getFinalColor() Color
     * this.paint1.countPaints() int
     * this.paint1.countMixes()  int
     * this.paint1.formulaDepth()  int
     * this.paint1.invert() IPaint
     * this.paint1.mixingFormula(int depth)  String
     * this.paint2.getFinalColor() Color
     * this.paint2.countPaints() int
     * this.paint2.countMixes()  int
     * this.paint2.formulaDepth()  int
     * this.paint2.invert() IPaint
     * this.paint2.mixingFormula(int depth)  String
     */

    //determines what colors are used in this Blend
    public Color findColors() {
        return
                new Color(((this.paint1.getFinalColor().getRed() / 2)
                        + (this.paint2.getFinalColor().getRed() / 2)),
                        ((this.paint1.getFinalColor().getGreen() / 2)
                                + (this.paint2.getFinalColor().getGreen() / 2)),
                        ((this.paint1.getFinalColor().getBlue() / 2)
                                + (this.paint2.getFinalColor().getBlue() / 2)));
    }

    //this blending does not add a Solid color directly, must look to see if the
    //blended Colors themselves contain Solids
    public int findSolids() {
        return this.paint1.countPaints() + this.paint2.countPaints();
    }

    //this Blend is one mix but also must see is there are underlying mixes
    //in the Paints
    public int howManyMixes() {
        return 1 + this.paint1.countMixes() + this.paint2.countMixes();
    }

    //this blend adds one to the formula depth but must see if there
    //are any underlying formulas
    public int calculateDepth() {
        if ((1 + this.paint1.formulaDepth()) >= (1 + this.paint2.formulaDepth())) {
            return 1 + this.paint1.formulaDepth();
        }
        else {
            return 1 + this.paint2.formulaDepth();
        }
    }

    //this Blend does not have Brighten or Darken IMixtures directly,
    //but must check to see if the paints do
    public IMixture switchOperation() {
        return new Blend(this.paint1.invert(), this.paint2.invert());
    }

    //produces a String representing the contents of this Blend,
    //where the formula for the paint has been expanded only depth times
    public String writeFormula(int depth) {
        if (depth == 0) {
            return "blend(" + this.paint1.mixingFormula(0) + ", " + this.paint2.mixingFormula(0) + ")";
        }
        else {
            return
                    "blend(" + this.paint1.mixingFormula(depth - 1)
                            + ", " + this.paint2.mixingFormula(depth - 1) + ")";
        }
    }

}

class ExamplesPaint {
    //examples Color
    Color red1 = new Color(255, 0, 0);
    Color green1 = new Color(0, 255, 0);
    Color blue1 = new Color(0, 0, 255);

    //examples Solid
    IPaint red = new Solid("red", this.red1);
    IPaint green = new Solid("green", this.green1);
    IPaint blue = new Solid("blue", this.blue1);

    //examples Combo
    IMixture purple1 = new Blend(this.red, this.blue);
    IPaint purple = new Combo("purple", this.purple1);

    IMixture darkPurple1 = new Darken(this.purple);
    IPaint darkPurple = new Combo("dark purple", this.darkPurple1);

    IMixture khaki1 = new Blend(this.red, this.green);
    IPaint khaki = new Combo("khaki", this.khaki1);

    IMixture yellow1 = new Brighten(this.khaki);
    IPaint yellow = new Combo("yellow", this.yellow1);

    IMixture mauve1 = new Blend(this.purple, this.khaki);
    IPaint mauve = new Combo("mauve", this.mauve1);

    IMixture pink1 = new Brighten(this.mauve);
    IPaint pink = new Combo("pink", this.pink1);

    IMixture coral1 = new Blend(this.pink, this.khaki);
    IPaint coral = new Combo("coral", this.coral1);

    IMixture cyan1 = new Blend(this.blue, this.green);
    IPaint cyan = new Combo("cyan", this.cyan1);

    IMixture orange1 = new Brighten(this.red);
    IPaint orange = new Combo("orange", this.orange1);

    IMixture avocado1 = new Darken(this.green);
    IPaint avocado = new Combo("avocado", this.avocado1);

    //tests getFinalColor
    boolean testgetFinalColor(Tester t) {
        return
                //solids
                t.checkExpect(this.red.getFinalColor(), (new Color(255, 0, 0))) &&
                        t.checkExpect(this.green.getFinalColor(), (new Color(0, 255, 0))) &&
                        t.checkExpect(this.blue.getFinalColor(), (new Color(0, 0, 255))) &&
                        //combos
                        t.checkExpect(this.purple.getFinalColor(), (new Color(127, 0, 127))) &&
                        t.checkExpect(this.darkPurple.getFinalColor(), (new Color(127, 0, 127).darker())) &&
                        t.checkExpect(this.khaki.getFinalColor(), (new Color(127, 127, 0))) &&
                        t.checkExpect(this.yellow.getFinalColor(), (new Color(127, 127, 0).brighter())) &&
                        t.checkExpect(this.avocado.getFinalColor(), (new Color(0, 255, 0).darker())) &&
                        t.checkExpect(this.orange.getFinalColor(), (new Color(255, 0, 0).brighter()));
    }

    //tests findColors()
    boolean testfindColors(Tester t) {
        return
                t.checkExpect(this.purple1.findColors(),
                        (new Color(127, 0, 127))) &&
                        t.checkExpect(this.darkPurple1.findColors(),
                                (new Color(127, 0, 127).darker())) &&
                        t.checkExpect(this.khaki1.findColors(),
                                (new Color(127, 127, 0))) &&
                        t.checkExpect(this.yellow1.findColors(),
                                (new Color(127, 127, 0)).brighter()) &&
                        t.checkExpect(this.avocado1.findColors(),
                                (new Color(0, 255, 0)).darker()) &&
                        t.checkExpect(this.orange1.findColors(),
                                (new Color(255, 0, 0)).brighter());
    }

    //tests countPaints()
    boolean testcountPaints(Tester t) {
        return
                //solids
                t.checkExpect(this.red.countPaints(), 1) &&
                        t.checkExpect(this.green.countPaints(), 1) &&
                        t.checkExpect(this.blue.countPaints(), 1) &&
                        //combos
                        t.checkExpect(this.purple.countPaints(), 2) &&
                        t.checkExpect(this.darkPurple.countPaints(), 3) &&
                        t.checkExpect(this.khaki.countPaints(), 2) &&
                        t.checkExpect(this.yellow.countPaints(), 3) &&
                        t.checkExpect(this.avocado.countPaints(), 2) &&
                        t.checkExpect(this.orange.countPaints(), 2);
    }

    //tests findSolids()
    boolean testfindSolids(Tester t) {
        return
                t.checkExpect(this.purple1.findSolids(), 2) &&
                        t.checkExpect(this.darkPurple1.findSolids(), 3) &&
                        t.checkExpect(this.khaki1.findSolids(), 2) &&
                        t.checkExpect(this.yellow1.findSolids(), 3) &&
                        t.checkExpect(this.avocado1.findSolids(), 2) &&
                        t.checkExpect(this.orange1.findSolids(), 2);
    }

    //tests countMixes()
    boolean testcountMixes(Tester t) {
        return
                //solids
                t.checkExpect(this.red.countMixes(), 0) &&
                        t.checkExpect(this.green.countMixes(), 0) &&
                        t.checkExpect(this.blue.countMixes(), 0) &&
                        //combos
                        t.checkExpect(this.purple.countMixes(), 1) &&
                        t.checkExpect(this.darkPurple.countMixes(), 2) &&
                        t.checkExpect(this.khaki.countMixes(), 1) &&
                        t.checkExpect(this.yellow.countMixes(), 2) &&
                        t.checkExpect(this.avocado.countMixes(), 1) &&
                        t.checkExpect(this.orange.countMixes(), 1);
    }

    //tests howManyMixes()
    boolean testhowManyMixes(Tester t) {
        return
                t.checkExpect(this.purple1.howManyMixes(), 1) &&
                        t.checkExpect(this.darkPurple1.howManyMixes(), 2) &&
                        t.checkExpect(this.khaki1.howManyMixes(), 1) &&
                        t.checkExpect(this.yellow1.howManyMixes(), 2) &&
                        t.checkExpect(this.avocado1.howManyMixes(), 1) &&
                        t.checkExpect(this.orange1.howManyMixes(), 1);
    }

    //tests formulaDepth()
    boolean testformulaDepth(Tester t) {
        return
                //solids
                t.checkExpect(this.red.formulaDepth(), 0) &&
                        t.checkExpect(this.green.formulaDepth(), 0) &&
                        t.checkExpect(this.blue.formulaDepth(), 0) &&
                        //combos
                        t.checkExpect(this.purple.formulaDepth(), 1) &&
                        t.checkExpect(this.darkPurple.formulaDepth(), 2) &&
                        t.checkExpect(this.khaki.formulaDepth(), 1) &&
                        t.checkExpect(this.yellow.formulaDepth(), 2) &&
                        t.checkExpect(this.avocado.formulaDepth(), 1) &&
                        t.checkExpect(this.orange.formulaDepth(), 1) &&
                        t.checkExpect(new Combo("dark purple", new Blend(this.darkPurple,
                                this.darkPurple)).formulaDepth(), 3);
    }

    //tests calculateDepth()
    boolean testcalculateDepth(Tester t) {
        return
                t.checkExpect(this.purple1.calculateDepth(), 1) &&
                        t.checkExpect(this.darkPurple1.calculateDepth(), 2) &&
                        t.checkExpect(this.khaki1.calculateDepth(), 1) &&
                        t.checkExpect(this.yellow1.calculateDepth(), 2) &&
                        t.checkExpect(this.avocado1.calculateDepth(), 1) &&
                        t.checkExpect(this.orange1.calculateDepth(), 1);
    }


    //tests invert()
    boolean testinvert(Tester t) {
        return
                //solids
                t.checkExpect(this.red.invert(), this.red) &&
                        t.checkExpect(this.green.invert(), this.green) &&
                        t.checkExpect(this.blue.invert(), this.blue) &&
                        //combos
                        t.checkExpect(this.purple.invert(),
                                (new Combo("purple", new Blend(this.red, this.blue)))) &&
                        t.checkExpect(this.darkPurple.invert(),
                                (new Combo("dark purple", new Brighten(new Combo("purple",
                                        new Blend(this.red, this.blue)))))) &&
                        t.checkExpect(this.khaki.invert(),
                                (new Combo("khaki", new Blend(this.red, this.green)))) &&
                        t.checkExpect(this.yellow.invert(),
                                (new Combo("yellow", new Darken(new Combo("khaki",
                                        new Blend(this.red, this.green)))))) &&
                        t.checkExpect(this.avocado.invert(),
                                (new Combo("avocado", new Brighten(this.green)))) &&
                        t.checkExpect(this.orange.invert(),
                                (new Combo("orange", new Darken(this.red))));
    }

    //tests switchOperation()
    boolean testswitchOperation(Tester t) {
        return
                t.checkExpect(this.purple1.switchOperation(),
                        (new Blend(this.red, this.blue))) &&
                        t.checkExpect(this.darkPurple1.switchOperation(),
                                (new Brighten(new Combo("purple",
                                        new Blend(this.red, this.blue))))) &&
                        t.checkExpect(this.khaki1.switchOperation(),
                                (new Blend(this.red, this.green))) &&
                        t.checkExpect(this.yellow1.switchOperation(),
                                (new Darken(new Combo("khaki",
                                        new Blend(this.red, this.green))))) &&
                        t.checkExpect(this.avocado1.switchOperation(),
                                (new Brighten(this.green))) &&
                        t.checkExpect(this.orange1.switchOperation(),
                                (new Darken(this.red)));
    }

    //tests mixingFormula()
    boolean testmixingFormula(Tester t) {
        return
                //solids
                t.checkExpect(this.red.mixingFormula(0), "red") &&
                        t.checkExpect(this.green.mixingFormula(0), "green") &&
                        t.checkExpect(this.blue.mixingFormula(1), "blue") &&
                        //combos
                        t.checkExpect(this.purple.mixingFormula(0), "purple") &&
                        t.checkExpect(this.purple.mixingFormula(1), "blend(red, blue)") &&
                        t.checkExpect(this.darkPurple.mixingFormula(0), "dark purple") &&
                        t.checkExpect(this.darkPurple.mixingFormula(1), "darken(purple)") &&
                        t.checkExpect(this.darkPurple.mixingFormula(2), "darken(blend(red, blue))") &&
                        t.checkExpect(this.yellow.mixingFormula(0), "yellow") &&
                        t.checkExpect(this.yellow.mixingFormula(1), "brighten(khaki)") &&
                        t.checkExpect(this.yellow.mixingFormula(2), "brighten(blend(red, green))");

    }

    //tests writeFormula(int depth)
    boolean testwriteFormula(Tester t) {
        return
                t.checkExpect(this.purple1.writeFormula(0), "blend(red, blue)") &&
                        t.checkExpect(this.darkPurple1.writeFormula(0), "darken(purple)") &&
                        t.checkExpect(this.darkPurple1.writeFormula(2), "darken(blend(red, blue))") &&
                        t.checkExpect(this.khaki1.writeFormula(0), "blend(red, green)") &&
                        t.checkExpect(this.yellow1.writeFormula(0), "brighten(khaki)") &&
                        t.checkExpect(this.yellow1.writeFormula(2), "brighten(blend(red, green))") &&
                        t.checkExpect(this.avocado1.writeFormula(0), "darken(green)") &&
                        t.checkExpect(this.orange1.writeFormula(0), "brighten(red)");
    }

}
