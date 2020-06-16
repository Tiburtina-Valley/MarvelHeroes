package it.tiburtinavalley.marvelheroes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Series implements Parcelable {
    List<Items> items;

    protected Series(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }
}
