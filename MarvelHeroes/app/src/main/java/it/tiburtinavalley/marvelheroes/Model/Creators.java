package it.tiburtinavalley.marvelheroes.Model;

/* Questa Model mantiene informazioni sui creators di un fumetto*/

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Creators implements Parcelable {
    private List<Items> items;
    private String id; // può servire per fare delle query relative all'autore
    private String fullName;
    private Thumbnail thumbnail;

    protected Creators(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
        id = in.readString();
        fullName = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeString(id);
        dest.writeString(fullName);
        dest.writeParcelable(thumbnail, flags);
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

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }
}
