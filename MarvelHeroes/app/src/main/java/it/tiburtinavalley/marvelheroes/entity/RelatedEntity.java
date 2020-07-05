package it.tiburtinavalley.marvelheroes.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "related")
public class RelatedEntity {

    @PrimaryKey
    private int relatedId;

    @ColumnInfo(name = "hero_id")
    private int heroId;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "picture_path")
    private String picturePath;
    @ColumnInfo(name = "type")
    private String type;

    public RelatedEntity(int relatedId, int heroId, String name, String picturePath, String type) {
        this.relatedId = relatedId;
        this.heroId = heroId;
        this.name = name;
        this.picturePath = picturePath;
        this.type = type;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public int getHeroId() {
        return heroId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
