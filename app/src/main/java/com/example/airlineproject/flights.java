package com.example.airlineproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class flights extends AppCompatActivity {
    private ListView flightListView;
    private DatabaseReference flightsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flights);

        flightListView = findViewById(R.id.flightListView);
        flightsRef = FirebaseDatabase.getInstance().getReference("flights");

        String departure = getIntent().getStringExtra("departure");
        String destination = getIntent().getStringExtra("destination");
        String date = getIntent().getStringExtra("date");

        searchFlights(departure, destination, date);
    }

    private void searchFlights(String departure, String destination, String date) {
        ArrayList<String> flightList = new ArrayList<>();
        ArrayList<String> flightIds = new ArrayList<>();

        flightsRef.orderByChild("departure").equalTo(departure).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot flightSnapshot : dataSnapshot.getChildren()) {
                    String dest = flightSnapshot.child("destination").getValue(String.class);
                    String depDate = flightSnapshot.child("departure_date").getValue(String.class);

                    if (dest.equals(destination) && depDate.equals(date)) {
                        String flightId = flightSnapshot.getKey();
                        flightIds.add(flightId);

                        String flightDetails = "Flight: " + flightSnapshot.child("flight_number").getValue(String.class) +
                                "\nDeparture: " + flightSnapshot.child("departure").getValue(String.class) +
                                "\nDestination: " + flightSnapshot.child("destination").getValue(String.class) +
                                "\nDate: " + flightSnapshot.child("departure_date").getValue(String.class) +
                                "\nSeats Available: " + flightSnapshot.child("seats_available").getValue(Long.class) +
                                "\nPrice: $" + flightSnapshot.child("price").getValue(Double.class);
                        flightList.add(flightDetails);
                    }
                }

                if (flightList.isEmpty()) {
                    Toast.makeText(flights.this, "No flights found", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(flights.this, android.R.layout.simple_list_item_1, flightList);
                    flightListView.setAdapter(adapter);

                    flightListView.setOnItemClickListener((parent, view, position, id) -> {
                        String selectedFlightId = flightIds.get(position);
                        checkAndBookFlight(selectedFlightId);
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Flights", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void checkAndBookFlight(String flightId) {
        flightsRef.child(flightId).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long seatsAvailable = dataSnapshot.child("seats_available").getValue(Long.class);

                if (seatsAvailable > 0) {
                    bookFlight(flightId, seatsAvailable);
                } else {
                    Toast.makeText(flights.this, "No seats available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Flights", "Database error: " + databaseError.getMessage());
            }
        });
    }

    private void bookFlight(String flightId, long seatsAvailable) {
        // Reduce the seat count and create a booking record
        DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("bookings");
        DatabaseReference userBookingsRef = bookingRef.child("user_1").push();  // Assuming user ID is 1 for now

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("flight_id", flightId);
        bookingData.put("booking_date", System.currentTimeMillis());

        // Update the seats available in flights node
        flightsRef.child(flightId).child("seats_available").setValue(seatsAvailable - 1)
                .addOnSuccessListener(aVoid -> {
                    userBookingsRef.setValue(bookingData)
                            .addOnSuccessListener(aVoid1 -> {
                                Intent intent = new Intent(flights.this, Activity_FillForm.class);
                                intent.putExtra("flight_id", flightId);
                                intent.putExtra("seats_available", seatsAvailable);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> Log.e("Flights", "Failed to create booking: " + e.getMessage()));
                })
                .addOnFailureListener(e -> Log.e("Flights", "Failed to update seats: " + e.getMessage()));
    }
}
