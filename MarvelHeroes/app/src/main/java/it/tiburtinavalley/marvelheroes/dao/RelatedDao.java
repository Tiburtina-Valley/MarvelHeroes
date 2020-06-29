package it.tiburtinavalley.marvelheroes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.tiburtinavalley.marvelheroes.entity.RelatedEntity;

@Dao
public interface RelatedDao {


    @Query("Select * from related")
    List<RelatedEntity> getRelatedList();

    @Insert
    void insertHero(RelatedEntity related);

    @Update
    void updateHero(RelatedEntity related);

    @Delete
    void deleteHero(RelatedEntity related);


}
