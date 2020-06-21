package it.tiburtinavalley.marvelheroes.Model;

/* Model che mantiene le informazioni relative ad un evento per un particolare eroe */

import android.os.Parcel;
import android.os.Parcelable;

public class Events implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String start;
    private String end;
    private Thumbnail thumbnail;

    protected Events(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        start = in.readString();
        end = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
    }

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(start);
        parcel.writeString(end);
        parcel.writeParcelable(thumbnail, i);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }
}
