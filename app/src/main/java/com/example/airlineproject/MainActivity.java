package com.example.airlineproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Function for login action
    public void login(View view) {
        Intent intent = new Intent(MainActivity.this, login.class);

        startActivity(intent);
    }

    // Function for register action
    public void register(View view) {
        Intent intent = new Intent(MainActivity.this, register.class);

        startActivity(intent);
    }
}
