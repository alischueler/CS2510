import tester.Tester;

class BagelRecipe { //represents a Bagel Recipe
  double flour;
  double water;
  double yeast;
  double salt;
  double malt;



  BagelRecipe(double flour, double water, double yeast, double salt, double malt) {
    this.flour = checkRange(flour, water, "Water weight is not equal to flour weight");
    this.water = checkRange(water, flour, "Water weight is not equal to flour weight");
    this.yeast = checkRange(yeast, malt, "Yeast weight is not equal to malt weight");
    this.salt = checkRange(salt, ((flour / 20) - yeast), "The sum of the weights of salt "
            + "and yeast are not equal to 1/20th the weight of flour");
    this.malt = checkRange(malt, yeast, "Yeast weight is not equal to malt weight");
  }

  BagelRecipe(double flour, double yeast) {
    this(flour, flour, yeast, ((flour / 20) - yeast), yeast);
  }

  BagelRecipe(double flour, double yeast, double salt) {
    this((flour * 4.25), ((4.25 / 8) * (flour * 8)), ((yeast / 48) * 5), (10 * (salt / 48)),
            ((yeast / 48) * 5));
  }

  //determines of the given BagelRecipe is the same as this BagelRecipe
  boolean sameRecipe(BagelRecipe other) {
    return (Math.abs(this.flour - other.flour) <= 0.001)
            && (Math.abs(this.water - other.water) <= 0.001)
            && (Math.abs(this.yeast - other.yeast) <= 0.001)
            && (Math.abs(this.salt - other.salt) <= 0.001)
            && (Math.abs(this.malt - other.malt) <= 0.001);


  }

  //checks if the given ingredientS are within the range of 0.001
  double checkRange(double i1, double i2, String message) {
    if ((Math.abs(i1 - i2) <= 0.001) && (i1 > 0)) {
      return i1;
    }
    else {
      throw new IllegalArgumentException(message);
    }
  }

}


class ExamplesBagelRecipe {

  //examples of Perfect Bagel Recipes
  BagelRecipe b1 = new BagelRecipe(30.0, 30.0, 0.9, 0.6, 0.9);
  BagelRecipe b2 = new BagelRecipe(60.0, 60.0, 1.8, 1.2, 1.8);
  BagelRecipe b3 = new BagelRecipe(10.0, 10.0, 0.3, 0.2, 0.3);
  BagelRecipe b4 = new BagelRecipe(20.0, 20.0, 0.6, 0.4, 0.6);

  //tests for sameRecipe(BagelRewcipe other)
  boolean testsameRecipe(Tester t) {
    return
            t.checkExpect(this.b3.sameRecipe(this.b3), true)
                    && t.checkExpect(this.b3.sameRecipe(this.b4), false)
                    && t.checkExpect(this.b4.sameRecipe(this.b3), false)
                    && t.checkExpect(this.b1.sameRecipe(this.b1), true)
                    && t.checkExpect(this.b2.sameRecipe(this.b1), false)
                    && t.checkExpect(this.b1.sameRecipe(this.b2), false)
                    && t.checkConstructorException(new IllegalArgumentException(
                            "Water weight is not equal to flour weight"),
                    "BagelRecipe", 50.0, 40.0, 20.0, 10.0, 5.0)
                    && t.checkConstructorException(new IllegalArgumentException(
                            "Water weight is not equal to flour weight"),
                    "BagelRecipe", 22.0, 20.0, 20.0, 20.0, 20.0)
                    && t.checkConstructorException(new IllegalArgumentException(
                            "Water weight is not equal to flour weight"),
                    "BagelRecipe", 22.0, 20.0, 20.0, 20.0, -20.0)
                    && t.checkConstructorException(new IllegalArgumentException(
                            "Yeast weight is not equal to malt weight"),
                    "BagelRecipe", 20.0, 20.0, 70.0, 20.0, 22.0)
                    && t.checkConstructorException(new IllegalArgumentException(
                            "Yeast weight is not equal to malt weight"),
                    "BagelRecipe", 30.0, 30.0, 40.0, 33.0, 30.0)
                    && t.checkConstructorException(
                    new IllegalArgumentException("Yeast weight is not equal to malt weight"),
                    "BagelRecipe", 20.0, 20.0, -70.0, 20.0, 22.0)
                    && t.checkConstructorException(
                    new IllegalArgumentException("The sum of the weights of salt and "
                            + "yeast are not equal to 1/20th the weight of flour"),
                    "BagelRecipe", 20.0, 20.0, 70.0, 20.0, 70.0)
                    && t.checkConstructorException(
                    new IllegalArgumentException("Yeast weight is not equal to malt weight"),
                    "BagelRecipe", 30.0, 30.0, -80.0, 39.0, -80.0)
                    && t.checkConstructorException(
                    new IllegalArgumentException(
                            "The sum of the weights of salt and yeast are not "
                                    + "equal to 1/20th the weight of flour"),
                    "BagelRecipe", 56.0, 56.0, 40.0, 20.0, 40.0)
                    && t.checkConstructorException(
                    new IllegalArgumentException("The sum of the weights of salt and yeast "
                            + "are not equal to 1/20th the weight of flour"),
                    "BagelRecipe", 70.0, 70.0, 20.0, -20.0, 20.0);

  }
}