package com.example.personaleventplanner;

import android.content.Context;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    // FIXED: Use callback instead of returning directly on main thread
    public void getAllEvents(OnEventsLoadedListener listener) {
        executorService.execute(() -> {
            List<Event> events = eventDao.getAllEvents();
            android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
            mainHandler.post(() -> listener.onEventsLoaded(events));
        });
    }

    public interface OnEventsLoadedListener {
        void onEventsLoaded(List<Event> events);
    }
}
