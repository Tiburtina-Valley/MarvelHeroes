package it.tiburtinavalley.marvelheroes.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "hero")
public class HeroEntity {

    @PrimaryKey
    private int heroId;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "picture_path")
    private String picturePath;
    @ColumnInfo(name = "description")
    private String description;

    public HeroEntity(int heroId, String name, String picturePath, String description) {
        this.heroId = heroId;
        this.name = name;
        this.picturePath = picturePath;
        this.description = description;
    }

    public int getHeroId() {
        return heroId;
    }

    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
