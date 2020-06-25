package it.tiburtinavalley.marvelheroes.model;

/* Questa Model mantiene informazioni sui creators di un fumetto*/

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Creators extends BasicElement {
    private List<Items> items;
    private String fullName;

    protected Creators(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
        id = in.readString();
        fullName = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        urls = in.readArrayList(Urls.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeString(id);
        dest.writeString(fullName);
        dest.writeParcelable(thumbnail, flags);
        dest.writeTypedList(urls);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Creators> CREATOR = new Creator<Creators>() {
        @Override
        public Creators createFromParcel(Parcel in) {
            return new Creators(in);
        }

        @Override
        public Creators[] newArray(int size) {
            return new Creators[size];
        }
    };

    public List<Items> getItems() {
        return items;
    }

    public String getFullName() {
        return fullName;
    }
}
