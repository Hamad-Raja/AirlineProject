package com.example.airlineproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton getStartedButton = findViewById(R.id.getstarted);
        TextView loginText = findViewById(R.id.login);

        getStartedButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, register.class);
            startActivity(intent);
        });

        loginText.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
        });
    }
}
