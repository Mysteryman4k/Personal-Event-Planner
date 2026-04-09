package com.example.personaleventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.Navigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private EventRepository repository;
    private List<Event> events = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        repository = new EventRepository(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set empty adapter first
        eventAdapter = new EventAdapter(events,
                event -> {
                    // Edit event
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isEdit", true);
                    bundle.putInt("eventId", event.getId());
                    bundle.putString("title", event.getTitle());
                    bundle.putString("category", event.getCategory());
                    bundle.putString("location", event.getLocation());
                    bundle.putLong("dateTime", event.getDateTime().getTime());
                    Navigation.findNavController(getView()).navigate(R.id.action_eventList_to_addEvent, bundle);
                },
                event -> {
                    // Delete event
                    repository.delete(event);
                    Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                    loadEvents(); // Reload after delete
                }
        );
        recyclerView.setAdapter(eventAdapter);

        loadEvents();

        FloatingActionButton fab = view.findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isEdit", false);
            Navigation.findNavController(v).navigate(R.id.action_eventList_to_addEvent, bundle);
        });

        return view;
    }

    private void loadEvents() {
        repository.getAllEvents(new EventRepository.OnEventsLoadedListener() {
            @Override
            public void onEventsLoaded(List<Event> loadedEvents) {
                events.clear();
                events.addAll(loadedEvents);
                eventAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
    }
}
