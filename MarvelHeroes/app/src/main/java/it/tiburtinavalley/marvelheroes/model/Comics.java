package it.tiburtinavalley.marvelheroes.model;

import android.os.Parcel;

import java.util.List;

/** Model per mantenere i dati relativi ai fumetti legati agli eroi */

public class Comics extends Element {
    private List<Items> variants; // Varianti del fumetto
    private List<Images> images; // Altre immagini del fumetto
    private String upc; // Codice UPC del fumetto
    private String diamondCode;
    private String isbn; //ISBN del fumetto
    private String pageCount; // Numero di pagine del fumetto

    /** Costruttore di default necessario per caricare il dettaglio di un elemento dal Fragment dei preferiti dell'utente */
    public Comics(){}

    /** Costruttore chiamato quando viene estratto il Parcelable in un'Activity */
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

    /** Metodo per inserire il Parcel cone Extra nell'Intent */
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

    /** Descrive il tipo di possibili oggetti speciali contenuti nel Parcelable */
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

    // Getter per ottene il valore degli attributi

    public String getUpc() {
        return upc;
    }

    public String getPageCount() {
        return pageCount;
    }
}
