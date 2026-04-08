package com.example.personaleventplanner;

import android.content.Context;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventRepository {
    private EventDao eventDao;
    private ExecutorService executorService;

    public EventRepository(Context context) {
        EventDatabase db = EventDatabase.getInstance(context);
        eventDao = db.eventDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Event event) {
        executorService.execute(() -> eventDao.insert(event));
    }

    public void update(Event event) {
        executorService.execute(() -> eventDao.update(event));
    }

    public void delete(Event event) {
        executorService.execute(() -> eventDao.delete(event));
    }

    public List<Event> getAllEvents() {
        return eventDao.getAllEvents();
    }

    public Event getEventById(int id) {
        return eventDao.getEventById(id);
    }
}
