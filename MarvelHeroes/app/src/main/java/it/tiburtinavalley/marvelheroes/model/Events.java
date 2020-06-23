package it.tiburtinavalley.marvelheroes.model;

/* Model che mantiene le informazioni relative ad un evento per un particolare eroe */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Events extends Element {
    private String title;
    private String start;
    private String end;
    //private List<Creators> creators;
    private List<Images> images;
    private String pageCount;

    protected Events(Parcel in) {
        pageCount = in.readString();
        images = in.createTypedArrayList(Images.CREATOR);
        id = in.readString();
        title = in.readString();
        description = in.readString();
        start = in.readString();
        end = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        urls = in.createTypedArrayList(Urls.CREATOR);
        //creators = in.createTypedArrayList(Creators.CREATOR);
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
        parcel.writeTypedList(urls);
        //parcel.writeTypedList(creators);
        parcel.writeString(pageCount);
        parcel.writeTypedList(images);

    }

    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

   // public List<Creators> getCreators() {
     //   return creators;
   // }

    public List<Images> getImages() {
        return images;
    }

    public String getPageCount() {
        return pageCount;
    }
}
