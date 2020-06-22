package it.tiburtinavalley.marvelheroes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Series implements Parcelable {
    private List<Items> items;
    private String id;
    private String title;
    private String description;
    private String startYear;
    private String endYear;
    private String rating;
    private String type;
    private Thumbnail thumbnail;

    protected Series(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
        id = in.readString();
        title = in.readString();
        description = in.readString();
        startYear = in.readString();
        endYear = in.readString();
        rating = in.readString();
        type = in.readString();
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

    public String getDescription() {
        return description;
    }

    public String getStartYear() {
        return startYear;
    }

    public String getEndYear() {
        return endYear;
    }

    public String getRating() {
        return rating;
    }

    public String getType() {
        return type;
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
        dest.writeString(type);
        dest.writeString(description);
        dest.writeString(startYear);
        dest.writeString(endYear);
        dest.writeString(rating);
        dest.writeParcelable(thumbnail, flags);
    }
}
