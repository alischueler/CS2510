import tester.Tester;

interface ILoFeature {

    //determines the sum of average ratings of Restaurants within this List-of Features
    double averageFoodiness();

    //determines how many Restaurants are in this List-of Features
    int howManyRestaurants();

    //creates a string of all Restaurants and their type in this List-of Features
    String listOfRestaurants();

}

class MtLoFeature implements ILoFeature { //represents an empty List-of Features
    MtLoFeature() {}

    /*TEMPLATE
     * Methods:
     * this.averageFoodiness()  double
     * this.howManyRestaurants() int
     * this.listOfRestaurants() String
     */

    //determines the average food ratings of Restaurants in this empty list
    //there are no restaurants in an empty list, it is 0
    public double averageFoodiness() {
        return 0;
    }

    //determines how many Restaurants are in this MtList-of Features, it is 0
    public int howManyRestaurants() {
        return 0;
    }

    //creates a string of all Restaurants and their type in this MtList-of Features
    //there are no Restaurants in an empty List-of Features, it is an empty String
    public String listOfRestaurants() {
        return "";
    }

}

class ConsLoFeature implements ILoFeature { //represents a non-empty List-of Features
    IFeature first;
    ILoFeature rest;

    ConsLoFeature(IFeature first, ILoFeature rest) {
        this.first = first;
        this.rest = rest;
    }

    /*TEMPLATE
     * Fields:
     * this.first  IFeature
     * this.rest  ILoFeature
     * Methods:
     * this.averageFoodiness()  double
     * this.howManyRestaurants() int
     * this.listOfRestaurants() String
     * Methods of Fields:
     * this.first.averageRating()  double
     * this.first.addOneIfRestaurant()  int
     * this.first.restaurantType()  String
     * this.rest.averageFoodiness() double
     * this.rest.howManyRestaurants() int
     * this.rest.listOfRestaurants()  String
     */

    //calculates the sum of the average food ratings of all Restaurants in this List-of Features
    public double averageFoodiness() {
        return this.first.averageRating() + this.rest.averageFoodiness();
    }

    //calculates how many Restaurants are in this List-of Features
    public int howManyRestaurants() {
        return this.first.addOneIfRestaurant() + this.rest.howManyRestaurants();
    }

    //creates a String of all Restaurants and their type in this List-of Features
    public String listOfRestaurants() {
        String first = this.first.restaurantType();
        String rest = this.rest.listOfRestaurants();

        if (first.equals("")) {
            return this.rest.listOfRestaurants();
        }
        else {
            if (rest.equals("")) {
                return this.first.restaurantType();
            }
            else {
                return this.first.restaurantType() + ", " + this.rest.listOfRestaurants();
            }
        }
    }
}

interface IFeature { //represents a Feature of a Place

    //determines the average food rating of this Feature
    double averageRating();

    //adds one if this Feature is also a Restaurant
    int addOneIfRestaurant();

    //If this Feature is a Restaurant, produces a string of this Restaurant and its type
    String restaurantType();

}

class Restaurant implements IFeature { //represents a Restaurant
    String name;
    String type;
    double averageRating;

    Restaurant(String name, String type, double averageRating) {
        this.name = name;
        this.type = type;
        this.averageRating = averageRating;
    }

    /*TEMPLATE
     * Fields:
     * this.name  String
     * this.type  String
     * this.averageRating double
     * Methods:
     * this.averageRating()  double
     * this.addOneIfRestaurant()  int
     * this.restaurantType()  String
     */

    //determine the average food rating of this Restaurant
    public double averageRating() {
        return this.averageRating;
    }

    //adds one because this is Restaurant is a Restaurant
    public int addOneIfRestaurant() {
        return 1;
    }

    //This Restaurant is a Restaurant so produces a string of this Restaurant's name and type
    public String restaurantType() {
        return this.name + (" (" + (this.type + ")"));
    }

}

class Venue implements IFeature { //represents some kind of Venue
    String name;
    String type;
    int capacity;

    Venue(String name, String type, int capacity) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
    }

    /*TEMPLATE
     * Fields:
     * this.name  String
     * this.type  String
     * this.capacity  int
     * Methods:
     * this.averageRating() double
     * this.addOneIfRestaurant()  int
     * this.restaurantType()  String
     */

    //This Venue is not a restaurant, so adds 0
    public double averageRating() {
        return 0;
    }

    //This Venue is not a restaurant, so adds 0
    public int addOneIfRestaurant() {
        return 0;
    }

    //this Venue is not a restaurant so returns an empty String
    public String restaurantType() {
        return "";
    }

}

class ShuttleBus implements IFeature { //represents a ShuttleBus from one Place to another
    String name;
    Place destination;

    ShuttleBus(String name, Place destination) {
        this.name = name;
        this.destination = destination;
    }

    /*TEMPLATE
     * Fields:
     * this.name  String
     * this.destination  Place
     * Methods:
     * this.averageRating()  double
     * this.addOneIfRestaurant()  int
     * this.restaurantType()  String
     * Methods of Fields:
     * this.destination.continueCountingRatings() double
     * this.destination.foodinessRating()  double
     * this.destination.restaurantInfo()  String
     */

    //This ShuttleBus is not a Restaurant, but it can reach Restaurants of other Places,
    //so delegates to the Places class to see if that Place has any Restaurant ratings
    public double averageRating() {
        return this.destination.continueCountingRatings();
    }

    //this ShuttleBus is not a Restaurant, but it can reach Restaurants of other Places,
    //so delegates back to Places class to see if that Place has any Restaurants to add
    public int addOneIfRestaurant() {
        return this.destination.continueCountingRestaurants();
    }

    //this ShuttleBus is not a Restaurant, but it can reach Restaurants of other Places,
    //so delegates back to Places class to see if that Place has any Restaurants to include
    public String restaurantType() {
        return this.destination.restaurantInfo();
    }

}

class Place { //represents a Place
    String name;
    ILoFeature features;

    Place(String name, ILoFeature features) {
        this.name = name;
        this.features = features;
    }
    /*TEMPLATE
     * Fields:
     * this.name  String
     * this.features  ILoFeature
     * Methods:
     * this.foodinessRating()  double
     * this.continueCountingRatings() double
     * this.restaurantInfo()  String
     * this.continueCountingRestaurants() int
     * Methods of Fields:
     * this.features.averageFoodiness() double
     * this.features.howManyRestaurants() int
     * this.features.listOfRestaurants()  String
     */

    //Computes the average rating of all the Restaurants ratings
    //for all Restaurants reachable by this Place
    double foodinessRating() {
        if (this.features.howManyRestaurants() == 0) {
            return 0.0;
        }
        else {
            return this.features.averageFoodiness() / this.features.howManyRestaurants();
        }
    }

    //Continues counting the average ratings of the Restaurants reachable by that ShuttleBus
    double continueCountingRatings() {
        return this.features.averageFoodiness();
    }

    //Produces a String of all Restaurants and their types reachable from this Place
    String restaurantInfo() {
        return this.features.listOfRestaurants();
    }

    //Continues counting the Restaurants reachable by that ShuttleBus
    int continueCountingRestaurants() {
        return this.features.howManyRestaurants();
    }

}

class ExamplesPlaces {
    ExamplesPlaces() {}

    ILoFeature mtPlace = new MtLoFeature();

    IFeature theGarden = new Venue("TD Garden", "stadium", 19580);
    IFeature dailyCatch = new Restaurant("The Daily Catch", "Sicilian", 4.4);
    ILoFeature place3B = new ConsLoFeature(this.dailyCatch, this.mtPlace);
    ILoFeature place3A = new ConsLoFeature(this.theGarden, this.place3B);
    Place northEnd = new Place("North End", this.place3A);

    IFeature fresh15 = new ShuttleBus("Freshmen-15", this.northEnd);
    IFeature borderCafe = new Restaurant("Border Cafe", "Tex-Mex", 4.5);
    IFeature harvStadium = new Venue("Harvard Stadium", "football", 30323);
    ILoFeature place4A = new ConsLoFeature(this.harvStadium, this.mtPlace);
    ILoFeature place4B = new ConsLoFeature(this.borderCafe, this.place4A);
    ILoFeature place4C = new ConsLoFeature(this.fresh15, this.place4B);
    Place harvard = new Place("Harvard", this.place4C);

    IFeature littleItaly = new ShuttleBus("Little Italy Express", this.northEnd);
    IFeature regPizza = new Restaurant("Regina's Pizza", "pizza", 4.0);
    IFeature crimsCruiser = new ShuttleBus("Crimson Cruiser", this.harvard);
    IFeature commons = new Venue("Boston Common", "public", 150000);
    ILoFeature place2A = new ConsLoFeature(this.commons, mtPlace);
    ILoFeature place2B = new ConsLoFeature(this.crimsCruiser, this.place2A);
    ILoFeature place2C = new ConsLoFeature(this.regPizza, this.place2B);
    ILoFeature place2D = new ConsLoFeature(this.littleItaly, this.place2C);
    Place southStation = new Place("South Station", this.place2D);

    IFeature teriyaki = new Restaurant("Sarku Japan", "teriyaki", 3.9);
    IFeature starbs = new Restaurant("Starbucks", "coffee", 4.1);
    IFeature bridgeShuttle = new ShuttleBus("bridge shuttle", this.southStation);
    ILoFeature place1A = new ConsLoFeature(this.bridgeShuttle, this.mtPlace);
    ILoFeature place1B = new ConsLoFeature(this.starbs, this.place1A);
    ILoFeature place1C = new ConsLoFeature(this.teriyaki, this.place1B);
    Place cambridgeSide = new Place("CambridgeSide Galleria", this.place1C);

    //my example
    IFeature dunks = new Restaurant("Dunks", "donuts", 2.7);
    ILoFeature donutLandPlace = new ConsLoFeature(this.dunks, this.mtPlace);
    Place donutLand = new Place("Donut Land", this.donutLandPlace);

    IFeature earls = new Restaurant("Earls", "american", 3.8);
    ILoFeature pruPlace = new ConsLoFeature(this.earls, this.mtPlace);
    Place pru = new Place("Prudential", this.pruPlace);

    IFeature fenwayPark = new Venue("Fenway Park", "baseball", 10000);
    IFeature neuShuttle = new ShuttleBus("Northeastern Shuttle", this.neu);
    IFeature earlsShuttle = new ShuttleBus("Earls Shuttle", this.pru);
    ILoFeature fenwayPlace1 = new ConsLoFeature(this.fenwayPark, this.mtPlace);
    ILoFeature fenwayPlace2 = new ConsLoFeature(this.neuShuttle, this.fenwayPlace1);
    ILoFeature fenwayPlace = new ConsLoFeature(this.earlsShuttle, this.fenwayPlace2);
    Place fenway = new Place("Fenway", this.fenwayPlace);

    IFeature tatte = new Restaurant("Tatte", "brunch", 4.2);
    IFeature pruShuttle = new ShuttleBus("Prudential Shuttle", this.pru);
    ILoFeature neuPlace1 = new ConsLoFeature(this.tatte, this.mtPlace);
    ILoFeature neuPlace = new ConsLoFeature(this.pruShuttle, this.neuPlace1);
    Place neu = new Place("Northeastern", this.neuPlace);

    //tests for foodinessRating()
    boolean testfoodinessRating(Tester t) {
        return
                t.checkInexact(this.cambridgeSide.foodinessRating(), 4.22, 0.01) &&
                        t.checkInexact(this.southStation.foodinessRating(), 4.33, 0.01) &&
                        t.checkInexact(this.northEnd.foodinessRating(), 4.4, 0.01) &&
                        t.checkInexact(this.harvard.foodinessRating(), 4.45, 0.01);
    }

    //tests for howManyRestaurants()
    boolean testhowManyRestaurants(Tester t) {
        return
                t.checkExpect(this.place1C.howManyRestaurants(), 6) &&
                        t.checkExpect(this.place2D.howManyRestaurants(), 4) &&
                        t.checkExpect(this.place3A.howManyRestaurants(), 1) &&
                        t.checkExpect(this.place4C.howManyRestaurants(), 2) &&
                        t.checkExpect(this.mtPlace.howManyRestaurants(), 0);
    }

    //tests for addOneIfRestaurant()
    boolean testaddOneIfRestaurant(Tester t) {
        return
                t.checkExpect(this.teriyaki.addOneIfRestaurant(), 1) &&
                        t.checkExpect(this.regPizza.addOneIfRestaurant(), 1) &&
                        t.checkExpect(this.bridgeShuttle.addOneIfRestaurant(), 4) &&
                        t.checkExpect(this.crimsCruiser.addOneIfRestaurant(), 2) &&
                        t.checkExpect(this.theGarden.addOneIfRestaurant(), 0) &&
                        t.checkExpect(this.harvStadium.addOneIfRestaurant(), 0);
    }

    //tests for continueCountingRestaurants()
    boolean testcontinueCountingRestaurants(Tester t) {
        return
                t.checkExpect(this.cambridgeSide.continueCountingRestaurants(), 6) &&
                        t.checkExpect(this.southStation.continueCountingRestaurants(), 4) &&
                        t.checkExpect(this.northEnd.continueCountingRestaurants(), 1) &&
                        t.checkExpect(this.harvard.continueCountingRestaurants(), 2);
    }

    //tests for averageRating()
    boolean testaverageRating(Tester t) {
        return
                t.checkExpect(this.teriyaki.averageRating(), 3.9) &&
                        t.checkExpect(this.regPizza.averageRating(), 4.0) &&
                        t.checkExpect(this.bridgeShuttle.averageRating(), 17.3) &&
                        t.checkExpect(this.crimsCruiser.averageRating(), 8.9) &&
                        t.checkExpect(this.theGarden.averageRating(), 0.0) &&
                        t.checkExpect(this.harvStadium.averageRating(), 0.0);
    }

    //tests for averageFoodiness()
    boolean testaverageFoodiness(Tester t) {
        return
                t.checkInexact(this.place1C.averageFoodiness(), 25.3, 0.01) &&
                        t.checkInexact(this.place2D.averageFoodiness(), 17.3, 0.01) &&
                        t.checkInexact(this.place3A.averageFoodiness(), 4.4, 0.01) &&
                        t.checkInexact(this.place4C.averageFoodiness(), 8.9, 0.01) &&
                        t.checkInexact(this.mtPlace.averageFoodiness(), 0.0, 0.01);
    }

    //tests for continueCountingRatings()
    boolean testcontinueCountingRatings(Tester t) {
        return
                t.checkInexact(this.cambridgeSide.continueCountingRatings(), 25.3, 0.01) &&
                        t.checkExpect(this.southStation.continueCountingRatings(), 17.3) &&
                        t.checkExpect(this.northEnd.continueCountingRatings(), 4.4) &&
                        t.checkExpect(this.harvard.continueCountingRatings(), 8.9);
    }

    //tests for restaurantInfo()
    boolean testrestaurantInfo(Tester t) {
        return
                t.checkExpect(this.cambridgeSide.restaurantInfo(),
                        "Sarku Japan (teriyaki), Starbucks (coffee), The Daily Catch (Sicilian), "
                                + "Regina's Pizza (pizza), The Daily Catch (Sicilian), Border Cafe (Tex-Mex)") &&
                        t.checkExpect(this.southStation.restaurantInfo(),
                                "The Daily Catch (Sicilian), Regina's Pizza (pizza), "
                                        + "The Daily Catch (Sicilian), Border Cafe (Tex-Mex)") &&
                        t.checkExpect(this.northEnd.restaurantInfo(),
                                "The Daily Catch (Sicilian)") &&
                        t.checkExpect(this.harvard.restaurantInfo(),
                                "The Daily Catch (Sicilian), Border Cafe (Tex-Mex)");
    }

    //tests for listOfRestaurants()
    boolean testlistOfRestaurants(Tester t) {
        return
                t.checkExpect(this.place1C.listOfRestaurants(),
                        "Sarku Japan (teriyaki), Starbucks (coffee), The Daily Catch (Sicilian), "
                                + "Regina's Pizza (pizza), The Daily Catch (Sicilian), Border Cafe (Tex-Mex)") &&
                        t.checkExpect(this.place2D.listOfRestaurants(),
                                "The Daily Catch (Sicilian), Regina's Pizza (pizza), The Daily Catch (Sicilian), "
                                        + "Border Cafe (Tex-Mex)") &&
                        t.checkExpect(this.place3A.listOfRestaurants(),
                                "The Daily Catch (Sicilian)") &&
                        t.checkExpect(this.place4C.listOfRestaurants(),
                                "The Daily Catch (Sicilian), Border Cafe (Tex-Mex)") &&
                        t.checkExpect(this.mtPlace.listOfRestaurants(),
                                "");
    }

    //tests for restaurantType()
    boolean testrestaurantType(Tester t) {
        return
                t.checkExpect(this.teriyaki.restaurantType(),
                        "Sarku Japan (teriyaki)") &&
                        t.checkExpect(this.regPizza.restaurantType(),
                                "Regina's Pizza (pizza)") &&
                        t.checkExpect(this.bridgeShuttle.restaurantType(),
                                "The Daily Catch (Sicilian), Regina's Pizza (pizza), The Daily Catch (Sicilian), "
                                        + "Border Cafe (Tex-Mex)") &&
                        t.checkExpect(this.crimsCruiser.restaurantType(),
                                "The Daily Catch (Sicilian), Border Cafe (Tex-Mex)") &&
                        t.checkExpect(this.theGarden.restaurantType(),
                                "") &&
                        t.checkExpect(this.harvStadium.restaurantType(),
                                "");
    }
}

//these methods are double counting values since both the Little Italy Shuttle 
//and Freshman 15 Shuttle go to the North End from their respective places and
//currently, there is no way to distinguish if a feature has already been looked
//at and can be disregarded.
//This duplication occurs in both the foodinessRating and restaurantInfo methods.