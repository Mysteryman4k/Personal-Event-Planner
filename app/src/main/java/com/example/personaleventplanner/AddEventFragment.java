package com.example.personaleventplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEventFragment extends Fragment {
    private EditText titleInput, locationInput;
    private Spinner categorySpinner;
    private TextView dateTimeText;
    private Button saveButton;
    private EventRepository repository;
    private Calendar selectedDateTime;
    private boolean isEdit = false;
    private int editEventId = -1;
    private SimpleDateFormat dateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        repository = new EventRepository(getContext());
        selectedDateTime = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());

        titleInput = view.findViewById(R.id.editTitle);
        locationInput = view.findViewById(R.id.editLocation);
        categorySpinner = view.findViewById(R.id.spinnerCategory);
        dateTimeText = view.findViewById(R.id.textDateTime);
        saveButton = view.findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        if (getArguments() != null && getArguments().getBoolean("isEdit", false)) {
            isEdit = true;
            editEventId = getArguments().getInt("eventId");
            titleInput.setText(getArguments().getString("title"));
            locationInput.setText(getArguments().getString("location"));
            String category = getArguments().getString("category");
            int spinnerPosition = adapter.getPosition(category);
            categorySpinner.setSelection(spinnerPosition);
            selectedDateTime.setTime(new Date(getArguments().getLong("dateTime")));
            updateDateTimeDisplay();
        } else {
            // Set default to tomorrow (avoid past date error)
            selectedDateTime.add(Calendar.DAY_OF_MONTH, 1);
            updateDateTimeDisplay();
        }

        dateTimeText.setOnClickListener(v -> showDateTimePicker());
        saveButton.setOnClickListener(v -> saveEvent());

        return view;
    }

    private void showDateTimePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(year, month, dayOfMonth);
                    TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                            (view1, hourOfDay, minute) -> {
                                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDateTime.set(Calendar.MINUTE, minute);
                                updateDateTimeDisplay();
                            }, selectedDateTime.get(Calendar.HOUR_OF_DAY),
                            selectedDateTime.get(Calendar.MINUTE), false);
                    timePicker.show();
                }, selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void updateDateTimeDisplay() {
        dateTimeText.setText(dateFormat.format(selectedDateTime.getTime()));
    }

    private void saveEvent() {
        String title = titleInput.getText().toString().trim();
        String location = locationInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        // Validation 1: Title not empty
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Date eventDate = selectedDateTime.getTime();
        Date currentDate = new Date();

        // Validation 2: No past dates
        if (eventDate.before(currentDate)) {
            Toast.makeText(getContext(), "Cannot create event in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, category, location, eventDate);

        if (isEdit) {
            event.setId(editEventId);
            repository.update(event);
            Toast.makeText(getContext(), "Event updated", Toast.LENGTH_SHORT).show();
        } else {
            repository.insert(event);
            Toast.makeText(getContext(), "Event saved", Toast.LENGTH_SHORT).show();
        }

        Navigation.findNavController(getView()).navigateUp();
    }
}