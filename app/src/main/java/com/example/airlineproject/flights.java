package com.example.airlineproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class flights extends AppCompatActivity {
    private ListView flightListView;
    private static final String DB_URL = "jdbc:mysql://192.168.1.100:3306/airline_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Eshan9296";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flights);

        flightListView = findViewById(R.id.flightListView);

        String departure = getIntent().getStringExtra("departure");
        String destination = getIntent().getStringExtra("destination");
        String date = getIntent().getStringExtra("date");

        new Thread(() -> searchFlights(departure, destination, date)).start();
    }

    private void searchFlights(String departure, String destination, String date) {
        ArrayList<String> flightList = new ArrayList<>();
        ArrayList<Integer> flightIds = new ArrayList<>();  // To store flight IDs for booking

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (conn != null) {
                String query = "SELECT * FROM flights WHERE departure = ? AND destination = ? AND DATE(departure_date) = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, departure);
                statement.setString(2, destination);
                statement.setString(3, date);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int flightId = resultSet.getInt("id");
                    flightIds.add(flightId);  // Store the flight ID

                    String flightDetails = "Flight: " + resultSet.getString("flight_number") +
                            "\nDeparture: " + resultSet.getString("departure") +
                            "\nDestination: " + resultSet.getString("destination") +
                            "\nDate: " + resultSet.getString("departure_date") +
                            "\nSeats Available: " + resultSet.getInt("seats");
                    flightList.add(flightDetails);
                }

                resultSet.close();
                statement.close();
                conn.close();

                runOnUiThread(() -> {
                    if (flightList.isEmpty()) {
                        Toast.makeText(flights.this, "No flights found", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(flights.this, android.R.layout.simple_list_item_1, flightList);
                        flightListView.setAdapter(adapter);

                        flightListView.setOnItemClickListener((parent, view, position, id) -> {
                            int selectedFlightId = flightIds.get(position);
                            checkAndBookFlight(selectedFlightId);
                        });
                    }
                });
            }
        } catch (ClassNotFoundException e) {
            Log.e("Flights", "JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            Log.e("Flights", "SQL Error: " + e.getMessage());
        }
    }

    private void checkAndBookFlight(int flightId) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                if (conn != null) {
                    String query = "SELECT seats_available FROM flights WHERE id = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setInt(1, flightId);

                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        int seatsAvailable = resultSet.getInt("seats");

                        if (seatsAvailable > 0) {
                            // Book the flight (reduce seat count and insert booking record)
                            bookFlight(flightId);
                        } else {
                            runOnUiThread(() -> Toast.makeText(flights.this, "No seats available", Toast.LENGTH_SHORT).show());
                        }
                    }

                    resultSet.close();
                    statement.close();
                    conn.close();
                }
            } catch (ClassNotFoundException | SQLException e) {
                Log.e("Flights", "Error: " + e.getMessage());
            }
        }).start();
    }

    private void bookFlight(int flightId) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                if (conn != null) {
                    String updateSeatsQuery = "UPDATE flights SET seats_available = seats_available - 1 WHERE id = ?";
                    String insertBookingQuery = "INSERT INTO bookings (user_id, flight_id, booking_date) VALUES (?, ?, NOW())";

                    // Assume user_id is 1 for now (you can change this to the actual logged-in user)
                    int userId = 1;

                    PreparedStatement updateSeatsStmt = conn.prepareStatement(updateSeatsQuery);
                    updateSeatsStmt.setInt(1, flightId);
                    updateSeatsStmt.executeUpdate();

                    PreparedStatement insertBookingStmt = conn.prepareStatement(insertBookingQuery);
                    insertBookingStmt.setInt(1, userId);
                    insertBookingStmt.setInt(2, flightId);
                    insertBookingStmt.executeUpdate();

                    runOnUiThread(() -> {
                        Toast.makeText(flights.this, "Flight booked successfully!", Toast.LENGTH_SHORT).show();
                        // Navigate to the ticket fragment to show booking details
                        Intent intent = new Intent(flights.this, ticket.class);  // Create a new activity for showing ticket details
                        intent.putExtra("flight_id", flightId);
                        startActivity(intent);
                    });

                    updateSeatsStmt.close();
                    insertBookingStmt.close();
                    conn.close();
                }
            } catch (ClassNotFoundException | SQLException e) {
                Log.e("Flights", "Booking Error: " + e.getMessage());
            }
        }).start();
    }
}
