import tester.Tester;

class Deque<T> { //represents a Deque
    Sentinel<T> header;

    Deque(Sentinel<T> header) {
        this.header = header;
    }

    Deque() {
        this.header = new Sentinel<T>();
    }

    //counts the number of Node<T> in this, not including the header node
    int size() {
        return this.header.sizeHelp();
    }

    //consumes a value of type T and inserts it at the front of the list
    //EFFECT: modifies this Deque to include the given value as a Node<T> into the front
    void addAtHead(T value) {
        this.header.next.addBetween(value, this.header);
    }

    //consumes a value of type T and inserts it at the tail of this list
    //EFFECT: modifies this Deque to include the given value as a Node<T> into the back
    void addAtTail(T value) {
        this.header.addBetween(value, this.header.prev);
    }

    //removes the first node from this Deque and return the item that’s been removed from the list
    T removeFromHead() {
        if (this.header.next == null) {
            throw new RuntimeException("Not Allowed");
        }
        return this.header.next.removeBetween(this.header, this.header.next.next);
    }

    //removes the last node from this Deque and return the item that’s been removed from the list
    T removeFromTail() {
        if (this.header.prev == null) {
            throw new RuntimeException("Not Allowed");
        }
        return this.header.prev.removeBetween(this.header.prev.prev, this.header);
    }

    //takes an IPred<T> and produces the first node in this Deque for which the given predicate
    //returns true, if pred never returns true, return the header node in this Deque
    ANode<T> find(IPred<T> pred) {
        return this.header.next.findHelp(pred);
    }

    //removes the given node from this Deque, If the given node is the Sentinel header
    //the method does nothing
    //EFFECT: modifies this Deque by removing the given ANode<T>
    void removeNode(ANode<T> toRemove) {
        toRemove.removeNodeHelp();
    }
}

//Represents a boolean-valued question over values of type T
interface IPred<T> {
    //asks a question about T
    boolean apply(T t);
}

//represents a class that sees if the given String is "cat"
class CAT implements IPred<String> {
    String s = "cat";

    //asks if T contains "cat"
    public boolean apply(String t) {
        return s.equals(t);
    }
}

//represents a class that sees if the given String is "animal"
class ANIMAL implements IPred<String> {
    String s = "animal";

    //asks if T contains "animal"
    public boolean apply(String t) {
        return s.equals(t);
    }
}

//represents a class that sees if the given String is "zebra"
class ZEBRA implements IPred<String> {
    String s = "zebra";

    //asks if T contains "zebra"
    public boolean apply(String t) {
        return s.equals(t);
    }
}

//represents a class that sees if the given String is "SHEEP"
class SHEEP implements IPred<String> {
    String s = "sheep";

    //asks if T contains "sheep"
    public boolean apply(String t) {
        return s.equals(t);
    }
}

//represents a class that sees if the given String is "xyz"
class XYZ implements IPred<String> {
    String s = "xyz";

    //asks if T contains "xyz"
    public boolean apply(String t) {
        return s.equals(t);
    }
}

abstract class ANode<T> { //represents an abstract class of Sentinel<T> and Node<T>
    ANode<T> next;
    ANode<T> prev;

    //returns the given acc because this is the number of Node<T> seen in total
    //acc: keeps track of how many Node<T> gone over so far
    int count(int acc) {
        return acc;
    }


    //adds the given value before the given ANode<T> and after this ANode<T>
    //EFFECT: modifies the prev of this and the next of the given ANode<T> by adding
    //the given value as a Node<T>
    void addBetween(T value, ANode<T> before) {
        Node<T> newNode = new Node<T>(value, this, before);

        this.next = newNode;
    }

    //throws a RuntimeException because this Sentinel cannot be removed from a Deque
    T removeBetween(ANode<T> before, ANode<T> after) {
        throw new RuntimeException("Not Allowed");
    }

    //returns this Sentinel because the given predicate did not return true for any Node<T>
    //in the list
    ANode<T> findHelp(IPred<T> pred) {
        return this;
    }

    //removes this Node<T>
    //EFFECT: modifies the next and previous this's previous and next to not include this, and
    //linking the others
    void removeNodeHelp() {
        this.removeBetween(this.prev, this.next);
    }

}

class Node<T> extends ANode<T> { //represents a Node
    T data;

    Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    Node(T data, ANode<T> next, ANode<T> prev) {
        this.data = data;
        if (next == null) {
            throw new IllegalArgumentException();
        }

        else {
            this.next = next;
            this.next.prev = this;
        }

        if (prev == null) {
            throw new IllegalArgumentException();
        }

        else {
            this.prev = prev;
            prev.next = this;
        }
    }

    //adds 1 to the acc and continues counting the number of Node<T> next because this
    //is also a Node<T>
    //acc: keeps track of how many Node<T> gone over so far
    int count(int acc) {
        return this.next.count(1 + acc);
    }

    //removes this Node<T>, linking the prev ANode<T> of this and the next ANode<T> of this
    T removeBetween(ANode<T> before, ANode<T> after) {
        this.prev.next = after;
        this.next.prev = before;
        return this.data;
    }

    //returns this Node<T> if it passes the given predicate, if not keeps searching
    //for the ANode<T> that passes
    ANode<T> findHelp(IPred<T> pred) {
        if (pred.apply(this.data)) {
            return this;
        }
        else {
            return this.next.findHelp(pred);
        }
    }
}

class Sentinel<T> extends ANode<T> { //represents a Sentinel of a Deque

    Sentinel() {
        this.next = this;
        this.prev = this;
    }

    //counts the number of Node<T> that come after this, but not this because a
    //Sentinel<T> is not included in the size of a Deque
    int sizeHelp() {
        return this.next.count(0);
    }

    //does not change anything because this Sentinel<T> cannot be removed
    //EFFECT: does not modify anything about this
    void removeNodeHelp() {
        return;
    }

}

class ExamplesDeque {

    IPred<String> p1 = new CAT();
    IPred<String> p2 = new ANIMAL();
    IPred<String> p3 = new ZEBRA();
    IPred<String> p4 = new SHEEP();
    IPred<String> p5 = new XYZ();

    Deque<String> deque1;
    Deque<String> deque2;
    Deque<String> deque3;
    Sentinel<String> s1;
    Sentinel<String> s2;
    Sentinel<String> s3;
    Sentinel<String> mt;
    ANode<String> node1;
    ANode<String> node2;
    ANode<String> node3;
    ANode<String> node4;
    ANode<String> node2A;
    ANode<String> node3A;
    ANode<String> node4A;
    ANode<String> node1B;
    ANode<String> node2B;
    ANode<String> node3B;

    void initData() {
        s1 = new Sentinel<String>();
        mt = new Sentinel<String>();
        deque1 = new Deque<String>(this.s1);
        node1 = new Node<String>("cat", this.s1, this.s1);
        node2 = new Node<String>("animal", this.s1, this.node1);
        node3 = new Node<String>("zebra", this.s1, this.node2);
        node4 = new Node<String>("sheep", this.s1, this.node3);
        s2 = new Sentinel<String>();
        deque2 = new Deque<String>(this.s2);
        node2A = new Node<String>("animal", this.s2, this.s2);
        node3A = new Node<String>("zebra", this.s2, this.node2A);
        node4A = new Node<String>("sheep", this.s2, this.node3A);
        s3 = new Sentinel<String>();
        deque3 = new Deque<String>(this.s3);
        node1B = new Node<String>("cat", this.s3, this.s3);
        node2B = new Node<String>("animal", this.s3, this.node1B);
        node3B = new Node<String>("zebra", this.s3, this.node2B);
    }

    //tests addAtHead
    void testAddAtHead(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        this.deque1.addAtHead("cat");
        t.checkExpect(this.deque1.header.next, new Node<String>("cat",
                this.node1, this.s1));
        this.initData();
        this.deque1.addAtHead("animal");
        t.checkExpect(this.deque1.header.next, new Node<String>("animal", this.node1, this.s1));
        this.initData();
        this.deque1.addAtHead("zebra");
        t.checkExpect(this.deque1.header.next, new Node<String>("zebra", this.node1, this.s1));
    }

    //tests addAtTail
    void testAddAtTail(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        this.deque1.addAtTail("cat");
        t.checkExpect(this.deque1.header.next, new Node<String>("cat",
                this.s1, this.node4));
        this.initData();
        this.deque1.addAtTail("animal");
        t.checkExpect(this.deque1.header.next, new Node<String>("animal", this.s1, this.node4));
        this.initData();
        this.deque1.addAtTail("zebra");
        t.checkExpect(this.deque1.header.next, new Node<String>("zebra", this.s1, this.node4));
    }

    //tests removeFromHead
    void testRemoveFromHead(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.removeFromHead(), "cat");
        t.checkExpect(this.deque1, this.deque2);
        t.checkExpect(this.deque1.removeFromHead(), "animal");
        t.checkExpect(this.deque1.header.next, this.node3);
        t.checkExpect(this.deque1.removeFromHead(), "zebra");
        t.checkExpect(this.deque1.header.next, this.node4);
        t.checkExpect(this.deque1.removeFromHead(), "sheep");
        t.checkExpect(this.deque1.header.next, this.mt);
        t.checkException(new RuntimeException("Not Allowed"), this.deque1, "removeFromHead");
    }

    //tests removeFromTail
    void testRemoveFromTail(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.removeFromTail(), "sheep");
        t.checkExpect(this.deque1, this.deque3);
        t.checkExpect(this.deque1.removeFromTail(), "zebra");
        t.checkExpect(this.deque1.header.prev, this.node2);
        t.checkExpect(this.deque1.removeFromTail(), "animal");
        t.checkExpect(this.deque1.header.prev, this.node1);
        t.checkExpect(this.deque1.removeFromTail(), "cat");
        t.checkExpect(this.deque1.header.prev, this.mt);
        t.checkException(new RuntimeException("Not Allowed"), this.deque1, "removeFromTail");

    }

    //tests find
    void testFind(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.find(p1), this.node1);
        t.checkExpect(this.deque1.find(p2), this.node2);
        t.checkExpect(this.deque1.find(p3), this.node3);
        t.checkExpect(this.deque1.find(p4), this.node4);
        t.checkExpect(this.deque1.header.next, this.node1);
        t.checkExpect(this.deque1.removeFromHead(), "cat");
        t.checkExpect(this.deque1.find(p1), this.s1);
        t.checkExpect(this.deque1.removeFromHead(), "animal");
        t.checkExpect(this.deque1.find(p2), this.s1);
        t.checkExpect(this.deque1.removeFromHead(), "zebra");
        t.checkExpect(this.deque1.find(p3), this.s1);
        t.checkExpect(this.deque1.removeFromHead(), "sheep");
        t.checkExpect(this.deque1.find(p4), this.s1);
        this.initData();
        t.checkExpect(this.deque1.find(p5), this.s1);
    }

    //tests removeNode
    void testRemoveNode(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        this.deque1.removeNode(this.s1);
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.header.next, this.node1);
        this.deque1.removeNode(this.node1);
        t.checkExpect(this.deque1.header.next, this.node2);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.deque1.removeNode(this.node2);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.deque1.removeNode(this.node3);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.deque1.removeNode(this.node4);
        t.checkExpect(this.deque1.header.prev, this.mt);
    }

    //tests size
    void testSize(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.size(), 4);
        this.deque1.removeFromHead();
        t.checkExpect(this.deque1.size(), 3);
        this.deque1.removeNode(this.node3);
        t.checkExpect(this.deque1.size(), 2);
        this.deque1.removeFromTail();
        t.checkExpect(this.deque1.size(), 1);
        this.deque1.removeFromTail();
        t.checkExpect(this.deque1.size(), 0);
        this.deque1.removeNode(this.s1);
        t.checkExpect(this.deque1.size(), 0);
    }

    //tests sizeHelp
    void testSizeHelp(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.header.sizeHelp(), 4);
        this.deque1.removeFromHead();
        t.checkExpect(this.deque1.header.sizeHelp(), 3);
        this.deque1.removeNode(this.node3);
        t.checkExpect(this.deque1.header.sizeHelp(), 2);
        this.deque1.removeFromTail();
        t.checkExpect(this.deque1.header.sizeHelp(), 1);
        this.deque1.removeFromTail();
        t.checkExpect(this.deque1.header.sizeHelp(), 0);
        this.deque1.removeNode(this.s1);
        t.checkExpect(this.deque1.header.sizeHelp(), 0);
    }

    //tests count
    void testCount(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.header.next.count(0), 4);
        this.deque1.removeFromHead();
        t.checkExpect(this.deque1.header.next.count(1), 4);
        this.deque1.removeNode(this.node3);
        t.checkExpect(this.deque1.header.next.count(0), 2);
        this.deque1.removeFromTail();
        t.checkExpect(this.deque1.header.next.count(2), 3);
        this.deque1.removeFromTail();
        t.checkExpect(this.deque1.header.next.count(0), 0);
        this.deque1.removeNode(this.s1);
        t.checkExpect(this.deque1.header.sizeHelp(), 0);
    }

    //tests addBetween
    void testAddBetween(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        this.node1.addBetween("cat", this.s1);
        t.checkExpect(this.deque1.header.next, new Node<String>("cat",
                this.node1, this.s1));
        this.initData();
        this.node1.addBetween("animal", this.s1);
        t.checkExpect(this.deque1.header.next, new Node<String>("animal", this.node1, this.s1));
        this.initData();
        this.node1.addBetween("zebra", this.s1);
        t.checkExpect(this.deque1.header.next, new Node<String>("zebra", this.node1, this.s1));
        this.initData();
        this.node1.addBetween("sheep", this.s1);
        t.checkExpect(this.deque1.header.next, new Node<String>("sheep", this.node1, this.s1));
        this.initData();
        this.s1.addBetween("cat", this.node4);
        t.checkExpect(this.deque1.header.next, new Node<String>("cat", this.s1, this.node4));
        this.initData();
        this.s1.addBetween("animal", this.node4);
        t.checkExpect(this.deque1.header.next, new Node<String>("animal", this.s1, this.node4));
        this.initData();
        this.s1.addBetween("zebra", this.node4);
        t.checkExpect(this.deque1.header.next, new Node<String>("zebra", this.s1, this.node4));
        this.initData();
        this.s1.addBetween("sheep", this.node4);
        t.checkExpect(this.deque1.header.next, new Node<String>("sheep", this.s1, this.node4));
    }

    //tests removeBetween
    void testRemoveBetween(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        this.node1.removeBetween(this.s1, this.node2);
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.header.next, this.node2);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.node2.removeBetween(this.s1, this.node3);
        t.checkExpect(this.deque1.header.next, this.node3);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.node3.removeBetween(this.s1, this.node4);
        t.checkExpect(this.deque1.header.next, this.node4);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.node4.removeBetween(this.s1, this.s1);
        t.checkExpect(this.deque1.header.prev, this.mt);
        this.initData();
        t.checkException(new RuntimeException("Not Allowed"), this.s1, "removeBetween",
                this.node4, this.node1);
    }

    //tests findHelp
    void testFindHelp(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.header.next.findHelp(p1), this.node1);
        t.checkExpect(this.deque1.header.next.findHelp(p2), this.node2);
        t.checkExpect(this.deque1.header.next.findHelp(p3), this.node3);
        t.checkExpect(this.deque1.header.next.findHelp(p4), this.node4);
        t.checkExpect(this.deque1.removeFromHead(), "cat");
        t.checkExpect(this.deque1.header.next.findHelp(p1), this.s1);
        t.checkExpect(this.deque1.removeFromHead(), "animal");
        t.checkExpect(this.deque1.header.next.findHelp(p2), this.s1);
        t.checkExpect(this.deque1.removeFromHead(), "zebra");
        t.checkExpect(this.deque1.header.next.findHelp(p3), this.s1);
        t.checkExpect(this.deque1.removeFromHead(), "sheep");
        t.checkExpect(this.deque1.header.next.findHelp(p4), this.s1);
        this.initData();
        t.checkExpect(this.deque1.header.next.findHelp(p5), this.s1);
    }

    //tests removeNodeHelp
    void testRemoveNodeHelp(Tester t) {
        this.initData();
        t.checkExpect(this.deque1.header, this.s1);
        this.deque1.header.removeNodeHelp();
        t.checkExpect(this.deque1.header, this.s1);
        t.checkExpect(this.deque1.header.next, this.node1);
        this.deque1.header.next.removeNodeHelp();
        t.checkExpect(this.deque1.header.next, this.node2);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.deque1.header.next.removeNodeHelp();
        t.checkExpect(this.deque1.header.next, this.node3);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.deque1.header.next.removeNodeHelp();
        t.checkExpect(this.deque1.header.next, this.node4);
        t.checkExpect(this.deque1.header.prev, this.node4);
        this.deque1.header.next.removeNodeHelp();
        t.checkExpect(this.deque1.header.prev, this.mt);
    }

    //tests apply
    void testApply(Tester t) {
        this.initData();
        t.checkExpect(p1.apply("cat"), true);
        t.checkExpect(p2.apply("cat"), false);
        t.checkExpect(p3.apply("cat"), false);
        t.checkExpect(p4.apply("cat"), false);
        t.checkExpect(p2.apply("animal"), true);
        t.checkExpect(p1.apply("animal"), false);
        t.checkExpect(p3.apply("animal"), false);
        t.checkExpect(p4.apply("animal"), false);
        t.checkExpect(p3.apply("zebra"), true);
        t.checkExpect(p2.apply("zebra"), false);
        t.checkExpect(p1.apply("zebra"), false);
        t.checkExpect(p4.apply("zebra"), false);
        t.checkExpect(p4.apply("sheep"), true);
        t.checkExpect(p2.apply("sheep"), false);
        t.checkExpect(p3.apply("sheep"), false);
        t.checkExpect(p1.apply("sheep"), false);

    }

}