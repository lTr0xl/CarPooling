package com.example.carpooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.carpooling.R;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    private boolean goToRides = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        frameLayout = findViewById(R.id.contentContainer);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        loadFragment(new HomeFragment()); // default

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if(item.getItemId() == R.id.home){
                fragment = new HomeFragment();
            }else if(item.getItemId() == R.id.rides){
                fragment = new RidesFragment();
            }else if(item.getItemId() == R.id.profile){
                fragment = new ProfileFragment();
            }
            return loadFragment(fragment);
        });


        databaseHelper = new DatabaseHelper(this);

    }


    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.contentContainer);
            if(currentFragment != null && currentFragment.getClass().equals(fragment.getClass())){
                return false;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, fragment).commit();
            return true;
        }
        return false;
    }
}