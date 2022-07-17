import tester.Tester;

import java.util.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {

    // Create a new instance of the encoder/decoder with a new permutation code
    PermutationCode() {
        this.code = this.initEncoder();
    }

    // Create a new instance of the encoder/decoder with the given code
    PermutationCode(ArrayList<Character> code) {
        this.code = code;
    }

    // The original list of characters to be encoded
    ArrayList<Character> alphabet =
            new ArrayList<Character>(Arrays.asList(
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                    't', 'u', 'v', 'w', 'x', 'y', 'z'));

    ArrayList<Character> code = new ArrayList<Character>(26);

    // The copy of original list of characters to be encoded and mutated
    ArrayList<Character> alphabetToChange =
            new ArrayList<Character>(alphabet);

    // A random number generator
    Random rand = new Random();

    // Initialize the encoding permutation of the characters
    ArrayList<Character> initEncoder() {
        ArrayList<Character> ar0 = code;

        while (ar0.size() != alphabet.size()) {
            Character c = alphabetToChange.get(rand.nextInt(alphabetToChange.size()));
            alphabetToChange.remove(c);
            ar0.add(c);
        }
        return ar0;
    }

    // produce an encoded String from the given String
    String encode(String source) {
        String s0 = "";
        for (int i = 0; i < source.length(); i++) {
            //get the character at the given index
            Character c = source.charAt(i);
            //finds the given character in the alphabet and returns the encoded char
            int index = this.alphabet.indexOf(c);
            Character c2 = this.code.get(index);
            //combines all found Strings into one String
            s0 += c2;
        }
        //returns the encoded String
        return s0;
    }

    // produce a decoded String from the given String
    String decode(String code) {
        String s0 = "";

        for (int i = 0; i < code.length(); i++) {
            //get the character at the given index
            Character c = code.charAt(i);
            //finds the given character in the alphabet and returns the decoded char
            int index = this.code.indexOf(c);
            Character c2 = this.alphabet.get(index);
            //combines all found Strings into one String
            s0 += c2;
        }
        //returns the encoded String
        return s0;
    }
}

class ExamplesPermutationCode {

    ArrayList<Character> alphabet =
            new ArrayList<Character>(Arrays.asList(
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
                    't', 'u', 'v', 'w', 'x', 'y', 'z'));
    ArrayList<Character> code =
            new ArrayList<Character>(Arrays.asList(
                    'z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                    's', 't', 'u', 'v', 'w', 'x', 'y'));
    PermutationCode perm1 = new PermutationCode(this.alphabet);
    PermutationCode perm2 = new PermutationCode(this.code);

    //tests initEncoder
    void testInitEncoder(Tester t) {
        ArrayList<Character> encoding = this.perm1.initEncoder();
        for (int i = 0; i < alphabet.size(); i++) {
            //check to see if the character contained
            t.checkExpect(encoding.contains(this.alphabet.get(i)), true);
        }
        ArrayList<Character> encoding2 = this.perm2.initEncoder();
        for (int i = 0; i < alphabet.size(); i++) {
            //check to see if the character contained
            t.checkExpect(encoding2.contains(this.alphabet.get(i)), true);
        }
    }

    //tests encode
    void testEncode(Tester t) {
        t.checkExpect(this.perm2.encode("z"), "y");
        t.checkExpect(this.perm2.encode("z"), this.perm1.decode("y"));
        t.checkExpect(this.perm2.encode("abc"), "zab");
        t.checkExpect(this.perm2.encode("abc"), this.perm1.decode("zab"));
        t.checkExpect(this.perm2.encode("tuv"), "stu");
        t.checkExpect(this.perm2.encode("tuv"), this.perm1.decode("stu"));
        t.checkExpect(this.perm2.encode("lrx"), "kqw");
        t.checkExpect(this.perm2.encode("lrx"), this.perm1.decode("kqw"));
        t.checkExpect(this.perm2.encode("dnweo"), "cmvdn");
        t.checkExpect(this.perm2.encode("dnweo"), this.perm1.decode("cmvdn"));
    }

    //tests decode
    void testDecode(Tester t) {
        t.checkExpect(this.perm2.decode("l"), "m");
        t.checkExpect(this.perm2.decode("l"), this.perm1.encode("m"));
        t.checkExpect(this.perm1.decode("abc"), "abc");
        t.checkExpect(this.perm2.decode("abc"), "bcd");
        t.checkExpect(this.perm2.decode("abc"), this.perm1.encode("bcd"));
        t.checkExpect(this.perm2.decode("tuv"), "uvw");
        t.checkExpect(this.perm2.decode("tuv"), this.perm1.encode("uvw"));
        t.checkExpect(this.perm2.decode("lrx"), "msy");
        t.checkExpect(this.perm2.decode("lrx"), this.perm1.encode("msy"));
        t.checkExpect(this.perm2.decode("dnweo"), "eoxfp");
        t.checkExpect(this.perm2.decode("dnweo"), this.perm1.encode("eoxfp"));
    }

}