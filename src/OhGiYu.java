//to represent a resource in a game
interface IResource {

}

//to represent a monster in a game
class Monster implements IResource {
  String name;
  int hp;
  int attack;

  //the constructor
  Monster(String name, int hp, int attack) {
    this.name = name;
    this.hp = hp;
    this.attack = attack;
  }
}

//to represent the union of two monsters in a game
class Fusion implements IResource {
  String name;
  IResource monster1;
  IResource monster2;

  //the constructor
  Fusion(String name, IResource monster1, IResource monster2) {
    this.name = name;
    this.monster1 = monster1;
    this.monster2 = monster2;
  }
}

//to represent a trap in a game
class Trap implements IResource {
  String description;
  boolean continuous;

  //the constructor
  Trap(String description, boolean continuous) {
    this.description = description;
    this.continuous = continuous;
  }
}

//to represent an action taken in the game
interface IAction {

}

//to represent an attack in a game
class Attack implements IAction {
  IResource attacker;
  IResource defender;
  boolean isattackergreater;

  //the constructor
  Attack(IResource attacker, IResource defender, boolean isattackergreater) {
    this.attacker = attacker;
    this.defender = defender;
    this.isattackergreater = isattackergreater;
  }
}

//to represent a trap placed on another game piece in a game
class Activate implements IAction {
  IResource trap;
  IResource target;

  //the constructor
  Activate(IResource trap, IResource target) {
    this.trap = trap;
    this.target = target;
  }
}

//examples of resources and actions in the game
class ExamplesGame {
  ExamplesGame() {}
  
  //Resource Examples
  IResource kuriboh = new Monster("Kuriboh", 200, 100);
  IResource jinzo = new Monster("Jinzo", 500, 400);
  IResource shoe = new Monster("Shoe", 100, 600);
  IResource trapHole = new Trap("Kills a monster", false);
  IResource kurizo = new Fusion("Kurizo", this.kuriboh, this.jinzo);
  IResource shoelaces = new Fusion("Shoelaces", this.shoe, this.jinzo);

  //Action Examples
  IAction attack1 = new Attack(this.jinzo, this.kuriboh, true);
  IAction attack2 = new Attack(this.shoe, this.kuriboh, true);
  IAction activate1 = new Activate(this.trapHole, this.shoelaces);
  IAction activate2 = new Activate(this.trapHole, this.jinzo);
  
  //I believe this is a poor design choice because
  //activates and attacks are both composed of Resources
  //so it does not make sense to separate them, in Actions.
  //A better design for this would have been to keep Attack
  //and Activate classes under IResource("one of") and called
  //on Monsters, Fusion, or Trap classes that could have potentially
  //been apart of another class("has one").
}