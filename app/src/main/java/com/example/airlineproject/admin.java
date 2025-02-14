package com.example.airlineproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class admin extends AppCompatActivity {
    private EditText etFlightNumber, etDeparture, etDestination, etDepartureDate, etSeats, etPrice;
    private Button btnAddFlight;

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        // Initialize input fields
        etFlightNumber = findViewById(R.id.flightNumber);
        etDeparture = findViewById(R.id.departure);
        etDestination = findViewById(R.id.destination);
        etDepartureDate = findViewById(R.id.departuredate);
        etSeats = findViewById(R.id.seats);
        etPrice = findViewById(R.id.price);  // New input for price

        // Initialize Add Flight button
        btnAddFlight = findViewById(R.id.addFlightButton);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("flights");

        btnAddFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flightNumber = etFlightNumber.getText().toString().trim();
                String departure = etDeparture.getText().toString().trim();
                String destination = etDestination.getText().toString().trim();
                String departureDate = etDepartureDate.getText().toString().trim();
                String seats = etSeats.getText().toString().trim();
                String price = etPrice.getText().toString().trim();

                if (flightNumber.isEmpty() || departure.isEmpty() || destination.isEmpty() || departureDate.isEmpty() || seats.isEmpty() || price.isEmpty()) {
                    Toast.makeText(admin.this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                } else {
                    int seatsAvailable = Integer.parseInt(seats);
                    double flightPrice = Double.parseDouble(price);
                    addFlightToFirebase(flightNumber, departure, destination, departureDate, seatsAvailable, flightPrice);
                }
            }
        });
    }

    // Method to insert flight details into Firebase
    private void addFlightToFirebase(String flightNumber, String departure, String destination, String departureDate, int seatsAvailable, double price) {
        // Generate a unique key for the flight
        String flightId = databaseReference.push().getKey();

        if (flightId != null) {
            // Create a Flight object with the new price attribute
            Flight flight = new Flight(flightNumber, departure, destination, departureDate, seatsAvailable, price);

            // Save the flight object in the "flights" node
            databaseReference.child(flightId).setValue(flight)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(admin.this, "Flight added successfully!", Toast.LENGTH_SHORT).show();
                        Log.d("Admin", "Flight details added successfully.");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(admin.this, "Failed to add flight: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Admin", "Error: " + e.getMessage());
                    });
        }
    }
}
