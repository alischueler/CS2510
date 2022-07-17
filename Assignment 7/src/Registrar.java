import tester.Tester;

interface IList<T> { //represents a List of element T

    // determines if the given element is in this List using the given comparator
    boolean contains(T at, IComparator<T> pred);

    // determines if this List and the given List contain any of the same elements
    // using the given Comparator
    boolean compareMates(IList<T> at, IComparator<T> pred);

    //helps determine if this List and the given ConsList contain any of the same elements
    //using the given Comparator
    boolean compareMatesHelp(ConsList<T> at, IComparator<T> pred);

    //counts the number of times the element passed into the given predicate is found in this List,
    //using the given predicate, returns 1 every time it is found and 0 when an MtList is reached
    int atLeastTwo(IPred<T> pred);
}

class MtList<T> implements IList<T> { //represents an empty list of element T

    //determines if the given element is in this MtList
    public boolean contains(T at, IComparator<T> pred) {
        return false;
    }

    //determines if this MtList and the given List contain any of the same elements
    public boolean compareMates(IList<T> at, IComparator<T> pred) {
        return false;
    }

    //helps determines if this MtList and the given ConsList contain any of the same elements
    public boolean compareMatesHelp(ConsList<T> at, IComparator<T> pred) {
        return false;
    }

    //counts the number of times the element passed into the given predicate is found in this
    //MtList, using the given predicate, returns 0 because there are no elements in an MtList
    public int atLeastTwo(IPred<T> pred) {
        return 0;
    }
}

class ConsList<T> implements IList<T> { //represents a non empty List of element T
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    //determines if the given element is in this ConsList using the given comparator
    public boolean contains(T at, IComparator<T> pred) {
        return (pred.apply(this.first, at)) || this.rest.contains(at, pred);
    }

    //determines if this ConsList and the given List contain any of the same elements
    //using the given Comparator
    public boolean compareMates(IList<T> at, IComparator<T> pred) {
        return at.compareMatesHelp(this, pred);
    }

    //helps determine if this ConsList and the given ConsList contain any of the same
    //elements using the given Comparator
    public boolean compareMatesHelp(ConsList<T> at, IComparator<T> pred) {
        return this.contains(at.first, pred) || this.compareMates(at.rest, pred);
    }


    //counts the number of times the element passed into the given predicate is found in this
    //ConsList, using the given predicate; returns 1 every time found, 0 when an MtList is reached
    public int atLeastTwo(IPred<T> pred) {
        if (pred.apply(this.first)) {
            return 1 + this.rest.atLeastTwo(pred);
        }
        else {
            return this.rest.atLeastTwo(pred);
        }
    }

}

interface IComparator<T> { //represents a way to compare to elements T

    // Compares the given T and T for equality
    boolean apply(T t1, T t2);
}

class CompareAllCourse implements IComparator<Course> {
    // compare two Courses for total equality
    public boolean apply(Course t1, Course t2) {
        return t1.students.compareMates(t2.students, new CompareStudent())
                && t1.name.equals(t2.name);
    }
}

class CompareStudent implements IComparator<Student> {

    // compare two Students for equality
    public boolean apply(Student i1, Student i2) {
        return (i1.id == i2.id);
    }
}

interface IPred<T> { //asks a question about element T

    //asks the question about T
    boolean apply(T t);
}


class CompareOneCourse implements IPred<Course> {
    Student student;

    CompareOneCourse(Student student) {
        this.student = student;
    }

    //asks if this Student is enrolled in the given Course
    public boolean apply(Course t) {
        return t.students.contains(this.student, new CompareStudent());
    }
}


class Course { //represents a school course
    String name;
    Instructor prof;
    IList<Student> students;

    Course(String name, Instructor prof, IList<Student> students) {
        this.name = name;
        this.prof = prof;
        this.students = students;
        this.prof.courses = new ConsList<Course>(this, prof.courses);
    }

    Course(String name, Instructor prof) {
        this(name, prof, new MtList<Student>());
    }

    // adds the given Student into the Student's list of this Course
    // EFFECT: modify this Course's Student field, adding the given Student
    void enrollHelp(Student s) {
        this.students = new ConsList<Student>(s, this.students);
    }


}

class Instructor { //represents a professor
    String name;
    IList<Course> courses;

    Instructor(String name, IList<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    Instructor(String name) {
        this(name, new MtList<Course>());
    }

    // determines whether the given Student is enrolled in more than one of this
    //Instructor's Courses
    boolean dejavu(Student c) {
        return this.courses.atLeastTwo(new CompareOneCourse(c)) > 1;
    }
}

class Student { //represents a student
    String name;
    int id;
    IList<Course> courses;

    Student(String name, int id, IList<Course> courses) {
        this.name = name;
        this.id = id;
        this.courses = courses;
    }

    Student(String name, int id) {
        this(name, id, new MtList<Course>());
    }

    // enroll the Student in a given Course
    // EFFECT: modify this Student's Course field, adding the given Course
    void enroll(Course c) {
        if (this.courses.contains(c, new CompareAllCourse())) {
            return;
        }
        else {
            this.courses = new ConsList<Course>(c, this.courses);
            c.enrollHelp(this);
        }
    }

    // determines whether the given Student is in any of the same classes as this Student
    boolean classmates(Student c) {
        return this.courses.compareMates(c.courses, new CompareAllCourse());
    }
}

class ExamplesRegistrar {

    //examples IComparator<T>
    IComparator<Course> compCourse = new CompareAllCourse();
    IComparator<Student> compareStud = new CompareStudent();

    //examples IList<Courses>
    IList<Course> mtCourse = new MtList<Course>();

    //examples IList<Student>
    IList<Student> mtStudent = new MtList<Student>();


    Instructor razzaq;
    Instructor hescott;
    Instructor nye;
    Instructor mathews;

    Student mike;
    Student alice;
    Student rachel;
    Student bill;
    Student dan;

    Course discrete;
    Course fundies2;
    Course bio;
    Course fun;
    Course noFun;
    Course chem;
    Course fundies1;

    //EFFECT: initializes the data to original condition
    void initData() {
        razzaq = new Instructor("Razzaq");
        hescott = new Instructor("Hescott");
        nye = new Instructor("Nye");
        mathews = new Instructor("Mathews");

        mike = new Student("Mike", 123);
        alice = new Student("Alice", 321);
        rachel = new Student("Rachel", 116);
        bill = new Student("Bill", 876);
        dan = new Student("Dan", 876);

        discrete = new Course("Discrete", this.hescott);
        fundies2 = new Course("Fundies2", this.razzaq);
        fundies1 = new Course("Fundies1", this.razzaq);
        bio = new Course("Biology", this.nye);
        fun = new Course("Fun", this.nye);
        noFun = new Course("No Fun", this.nye);
        chem = new Course("Chemistry", this.mathews);
    }

    //test for enroll
    void testEnroll(Tester t) {
        this.initData();
        //adding Mike to Fundies2
        t.checkExpect(this.mike.courses, this.mtCourse);
        this.mike.enroll(this.fundies2);
        t.checkExpect(this.mike.courses, new ConsList<Course>(this.fundies2, this.mtCourse));
        //adding Mike to Fundies2, again
        this.mike.enroll(this.fundies2);
        t.checkExpect(this.mike.courses, new ConsList<Course>(this.fundies2, this.mtCourse));
        t.checkExpect(this.fundies2.students, new ConsList<Student>(this.mike, this.mtStudent));
        //adding Alice to discrete
        t.checkExpect(this.alice.courses, this.mtCourse);
        this.alice.enroll(this.discrete);
        t.checkExpect(this.alice.courses, new ConsList<Course>(this.discrete, this.mtCourse));
        //adding Mike to discrete
        this.mike.enroll(this.discrete);
        t.checkExpect(this.mike.courses, new ConsList<Course>(this.discrete,
                new ConsList<Course>(this.fundies2, this.mtCourse)));
        //adding Mike to chem
        this.mike.enroll(this.chem);
        t.checkExpect(this.mike.courses, new ConsList<Course>(this.chem,
                new ConsList<Course>(this.discrete,
                        new ConsList<Course>(this.fundies2, this.mtCourse))));
    }

    //test for enrollHelp
    void testEnrollHelp(Tester t) {
        this.initData();
        //adding Mike to Fundies
        t.checkExpect(this.fundies2.students, this.mtStudent);
        this.fundies2.enrollHelp(this.mike);
        t.checkExpect(this.fundies2.students, new ConsList<Student>(this.mike, this.mtStudent));
        //adding Alice to discrete
        t.checkExpect(this.discrete.students, this.mtStudent);
        this.discrete.enrollHelp(this.alice);
        t.checkExpect(this.discrete.students, new ConsList<Student>(this.alice, this.mtStudent));
        //adding Mike to discrete
        t.checkExpect(this.discrete.students, new ConsList<Student>(this.alice, this.mtStudent));
        this.discrete.enrollHelp(this.mike);
        t.checkExpect(this.discrete.students, new ConsList<Student>(this.mike,
                new ConsList<Student>(this.alice, this.mtStudent)));
        //adding Mike to chem
        t.checkExpect(this.chem.students, this.mtStudent);
        this.chem.enrollHelp(this.mike);
        t.checkExpect(this.chem.students, new ConsList<Student>(this.mike, this.mtStudent));
        //adding Rachel to discrete
        t.checkExpect(this.discrete.students, new ConsList<Student>(this.mike,
                new ConsList<Student>(this.alice, this.mtStudent)));
        this.discrete.enrollHelp(this.rachel);
        t.checkExpect(this.discrete.students, new ConsList<Student>(this.rachel,
                new ConsList<Student>(this.mike,
                        new ConsList<Student>(this.alice, this.mtStudent))));
    }

    //test for enrollHelp
    void testContains(Tester t) {
        this.initData();
        //checking if Mike is in Fundies2
        t.checkExpect(this.mike.courses, this.mtCourse);
        this.mike.courses.contains(this.fundies2, this.compCourse);
        t.checkExpect(this.mike.courses, this.mtCourse);
        t.checkExpect(this.mike.courses.contains(this.fundies2, this.compCourse), false);
        //checking if Mike is i Fundies2 after being added
        this.mike.enroll(this.fundies2);
        t.checkExpect(this.mike.courses, new ConsList<Course>(this.fundies2, this.mtCourse));
        t.checkExpect(this.mike.courses.contains(this.fundies2, this.compCourse), true);
        //checking to see if Mike is in Discrete
        this.mike.courses.contains(this.discrete, new CompareAllCourse());
        t.checkExpect(this.mike.courses, new ConsList<Course>(this.fundies2, this.mtCourse));
        t.checkExpect(this.mike.courses.contains(this.discrete, this.compCourse), false);
        //checking to see if Mike is in Discrete after being added
        this.mike.enroll(this.discrete);
        t.checkExpect(this.mike.courses,
                new ConsList<Course>(this.discrete,
                        new ConsList<Course>(this.fundies2, this.mtCourse)));
        t.checkExpect(this.mike.courses.contains(this.discrete, this.compCourse), true);
        //checking to see if Alice is in discrete
        t.checkExpect(this.alice.courses, this.mtCourse);
        this.alice.courses.contains(this.discrete, new CompareAllCourse());
        t.checkExpect(this.alice.courses, this.mtCourse);
        t.checkExpect(this.alice.courses.contains(this.discrete, this.compCourse), false);
        //checking to see if Alice is in discrete after being added
        this.alice.enroll(this.discrete);
        t.checkExpect(this.alice.courses, new ConsList<Course>(this.discrete, this.mtCourse));
        t.checkExpect(this.alice.courses.contains(this.discrete, this.compCourse), true);
    }

    //test for classmates
    void testClassmates(Tester t) {
        this.initData();
        //checking if Mike and Alice are in any classes together
        t.checkExpect(this.mike.courses, this.mtCourse);
        t.checkExpect(this.alice.courses, this.mtCourse);
        t.checkExpect(this.alice.classmates(this.mike), false);
        //checking if Mike and Alice are in any classes together after Mike is added to
        //Fundies2 and Alice is added to discrete
        this.mike.enroll(this.fundies2);
        this.alice.enroll(this.discrete);
        t.checkExpect(this.mike.classmates(this.alice), false);
        //checking to see if Mike and Alice are in any classes together after Mike is added
        //to discrete and
        //Alice is added to chem
        this.mike.enroll(this.discrete);
        this.alice.enroll(this.chem);
        t.checkExpect(this.mike.classmates(this.alice), true);
        //checking to see if Alice is in any of the same classes as Rachel after she is
        //added to chem
        this.rachel.enroll(this.chem);
        t.checkExpect(this.rachel.classmates(this.alice), true);
        //checking to see if Alice is in any of the same classes as bill after he is added
        //to discrete
        this.bill.enroll(this.discrete);
        t.checkExpect(this.alice.classmates(this.bill), true);
        //checking to see if Mike is in any of the same classes as Dan after he is added to bio
        this.dan.enroll(this.bio);
        t.checkExpect(this.mike.classmates(this.dan), false);
    }

    //test for compareMates
    void testCompareMates(Tester t) {
        this.initData();
        //checking if Mike and Alice are in any classes together
        t.checkExpect(this.mike.courses, this.mtCourse);
        t.checkExpect(this.alice.courses, this.mtCourse);
        t.checkExpect(this.alice.courses.compareMates(this.mike.courses, this.compCourse), false);
        //checking if Mike and Alice are in any classes together after Mike is added to
        //Fundies2 and Alice is added to discrete
        this.mike.enroll(this.fundies2);
        this.alice.enroll(this.discrete);
        t.checkExpect(this.mike.courses.compareMates(this.alice.courses, this.compCourse), false);
        //checking to see if Mike and Alice are in any classes together after Mike is added
        //to discrete and
        //Alice is added to chem
        this.mike.enroll(this.discrete);
        this.alice.enroll(this.chem);
        t.checkExpect(this.mike.courses.compareMates(this.alice.courses, this.compCourse), true);
        //checking to see if Alice is in any of the same classes as Rachel after she is
        //added to chem
        this.rachel.enroll(this.chem);
        t.checkExpect(this.rachel.courses.compareMates(this.alice.courses, this.compCourse), true);
        //checking to see if Alice is in any of the same classes as bill after he is added
        //to discrete
        this.bill.enroll(this.discrete);
        t.checkExpect(this.alice.courses.compareMates(this.bill.courses, this.compCourse), true);
        //checking to see if Mike is in any of the same classes as Dan after he is added to bio
        this.dan.enroll(this.bio);
        t.checkExpect(this.mike.courses.compareMates(this.dan.courses, this.compCourse), false);
    }

    //test for compareMatesHelp
    void testCompareMatesHelp(Tester t) {
        this.initData();
        //checking if Mike and Alice are in any classes together after Mike is added to
        //Fundies2 and Alice is added to discrete
        this.mike.enroll(this.fundies2);
        this.alice.enroll(this.discrete);
        t.checkExpect(this.mike.courses.compareMatesHelp((ConsList<Course>)this.alice.courses,
                this.compCourse), false);
        //checking to see if Mike and Alice are in any classes together after Mike is added
        //to discrete and
        //Alice is added to chem
        this.mike.enroll(this.discrete);
        this.alice.enroll(this.chem);
        t.checkExpect(this.mike.courses.compareMatesHelp((ConsList<Course>)this.alice.courses,
                this.compCourse), true);
        //checking to see if Alice is in any of the same classes as Rachel after she is
        //added to chem
        this.rachel.enroll(this.chem);
        t.checkExpect(this.rachel.courses.compareMatesHelp((ConsList<Course>)this.alice.courses,
                this.compCourse), true);
        //checking to see if Alice is in any of the same classes as bill after he is added
        //to discrete
        this.bill.enroll(this.discrete);
        t.checkExpect(this.alice.courses.compareMatesHelp((ConsList<Course>)this.bill.courses,
                this.compCourse), true);
        //checking to see if Mike is in any of the same classes as Dan after he is added to bio
        this.dan.enroll(this.bio);
        t.checkExpect(this.mike.courses.compareMatesHelp((ConsList<Course>)this.dan.courses,
                this.compCourse), false);
    }


    //test for dejavu
    void testDejavu(Tester t) {
        this.initData();
        //checking if Mike is in more than one of the classes Razzaq teaches
        t.checkExpect(this.razzaq.courses, new ConsList<Course>(this.fundies1,
                new ConsList<Course>(this.fundies2, this.mtCourse)));
        t.checkExpect(this.mike.courses, this.mtCourse);
        t.checkExpect(this.razzaq.dejavu(this.mike), false);
        //checking if Mike is in more than one of the classes Razzaq teaches after Mike is
        //added to fundies2
        this.mike.enroll(this.fundies2);
        t.checkExpect(this.razzaq.dejavu(this.mike), false);
        //checking if Mike is in more than one of the classes Razzaq teaches Mike is added to bio
        this.mike.enroll(this.bio);
        t.checkExpect(this.razzaq.dejavu(this.mike), false);
        //checking if Mike is in more than one of the classes Razzaq teaches after Mike is
        //added to fundies1
        this.mike.enroll(this.fundies1);
        t.checkExpect(this.razzaq.dejavu(this.mike), true);
        //checking if Mike is in more than one of the classes nye teaches
        t.checkExpect(this.nye.dejavu(this.mike), false);
        //checking if Mike is in more than one of the classes nye teaches after being added to fun
        this.mike.enroll(this.fun);
        t.checkExpect(this.nye.dejavu(this.mike), true);
        //checking if Mike is in more than one of the classes nye teaches after being added to no Fun
        this.mike.enroll(this.noFun);
        t.checkExpect(this.nye.dejavu(this.mike), true);
    }

    //test for atLeastTwo
    void testAtLeastTwo(Tester t) {
        this.initData();
        //checking if Mike is in more than one of the classes Razzaq teaches
        t.checkExpect(this.razzaq.courses, new ConsList<Course>(this.fundies1,
                new ConsList<Course>(this.fundies2, this.mtCourse)));
        t.checkExpect(this.mike.courses, this.mtCourse);
        t.checkExpect(this.razzaq.courses.atLeastTwo(new CompareOneCourse(this.mike)), 0);
        //checking if Mike is in more than one of the classes Razzaq teaches after Mike
        //is added to fundies2
        this.mike.enroll(this.fundies2);
        t.checkExpect(this.razzaq.courses.atLeastTwo(new CompareOneCourse(this.mike)), 1);
        //checking if Mike is in more than one of the classes Razzaq teaches Mike is added to bio
        this.mike.enroll(this.bio);
        t.checkExpect(this.razzaq.courses.atLeastTwo(new CompareOneCourse(this.mike)), 1);
        //checking if Mike is in more than one of the classes Razzaq teaches after Mike
        //is added to fundies1
        this.mike.enroll(this.fundies1);
        t.checkExpect(this.razzaq.courses.atLeastTwo(new CompareOneCourse(this.mike)), 2);
        //checking if Mike is in more than one of the classes nye teaches
        t.checkExpect(this.nye.courses.atLeastTwo(new CompareOneCourse(this.mike)), 1);
        //checking if Mike is in more than one of the classes nye teaches after being
        //added to fun
        this.mike.enroll(this.fun);
        t.checkExpect(this.nye.courses.atLeastTwo(new CompareOneCourse(this.mike)), 2);
        //checking if Mike is in more than one of the classes nye teaches after
        //being added to no Fun
        this.mike.enroll(this.noFun);
        t.checkExpect(this.nye.courses.atLeastTwo(new CompareOneCourse(this.mike)), 3);
    }

    //test for apply
    void testApply(Tester t) {
        this.initData();
        //IComparator<Course> Tests
        this.mike.enroll(this.fundies1);
        this.mike.enroll(this.bio);
        this.rachel.enroll(this.fundies2);
        this.mike.enroll(this.chem);
        this.bill.enroll(this.discrete);
        this.dan.enroll(this.fun);
        this.alice.enroll(this.fundies1);
        this.alice.enroll(this.noFun);
        this.dan.enroll(this.noFun);
        this.rachel.enroll(this.discrete);
        t.checkExpect(this.compCourse.apply(this.fundies1, this.fundies1), true);
        t.checkExpect(this.compCourse.apply(this.chem, this.chem), true);
        t.checkExpect(this.compCourse.apply(this.bio, this.bio), true);
        t.checkExpect(this.compCourse.apply(this.fun, this.fun), true);
        t.checkExpect(this.compCourse.apply(this.noFun, this.noFun), true);
        t.checkExpect(this.compCourse.apply(this.fundies2, this.fundies2), true);
        t.checkExpect(this.compCourse.apply(this.discrete, this.discrete), true);
        t.checkExpect(this.compCourse.apply(this.bio, this.chem), false);
        t.checkExpect(this.compCourse.apply(this.chem, this.fundies2), false);
        t.checkExpect(this.compCourse.apply(this.noFun, this.chem), false);
        t.checkExpect(this.compCourse.apply(this.fun, this.bio), false);
        t.checkExpect(this.compCourse.apply(this.discrete, this.fundies2), false);


        //IComparator<Student> Tests
        t.checkExpect(this.compareStud.apply(this.alice, this.alice), true);
        t.checkExpect(this.compareStud.apply(this.dan, this.dan), true);
        t.checkExpect(this.compareStud.apply(this.bill, this.bill), true);
        t.checkExpect(this.compareStud.apply(this.rachel, this.rachel),true);
        t.checkExpect(this.compareStud.apply(this.mike, this.mike), true);
        t.checkExpect(this.compareStud.apply(this.bill, this.mike), false);
        t.checkExpect(this.compareStud.apply(this.alice, this.bill), false);
        t.checkExpect(this.compareStud.apply(this.dan, this.rachel), false);
        t.checkExpect(this.compareStud.apply(this.mike, this.alice), false);
        t.checkExpect(this.compareStud.apply(this.rachel, this.alice), false);

        //IPred<Course> Tests
        t.checkExpect(new CompareOneCourse(this.mike).apply(this.fundies1), true);
        t.checkExpect(new CompareOneCourse(this.mike).apply(this.bio), true);
        t.checkExpect(new CompareOneCourse(this.mike).apply(this.fundies1), true);
        t.checkExpect(new CompareOneCourse(this.bill).apply(this.discrete), true);
        t.checkExpect(new CompareOneCourse(this.alice).apply(this.chem), false);
        t.checkExpect(new CompareOneCourse(this.rachel).apply(this.chem), false);
        t.checkExpect(new CompareOneCourse(this.rachel).apply(this.bio), false);
        t.checkExpect(new CompareOneCourse(this.mike).apply(this.fundies2), false);
        t.checkExpect(new CompareOneCourse(this.bill).apply(this.noFun), true);
    }

}