package it.tiburtinavalley.marvelheroes;

import android.os.Parcel;
import android.os.Parcelable;


public class HeroModel implements Parcelable {
    String id;
    String name;
    String description;
    String resourceURI;
    Thumbnail thumbnail;


    protected HeroModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        resourceURI = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
    }

    class Thumbnail {
        private String path;
        private String extension;

        public String getPath(){
            return this.path;
        }

        public String getExtension(){
            return this.extension;
        }
    }
}
