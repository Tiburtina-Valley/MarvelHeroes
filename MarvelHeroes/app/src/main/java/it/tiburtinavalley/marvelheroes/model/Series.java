package it.tiburtinavalley.marvelheroes.model;

import android.os.Parcel;

/** Classe series che estende la classe Element. Possiede come attributi, un titolo, un anno d'inizio e un anno di fine,
una descrizione, un id, un immagine thumbnails, una lista di urls, la tipologia di series e il rating associatogli */
public class Series extends Element {
    private String startYear;
    private String endYear;
    private String rating;
    private String type;

    public Series(){}

    /** Definisco le operazioni necessarie per rendere l'elemento parcelable */
    protected Series(Parcel in) {
        urls = in.createTypedArrayList(Urls.CREATOR);
        id = in.readString();
        title = in.readString();
        type = in.readString();
        description = in.readString();
        startYear = in.readString();
        endYear = in.readString();
        rating = in.readString();
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

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(urls);
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
