package it.tiburtinavalley.marvelheroes.model;
import android.os.Parcelable;

import java.util.List;
//Classe basic element che presenta gli attribute base di tutti gli oggetti con cui andremo a lavorare(catena di generalizzazioni)
//che è inoltre un oggetto Parcelable
public abstract class BasicElement implements Parcelable {
    protected String id; //id di un Model
    protected Thumbnail thumbnail; // Thumbnail del Model
    protected List<Urls> urls; // Lista di url di un Model

    // Getter per ottene il valore degli attributi

    public String getId(){
        return this.id;
    }

    public Thumbnail getThumbnail(){
        return this.thumbnail;
    }

    public List<Urls> getUrls() {
        return urls;
    }

    public void setId(String id) {
        this.id = id;
    }
}
