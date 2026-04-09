package com.example.personaleventplanner;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

@Database(entities = {Event.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class EventDatabase extends RoomDatabase {
    private static EventDatabase instance;

    public abstract EventDao eventDao();

    public static synchronized EventDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            EventDatabase.class, "event_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()  // <-- ADD THIS LINE
                    .build();
        }
        return instance;
    }
}