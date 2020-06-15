package it.tiburtinavalley.marvelheroes;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Comics implements Parcelable {
    private List<Items> items;

    protected Comics(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comics> CREATOR = new Creator<Comics>() {
        @Override
        public Comics createFromParcel(Parcel in) {
            return new Comics(in);
        }

        @Override
        public Comics[] newArray(int size) {
            return new Comics[size];
        }
    };

    public List<Items> getItems() {
        return items;
    }
}
