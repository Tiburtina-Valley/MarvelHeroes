package it.tiburtinavalley.marvelheroes.Model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

import it.tiburtinavalley.marvelheroes.Volley.ImageApiVolley;

/* Model per mantenere i dati relativi ai fumetti legati agli eroi */

public class Comics implements Parcelable {
    private List<Items> items;
    private List<Images> images;
    private List<Urls> urls;
    private String id;
    private String title;
    private Thumbnail thumbnail;
    private String description;
    private String upc;
    private String diamondCode;
    private String isbn;
    private String pageCount;
    //private List<Creators> creators;

    protected Comics(Parcel in) {
        items = in.createTypedArrayList(Items.CREATOR);
        urls = in.createTypedArrayList(Urls.CREATOR);
        images = in.createTypedArrayList(Images.CREATOR);
        //creators = in.createTypedArrayList(Creators.CREATOR);
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
        dest.writeTypedList(items);
        dest.writeTypedList(urls);
        dest.writeTypedList(images);
        //dest.writeTypedList(creators);
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

    public List<Images> getImages() {
        return images;
    }

    public String getDescription() {
        return description;
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

    public List<Urls> getUrls() {
        return urls;
    }

    /*public List<Creators> getCreators() {
        return creators;
    }*/
}
