package com.example.carpooling;

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


public class HomeFragment extends Fragment {



    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        String type = sharedPreferences.getString("type", null);
        Button rideButton = requireActivity().findViewById(R.id.rideButton);

        if(type.equals("Driver")){
            rideButton.setText("Offer a Ride");
        } else if (type.equals("Passenger")) {
            rideButton.setText("Find a Ride");
        }

        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(type.equals("Driver")){
                    intent = new Intent(getActivity(), OfferRideActivity.class);

                }else if(type.equals("Passenger")){
                    intent = new Intent(getActivity(), FindRideActivity.class);
                }
                startActivity(intent);
            }
        });
    }


}