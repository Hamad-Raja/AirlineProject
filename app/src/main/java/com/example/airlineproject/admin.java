package com.example.airlineproject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class admin extends AppCompatActivity {
    private EditText etFlightNumber, etDeparture, etDestination, etDepartureDate, etSeats;
    private Button btnAddFlight;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://192.168.1.100:3306/airline_system"; // Replace with your DB IP and port
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);  // Make sure you have an admin layout with the required input fields.

        // Initialize input fields
        etFlightNumber = findViewById(R.id.flightNumber);
        etDeparture = findViewById(R.id.departure);
        etDestination = findViewById(R.id.destination);
        etDepartureDate = findViewById(R.id.departuredate);  // Ensure this is in "yyyy-MM-dd HH:mm:ss" format
        etSeats = findViewById(R.id.seats);

        // Initialize Add Flight button
        btnAddFlight = findViewById(R.id.addFlightButton);
        btnAddFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String flightNumber = etFlightNumber.getText().toString().trim();
                String departure = etDeparture.getText().toString().trim();
                String destination = etDestination.getText().toString().trim();
                String departureDate = etDepartureDate.getText().toString().trim();
                int seatsAvailable = Integer.parseInt(etSeats.getText().toString().trim());

                if (flightNumber.isEmpty() || departure.isEmpty() || destination.isEmpty() || departureDate.isEmpty() || seatsAvailable <= 0) {
                    Toast.makeText(admin.this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> insertFlightDetails(flightNumber, departure, destination, departureDate, seatsAvailable)).start();
                }
            }
        });
    }

    // Method to insert flight details into the database
    private void insertFlightDetails(String flightNumber, String departure, String destination, String departureDate, int seatsAvailable) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (conn != null) {
                String query = "INSERT INTO flights (flight_number, departure, destination, departure_date, seats_available) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, flightNumber);
                statement.setString(2, departure);
                statement.setString(3, destination);
                statement.setString(4, departureDate);  // Ensure the date format is correct
                statement.setInt(5, seatsAvailable);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    Log.d("Admin", "Flight details added successfully.");
                    runOnUiThread(() -> Toast.makeText(admin.this, "Flight added successfully!", Toast.LENGTH_SHORT).show());
                }

                statement.close();
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            Log.e("Admin", "JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            Log.e("Admin", "SQL Error: " + e.getMessage());
        }
    }
}
