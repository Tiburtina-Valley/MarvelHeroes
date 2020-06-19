package it.tiburtinavalley.marvelheroes.Model;

import android.os.Parcel;
import android.os.Parcelable;

/* Model che raccoglie tutte le informazioni relative al singolo eroe */

public class HeroModel implements Parcelable {
    private String id;
    private String name;
    private String description;
    private String resourceURI;
    private Thumbnail thumbnail;
    private Comics comics;
    private Series series;
    private Stories stories;


    protected HeroModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        resourceURI = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        comics = in.readParcelable(Comics.class.getClassLoader());
        stories = in.readParcelable(Stories.class.getClassLoader());
        series = in.readParcelable(Series.class.getClassLoader());
    }

    public static final Creator<HeroModel> CREATOR = new Creator<HeroModel>() {
        @Override
        public HeroModel createFromParcel(Parcel in) {
            return new HeroModel(in);
        }

        @Override
        public HeroModel[] newArray(int size) {
            return new HeroModel[size];
        }
    };

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public Thumbnail getThumbnail(){
        return this.thumbnail;
    }

    public Comics getComics() {
        return comics;
    }

    public Series getSeries() {
        return series;
    }

    public Stories getStories() {
        return stories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(resourceURI);
        dest.writeParcelable(thumbnail, flags);
        dest.writeParcelable(comics, flags);
        dest.writeParcelable(stories, flags);
        dest.writeParcelable(series, flags);
    }
}
