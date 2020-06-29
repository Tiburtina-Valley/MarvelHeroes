package it.tiburtinavalley.marvelheroes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.tiburtinavalley.marvelheroes.entity.HeroEntity;

@Dao
public interface HeroDao {

    @Query("Select * from hero")
    List<HeroEntity> getHeroList();

    @Insert
    void insertHero(HeroEntity hero);

    @Update
    void updateHero(HeroEntity hero);

    @Delete
    void deleteHero(HeroEntity hero);
}