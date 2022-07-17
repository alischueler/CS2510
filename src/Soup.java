interface ISoup {
  
}

class Broth implements ISoup {
  String type;

  Broth(String type) {
    this.type = type;
  }
}

class Ingredient implements ISoup {
  ISoup more;
  String name;

  Ingredient(ISoup more, String name) {
    this.more = more;
    this.name = name;
  }
}

class ExamplesSoup {
  ISoup chicken = new Broth("chicken");
  ISoup yummy1 = new Ingredient(this.chicken, "carrots");
  ISoup yummy2 = new Ingredient(this.yummy1, "celery");
  ISoup yummy = new Ingredient(this.yummy2, "noodles");
  ISoup vanilla = new Broth("vanilla");
  ISoup noThankYou1 = new Ingredient(this.vanilla, "horseradish");
  ISoup noThankYou2 = new Ingredient(this.noThankYou1, "hot dogs");
  ISoup noThankYou = new Ingredient(this.noThankYou2, "plum sauce");
}