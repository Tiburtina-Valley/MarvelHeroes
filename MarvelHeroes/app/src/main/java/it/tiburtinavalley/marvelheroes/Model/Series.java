package it.tiburtinavalley.marvelheroes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Series implements Parcelable {
    List<Items> items;
        String id;
    String title;
    Thumbnail thumbnail;

    protected Series(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
        id = in.readString();
        title = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
    }

    public static final Creator<Series> CREATOR = new Creator<Series>() {
        @Override
        public Series createFromParcel(Parcel in) {
            return new Series(in);
        }

        @Override
        public Series[] newArray(int size) {
            return new Series[size];
        }
    };

    public List<Items> getItems() {
        return items;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeParcelable(thumbnail, flags);
    }
}
