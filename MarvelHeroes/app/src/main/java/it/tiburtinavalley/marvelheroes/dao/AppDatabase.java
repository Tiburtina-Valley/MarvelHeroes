package it.tiburtinavalley.marvelheroes.dao;

import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import it.tiburtinavalley.marvelheroes.entity.HeroEntity;

@Database(entities = {HeroEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "hero_db";

    private static AppDatabase instance;

    // Per ottenere l'istanza del singleton
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            // Inizializzazione dell'istanza quando il database ancora non è istanziato
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }

    public abstract HeroDao heroDao();
}
