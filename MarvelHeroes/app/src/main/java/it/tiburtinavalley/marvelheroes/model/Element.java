package it.tiburtinavalley.marvelheroes.model;

import java.util.Collection;

//Classe element che implementa basic element e cosituisce la base degli oggetti events,series,comics e HeroModel
public abstract class Element extends BasicElement {
    protected String description;

    public String getDescription() {
        return this.description;
    }

    ;
}

