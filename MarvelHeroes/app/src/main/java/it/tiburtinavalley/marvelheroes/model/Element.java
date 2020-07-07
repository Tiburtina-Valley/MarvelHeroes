package it.tiburtinavalley.marvelheroes.model;

/** Classe Wlement che estende basic element e cosituisce la base degli oggetti Events,Series,Comics e Stories */
public abstract class Element extends BasicElement {
    protected String description; // Descrizione dell'oggetto
    protected String title; // Titolo dell'oggetto

    // Getter per prendere il valore degli attributi

    public String getDescription() {
        return this.description;
    }

    public String getTitle(){
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

