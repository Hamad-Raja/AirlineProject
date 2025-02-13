package com.example.airlineproject;

import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.airlineproject.R;
import com.example.airlineproject.searchFragment;

public class practice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);

        // Set the default fragment to HomeFragment
        loadFragment(new searchFragment());

            }
   void homeclick(){
       loadFragment(new searchFragment());

   }
   void clickticket(){
       loadFragment(new searchFragment());

   }
   void profile(){
       loadFragment(new searchFragment());

   }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
