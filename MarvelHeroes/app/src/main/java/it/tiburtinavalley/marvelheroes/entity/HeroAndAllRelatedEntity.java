package it.tiburtinavalley.marvelheroes.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class HeroAndAllRelatedEntity {

    @Embedded
    public HeroEntity hero;

    @Relation(parentColumn = "", entityColumn = "", entity = RelatedEntity.class)
    public List<RelatedEntity> relateds;

}
