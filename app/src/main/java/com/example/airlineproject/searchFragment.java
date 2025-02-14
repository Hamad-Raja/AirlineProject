package com.example.airlineproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class searchFragment extends Fragment {

    private EditText etDeparture, etDestination, etDepartureDate , etseats;
    private Button btnSearchFlight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize views
        etDeparture = view.findViewById(R.id.departure);
        etDestination = view.findViewById(R.id.destination);
        etDepartureDate = view.findViewById(R.id.departuredate);
        etseats = view.findViewById(R.id.seats);
        btnSearchFlight = view.findViewById(R.id.searchFlightButton);

        // Set click listener for search button
        btnSearchFlight.setOnClickListener(v -> {
            String departure = etDeparture.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();
            String departureDate = etDepartureDate.getText().toString().trim();
            String seats =etseats.getText().toString().trim();

            if (departure.isEmpty() || destination.isEmpty() || departureDate.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields for search", Toast.LENGTH_SHORT).show();
            } else {
                // Start the flights activity
                Intent intent = new Intent(getActivity(), flights.class);
                intent.putExtra("departure", departure);
                intent.putExtra("destination", destination);
                intent.putExtra("date", departureDate);
                intent.putExtra("seats", seats);
                startActivity(intent);
            }
        });

        return view;
    }
}
