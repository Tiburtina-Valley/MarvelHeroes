package it.tiburtinavalley.marvelheroes.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = HeroEntity.class,
        parentColumns = "iArticleID",
        childColumns = "article_id",
        onDelete = CASCADE))
public class ReleatedEntity {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "hero_id")
    private int heroId;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "picture_path")
    private String picturePath;
    @ColumnInfo(name = "type")
    private String type;

}
