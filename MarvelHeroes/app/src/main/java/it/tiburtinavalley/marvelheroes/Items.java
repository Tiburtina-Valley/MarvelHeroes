package it.tiburtinavalley.marvelheroes;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {
    private String resourceURI;
    private String name;

    protected Items(Parcel in) {
        resourceURI = in.readString();
        name = in.readString();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public String getName(){
        return this.name;
    }

    public String getResourceURI(){
        return this.resourceURI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resourceURI);
        dest.writeString(name);
    }
}
