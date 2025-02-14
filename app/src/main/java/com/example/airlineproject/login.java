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
import java.sql.ResultSet;
import java.sql.SQLException;

public class login extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://192.168.1.100:3306/airline_system"; // Replace with your DB IP and port
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "@Eshan9296";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etEmail = findViewById(R.id.etemail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(() -> checkLogin(email, password)).start();
                }
            }
        });
    }

    // Method to check login credentials from the database
    private void checkLogin(String email, String password) {
        String adminUser  = "admin@gmail.com";
        String adminPassword  = "admin";

        if(email.equals(adminUser) && password.equals(adminPassword))
        {
            Intent intent = new Intent(login.this, admin.class);
            startActivity(intent);
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            if (conn != null) {
                String query = "SELECT * FROM users WHERE email = ? AND password = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Login successful, navigate to the practice activity
                    Log.d("Login", "Login successful.");
                    runOnUiThread(() -> {
                        Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login.this, practice.class);
                        startActivity(intent);
                    });
                } else {
                    // Login failed
                    Log.e("Login", "Invalid email or password.");
                    runOnUiThread(() -> Toast.makeText(login.this, "Invalid email or password.", Toast.LENGTH_SHORT).show());
                }

                resultSet.close();
                statement.close();
                conn.close();
            }
        } catch (ClassNotFoundException e) {
            Log.e("Login", "JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            Log.e("Login", "SQL Error: " + e.getMessage());
        }
    }
}
