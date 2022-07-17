import tester.Tester;

//a well formed ancestry tree has children who are younger than their parents
//and these children can be older, younger, or the same age as each other
//a valid binary search tree has all data on the left side coming before all data
//on the right side and the node having a value between the two
//the difference between the two is that a binary search tree differentiates between
//left and right sides of the tree whereas ancestory tree only differentiates between
//children and parents


abstract class ABST<T> {
    IComparator<T> order;

    ABST(IComparator<T> order) {
        this.order = order;
    }

    //inserts the given T into this ABST<T>
    abstract ABST<T> insert(T b);

    //returns the leftmost item contained in this ABST<T>
    abstract T getLeftmost();

    //helps return the leftmost item contained in this ABST<T>
    boolean leftmostHelp() {
        return false;
    }

    //returns this ABST<T> without the leftmost item contained in this ABST<T>
    abstract ABST<T> getRight();

    //helps return this ABST<T> without the leftmost item contained in this ABST<T>
    abstract ABST<T> getRightHelp();


    //determines whether this ABST<T> is the same as the given ABST<T>
    abstract boolean sameTree(ABST<T> tree);

    //determines whether this ABST<T> is the same as the given Node<T>
    boolean sameTreeHelpNode(Node<T> tree) {
        return false;
    }

    //determines whether this ABST<T> is the same as the given Leaf<T>
    boolean sameHelpLeaf(Leaf<T> tree) {
        return false;
    }

    //determines whether this ABST<T> contains the same data in the
    //same order as the given tree
    abstract boolean sameData(ABST<T> tree);

    //determines whether this binary search tree contains the same data in the
    //same order as the given tree
    boolean sameDataHelpNode(Node<T> tree) {
        return false;
    }

    //determines whether this ABST<T> contains the same data as the given IList<T>
    abstract boolean sameAsList(IList<T> list);


    //consumes a list of type T and adds to it one at a time all items from this ABST<T>
    //are in the sorted order
    abstract IList<T> buildList(IList<T> list);

    //helps create a sorted IList<T> of all items from this ABST<T>
    //acc: keeps track of the elements of ABST<T> that have been added to this list so far
    abstract IList<T> buildListHelp(IList<T> acc);

}

class Leaf<T> extends ABST<T> {
    Leaf(IComparator<T> order) {
        super(order);
    }

    //inserts the given T into this Leaf<T>
    public ABST<T> insert(T b) {
        return new Node<T>(this.order, b, new Leaf<T>(this.order), new Leaf<T>(this.order));
    }

    //returns the leftmost item contained in this Leaf<T>
    public T getLeftmost() {
        throw new RuntimeException("No leftmost item of an empty tree");
    }

    //returns this Leaf<T> without the leftmost item
    public ABST<T> getRight() {
        throw new RuntimeException("No right of an empty tree");
    }

    //helps return this Leaf<T> without the leftmost item
    public ABST<T> getRightHelp() {
        return new Leaf<T>(this.order);
    }


    //determines whether this Leaf<T> is the same as the given ABST<T>
    public boolean sameTree(ABST<T> tree) {
        return tree.sameHelpLeaf(this);
    }

    //determines whether this Leaf<T> is the same as the given Leaf<T>
    public boolean sameHelpLeaf(Leaf<T> tree) {
        return true;
    }

    //comparators are not tested for because we do not know what the order is,
    //we are testing for data equality and not order equality.

    //determines whether this Leaf<T> contains the same data in the same order as the given ABST<T>
    public boolean sameData(ABST<T> tree) {
        return tree.sameHelpLeaf(this);
    }

    //determines whether this Leaf<T> contains the same data as the given IList<T>
    public boolean sameAsList(IList<T> list) {
        return list.buildTree(new Leaf<T>(this.order)).sameTree(this);
    }

    //consumes a IList<T> and adds to it one at a time all items from this Leaf<T>
    //in the sorted order
    public IList<T> buildList(IList<T> list) {
        return list;
    }

    //helps create a sorted IList<T> of all items from this Leaf<T>
    //in the sorted order
    //acc: keeps track of the elements of ABST<T> that have been added to this list so far
    public IList<T> buildListHelp(IList<T> acc) {
        return acc;
    }
}

class Node<T> extends ABST<T> {
    T data;
    ABST<T> left;
    ABST<T> right;

    Node(IComparator<T> order, T data, ABST<T> left, ABST<T> right) {
        super(order);
        this.data = data;
        this.left = left;
        this.right = right;
    }

    //inserts the given Book into this Node<T>
    public ABST<T> insert(T b) {
        if (this.order.compare(this.data, b) >= 1) {
            return new Node<T>(this.order, this.data, this.left.insert(b), this.right);
        }
        else if (this.order.compare(this.data, b) <= -1) {
            return new Node<T>(this.order, this.data, this.left, this.right.insert(b));
        }
        else {
            return new Node<T>(this.order, this.data, this.left, this.right.insert(b));
        }
    }

    //returns the leftmost item contained in this Node<T>
    public T getLeftmost() {
        if (this.left.leftmostHelp()) {
            return this.left.getLeftmost();
        }
        else {
            return this.data;
        }
    }

    //helps return the leftmost item contained in this Node<T>
    public boolean leftmostHelp() {
        return true;
    }

    //returns this Node<T> without the leftmost item
    public ABST<T> getRight() {
        if (this.left.leftmostHelp()) {
            return new Node<T>(this.order, this.data, this.left.getRightHelp(), this.right);
        }
        else {
            return this.right;
        }
    }

    //helps return this Node<T> without the leftmost item
    public ABST<T> getRightHelp() {
        if (this.left.leftmostHelp()) {
            return this.getRight();
        }
        else {
            return new Leaf<T>(this.order);
        }
    }

    //determines whether this Node<T> is the same as the given ABST<T>
    public boolean sameTree(ABST<T> tree) {
        return tree.sameTreeHelpNode(this);
    }

    //helps determine whether this Node<T> is the same as the given Node<T>
    public boolean sameTreeHelpNode(Node<T> tree) {
        if ((this.order.compare(this.data, tree.data) == 0) && !this.right.leftmostHelp()
                && !this.right.leftmostHelp()) {
            return true;
        }
        else if ((this.order.compare(this.data, tree.data) == 0) && !this.left.leftmostHelp()) {
            return !this.left.leftmostHelp() && !tree.left.leftmostHelp()
                    && this.right.sameTree(tree.right);
        }
        else if ((this.order.compare(this.data, tree.data) == 0) && !this.right.leftmostHelp()) {
            return !this.right.leftmostHelp() && !tree.right.leftmostHelp()
                    && this.left.sameTree(tree.left);
        }
        else {
            return (this.order.compare(this.data, tree.data) == 0) &&
                    this.left.sameTree(tree.left) && this.right.sameTree(tree.right);
        }
    }

    //determines whether this Node<T> contains the same data in the same order as the given ABST<T>
    public boolean sameData(ABST<T> tree) {
        return tree.sameDataHelpNode(this);
    }

    //helps determine whether this Node<T> contains the same data in the same order
    //as the given Node<T>
    public boolean sameDataHelpNode(Node<T> tree) {
        return this.sameTree(tree) ||
                this.buildList(new MtList<T>()).compareList(tree.buildList(new MtList<T>()), this.order);
    }

    //determines whether this Node<T> contains the same data as the given IList<T>
    public boolean sameAsList(IList<T> list) {
        return this.buildList(new MtList<T>()).compareList(list, this.order);
    }

    //consumes a IList<T> and adds to it one at a time all items from this Node<T>
    //in the sorted order
    public IList<T> buildList(IList<T> list) {
        return list.buildTree(this).buildListHelp(new MtList<T>());
    }

    //helps create a sorted IList<T> of all items from this Node<T>
    //acc: keeps track of the elements of ABST<T> that have been added to this list so far
    public IList<T> buildListHelp(IList<T> acc) {
        return this.getRight().buildListHelp(new ConsList<T>(this.getLeftmost(), acc));
    }


}

interface IComparator<T> {
    // Returns a negative number if t1 comes before t2 in this ordering
    // Returns zero              if t1 is the same as t2 in this ordering
    // Returns a positive number if t1 comes after t2 in this ordering
    int compare(T t1, T t2);
}


class BooksByTitle implements IComparator<Book> {
    //compares the titles of the given books
    public int compare(Book t1, Book t2) {
        return t1.title.compareTo(t2.title);
    }
}

class BooksByAuthor implements IComparator<Book> {
    //compares the authors of the given books
    public int compare(Book t1, Book t2) {
        return t1.author.compareTo(t2.author);
    }
}

class BooksByPrice implements IComparator<Book> {
    //compares the prices of the given books
    public int compare(Book t1, Book t2) {
        if (t1.price < t2.price) {
            return -1;
        }
        else if (t1.price > t2.price) {
            return 1;
        }
        else {
            return 0;
        }
    }
}

class Book {
    String title;
    String author;
    int price;

    Book(String title, String author, int price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }
}

interface IList<T> {
    //compares this IList<T> and the given IList<T> for sameness using the
    //given IComparator<T>
    boolean compareList(IList<T> list, IComparator<T> order);

    //compares this IList<T> and the given MtList<T> for sameness
    boolean compareListMt(MtList<T> list);

    //compares this IList<T> and the given ConsList<T> for sameness using the
    //given IComparator<T>
    boolean compareListCons(ConsList<T> list, IComparator<T> order);

    //returns an ABST<T> that contains all items in this IList<T> and all items already
    //in the ABST<T>
    ABST<T> buildTree(ABST<T> tree);


}

class MtList<T> implements IList<T> {

    //compares this MtList<T> and the given IList<T> for sameness
    public boolean compareList(IList<T> list, IComparator<T> order) {
        return list.compareListMt(this);
    }

    //compares this MtList<T> and the given MtList<T> for sameness
    public boolean compareListMt(MtList<T> list) {
        return true;
    }

    //compares this MtList<T> and the given ConsList<T> for sameness
    public boolean compareListCons(ConsList<T> list, IComparator<T> order) {
        return false;
    }

    //returns an ABST<T> that contains all items in this MtList<T> and all items
    //already in the ABST<T>
    public ABST<T> buildTree(ABST<T> tree) {
        return tree;
    }

}

class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    //compares this ConsList<T> and the given IList<T> for sameness
    //using the given IComparator<T>
    public boolean compareList(IList<T> list, IComparator<T> order) {
        return list.compareListCons(this, order);
    }

    //compares this ConsList<T> and the given MtList<T> for sameness
    public boolean compareListMt(MtList<T> list) {
        return false;
    }

    //compares this ConsList<T> and the given ConsList<T> for sameness
    //using the given IComparator<T>
    public boolean compareListCons(ConsList<T> list, IComparator<T> order) {
        return (order.compare(this.first, list.first) == 0) &&
                (this.rest.compareList(list.rest, order));
    }

    //returns an ABST<T> that contains all items in this ConsList<T> and all items
    //already in the ABST<T>
    public ABST<T> buildTree(ABST<T> tree) {
        return this.rest.buildTree(tree.insert(this.first));
    }

}

class ExamplesBooks {
    //examples Books
    Book book1 = new Book("Cat in the Hat", "Dr. Seuss", 14);
    Book book2 = new Book("Harry Potter", "JK Rowling", 57);
    Book book3 = new Book("About Me", "Zebra", 22);
    Book book5 = new Book("Wound", "Annie", 21);
    Book book6 = new Book("Moon", "ABC", 20);
    Book book7 = new Book("Sun", "CDF", 51);
    Book book8 = new Book("Neptune", "ASD", 53);
    //examples IComparator<Book>
    BooksByTitle titles = new BooksByTitle();
    BooksByAuthor authors = new BooksByAuthor();
    BooksByPrice prices = new BooksByPrice();
    //examples Leaf<Book>
    ABST<Book> leafT = new Leaf<Book>(this.titles);
    ABST<Book> leafA = new Leaf<Book>(this.authors);
    ABST<Book> leafP = new Leaf<Book>(this.prices);
    //examples Node<Book>
    ABST<Book> tree1 = new Node<Book>(this.titles, this.book1, this.leafT, this.leafT);
    ABST<Book> tree1A = new Node<Book>(this.titles, this.book1, this.leafT, this.leafT);
    ABST<Book> tree2 = new Node<Book>(this.authors, this.book2, this.leafA, this.leafA);
    ABST<Book> tree3 = new Node<Book>(this.prices, this.book3, this.leafP, this.leafP);
    ABST<Book> tree3A = new Node<Book>(this.authors, this.book3, this.leafA, this.leafA);
    ABST<Book> tree4 = new Node<Book>(this.authors, this.book1, this.leafA, this.tree2);
    ABST<Book> tree4A = new Node<Book>(this.authors, this.book2, this.tree1, this.leafA);
    ABST<Book> tree5 = new Node<Book>(this.authors, this.book6, this.leafA, this.leafA);
    ABST<Book> tree6 = new Node<Book>(this.authors, this.book5, this.tree5, this.leafA);
    ABST<Book> tree7 = new Node<Book>(this.authors, this.book2, this.leafA, this.tree3A);
    ABST<Book> tree8 = new Node<Book>(this.authors, this.book7, this.tree6, this.tree7);
    //examples IList<Book>
    IList<Book> mt = new MtList<Book>();
    IList<Book> list1 = new ConsList<Book>(this.book1, this.mt);
    IList<Book> list2 = new ConsList<Book>(this.book2, this.mt);
    IList<Book> list3 = new ConsList<Book>(this.book3, this.mt);
    IList<Book> list4 = new ConsList<Book>(this.book2, this.list1);
    IList<Book> list4A = new ConsList<Book>(this.book2, this.list1);

    //tests for insert(Book b)
    boolean testInsert(Tester t) {
        return t.checkExpect(this.leafT.insert(this.book1),
                new Node<Book>(this.titles, this.book1, this.leafT, this.leafT))
                && t.checkExpect(this.leafA.insert(this.book2),
                new Node<Book>(this.authors, this.book2, this.leafA, this.leafA))
                && t.checkExpect(this.leafP.insert(this.book3),
                new Node<Book>(this.prices, this.book3, this.leafP, this.leafP))
                && t.checkExpect(this.tree1.insert(this.book2),
                new Node<Book>(this.titles, this.book1, this.leafT,
                        new Node<Book>(this.titles, this.book2, this.leafT, this.leafT)))
                && t.checkExpect(this.tree1.insert(this.book3),
                new Node<Book>(this.titles, this.book1,
                        new Node<Book>(this.titles, this.book3, this.leafT, this.leafT), this.leafT))
                && t.checkExpect(this.tree1.insert(this.book1),
                new Node<Book>(this.titles, this.book1, this.leafT,
                        new Node<Book>(this.titles, this.book1, this.leafT, this.leafT)))
                && t.checkExpect(this.tree2.insert(this.book1),
                new Node<Book>(this.authors, this.book2,
                        new Node<Book>(this.authors, this.book1, this.leafA, this.leafA), this.leafA))
                && t.checkExpect(this.tree2.insert(this.book3),
                new Node<Book>(this.authors, this.book2, this.leafA,
                        new Node<Book>(this.authors, this.book3, this.leafA, this.leafA)))
                && t.checkExpect(this.tree2.insert(this.book2),
                new Node<Book>(this.authors, this.book2, this.leafA,
                        new Node<Book>(this.authors, this.book2, this.leafA, this.leafA)))
                && t.checkExpect(this.tree3.insert(this.book1),
                new Node<Book>(this.prices, this.book3,
                        new Node<Book>(this.prices, this.book1, this.leafP, this.leafP), this.leafP))
                && t.checkExpect(this.tree3.insert(this.book2),
                new Node<Book>(this.prices, this.book3, this.leafP,
                        new Node<Book>(this.prices, this.book2, this.leafP, this.leafP)))
                && t.checkExpect(this.tree3.insert(this.book3),
                new Node<Book>(this.prices, this.book3, this.leafP,
                        new Node<Book>(this.prices, this.book3, this.leafP, this.leafP)))
                && t.checkExpect(this.tree4.insert(this.book3),
                new Node<Book>(this.authors, this.book1, this.leafA,
                        new Node<Book>(this.authors, this.book2, this.leafA,
                                new Node<Book>(this.authors, this.book3, this.leafA, this.leafA))))
                && t.checkExpect(this.tree4.insert(this.book2),
                new Node<Book>(this.authors, this.book1, this.leafA,
                        new Node<Book>(this.authors, this.book2, this.leafA,
                                new Node<Book>(this.authors, this.book2, this.leafA, this.leafA))));
    }

    //tests for getLeftmost()
    boolean testGetLeftmost(Tester t) {
        return t.checkExpect(this.tree1.getLeftmost(), this.book1)
                && t.checkExpect(this.tree2.getLeftmost(), this.book2)
                && t.checkExpect(this.tree3.getLeftmost(), this.book3)
                && t.checkExpect(this.tree4.getLeftmost(), this.book1)
                && t.checkException(new RuntimeException("No leftmost item of an empty tree"),
                this.leafA, "getLeftmost")
                && t.checkException(new RuntimeException("No leftmost item of an empty tree"),
                this.leafT, "getLeftmost")
                && t.checkException(new RuntimeException("No leftmost item of an empty tree"),
                this.leafP, "getLeftmost");
    }

    //tests for leftmostHelp()
    boolean testLeftmostHelp(Tester t) {
        return t.checkExpect(this.leafA.leftmostHelp(), false)
                && t.checkExpect(this.leafT.leftmostHelp(), false)
                && t.checkExpect(this.leafP.leftmostHelp(), false)
                && t.checkExpect(this.tree1.leftmostHelp(), true)
                && t.checkExpect(this.tree2.leftmostHelp(), true)
                && t.checkExpect(this.tree3.leftmostHelp(), true)
                && t.checkExpect(this.tree4.leftmostHelp(), true);
    }

    //tests for getRight()
    boolean testGetRight(Tester t) {
        return t.checkExpect(this.tree1.getRight(), this.leafT)
                && t.checkExpect(this.tree2.getRight(), this.leafA)
                && t.checkExpect(this.tree3.getRight(), this.leafP)
                && t.checkExpect(this.tree4.getRight(), this.tree2)
                && t.checkException(new RuntimeException("No right of an empty tree"),
                this.leafA, "getRight")
                && t.checkException(new RuntimeException("No right of an empty tree"),
                this.leafT, "getRight")
                && t.checkException(new RuntimeException("No right of an empty tree"),
                this.leafP, "getRight");
    }

    //tests for getRightHelp()
    boolean testGetRightHelp(Tester t) {
        return t.checkExpect(this.leafA.getRightHelp(), this.leafA)
                && t.checkExpect(this.leafT.getRightHelp(), this.leafT)
                && t.checkExpect(this.leafP.getRightHelp(), this.leafP)
                && t.checkExpect(this.tree1.getRightHelp(), this.leafT)
                && t.checkExpect(this.tree2.getRightHelp(), this.leafA)
                && t.checkExpect(this.tree3.getRightHelp(), this.leafP)
                && t.checkExpect(this.tree4.getRightHelp(), this.leafA);
    }

    //tests for sameTree(ABST<T> tree)
    boolean testSameTree(Tester t) {
        return t.checkExpect(this.leafA.sameTree(this.leafA), true)
                && t.checkExpect(this.leafT.sameTree(this.leafT), true)
                && t.checkExpect(this.leafP.sameTree(this.leafP), true)
                && t.checkExpect(this.leafT.sameTree(this.leafA), true)
                && t.checkExpect(this.leafT.sameTree(this.leafP), true)
                && t.checkExpect(this.tree1.sameTree(this.tree1), true)
                && t.checkExpect(this.tree1.sameTree(this.tree2), false)
                && t.checkExpect(this.tree4.sameTree(this.tree4), true)
                && t.checkExpect(this.tree4.sameTree(this.tree2), false)
                && t.checkExpect(this.leafA.sameTree(this.tree2), false)
                && t.checkExpect(this.tree2.sameTree(this.leafA), false)
                && t.checkExpect(this.tree4A.sameTree(this.tree4), false);
    }

    //tests for sameTreeHelpNode(Node<T> tree)
    boolean testSameTreeHelpNode(Tester t) {
        return t.checkExpect(this.tree1.sameTreeHelpNode((Node<Book>)this.tree1), true)
                && t.checkExpect(this.tree2.sameTreeHelpNode((Node<Book>)this.tree2), true)
                && t.checkExpect(this.tree3.sameTreeHelpNode((Node<Book>)this.tree3), true)
                && t.checkExpect(this.tree4.sameTreeHelpNode((Node<Book>)this.tree4), true)
                && t.checkExpect(this.tree1.sameTreeHelpNode((Node<Book>)this.tree2), false)
                && t.checkExpect(this.tree4.sameTreeHelpNode((Node<Book>)this.tree1), false)
                && t.checkExpect(this.leafA.sameTreeHelpNode((Node<Book>)this.tree3), false)
                && t.checkExpect(this.leafT.sameTreeHelpNode((Node<Book>)this.tree4), false);
    }

    //tests for sameHelpLeaf(Leaf<T> tree)
    boolean testSameTreeLeaf(Tester t) {
        return t.checkExpect(this.leafA.sameHelpLeaf((Leaf<Book>)this.leafA), true)
                && t.checkExpect(this.leafT.sameHelpLeaf((Leaf<Book>)this.leafT), true)
                && t.checkExpect(this.leafP.sameHelpLeaf((Leaf<Book>)this.leafP), true)
                && t.checkExpect(this.leafT.sameHelpLeaf((Leaf<Book>)this.leafA), true)
                && t.checkExpect(this.leafT.sameHelpLeaf((Leaf<Book>)this.leafP), true)
                && t.checkExpect(this.tree4.sameHelpLeaf((Leaf<Book>)this.leafT), false)
                && t.checkExpect(this.tree2.sameHelpLeaf((Leaf<Book>)this.leafP), false)
                && t.checkExpect(this.tree1.sameHelpLeaf((Leaf<Book>)this.leafA), false);
    }

    //tests sameData(ABST<T> tree)
    boolean testSameData(Tester t) {
        return t.checkExpect(this.leafA.sameData(this.leafA), true)
                && t.checkExpect(this.leafT.sameData(this.leafT), true)
                && t.checkExpect(this.leafP.sameData(this.leafP), true)
                && t.checkExpect(this.leafA.sameData(this.leafP), true)
                && t.checkExpect(this.tree1.sameData(this.tree1), true)
                && t.checkExpect(this.tree1.sameData(this.tree2), false)
                && t.checkExpect(this.tree1.sameData(this.tree1A), true)
                && t.checkExpect(this.tree1A.sameData(this.tree1), true)
                && t.checkExpect(this.tree4.sameData(this.tree4A), true)
                && t.checkExpect(this.tree4.sameData(this.tree2), false);
    }

    //tests sameDataHelpNode(Node<T> tree)
    boolean testSameDataHelpNode(Tester t) {
        return t.checkExpect(this.leafA.sameDataHelpNode((Node<Book>)this.tree4), false)
                && t.checkExpect(this.leafP.sameDataHelpNode((Node<Book>)this.tree7), false)
                && t.checkExpect(this.leafT.sameDataHelpNode((Node<Book>)this.tree3), false)
                && t.checkExpect(this.tree7.sameDataHelpNode((Node<Book>)this.tree7), true)
                && t.checkExpect(this.tree1.sameDataHelpNode((Node<Book>)this.tree1A), true)
                && t.checkExpect(this.tree1A.sameDataHelpNode((Node<Book>)this.tree1), true)
                && t.checkExpect(this.tree4.sameDataHelpNode((Node<Book>)this.tree4A), true)
                && t.checkExpect(this.tree7.sameDataHelpNode((Node<Book>)this.tree8), false);
    }

    //tests sameAsList(IList<T> list)
    boolean testSameAsList(Tester t) {
        return t.checkExpect(this.leafA.sameAsList(this.mt), true)
                && t.checkExpect(this.leafT.sameAsList(this.mt), true)
                && t.checkExpect(this.leafP.sameAsList(this.mt), true)
                && t.checkExpect(this.tree1.sameAsList(this.list1), true)
                && t.checkExpect(this.tree1.sameAsList(this.list3), false)
                && t.checkExpect(this.tree2.sameAsList(this.list2), true)
                && t.checkExpect(this.tree4.sameAsList(this.list4), true)
                && t.checkExpect(this.tree4.sameAsList(this.list4A), true)
                && t.checkExpect(this.tree4.sameAsList(this.list2), false);
    }

    //tests buildTree(ABST<T> tree)
    boolean testBuildTree(Tester t) {
        return t.checkExpect(this.mt.buildTree(this.leafA), this.leafA)
                && t.checkExpect(this.mt.buildTree(this.leafP), this.leafP)
                && t.checkExpect(this.mt.buildTree(this.leafT), this.leafT)
                && t.checkExpect(this.mt.buildTree(this.tree1), this.tree1)
                && t.checkExpect(this.mt.buildTree(this.tree4), this.tree4)
                && t.checkExpect(this.list1.buildTree(this.leafT), this.tree1)
                && t.checkExpect(this.list3.buildTree(this.leafP), this.tree3)
                && t.checkExpect(this.list2.buildTree(this.tree2),
                new Node<Book>(this.authors, this.book2, this.leafA,
                        new Node<Book>(this.authors, this.book2, this.leafA, this.leafA)))
                && t.checkExpect(this.list3.buildTree(this.tree1),
                new Node<Book>(this.titles, this.book1,
                        new Node<Book>(this.titles, this.book3, this.leafT, this.leafT), this.leafT))
                && t.checkExpect(this.list2.buildTree(this.tree4),
                new Node<Book>(this.authors, this.book1, this.leafA,
                        new Node<Book>(this.authors, this.book2, this.leafA,
                                new Node<Book>(this.authors, this.book2, this.leafA, this.leafA))));
    }

    //tests compareList(IList<T> list)
    boolean testCompareList(Tester t) {
        return t.checkExpect(this.mt.compareList(this.mt, this.authors), true)
                && t.checkExpect(this.mt.compareList(this.mt, this.prices), true)
                && t.checkExpect(this.mt.compareList(this.mt, this.titles), true)
                && t.checkExpect(this.mt.compareList(this.list1, this.authors), false)
                && t.checkExpect(this.mt.compareList(this.list2, this.prices), false)
                && t.checkExpect(this.mt.compareList(this.list3, this.titles), false)
                && t.checkExpect(this.list3.compareList(this.list3, this.prices), true)
                && t.checkExpect(this.list2.compareList(this.list2, this.authors), true)
                && t.checkExpect(this.list1.compareList(this.list1, this.titles), true)
                && t.checkExpect(this.list4.compareList(this.list4A, this.authors), true)
                && t.checkExpect(this.list4.compareList(this.list2, this.authors), false);
    }

    //tests for compareListMt(MtList<T> list)
    boolean testCompareListMt(Tester t) {
        return t.checkExpect(this.mt.compareListMt((MtList<Book>)this.mt), true)
                && t.checkExpect(this.list3.compareListMt((MtList<Book>)this.mt), false)
                && t.checkExpect(this.list2.compareListMt(((MtList<Book>)this.mt)), false)
                && t.checkExpect(this.list4.compareListMt(((MtList<Book>)this.mt)), false);
    }

    //tests for compareListCons(ConsList<T> list)
    boolean testCompareListCons(Tester t) {
        return t.checkExpect(this.list3.compareListCons((ConsList<Book>)this.list2,
                this.prices), false)
                && t.checkExpect(this.list2.compareListCons((ConsList<Book>)this.list2,
                this.authors), true)
                && t.checkExpect(this.list4.compareListCons((ConsList<Book>)this.list4A,
                this.authors), true)
                && t.checkExpect(this.list1.compareListCons((ConsList<Book>)this.list1,
                this.titles), true)
                && t.checkExpect(this.list3.compareListCons((ConsList<Book>)this.list2,
                this.titles), false);
    }

    //tests buildList(ABST<T> tree)
    boolean testBuildList(Tester t) {
        return t.checkExpect(this.leafT.buildList(this.mt), this.mt)
                && t.checkExpect(this.leafP.buildList(this.list3), this.list3)
                && t.checkExpect(this.leafA.buildList(this.list2), this.list2)
                && t.checkExpect(this.tree1.buildList(this.mt), this.list1)
                && t.checkExpect(this.tree2.buildList(this.list2),
                new ConsList<Book>(this.book2,
                        new ConsList<Book>(this.book2, this.mt)))
                && t.checkExpect(this.tree2.buildList(this.list2),
                new ConsList<Book>(this.book2,
                        new ConsList<Book>(this.book2, this.mt)))
                && t.checkExpect(this.tree4.buildList(this.list4),
                new ConsList<Book>(this.book2,
                        new ConsList<Book>(this.book2,
                                new ConsList<Book>(this.book1,
                                        new ConsList<Book>(this.book1, this.mt)))))
                && t.checkExpect(this.tree8.buildList(this.list4),
                new ConsList<Book>(this.book3,
                        new ConsList<Book>(this.book2,
                                new ConsList<Book>(this.book2,
                                        new ConsList<Book>(this.book1,
                                                new ConsList<Book>(this.book7,
                                                        new ConsList<Book>(this.book5,
                                                                new ConsList<Book>(this.book6, this.mt))))))));

    }

    //tests for buildListHelp(IList<T> acc)
    boolean testbuildListHelp(Tester t) {
        return t.checkExpect(this.leafA.buildListHelp(this.mt), this.mt)
                && t.checkExpect(this.leafP.buildListHelp(this.mt), this.mt)
                && t.checkExpect(this.leafT.buildListHelp(this.mt), this.mt)
                && t.checkExpect(this.tree1.buildListHelp(this.mt), this.list1)
                && t.checkExpect(this.tree3.buildListHelp(this.mt), this.list3)
                && t.checkExpect(this.tree4.buildListHelp(this.list1),
                new ConsList<Book>(this.book2,
                        new ConsList<Book>(this.book1,
                                new ConsList<Book>(this.book1, this.mt))))
                && t.checkExpect(this.tree8.buildListHelp(this.list4),
                new ConsList<Book>(this.book3,
                        new ConsList<Book>(this.book2,
                                new ConsList<Book>(this.book7,
                                        new ConsList<Book>(this.book5,
                                                new ConsList<Book>(this.book6,
                                                        new ConsList<Book>(this.book2,
                                                                new ConsList<Book>(this.book1, this.mt))))))));
    }
}