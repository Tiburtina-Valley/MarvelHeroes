package it.tiburtinavalley.marvelheroes.dao;

import androidx.room.Dao;
import androidx.room.Query;

import it.tiburtinavalley.marvelheroes.entity.HeroAndAllRelatedEntity;

@Dao
public interface HeroRelatedDao {

    @Query("SELECT * FROM hero")
    HeroAndAllRelatedEntity loadHeroAllRelateds();

}
