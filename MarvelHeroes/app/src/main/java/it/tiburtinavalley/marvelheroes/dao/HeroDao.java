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

    @Query("SELECT EXISTS(SELECT * FROM hero WHERE heroId = :heroId)")
    Boolean hasHero(int heroId);

    @Query("DELETE FROM hero WHERE heroId = :heroId")
    void deleteHeroFromId(int heroId);

    @Insert
    void insertHero(HeroEntity hero);

    @Update
    void updateHero(HeroEntity hero);

    @Delete
    void deleteHero(HeroEntity hero);
}