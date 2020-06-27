package it.tiburtinavalley.marvelheroes.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/* Model per mantenere i dati relativi ai fumetti legati agli eroi */

public class Comics extends Element {
    private List<Items> variants; //other comics related to the main one
    private List<Images> images;
    private String upc;
    private String diamondCode;
    private String isbn;
    private String pageCount;

    protected Comics(Parcel in) {
        variants = in.createTypedArrayList(Items.CREATOR);
        urls = in.createTypedArrayList(Urls.CREATOR);
        images = in.createTypedArrayList(Images.CREATOR);
        id = in.readString();
        title = in.readString();
        description = in.readString();
        upc = in.readString();
        diamondCode = in.readString();
        isbn = in.readString();
        pageCount = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(variants);
        dest.writeTypedList(urls);
        dest.writeTypedList(images);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(upc);
        dest.writeString(isbn);
        dest.writeString(diamondCode);
        dest.writeString(pageCount);
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
        return variants;
    }

    public List<Images> getImages() {
        return images;
    }

    public String getUpc() {
        return upc;
    }

    public String getDiamondCode() {
        return diamondCode;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPageCount() {
        return pageCount;
    }

}
