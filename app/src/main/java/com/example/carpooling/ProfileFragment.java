package com.example.carpooling;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    LinearLayout driverLayout;
    Button logOutButton;
    TextView fullNameText, emailText, typeText, carModelText, carPlateText;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        driverLayout = requireActivity().findViewById(R.id.carDetailsContainer);
        logOutButton = requireActivity().findViewById(R.id.logoutButton);
        fullNameText = requireActivity().findViewById(R.id.fullNameText);
        emailText = requireActivity().findViewById(R.id.emailText);
        typeText = requireActivity().findViewById(R.id.typeText);
        carModelText = requireActivity().findViewById(R.id.carModelText);
        carPlateText = requireActivity().findViewById(R.id.carLicenseText);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("session", MODE_PRIVATE);
        String type = sharedPreferences.getString("type", null);
        fullNameText.setText(sharedPreferences.getString("fullName", null));
        emailText.setText(sharedPreferences.getString("email", null));
        typeText.setText(sharedPreferences.getString("type", null));
        carModelText.setText(sharedPreferences.getString("carModel", null));
        carPlateText.setText(sharedPreferences.getString("carPlate", null));

        if (type != null) {
            if (type.equals("Driver")) {
                driverLayout.setVisibility(View.VISIBLE);
            } else if (type.equals("Passenger")) {
                driverLayout.setVisibility(View.GONE);
            }
        }

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("session", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });


    }
}