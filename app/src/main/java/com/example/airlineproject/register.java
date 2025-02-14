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

public class register extends AppCompatActivity {
    private EditText etFullName, etEmail, etPassword;
    private Button btnRegister, btnLogin;

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etemail);
        etPassword = findViewById(R.id.etPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(fullName, email, password);
                }
            }
        });
    }

    private void registerUser(String fullName, String email, String password) {
        // Generate a unique key for each user
        String userId = databaseReference.push().getKey();

        if (userId != null) {
            // Create a User object
            User user = new User(fullName, email, password);

            // Store the user in the database under the "users" node
            databaseReference.child(userId).setValue(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(register.this, MainActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(register.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Register", "Failed to insert user: " + e.getMessage());
                    });
        }
    }
}
