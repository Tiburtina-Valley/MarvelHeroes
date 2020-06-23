package it.tiburtinavalley.marvelheroes.model;
import android.os.Parcelable;

import java.util.List;

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
}
