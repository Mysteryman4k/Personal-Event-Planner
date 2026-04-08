package com.example.personaleventplanner ;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Delete
    void delete(Event event);

    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    List<Event> getAllEvents();

    @Query("SELECT * FROM events WHERE id = :id")
    Event getEventById(int id);
}