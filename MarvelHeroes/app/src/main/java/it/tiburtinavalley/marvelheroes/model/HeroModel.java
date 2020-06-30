package it.tiburtinavalley.marvelheroes.model;

import android.os.Parcel;
import android.os.Parcelable;

import it.tiburtinavalley.marvelheroes.entity.HeroEntity;

/* Model che raccoglie tutte le informazioni relative al singolo eroe */

public class HeroModel extends BasicElement {
    private String name;
    private String resourceURI;
    private String description;

    public HeroModel(){}

    public HeroModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        resourceURI = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());

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

    public String getName(){
        return this.name;
    }

    public String getDescription() {
        return description;
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

    }

    public void setHeroModelFromDb(HeroEntity hero){
        this.id=Integer.toString(hero.getHeroId());
        this.name=hero.getName();
        this.description=hero.getDescription();
        this.resourceURI=hero.getPicturePath();
    }
}
