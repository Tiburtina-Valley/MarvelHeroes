package it.tiburtinavalley.marvelheroes.model;

import android.os.Parcel;
import android.os.Parcelable;

/** Classe che mantiene le informazioni legate ad una thumbnail, presente in quasi tutte le model
    che vengono gestite */
public class Thumbnail implements Parcelable {
    private String path; //path della thumbnail
    private String extension; //extension della thumbnail

    /** Costruttore chiamato quando viene estratto il Parcelable in un'Activity */
    protected Thumbnail(Parcel in) {
        path = in.readString();
        extension = in.readString();
    }

    public static final Creator<Thumbnail> CREATOR = new Creator<Thumbnail>() {
        @Override
        public Thumbnail createFromParcel(Parcel in) {
            return new Thumbnail(in);
        }

        @Override
        public Thumbnail[] newArray(int size) {
            return new Thumbnail[size];
        }
    };

    // Getter per prendere il valore degli attributi

    public String getPath(){
        return this.path;
    }

    public String getExtension(){
        return this.extension;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /** Metodo per inserire il Parcel cone Extra nell'Intent */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(extension);
    }
}
