// CS 2510, Assignment 3

import tester.*;

// to represent a list of Strings
interface ILoString {
  // combine all Strings in this list into one
  String combine();

  //sorts this ILoString alphabetically
  ILoString sort();

  //inserts the given string into this ILoString
  ILoString insert(String that);

  //determines if this ILoString is sorted
  boolean isSorted();

  //determines if the given String is alphabetically before or the same to this String
  //accumulator: keeps track of all strings compared in the list
  boolean order(String acc);

  //weaves this ILoString with the given ILoString
  ILoString interleave(ILoString that);

  //produces a sorted list of Strings that contains all Strings in this ILoString
  //and the given ILoString
  ILoString merge(ILoString that);

  //reverses the order of Strings of this ILoString
  ILoString reverse();

  //helps to reverse the order of Strings of this ILoString
  //accumulator: keeps track of the Strings of this ILoString already reversed
  ILoString reverseHelp(ILoString acc);


  //determines if this ILoString contains pairs of identical strings
  boolean isDoubledList();

  //is the given string equivalent to the string next to it in this ILoString
  boolean anyDoubles(String comparing);

  //determines whether this list contains the same Strings reading the list in 
  //either order
  boolean isPalindromeList();

  //helps to determine whether this list contains the same Strings reading the list in 
  //either order
  boolean isPalindromeHelp(ILoString reversed);
}

// to represent an empty list of Strings
class MtLoString implements ILoString {
  MtLoString(){}

  /*
  TEMPLATE
  METHODS
  ... this.combine() ...     -- String
  ... this.sort() ...     -- ILoString
  ... this.insert(String that) ...     -- ILoString
  ... this.isSorted() ...     -- boolean
  ... this.order(String acc) ...     -- boolean
  ... this.interleave(ILoString that) ...     -- ILoString
  ... this.merge(ILoString that) ...     -- ILoString
  ... this.reverse() ...     -- ILoString
  ... this.reverseHelp(ILoString acc) ...     -- ILoString
  ... this.isDoubledList() ...     -- boolean
  ... this.anyDoubles(String comparing) ...     -- boolean
  ... this.isPalindrome() ...     -- boolean
  ... this.isPalindromeHelp(ILoString reversed) ...     -- boolean

   */

  // combine all Strings in this list into one
  public String combine() {
    return "";
  }

  //sorts this MtLoString alphabetically
  public ILoString sort() {
    return new MtLoString();
  }

  //inserts the given string into this MtLoString
  public ILoString insert(String that) {
    return new ConsLoString(that, this);
  }

  //determines if this MtLoString is sorted alphabetically
  public boolean isSorted() {
    return true;
  }

  //determines if the given String is alphabetically before or the same as this 
  //MtLoString
  public boolean order(String comparing) {
    return true;
  }

  //weaves this MtLoString with the given ILoString
  public ILoString interleave(ILoString that) {
    return that;
  }

  //produces a sorted list of Strings that contains all Strings in this MtLoString
  //and the given ILoString
  public ILoString merge(ILoString that) {
    return that.sort();
  }

  //reverses the order of this MtLoString
  public ILoString reverse() {
    return new MtLoString();
  }

  //helps to reverse the order of this MtLoString
  //accumulator: keeps track of the Strings of this ILoString already reversed
  public ILoString reverseHelp(ILoString acc) {
    return acc;
  }

  //determines if this MtLoString contains pairs of identical strings
  public boolean isDoubledList() {
    return true;
  }

  //is the given string equivalent to the String next to it in this MtLoString
  public boolean anyDoubles(String comparing) {
    return false;
  }

  //determines whether this MtLoString contains the same Strings reading the list in 
  //either order
  public boolean isPalindromeList() {
    return true;
  }

  //helps to determines whether this list contains the same Strings reading the list in 
  //either order
  public boolean isPalindromeHelp(ILoString reversed) {
    return true;
  }

}

// to represent a nonempty list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }
  /*
  TEMPLATE
  FIELDS:
    ... this.first ...         -- String
    ... this.rest ...          -- ILoString

    METHODS
    ... this.combine() ...     -- String
    ... this.sort() ...     -- ILoString
    ... this.insert(String that) ...     -- ILoString
    ... this.isSorted() ...     -- boolean
    ... this.order(String acc) ...     -- boolean
    ... this.interleave(ILoString that) ...     -- ILoString
    ... this.merge(ILoString that) ...     -- ILoString
    ... this.reverse() ...     -- ILoString
    ... this.reverseHelp(ILoString acc) ...     -- ILoString
    ... this.isDoubledList() ...     -- boolean
    ... this.anyDoubles(String comparing) ...     -- boolean
    ... this.isPalindrome() ...     -- boolean
    ... this.isPalindromeHelp(ILoString reversed) ...     -- boolean

    METHODS FOR FIELDS
    ... this.first.concat(String) ...        -- String
    ... this.first.compareTo(String) ...     -- int
    ... this.rest.combine() ...              -- String
    ... this.first.sort() ...     -- ILoString
    ... this.first.insert(String that) ...     -- ILoString
    ... this.first.isSorted() ...     -- boolean
    ... this.first.order(String acc) ...     -- boolean
    ... this.first.interleave(ILoString that) ...     -- ILoString
    ... this.first.merge(ILoString that) ...     -- ILoString
    ... this.first.reverse() ...     -- ILoString
    ... this.first.reverseHelp(ILoString acc) ...     -- ILoString
    ... this.first.isDoubledList() ...     -- boolean
    ... this.first.anyDoubles(String comparing) ...     -- boolean
    ... this.first.isPalindrome() ...     -- boolean
    ... this.first.isPalindromeHelp(ILoString reversed) ...     -- boolean
    ... this.rest.sort() ...     -- ILoString
    ... this.rest.insert(String that) ...     -- ILoString
    ... this.rest.isSorted() ...     -- boolean
    ... this.rest.order(String acc) ...     -- boolean
    ... this.rest.interleave(ILoString that) ...     -- ILoString
    ... this.rest.merge(ILoString that) ...     -- ILoString
    ... this.rest.reverse() ...     -- ILoString
    ... this.rest.reverseHelp(ILoString acc) ...     -- ILoString
    ... this.rest.isDoubledList() ...     -- boolean
    ... this.rest.anyDoubles(String comparing) ...     -- boolean
    ... this.rest.isPalindrome() ...     -- boolean
    ... this.rest.isPalindromeHelp(ILoString reversed) ...     -- boolean
   */

  // combine all Strings in this list into one
  public String combine() {
    return this.first.concat(this.rest.combine());
  }

  //sorts this ConsLoString alphabetically, ignoring caps
  public ILoString sort() {
    return this.rest.sort().insert(this.first);
  }

  //inserts the given String into this ConsLoString
  public ILoString insert(String that) {
    if (this.first.compareToIgnoreCase(that) < 0 ||
            this.first.compareToIgnoreCase(that) == 0) {
      return new ConsLoString(this.first, this.rest.insert(that));
    }
    else {
      return new ConsLoString(that, this);
    }
  }

  //determines of this ConsLoList is sorted
  public boolean isSorted() {
    return this.rest.order(this.first);
  }

  public boolean order(String comparing) {
    if (comparing.compareToIgnoreCase(this.first) < 0 ||
            comparing.compareToIgnoreCase(this.first) == 0) {
      return this.rest.order(this.first);
    }
    else {
      return false;
    }
  }

  //weaves this ConsLoString with the given List-of Strings
  public ILoString interleave(ILoString that) {
    return (new ConsLoString(this.first, that.interleave(this.rest)));
  }

  //produces a sorted list of Strings that contains all Strings in this ILoString
  //and the given ILoString
  public ILoString merge(ILoString that) {
    return this.interleave(that).sort();
  }

  //reverses the order of this ConsLoString
  public ILoString reverse() {
    return this.reverseHelp(new MtLoString());
  }

  //helps to reverse the order of this ConsLoString
  //accumulator: keeps track of the Strings of this ILoString already reversed
  public ILoString reverseHelp(ILoString acc) {
    return this.rest.reverseHelp(new ConsLoString(this.first, acc));
  }

  //determines if this ConsLoString contains pairs of identical Strings
  public boolean isDoubledList() {
    return this.rest.anyDoubles(this.first);
  }

  //determines if the given String is the same as this String
  public boolean anyDoubles(String comparing) {
    if (this.first.equals(comparing)) {
      return this.rest.isDoubledList();
    }
    else {
      return false;
    }
  }

  //determines whether this ConsLoString contains the same Strings reading the list in 
  //either order
  public boolean isPalindromeList() {
    return this.isPalindromeHelp(this.reverse());
  }

  //helps to determine whether this ConsLoString contains the same Strings reading the list in 
  //either order
  public boolean isPalindromeHelp(ILoString reversed) {
    return this.interleave(reversed).isDoubledList();
  }


}

// to represent examples for lists of strings
class ExamplesStrings {

  ILoString mt = new MtLoString();
  ILoString mary = new ConsLoString("Mary ",
          new ConsLoString("had ",
                  new ConsLoString("a ",
                          new ConsLoString("little ",
                                  new ConsLoString("lamb.", new MtLoString())))));
  ILoString alphabet = new ConsLoString("a",
          new ConsLoString("b",
                  new ConsLoString("c",
                          new ConsLoString("d",
                                  new ConsLoString("e", new MtLoString())))));
  ILoString letters = new ConsLoString("z",
          new ConsLoString("s",
                  new ConsLoString("b",
                          new ConsLoString("l",
                                  new ConsLoString("e", new MtLoString())))));
  ILoString shapes = new ConsLoString("circle",
          new ConsLoString("oval",
                  new ConsLoString("oval",
                          new ConsLoString("triangle",
                                  new ConsLoString("cube", new MtLoString())))));
  ILoString shapes2 = new ConsLoString("circle",
          new ConsLoString("circle",
                  new ConsLoString("oval",
                          new ConsLoString("triangle",
                                  new ConsLoString("cube", new MtLoString())))));
  ILoString animals = new ConsLoString("dog",
          new ConsLoString("dog",
                  new ConsLoString("cat",
                          new ConsLoString("cat",
                                  new ConsLoString("mouse",
                                          new ConsLoString("mouse", new MtLoString()))))));
  ILoString animals2 = new ConsLoString("bear",
          new ConsLoString("bear",
                  new ConsLoString("rat",
                          new ConsLoString("rat",
                                  new ConsLoString("horse",
                                          new ConsLoString("horse", new MtLoString()))))));

  // test the method combine for the lists of Strings
  boolean testCombine(Tester t) {
    return
            t.checkExpect(this.mary.combine(), "Mary had a little lamb.");
  }

  //test the method sort for the lists of Strings
  boolean testsort(Tester t) {
    return
            t.checkExpect(this.mt.sort(), new MtLoString()) &&
                    t.checkExpect(this.mary.sort(), new ConsLoString("a ",
                            new ConsLoString("had ",
                                    new ConsLoString("lamb.",
                                            new ConsLoString("little ",
                                                    new ConsLoString("Mary ", new MtLoString())))))) &&
                    t.checkExpect(this.alphabet.sort(), this.alphabet) &&
                    t.checkExpect(this.letters.sort(), new ConsLoString("b",
                            new ConsLoString("e",
                                    new ConsLoString("l",
                                            new ConsLoString("s",
                                                    new ConsLoString("z", new MtLoString()))))));
  }

  //test the method insert for the lists of Strings
  boolean testinsert(Tester t) {
    return
            t.checkExpect(this.mt.insert("l"), new ConsLoString("l", this.mt)) &&
                    t.checkExpect(this.mt.insert("shoe"), new ConsLoString("shoe", this.mt)) &&
                    t.checkExpect(this.mary.insert("aaron"), new ConsLoString("aaron",
                            new ConsLoString("Mary ",
                                    new ConsLoString("had ",
                                            new ConsLoString("a ",
                                                    new ConsLoString("little ",
                                                            new ConsLoString("lamb.", new MtLoString()))))))) &&
                    t.checkExpect(this.alphabet.insert("r"), new ConsLoString("a",
                            new ConsLoString("b",
                                    new ConsLoString("c",
                                            new ConsLoString("d",
                                                    new ConsLoString("e",
                                                            new ConsLoString("r", new MtLoString())))))));
  }

  // test the method isSorted for the lists of Strings
  boolean testisSorted(Tester t) {
    return
            t.checkExpect(this.mt.isSorted(), true) &&
                    t.checkExpect(this.mary.isSorted(), false) &&
                    t.checkExpect(this.alphabet.isSorted(), true) &&
                    t.checkExpect(this.letters.isSorted(), false) &&
                    t.checkExpect(this.shapes.isSorted(), false);
  }

  //test the method order for the lists of Strings
  boolean testorder(Tester t) {
    return
            t.checkExpect(this.mt.order("z"), true) &&
                    t.checkExpect(this.mary.order("z"), false) &&
                    t.checkExpect(this.alphabet.order("a"), true) &&
                    t.checkExpect(this.letters.order("r"), false) &&
                    t.checkExpect(this.shapes.order("rachel"), false);
  }

  //test the method interleave for the lists of Strings
  boolean testinterleave(Tester t) {
    return
            t.checkExpect(this.mt.interleave(this.mt), new MtLoString()) &&
                    t.checkExpect(this.mt.interleave(this.alphabet), this.alphabet) &&
                    t.checkExpect(this.mt.interleave(this.shapes), this.shapes) &&
                    t.checkExpect(this.mary.interleave(this.mt), this.mary) &&
                    t.checkExpect(this.alphabet.interleave(this.mt), this.alphabet) &&
                    t.checkExpect(this.letters.interleave(this.shapes), new ConsLoString("z",
                            new ConsLoString("circle",
                                    new ConsLoString("s",
                                            new ConsLoString("oval",
                                                    new ConsLoString("b",
                                                            new ConsLoString("oval",
                                                                    new ConsLoString("l",
                                                                            new ConsLoString("triangle",
                                                                                    new ConsLoString("e",
                                                                                            new ConsLoString("cube", this.mt))))))))))) &&
                    t.checkExpect(this.shapes.interleave(this.mary), new ConsLoString("circle",
                            new ConsLoString("Mary ",
                                    new ConsLoString("oval",
                                            new ConsLoString("had ",
                                                    new ConsLoString("oval",
                                                            new ConsLoString("a ",
                                                                    new ConsLoString("triangle",
                                                                            new ConsLoString("little ",
                                                                                    new ConsLoString("cube",
                                                                                            new ConsLoString("lamb.", this.mt)))))))))));
  }

  //test the method merge for the lists of Strings
  boolean testmerge(Tester t) {
    return
            t.checkExpect(this.mt.merge(this.mt), new MtLoString()) &&
                    t.checkExpect(this.mt.merge(this.alphabet), this.alphabet) &&
                    t.checkExpect(this.mt.merge(this.shapes), new ConsLoString("circle",
                            new ConsLoString("cube",
                                    new ConsLoString("oval",
                                            new ConsLoString("oval",
                                                    new ConsLoString("triangle", this.mt)))))) &&
                    t.checkExpect(this.mary.merge(this.mt), new ConsLoString("a ",
                            new ConsLoString("had ",
                                    new ConsLoString("lamb.",
                                            new ConsLoString("little ",
                                                    new ConsLoString("Mary ", new MtLoString())))))) &&
                    t.checkExpect(this.alphabet.merge(this.mt), this.alphabet) &&
                    t.checkExpect(this.letters.merge(this.shapes), new ConsLoString("b",
                            new ConsLoString("circle",
                                    new ConsLoString("cube",
                                            new ConsLoString("e",
                                                    new ConsLoString("l",
                                                            new ConsLoString("oval",
                                                                    new ConsLoString("oval",
                                                                            new ConsLoString("s",
                                                                                    new ConsLoString("triangle",
                                                                                            new ConsLoString("z", this.mt))))))))))) &&
                    t.checkExpect(this.shapes.merge(this.mary), new ConsLoString("a ",
                            new ConsLoString("circle",
                                    new ConsLoString("cube",
                                            new ConsLoString("had ",
                                                    new ConsLoString("lamb.",
                                                            new ConsLoString("little ",
                                                                    new ConsLoString("Mary ",
                                                                            new ConsLoString("oval",
                                                                                    new ConsLoString("oval",
                                                                                            new ConsLoString("triangle", this.mt)))))))))));
  }

  //test the method reverse for the lists of Strings
  boolean testreverse(Tester t) {
    return
            t.checkExpect(this.mt.reverse(), new MtLoString()) &&
                    t.checkExpect(this.mary.reverse(), new ConsLoString("lamb.",
                            new ConsLoString("little ",
                                    new ConsLoString("a ",
                                            new ConsLoString("had ",
                                                    new ConsLoString("Mary ", new MtLoString())))))) &&
                    t.checkExpect(this.alphabet.reverse(), new ConsLoString("e",
                            new ConsLoString("d",
                                    new ConsLoString("c",
                                            new ConsLoString("b",
                                                    new ConsLoString("a", new MtLoString())))))) &&
                    t.checkExpect(this.letters.reverse(), new ConsLoString("e",
                            new ConsLoString("l",
                                    new ConsLoString("b",
                                            new ConsLoString("s",
                                                    new ConsLoString("z", new MtLoString()))))));
  }

  //test the method reverseHelp for the lists of Strings
  boolean testreverseHelp(Tester t) {
    return
            t.checkExpect(this.mt.reverseHelp(this.mt), new MtLoString()) &&
                    t.checkExpect(this.mary.reverseHelp(this.mt), new ConsLoString("lamb.",
                            new ConsLoString("little ",
                                    new ConsLoString("a ",
                                            new ConsLoString("had ",
                                                    new ConsLoString("Mary ", new MtLoString())))))) &&
                    t.checkExpect(this.alphabet.reverseHelp(this.mt), new ConsLoString("e",
                            new ConsLoString("d",
                                    new ConsLoString("c",
                                            new ConsLoString("b",
                                                    new ConsLoString("a", new MtLoString())))))) &&
                    t.checkExpect(this.alphabet.reverseHelp(
                            new ConsLoString("c", new ConsLoString(
                                    "d", this.mt))),
                            new ConsLoString("e",
                                    new ConsLoString("d",
                                            new ConsLoString("c",
                                                    new ConsLoString("b",
                                                            new ConsLoString("a",
                                                                    new ConsLoString("c",
                                                                            new ConsLoString("d", new MtLoString())))))))) &&
                    t.checkExpect(this.letters.reverseHelp(
                            new ConsLoString("hi", new ConsLoString("f", new ConsLoString("cool", this.mt)))),
                            new ConsLoString("e",
                                    new ConsLoString("l",
                                            new ConsLoString("b",
                                                    new ConsLoString("s",
                                                            new ConsLoString("z",
                                                                    new ConsLoString("hi",
                                                                            new ConsLoString("f",
                                                                                    new ConsLoString("cool", new MtLoString())))))))));
  }

  //test the method isDoubledList for the lists of Strings
  boolean testisDoubledList(Tester t) {
    return
            t.checkExpect(this.mt.isDoubledList(), true) &&
                    t.checkExpect(this.mary.isDoubledList(), false) &&
                    t.checkExpect(this.alphabet.isDoubledList(), false) &&
                    t.checkExpect(this.letters.isDoubledList(), false) &&
                    t.checkExpect(this.shapes2.isDoubledList(), false) &&
                    t.checkExpect(this.animals.isDoubledList(), true) &&
                    t.checkExpect(this.animals2.isDoubledList(), true);
  }

  //test the method anyDoubles for the lists of Strings
  boolean testanyDoubles(Tester t) {
    return
            t.checkExpect(this.mt.anyDoubles("hi"), false) &&
                    t.checkExpect(this.mary.anyDoubles("Mary "), false) &&
                    t.checkExpect(this.alphabet.anyDoubles("a"), false) &&
                    t.checkExpect(this.letters.anyDoubles("q "), false) &&
                    t.checkExpect(this.shapes2.anyDoubles("circle"), false) &&
                    t.checkExpect(new ConsLoString("dog", this.mt).anyDoubles("dog"), true) &&
                    t.checkExpect(new ConsLoString("bear",
                            new ConsLoString("rat",
                                    new ConsLoString("rat", this.mt))).anyDoubles("bear"), true);
  }

  //test the method isPalindromeList for the lists of Strings
  boolean testisPalindromeList(Tester t) {
    return
            t.checkExpect(this.mt.isPalindromeList(), true) &&
                    t.checkExpect(this.mary.isPalindromeList(), false) &&
                    t.checkExpect(this.alphabet.isPalindromeList(), false) &&
                    t.checkExpect(this.letters.isPalindromeList(), false) &&
                    t.checkExpect(new ConsLoString("a",
                            new ConsLoString("b",
                                    new ConsLoString("c",
                                            new ConsLoString("c",
                                                    new ConsLoString("b",
                                                            new ConsLoString("a", this.mt)))))).isPalindromeList(), true) &&
                    t.checkExpect(new ConsLoString("house",
                            new ConsLoString("shape",
                                    new ConsLoString("word",
                                            new ConsLoString("word",
                                                    new ConsLoString("shape",
                                                            new ConsLoString("house", this.mt)))))).isPalindromeList(), true);
  }

  //test the method isPalindromeHelp for the lists of Strings
  boolean testisPalindromeHelp(Tester t) {
    return
            t.checkExpect(this.mt.isPalindromeHelp(this.mt), true) &&
                    t.checkExpect(this.animals.isPalindromeHelp(this.alphabet), false) &&
                    t.checkExpect(this.alphabet.isPalindromeHelp(this.shapes), false) &&
                    t.checkExpect(this.letters.isPalindromeHelp(new ConsLoString("e",
                            new ConsLoString("l",
                                    new ConsLoString("b",
                                            new ConsLoString("s",
                                                    new ConsLoString("z", new MtLoString())))))), false) &&
                    t.checkExpect(this.mary.isPalindromeHelp(new ConsLoString("lamb.",
                            new ConsLoString("little ",
                                    new ConsLoString("a ",
                                            new ConsLoString("had ",
                                                    new ConsLoString("Mary ", new MtLoString())))))), false) &&
                    t.checkExpect(new ConsLoString("a",
                            new ConsLoString("b",
                                    new ConsLoString("c",
                                            new ConsLoString("c",
                                                    new ConsLoString("b",
                                                            new ConsLoString("a", this.mt)))))).
                            isPalindromeHelp(new ConsLoString("a",
                                    new ConsLoString("b",
                                            new ConsLoString("c",
                                                    new ConsLoString("c",
                                                            new ConsLoString("b",
                                                                    new ConsLoString("a", this.mt))))))), true) &&
                    t.checkExpect(new ConsLoString("house",
                            new ConsLoString("shape",
                                    new ConsLoString("word",
                                            new ConsLoString("word",
                                                    new ConsLoString("shape",
                                                            new ConsLoString("house", this.mt)))))).
                            isPalindromeHelp(new ConsLoString("house",
                                    new ConsLoString("shape",
                                            new ConsLoString("word",
                                                    new ConsLoString("word",
                                                            new ConsLoString("shape",
                                                                    new ConsLoString("house", this.mt))))))), true);
  }

}

// does this String come before the given String lexicographically?
// produce value < 0   --- if this String comes before that String
// produce value zero  --- if this String is the same as that String
// produce value > 0   --- if this String comes after that String
