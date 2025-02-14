
package com.example.airlineproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class Activity_FillForm extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, passportEditText;
    private Button confirmBookingButton;
    private DatabaseReference bookingRef, flightsRef;
    private String flightId;
    private long seatsAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.Activity_FillForm);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passportEditText = findViewById(R.id.passportEditText);
        confirmBookingButton = findViewById(R.id.confirmBookingButton);

        flightId = getIntent().getStringExtra("flight_id");
        seatsAvailable = getIntent().getLongExtra("seats_available", 0);

        bookingRef = FirebaseDatabase.getInstance().getReference("bookings");
        flightsRef = FirebaseDatabase.getInstance().getReference("flights");

        confirmBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String passport = passportEditText.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || passport.isEmpty()) {
                    Toast.makeText(Activity_FillForm.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    confirmBooking(name, phone, passport);
                }
            }
        });
    }

    private void confirmBooking(String name, String phone, String passport) {
        DatabaseReference userBookingRef = bookingRef.child("user_1").push();  // Assuming user ID is 1 for now

        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("flight_id", flightId);
        bookingData.put("booking_date", System.currentTimeMillis());
        bookingData.put("name", name);
        bookingData.put("phone", phone);
        bookingData.put("passport", passport);

        // Update the seats available in flights node
        flightsRef.child(flightId).child("seats_available").setValue(seatsAvailable - 1)
                .addOnSuccessListener(aVoid -> {
                    userBookingRef.setValue(bookingData)
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(Activity_FillForm.this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Activity_FillForm.this, ticket.class);
                                intent.putExtra("flight_id", flightId);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> Log.e("FillFormActivity", "Failed to save booking data: " + e.getMessage()));
                })
                .addOnFailureListener(e -> Log.e("FillFormActivity", "Failed to update seats: " + e.getMessage()));
    }
}
