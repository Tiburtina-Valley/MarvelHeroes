package it.tiburtinavalley.marvelheroes.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

//TODO : Capire a che cazzo servono ste storie
public class Stories extends Element implements Parcelable {
    List<Items> items;
    private List<Images> images;
    private String pageCount;

    protected Stories(Parcel in) {

        items = in.createTypedArrayList(Items.CREATOR);

    pageCount = in.readString();
    images = in.createTypedArrayList(Images.CREATOR);}

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

    public List<Items> getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(items);
    }

    public Thumbnail getThumbnail() {
        return null;
    }

    public String getTitle() {
        return null;
    }
    public List<Images> getImages() {
        return images;
    }

    public String getPageCount() {
        return pageCount;
    }
}
