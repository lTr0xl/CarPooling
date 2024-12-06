package com.example.carpooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    TextView signUpText;
    Button loginButton;

    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        if(sharedPreferences.getInt("id", -1) != -1){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


        databaseHelper = new DatabaseHelper(this);

        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.textSignup);


        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddr = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(emailAddr.isEmpty() || pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }else{
                    Passenger loggedInPassenger = databaseHelper.loginPassenger(emailAddr,pass);
                    if( loggedInPassenger != null){
                        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id", loggedInPassenger.getId());
                        editor.putString("fullName", loggedInPassenger.getFullName());
                        editor.putString("email", loggedInPassenger.getEmail());
                        editor.putString("type", loggedInPassenger.getUserType());
                        editor.putFloat("rating", loggedInPassenger.getRating());
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); //zatvara ja aktivnosta
                    }else{
                        Driver loggedInDriver = databaseHelper.logInDriver(emailAddr,pass);
                        if(loggedInDriver != null){
                            SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Log.d("SHARED", String.valueOf(loggedInDriver.getId()));
                            editor.putInt("id", loggedInDriver.getId());
                            editor.putString("fullName", loggedInDriver.getFullName());
                            editor.putString("email", loggedInDriver.getEmail());
                            editor.putString("type", loggedInDriver.getUserType());
                            editor.putFloat("rating", loggedInDriver.getRating());
                            editor.putString("carModel", loggedInDriver.getCarModel());
                            editor.putString("carPlate", loggedInDriver.getCarLicensePlate());
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }
        });
    }
}