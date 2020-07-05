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

    // Query personalizzate
    @Query("Select * from hero ORDER BY name")
    List<HeroEntity> getHeroList();

    @Query("SELECT EXISTS(SELECT * FROM hero WHERE heroId = :heroId)")
    Boolean hasHero(int heroId);

    @Query("DELETE FROM hero WHERE heroId = :heroId")
    void deleteHeroFromId(int heroId);

    // Query predefinite
    @Insert
    void insertHero(HeroEntity hero);

    @Delete
    void deleteHero(HeroEntity hero);
}