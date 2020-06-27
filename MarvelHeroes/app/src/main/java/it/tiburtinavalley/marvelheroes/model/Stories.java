package it.tiburtinavalley.marvelheroes.model;

import android.os.Parcel;
import android.os.Parcelable;

//Storie collegate ad un fumetto e ad una serie
public class Stories implements Parcelable {
    private String id;
    private String type;
    private String title;

    protected Stories(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readString();
    }

    public static final Creator<Stories> CREATOR = new Creator<Stories>() {
        @Override
        public Stories createFromParcel(Parcel in) {
            return new Stories(in);
        }

        @Override
        public Stories[] newArray(int size) {
            return new Stories[size];
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
        parcel.writeString(type);
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
