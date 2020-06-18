package it.tiburtinavalley.marvelheroes.Model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Comics implements Parcelable {
    private List<Items> items;
    private String id;
    private String title;
    private Thumbnail thumbnail;

    protected Comics(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
        id = in.readString();
        title = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeParcelable(thumbnail, flags);
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

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }
}
