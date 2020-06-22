package it.tiburtinavalley.marvelheroes.Model;

/* model che mantiene le informazioni relative all'immagine di un fumetto*/

import android.os.Parcel;
import android.os.Parcelable;

public class Images implements Parcelable {
    private String path;
    private String extension;

    protected Images(Parcel in) {
        path = in.readString();
        extension = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(extension);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Images> CREATOR = new Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }
}
