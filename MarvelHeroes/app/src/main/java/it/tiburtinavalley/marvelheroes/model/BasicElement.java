package it.tiburtinavalley.marvelheroes.model;
import android.os.Parcelable;

import java.util.List;
//Classe basic element che presenta gli attribute base di tutti gli oggetti con cui andremo a lavorare(catena di generalizzazioni)
//che è inoltre un oggetto parcelable
public abstract class BasicElement implements Parcelable {
    protected String id;
    protected Thumbnail thumbnail;
    protected List<Urls> urls;

    public String getId(){
        return this.id;
    }

    public Thumbnail getThumbnail(){
        return this.thumbnail;
    }

    public List<Urls> getUrls() {
        return urls;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setId(String id) {
        this.id = id;
    }
}
