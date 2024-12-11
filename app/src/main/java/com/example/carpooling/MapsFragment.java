package com.example.carpooling;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    Button finishRideButton;
    Marker driverCurrentLocMarker = null;
    Marker passengerCurrentLocMarker = null;
    Marker destinationMarker = null;

    Polyline polyline = null;
    private LatLng driverCurrLoc;
    private LatLng passengerCurrLoc;
    private int rideId;
    private Ride ride;
    private String type;
    private LatLng destinationLoc;
    private static final int REQUEST_LOCATION_PERMISSION = 2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.remove(this);
        transaction.commitAllowingStateLoss();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences = requireActivity().getSharedPreferences("session", Context.MODE_PRIVATE);
        type = sharedPreferences.getString("type", null);

        databaseHelper = new DatabaseHelper(requireActivity());
        finishRideButton = requireActivity().findViewById(R.id.finishRideButton);

        if (type.equals("Driver")) {
            finishRideButton.setVisibility(View.VISIBLE);
        } else if (type.equals("Passenger")) {
            finishRideButton.setVisibility(View.GONE);
        }

            rideId = requireActivity().getIntent().getIntExtra("rideId", -1);

            ride = databaseHelper.getRideById(rideId);
            destinationLoc = new LatLng(ride.getPassengerDestinationLat(), ride.getPassengerDestinationLng());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

            finishRideButton.setOnClickListener(v -> {
                onDestinationReached();
            });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        destinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLoc).title("Destination"));

        startLocationTracking();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationTracking();
            } else {
                Log.e("MapsFragment", "Permission denied");
            }
        }
    }
    private void startLocationTracking(){
        if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION );
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for(Location location : locationResult.getLocations()){

                    if(type.equals("Driver")){
                        driverCurrLoc = new LatLng (location.getLatitude(), location.getLongitude());
                        databaseHelper.updateDriverCurrentLocation(rideId, location.getLatitude(), location.getLongitude());
                        passengerCurrLoc = databaseHelper.getPassengerCurrentLocation(rideId);

                    } else if (type.equals("Passenger")) {
                        passengerCurrLoc = new LatLng (location.getLatitude(), location.getLongitude());
                        databaseHelper.updatePassengerCurrentLocation(rideId, location.getLatitude(), location.getLongitude());
                        driverCurrLoc = databaseHelper.getDriverCurrentLocation(rideId);
                    }

                    if(driverCurrentLocMarker != null){
                        driverCurrentLocMarker.remove();
                    }
                    if(passengerCurrentLocMarker != null){
                        passengerCurrentLocMarker.remove();
                    }
                    driverCurrentLocMarker = mMap.addMarker(new MarkerOptions().position(driverCurrLoc).title("Driver"));
                    passengerCurrentLocMarker = mMap.addMarker(new MarkerOptions().position(passengerCurrLoc).title("Passenger"));
                    destinationMarker = mMap.addMarker(new MarkerOptions().position(destinationLoc).title("Destination"));
                    drawLine();

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverCurrLoc, 10)); // Adjust zoom level as needed

                    if(isNearDestination(driverCurrLoc, destinationLoc, 5)){
                        onDestinationReached();
                    }

                }

            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }



    private boolean isNearDestination(LatLng currLoc, LatLng destinationLoc, float thresholdMeters) {
        float[] results = new float[1];
        Location.distanceBetween(currLoc.latitude, currLoc.longitude, destinationLoc.latitude, destinationLoc.longitude, results);

        return results[0] < thresholdMeters;
    }
    private void onDestinationReached() {
        databaseHelper.updateRideStatus(rideId, "completed");
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        requireActivity().runOnUiThread(() -> {
            showDialog();
        });

    }

    private void showDialog() {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.rating_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireActivity(), R.drawable.dialog_background));

        TextView rateText = dialog.findViewById(R.id.rateText);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button finishButton = dialog.findViewById(R.id.finishButton);

        if(type.equals("Driver")){
            rateText.setText("Rate your passenger");
        }else if(type.equals("Passenger")){
            rateText.setText("Rate your driver");
        }

        finishButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Log.d("Rating", String.valueOf(rating));
            if(type.equals("Driver")){
                databaseHelper.setPassengerRatingFromRide(rideId, rating);
            }else if(type.equals("Passenger")){
                databaseHelper.setDriverRatingFromRide(rideId, rating);
            }

            Intent intent = new Intent(requireActivity(), HomeActivity.class);
            startActivity(intent);
            requireActivity().finish();
            dialog.dismiss();
        });

        dialog.show();
    }


    private void drawLine() {
        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions().add(driverCurrLoc).add(passengerCurrLoc).add(destinationLoc).width(5).color(Color.BLUE);
        polyline = mMap.addPolyline(polylineOptions);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null && locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}