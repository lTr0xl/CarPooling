package com.example.carpooling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class RideDetailsFragment extends Fragment {



    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    Ride ride;
    TextView driverName, passengerName, fromLocation, toLocation, price;
    Button showMapButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_ride_details, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        driverName = view.findViewById(R.id.fragmentDriverName);
        passengerName = view.findViewById(R.id.fragmentPassengerName);
        fromLocation = view.findViewById(R.id.fragmentFromLocation);
        toLocation = view.findViewById(R.id.fragmentToLocation);
        price = view.findViewById(R.id.fragmentPrice);
        showMapButton = view.findViewById(R.id.fragmentShowMapButton);


        databaseHelper = new DatabaseHelper(requireActivity());
        sharedPreferences = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE);


        int rideId = requireActivity().getIntent().getIntExtra("rideId", -1);

        ride = databaseHelper.getRideById(rideId);

        driverName.setText(databaseHelper.getDriverName(ride.getDriverId()));
        passengerName.setText(databaseHelper.getPassengerName(ride.getPassengerId()));
        fromLocation.setText(getLocationName(ride.getPassengerStartLat(), ride.getPassengerStartLng()));
        toLocation.setText(getLocationName(ride.getPassengerDestinationLat(), ride.getPassengerDestinationLng()));
        price.setText(String.valueOf(ride.getCost()));


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            showMapButton.setVisibility(View.GONE);
        }else{
            showMapButton.setVisibility(View.VISIBLE);
        }

        showMapButton.setOnClickListener(v -> {
            MapsFragment mapsFragment = new MapsFragment();

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailsAndMapFragment, mapsFragment).addToBackStack(null).commit();


        });

    }




    private String getLocationName(double lat, double lng){
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        String addressText = "";
        try{
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses != null && !addresses.isEmpty()){
                Address address = addresses.get(0);
                addressText = address.getAddressLine(0);
//                            String street = address.getThoroughfare();
//                            if(street != null){
//                                addressText += street + " ";
//                            }
//                            String subLocality = address.getSubLocality();
//                            if(subLocality != null){
//                                addressText += subLocality + " ";
//                            }
//                            subLocality = address.getSubThoroughfare();
//                            if(subLocality != null){
//                                addressText += subLocality + " ";
//                            }
//                            String city = address.getLocality();
//                            if(city != null){
//                                addressText += city + " ";
//                            }
//                            city = address.getPostalCode();
//                            if(city != null){
//                                addressText += city + " ";
//                            }
//                            String country = address.getCountryName();
//                            if(country != null){
//                                addressText += country;
//                            }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
            Log.d("CATCH", "ERROR u geocoder");
        }

        return addressText;
    }


}