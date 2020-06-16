package it.tiburtinavalley.marvelheroes.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {
    private String resourceURI;
    private String name;
    private String type;

    protected Items(Parcel in) {
        resourceURI = in.readString();
        name = in.readString();
        type = in.readString();
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

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resourceURI);
        dest.writeString(name);
        dest.writeString(type);
    }
}
