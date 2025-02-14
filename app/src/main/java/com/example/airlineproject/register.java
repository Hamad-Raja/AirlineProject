package com.example.airlineproject;

import android.content.Intent;
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

public class register extends AppCompatActivity {
    private EditText etFullName, etEmail, etPassword;
    private Button btnRegister;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://192.168.1.100:3306/airline_system"; // Replace with your database IP and port
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Eshan9296";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etemail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> insertData(fullName, email, password)).start();
                }
            }
        });
    }

    // Method to insert data into MySQL database
    private void insertData(String fullName, String email, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL JDBC Driver
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (conn != null) {
                String query = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, fullName);
                statement.setString(2, email);
                statement.setString(3, password);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    Log.d("Register", "User registered successfully.");
                    runOnUiThread(() -> Toast.makeText(register.this, "Registration successful!", Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(register.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("Register", "Failed to insert user.");
                    runOnUiThread(() -> Toast.makeText(register.this, "Registration failed.", Toast.LENGTH_SHORT).show());
                }

                statement.close();
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            Log.e("Register", "JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            Log.e("Register", "SQL Error: " + e.getMessage());
        }
    }
}
