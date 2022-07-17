import tester.Tester;

interface IFunc<A, R> {

    //applies an operation to the given A
    R apply(A arg);
}


interface IFunc2<A1, A2, R> {

    //applies an operation to the given A1 and A2
    R apply(A1 arg1, A2 arg2);
}

class AddInputs implements IFunc2<Double, Double, Double> { //represents addition
    //adds the arguments
    public Double apply(Double arg1, Double arg2) {
        return arg1 + arg2;
    }
}

class SubInputs implements IFunc2<Double, Double, Double> { //represents subtraction

    //subtracts the arguments
    public Double apply(Double arg1, Double arg2) {
        return arg1 - arg2;
    }
}

class MultInputs implements IFunc2<Double, Double, Double> { //represents multiplication

    //multiplies the arguments
    public Double apply(Double arg1, Double arg2) {
        return arg1 * arg2;
    }
}

class DivInputs implements IFunc2<Double, Double, Double> { //represents division

    //divides the arguments
    public Double apply(Double arg1, Double arg2) {
        return arg1 / arg2;
    }
}

interface IArithVisitor<R> extends IFunc<IArith, R> {

    //visits a constant
    R visit(Const c);

    //visits a Formula
    R visit(Formula f);
}


class EvalVisitor implements IArithVisitor<Double> {
    //evaluates a Const or a Formula to a single double

    //The given IArith accepts this visitor
    public Double apply(IArith ia) {
        return ia.accept(this);
    }

    //visits a Const and evaluates the tree to a Double answer
    public Double visit(Const c) {
        return c.num;
    }

    //visits a Formula and evaluates the tree to a Double answer
    public Double visit(Formula f) {
        Double leftVal = f.left.accept(this);
        Double rightVal = f.right.accept(this);

        return f.fun.apply(leftVal, rightVal);
    }
}

class PrintVisitor implements IArithVisitor<String> {
    //prints the operation of a Const or a Formula as a String

    //The given IArith accepts this visitor
    public String apply(IArith ia) {
        return ia.accept(this);
    }

    //visits a Const and evaluates the tree to a single String
    public String visit(Const c) {
        return "" + c.num + "";
    }

    //visits a Const and evaluates the tree to a single String
    public String visit(Formula f) {
        String leftString = f.left.accept(this);
        String rightString = f.right.accept(this);
        return "(" + f.name + " " + leftString + " " + rightString + ")";
    }
}

class DoublerVisitor implements IArithVisitor<IArith> {
    //Doubles every Const in a Const or a Formula

    //The given IArith accepts this visitor
    public IArith apply(IArith ia) {
        return ia.accept(this);
    }

    //visits a Const and evaluates the tree to a single IArith
    public IArith visit(Const c) {
        return new Const(c.num * 2);
    }

    //visits a Const and evaluates the tree to a single IArith
    public IArith visit(Formula f) {
        IArith leftDouble = f.left.accept(this);
        IArith rightDouble = f.right.accept(this);
        return new Formula(f.fun, f.name, leftDouble, rightDouble);
    }
}

class AllSmallVisitor implements IArithVisitor<Boolean> {
    //determines if all Const in the Const or Formula are smaller than 10

    //The given IArith accepts this visitor
    public Boolean apply(IArith ia) {
        return ia.accept(this);
    }

    //visits a Const and evaluates the tree to a single Boolean
    public Boolean visit(Const c) {
        return c.num < 10;
    }

    //visits a Const and evaluates the tree to a single Boolean
    public Boolean visit(Formula f) {
        Boolean leftSmall = f.left.accept(this);
        Boolean rightSmall = f.right.accept(this);
        return leftSmall && rightSmall;
    }
}

class NoDivBy0 implements IArithVisitor<Boolean> {
    //determines if a Formula uses division and if the denominator is greater than 0.0001 (roughly 0)

    //The given IArith accepts this visitor
    public Boolean apply(IArith ia) {
        return ia.accept(this);
    }

    //visits a Const and evaluates the tree to a single Boolean
    public Boolean visit(Const c) {
        return true;
    }

    //visits a Const and evaluates the tree to a single Boolean
    public Boolean visit(Formula f) {
        Boolean leftDiv = f.left.accept(this);
        Boolean rightDiv = f.right.accept(this);
        IArithVisitor<Double> evalVis = new EvalVisitor();
        Double rightDouble = f.right.accept(evalVis);
        if (f.name.equals("div")) {
            return (f.name.equals("div")) && (Math.abs(rightDouble) > 0.0001)
                    && leftDiv && rightDiv;
        }
        else {
            return leftDiv && rightDiv;
        }
    }
}

interface IArith { //represents an Arithmetic Statement

    //accepts a visitor to this IArith
    <R> R accept(IArithVisitor<R> ia);
}

class Const implements IArith { //represents a constant number
    double num;

    Const(double num) {
        this.num = num;
    }

    //accepts a visitor to this Const
    public <R> R accept(IArithVisitor<R> ia) {
        return ia.visit(this);
    }

}

class Formula implements IArith { //represents an Arithmetic Formula
    IFunc2<Double, Double, Double> fun;
    String name;
    IArith left;
    IArith right;

    Formula(IFunc2<Double, Double, Double> fun, String name, IArith left, IArith right) {
        this.fun = fun;
        this.name = name;
        this.left = left;
        this.right = right;
    }

    //accepts a visitor to this Formula
    public <R> R accept(IArithVisitor<R> ia) {
        return ia.visit(this);
    }
}


class ExamplesIArith {
    IArith c1 = new Const(5);
    IArith c2 = new Const(10);
    IArith c3 = new Const(4);
    IArith c4 = new Const(0);
    IFunc<IArith, Double> evalVisitor = new EvalVisitor();
    IFunc<IArith, String> printVisitor = new PrintVisitor();
    IFunc<IArith, IArith> doublerVisitor = new DoublerVisitor();
    IFunc<IArith, Boolean> allSmallVisitor = new AllSmallVisitor();
    IFunc<IArith, Boolean> noDivBy0 = new NoDivBy0();
    IFunc2<Double, Double, Double> plus = new AddInputs();
    IFunc2<Double, Double, Double> multiply = new MultInputs();
    IFunc2<Double, Double, Double> minus = new SubInputs();
    IFunc2<Double, Double, Double> divide = new DivInputs();
    IArith f1 = new Formula(this.plus, "plus", this.c1, this.c2);
    IArith f2 = new Formula(this.multiply, "multiply", this.c1, this.c3);
    IArith f3 = new Formula(this.plus, "plus", this.c1, this.f2);
    IArith f4 = new Formula(this.plus, "plus", this.f2, this.c1);
    IArith f5 = new Formula(this.minus, "minus", this.f4, this.f2);
    IArith f6 = new Formula(this.divide, "div", this.c1, this.c4);
    IArith f7 = new Formula(this.minus, "minus", this.f5, this.c1);
    IArith f8 = new Formula(this.divide, "div", this.f3, this.f7);
    IArith f9 = new Formula(this.minus, "div", this.f8, this.f3);

    //tests for Apply
    boolean testApply(Tester t) {
        //tests for EvalVisitor
        return t.checkExpect(this.evalVisitor.apply(this.c1), 5.0)
                && t.checkExpect(this.evalVisitor.apply(this.c2), 10.0)
                && t.checkExpect(this.evalVisitor.apply(this.c3), 4.0)
                && t.checkExpect(this.evalVisitor.apply(this.f1), 15.0)
                && t.checkExpect(this.evalVisitor.apply(this.f2), 20.0)
                && t.checkExpect(this.evalVisitor.apply(this.f3), 25.0)
                && t.checkExpect(this.evalVisitor.apply(this.f4), 25.0)
                //tests for PrintVisitor
                && t.checkExpect(this.printVisitor.apply(this.c1), "5.0")
                && t.checkExpect(this.printVisitor.apply(this.c2), "10.0")
                && t.checkExpect(this.printVisitor.apply(this.c3), "4.0")
                && t.checkExpect(this.printVisitor.apply(this.f1), "(plus 5.0 10.0)")
                && t.checkExpect(this.printVisitor.apply(this.f2), "(multiply 5.0 4.0)")
                && t.checkExpect(this.printVisitor.apply(this.f3), "(plus 5.0 (multiply 5.0 4.0))")
                && t.checkExpect(this.printVisitor.apply(this.f4), "(plus (multiply 5.0 4.0) 5.0)")
                //tests DoublerVisitor
                && t.checkExpect(this.doublerVisitor.apply(this.c1),
                new Const(10))
                && t.checkExpect(this.doublerVisitor.apply(this.f1),
                new Formula(this.plus, "plus", new Const(10.0), new Const(20.0)))
                && t.checkExpect(this.doublerVisitor.apply(this.f2),
                new Formula(this.multiply, "multiply", new Const(10.0), new Const(8.0)))
                && t.checkExpect(this.doublerVisitor.apply(this.f3),
                new Formula(this.plus, "plus", new Const(10.0),
                        new Formula(this.multiply, "multiply", new Const(10.0), new Const(8.0))))
                //tests AllSmallVisitor
                && t.checkExpect(this.allSmallVisitor.apply(this.c1), true)
                && t.checkExpect(this.allSmallVisitor.apply(this.c2), false)
                && t.checkExpect(this.allSmallVisitor.apply(this.c3), true)
                && t.checkExpect(this.allSmallVisitor.apply(this.f1), false)
                && t.checkExpect(this.allSmallVisitor.apply(this.f2), true)
                && t.checkExpect(this.allSmallVisitor.apply(this.f5), true)
                //tests NoDivBy0
                && t.checkExpect(this.noDivBy0.apply(this.c1), true)
                && t.checkExpect(this.noDivBy0.apply(this.c2), true)
                && t.checkExpect(this.noDivBy0.apply(this.c3), true)
                && t.checkExpect(this.noDivBy0.apply(this.c4), true)
                && t.checkExpect(this.noDivBy0.apply(this.f1), true)
                && t.checkExpect(this.noDivBy0.apply(this.f2), true)
                && t.checkExpect(this.noDivBy0.apply(this.f6), false)
                && t.checkExpect(this.noDivBy0.apply(this.f7), true)
                && t.checkExpect(this.noDivBy0.apply(this.f8), false)
                && t.checkExpect(this.noDivBy0.apply(this.f9), false);
    }

    //tests for Accept
    boolean testAccept(Tester t) {
        //tests for EvalVisitor
        return t.checkExpect(this.c1.accept((EvalVisitor)this.evalVisitor), 5.0)
                && t.checkExpect(this.c2.accept((EvalVisitor)this.evalVisitor), 10.0)
                && t.checkExpect(this.c3.accept((EvalVisitor)this.evalVisitor), 4.0)
                && t.checkExpect(this.f1.accept((EvalVisitor)this.evalVisitor), 15.0)
                && t.checkExpect(this.f2.accept((EvalVisitor)this.evalVisitor), 20.0)
                && t.checkExpect(this.f3.accept((EvalVisitor)this.evalVisitor), 25.0)
                && t.checkExpect(this.f4.accept((EvalVisitor)this.evalVisitor), 25.0)
                //tests for PrintVisitor
                && t.checkExpect(this.c1.accept((PrintVisitor)this.printVisitor),
                "5.0")
                && t.checkExpect(this.c2.accept((PrintVisitor)this.printVisitor),
                "10.0")
                && t.checkExpect(this.c3.accept((PrintVisitor)this.printVisitor),
                "4.0")
                && t.checkExpect(this.f1.accept((PrintVisitor)this.printVisitor),
                "(plus 5.0 10.0)")
                && t.checkExpect(this.f2.accept((PrintVisitor)this.printVisitor),
                "(multiply 5.0 4.0)")
                && t.checkExpect(this.f3.accept((PrintVisitor)this.printVisitor),
                "(plus 5.0 (multiply 5.0 4.0))")
                && t.checkExpect(this.f4.accept((PrintVisitor)this.printVisitor),
                "(plus (multiply 5.0 4.0) 5.0)")
                //tests DoublerVisitor
                && t.checkExpect(this.c1.accept((DoublerVisitor)this.doublerVisitor),
                new Const(10))
                && t.checkExpect(this.f1.accept((DoublerVisitor)this.doublerVisitor),
                new Formula(this.plus, "plus", new Const(10.0), new Const(20.0)))
                && t.checkExpect(this.f2.accept((DoublerVisitor)this.doublerVisitor),
                new Formula(this.multiply, "multiply", new Const(10.0), new Const(8.0)))
                && t.checkExpect(this.f3.accept((DoublerVisitor)doublerVisitor),
                new Formula(this.plus, "plus", new Const(10.0),
                        new Formula(this.multiply, "multiply", new Const(10.0), new Const(8.0))))
                //tests AllSmallVisitor
                && t.checkExpect(this.c1.accept((AllSmallVisitor)this.allSmallVisitor), true)
                && t.checkExpect(this.c2.accept((AllSmallVisitor)this.allSmallVisitor), false)
                && t.checkExpect(this.c3.accept((AllSmallVisitor)this.allSmallVisitor), true)
                && t.checkExpect(this.f1.accept((AllSmallVisitor)this.allSmallVisitor), false)
                && t.checkExpect(this.f2.accept((AllSmallVisitor)this.allSmallVisitor), true)
                && t.checkExpect(this.f5.accept((AllSmallVisitor)this.allSmallVisitor), true)
                //tests NoDivBy0
                && t.checkExpect(this.c1.accept((NoDivBy0)this.noDivBy0), true)
                && t.checkExpect(this.c2.accept((NoDivBy0)this.noDivBy0), true)
                && t.checkExpect(this.c3.accept((NoDivBy0)this.noDivBy0), true)
                && t.checkExpect(this.c4.accept((NoDivBy0)this.noDivBy0), true)
                && t.checkExpect(this.f1.accept((NoDivBy0)this.noDivBy0), true)
                && t.checkExpect(this.f2.accept((NoDivBy0)this.noDivBy0), true)
                && t.checkExpect(this.f6.accept((NoDivBy0)this.noDivBy0), false)
                && t.checkExpect(this.f7.accept((NoDivBy0)this.noDivBy0), true)
                && t.checkExpect(this.f8.accept((NoDivBy0)this.noDivBy0), false)
                && t.checkExpect(this.f9.accept((NoDivBy0)this.noDivBy0), false);
    }

    //tests for Visit
    boolean testVisit(Tester t) {
        //tests for EvalVisitor
        return t.checkExpect(new EvalVisitor().visit((Const)this.c1), 5.0)
                && t.checkExpect(new EvalVisitor().visit((Const)this.c2), 10.0)
                && t.checkExpect(new EvalVisitor().visit((Const)this.c3), 4.0)
                && t.checkExpect(new EvalVisitor().visit((Formula)this.f1), 15.0)
                && t.checkExpect(new EvalVisitor().visit((Formula)this.f2), 20.0)
                && t.checkExpect(new EvalVisitor().visit((Formula)this.f3), 25.0)
                && t.checkExpect(new EvalVisitor().visit((Formula)this.f4), 25.0)
                //tests for PrintVisitor
                && t.checkExpect(new PrintVisitor().visit((Const)this.c1),
                "5.0")
                && t.checkExpect(new PrintVisitor().visit((Const)this.c2),
                "10.0")
                && t.checkExpect(new PrintVisitor().visit((Const)this.c3),
                "4.0")
                && t.checkExpect(new PrintVisitor().visit((Formula)this.f1),
                "(plus 5.0 10.0)")
                && t.checkExpect(new PrintVisitor().visit((Formula)this.f2),
                "(multiply 5.0 4.0)")
                && t.checkExpect(new PrintVisitor().visit((Formula)this.f3),
                "(plus 5.0 (multiply 5.0 4.0))")
                && t.checkExpect(new PrintVisitor().visit((Formula)this.f4),
                "(plus (multiply 5.0 4.0) 5.0)")
                //tests DoublerVisitor
                && t.checkExpect(new DoublerVisitor().visit((Const)this.c1),
                new Const(10))
                && t.checkExpect(new DoublerVisitor().visit((Formula)this.f1),
                new Formula(this.plus, "plus", new Const(10.0), new Const(20.0)))
                && t.checkExpect(new DoublerVisitor().visit((Formula)this.f2),
                new Formula(this.multiply, "multiply", new Const(10.0), new Const(8.0)))
                && t.checkExpect(new DoublerVisitor().visit((Formula)this.f3),
                new Formula(this.plus, "plus", new Const(10.0),
                        new Formula(this.multiply, "multiply", new Const(10.0), new Const(8.0))))
                //tests AllSmallVisitor
                && t.checkExpect(new AllSmallVisitor().visit((Const)this.c1), true)
                && t.checkExpect(new AllSmallVisitor().visit((Const)this.c2), false)
                && t.checkExpect(new AllSmallVisitor().visit((Const)this.c3), true)
                && t.checkExpect(new AllSmallVisitor().visit((Formula)this.f1), false)
                && t.checkExpect(new AllSmallVisitor().visit((Formula)this.f2), true)
                && t.checkExpect(new AllSmallVisitor().visit((Formula)this.f5), true)
                //tests NoDivBy0
                && t.checkExpect(new NoDivBy0().visit((Const)this.c1), true)
                && t.checkExpect(new NoDivBy0().visit((Const)this.c2), true)
                && t.checkExpect(new NoDivBy0().visit((Const)this.c3), true)
                && t.checkExpect(new NoDivBy0().visit((Const)this.c4), true)
                && t.checkExpect(new NoDivBy0().visit((Formula)this.f1), true)
                && t.checkExpect(new NoDivBy0().visit((Formula)this.f2), true)
                && t.checkExpect(new NoDivBy0().visit((Formula)this.f6), false)
                && t.checkExpect(new NoDivBy0().visit((Formula)this.f7), true)
                && t.checkExpect(new NoDivBy0().visit((Formula)this.f8), false)
                && t.checkExpect(new NoDivBy0().visit((Formula)this.f9), false);
    }

}