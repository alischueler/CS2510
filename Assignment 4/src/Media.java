import tester.Tester;

// a piece of media
interface IMedia {

  // is this media really old?
  boolean isReallyOld();

  // are captions available in this language?
  boolean isCaptionAvailable(String language);

  // a string showing the proper display of the media
  String format();
}

abstract class AMedia implements IMedia {
  String title;
  ILoString captionOptions;

  AMedia(String title, ILoString captionOptions) {
    this.title = title;
    this.captionOptions = captionOptions;
  }

  public boolean isReallyOld() {
    return false;
  }

  public boolean isCaptionAvailable(String language) {
    return this.captionOptions.isCaption(language);
  }

  // a string showing the proper display of the media
  abstract public String format();
}

//represents a movie
class Movie extends AMedia {
  int year;

  Movie(String title, int year, ILoString captionOptions) {
    super(title, captionOptions);
    this.year = year;
  }

  // overides isReallyOld()
  public boolean isReallyOld() {
    return (this.year < 1930);
  }

  public String format() {
    return this.title + " (" + this.year + ")";
  }
}

class TVEpisode extends AMedia {
  String showName;
  int seasonNumber;
  int episodeOfSeason;

  TVEpisode(String title, String showName, int seasonNumber, int episodeOfSeason,
            ILoString captionOptions) {
    super(title, captionOptions);
    this.showName = showName;
    this.seasonNumber = seasonNumber;
    this.episodeOfSeason = episodeOfSeason;
  }

  public String format() {
    return this.showName + " " + this.seasonNumber + "." +
            this.episodeOfSeason + " - " + this.title;
  }
}

//represents a YouTube video
class YTVideo extends AMedia {
  String channelName;

  public YTVideo(String title, String channelName, ILoString captionOptions) {
    super(title, captionOptions);
    this.channelName = channelName;
  }

  public String format() {
    return this.title + " by " + this.channelName;
  }
}

//lists of strings
interface ILoString {

  //helps determine if the given language is available for caption
  boolean isCaption(String language);
}


//an empty list of strings
class MtLoString implements ILoString {
  public boolean isCaption(String language) {
    return false;
  }

}

//a non-empty list of strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean isCaption(String language) {
    if (this.first.equals(language)) {
      return true;
    }
    else {
      return this.rest.isCaption(language);

    }
  }
}


class ExamplesMedia {

  //movie exmaples
  IMedia movie1 = new Movie("Ten", 1925, new ConsLoString("Spanish",
          new ConsLoString("English", new MtLoString())));
  IMedia movie2 = new Movie("Karate Kid", 1990, new MtLoString());

  //tv examples
  IMedia tv1 = new TVEpisode("Pilot", "Friends", 1, 1, new ConsLoString("English",
          new ConsLoString("French", new MtLoString())));
  IMedia tv2 = new TVEpisode("This One", "That 70s Show", 4, 16, new MtLoString());

  //youtube examples
  IMedia yt1 = new YTVideo("Prank", "JaneSmith", new ConsLoString("English",
          new ConsLoString("Manderin", new ConsLoString("French", new MtLoString()))));
  IMedia ty2 = new YTVideo("Life Hacks", "CoolTrticks", new MtLoString());

  //test for isReallyOld()
  boolean testIsreallyOld(Tester t) {
    return
            t.checkExpect(this.movie1.isReallyOld(), true)
                    && t.checkExpect(this.movie2.isReallyOld(), false)
                    && t.checkExpect(this.tv1.isReallyOld(), false)
                    && t.checkExpect(this.tv2.isReallyOld(), false)
                    && t.checkExpect(this.yt1.isReallyOld(), false)
                    && t.checkExpect(this.ty2.isReallyOld(), false);
  }

  //test for isCaptionAvailable(String language)
  boolean testisCaptionAvailable(Tester t) {
    return
            t.checkExpect(this.movie1.isCaptionAvailable("English"), true)
                    && t.checkExpect(this.movie1.isCaptionAvailable("Hindi"), false)
                    && t.checkExpect(this.movie2.isCaptionAvailable("English"), false)
                    && t.checkExpect(this.tv1.isCaptionAvailable("French"), true)
                    && t.checkExpect(this.yt1.isCaptionAvailable("Japanese"), false);
  }


  //test for isCaption(String language)
  boolean testisCaption(Tester t) {
    return
            t.checkExpect(new ConsLoString("Spanish", new ConsLoString("English",
                    new MtLoString())).isCaption("English"), true)
                    && t.checkExpect(new MtLoString().isCaption("Hindi"), false)
                    && t.checkExpect(new MtLoString().isCaption("English"), false)
                    && t.checkExpect(new ConsLoString("English", new ConsLoString("French",
                    new MtLoString())).isCaption("French"), true)
                    && t.checkExpect(new ConsLoString("English", new ConsLoString("Manderin",
                    new ConsLoString("French", new MtLoString()))).isCaption("Japanese"), false);
  }

  //test for format()
  boolean testformat(Tester t) {
    return
            t.checkExpect(this.movie1.format(), "Ten (1925)")
                    && t.checkExpect(this.movie2.format(), "Karate Kid (1990)")
                    && t.checkExpect(this.tv1.format(), "Friends 1.1 - Pilot")
                    && t.checkExpect(this.tv2.format(), "That 70s Show 4.16 - This One")
                    && t.checkExpect(this.yt1.format(), "Prank by JaneSmith");

  }
}
