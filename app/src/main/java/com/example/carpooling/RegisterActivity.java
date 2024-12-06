package com.example.carpooling;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {
    EditText fullName, password, email, vehicleType, licensePlate;
    TextView loginText;
    Button signupButton;
    SwitchCompat switchCompat;


    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        email = findViewById(R.id.emailText);
        fullName = findViewById(R.id.fullNameText);
        password = findViewById(R.id.passwordText);
        signupButton = findViewById(R.id.signUpButton);
        loginText = findViewById(R.id.textLogin);
        switchCompat = findViewById(R.id.roleSwitch);
        vehicleType = findViewById(R.id.vehicleTypeText);
        licensePlate = findViewById(R.id.licensePlateText);

        switchCompat.setThumbTintList(ContextCompat.getColorStateList(this, R.color.colorAccent));
        switchCompat.setTrackTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked){
                vehicleType.setVisibility(View.GONE);
                licensePlate.setVisibility(View.GONE);
            }else{
                vehicleType.setVisibility(View.VISIBLE);
                licensePlate.setVisibility(View.VISIBLE);
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAdd = email.getText().toString().trim();
                String fullN = fullName.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String type = switchCompat.isChecked() ? "Driver" : "Passenger";

                if(fullN.isEmpty() || emailAdd.isEmpty() || pass.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please fill ", Toast.LENGTH_SHORT).show();
                }else{
                    if(type.equals("Driver")){
                        String model = vehicleType.getText().toString();
                        String plate = licensePlate.getText().toString();

                        Driver newDriver = new Driver(fullN, emailAdd, pass, type, -1, model, plate);

                        if(databaseHelper.addDriver(newDriver)){
                            Toast.makeText(RegisterActivity.this, "Registration succesuful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Passenger newPassenger = new Passenger(fullN, emailAdd, pass, type, -1);

                        if(databaseHelper.addPassenger(newPassenger)){
                            Toast.makeText(RegisterActivity.this, "Registration succesuful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });


    }
}