package com.example.airlineproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }
    public void practice(View view) {
        Intent intent = new Intent(login.this, practice.class);
        startActivity(intent);
    }

    public void setContentView(int activityMain) {
    }
}
