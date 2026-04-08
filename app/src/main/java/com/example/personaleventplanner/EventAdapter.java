package com.example.personaleventplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;
    private OnEventClickListener clickListener;
    private OnEventLongClickListener longClickListener;
    private SimpleDateFormat dateFormat;

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    public interface OnEventLongClickListener {
        void onEventLongClick(Event event);
    }

    public EventAdapter(List<Event> events, OnEventClickListener clickListener,
                        OnEventLongClickListener longClickListener) {
        this.events = events;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.titleText.setText(event.getTitle());
        holder.categoryText.setText(event.getCategory());
        holder.locationText.setText(event.getLocation());
        holder.dateText.setText(dateFormat.format(event.getDateTime()));

        holder.itemView.setOnClickListener(v -> clickListener.onEventClick(event));
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onEventLongClick(event);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, categoryText, locationText, dateText;

        EventViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.textTitle);
            categoryText = itemView.findViewById(R.id.textCategory);
            locationText = itemView.findViewById(R.id.textLocation);
            dateText = itemView.findViewById(R.id.textDate);
        }
    }
}
