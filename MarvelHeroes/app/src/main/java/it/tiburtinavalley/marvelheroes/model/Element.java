package it.tiburtinavalley.marvelheroes.model;

import java.util.Collection;

//Classe element che implementa basic element e cosituisce la base degli oggetti Events,Series,Comics e Stories
public abstract class Element extends BasicElement {
    protected String description;
    protected String title;

    public String getDescription() {
        return this.description;
    }

    public String getTitle(){
        return this.title;
    }
}

