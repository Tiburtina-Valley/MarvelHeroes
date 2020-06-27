package it.tiburtinavalley.marvelheroes.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*Definisco una classe evento,che ha come attributi un titolo,una data d'inizio/fine,una descrizione,
  un id,un immagine thumbnails,e una lista di urls(questi ultimi attributi presenti nelle classi
  element e basic element che i nostri eventi estendono)*/


public class Events extends Element {
    private String start;
    private String end;

    //Definisco le operazioni necessarie per rendere l'elemento parcelable
    protected Events(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        start = in.readString();
        end = in.readString();
        thumbnail = in.readParcelable(Thumbnail.class.getClassLoader());
        urls = in.createTypedArrayList(Urls.CREATOR);
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
    }


    //
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


    //Getter necessari per prendere gli attributi degli events(che sono private)

    public String getTitle() {
        return title;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() { return end; }

}
