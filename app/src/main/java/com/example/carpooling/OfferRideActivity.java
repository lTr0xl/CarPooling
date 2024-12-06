package com.example.carpooling;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OfferRideActivity extends AppCompatActivity implements OnMapReadyCallback {


    GoogleMap mMap;
    private int whoCalled = -1;
    private boolean currLoc = false;
    private Marker markerStart = null, markerDestination = null;
    Polyline polyline = null;
    Button currentLocationButton, sendOffer;
    EditText startSearch, destinationSearch;
    TextView timeAndDate, driverName, carModel, carPlate, price;
    private String selectedDate = "";
    private String selectedTime = "";
    private double startLat = -1;
    private double startLng = -1;
    private double destinationLat = -1;
    private double destinationLng = -1;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_offer_ride);

        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);

        databaseHelper = new DatabaseHelper(this);

        timeAndDate = findViewById(R.id.selectDateAndTime);
        currentLocationButton = findViewById(R.id.currentLocationButton);
        startSearch = findViewById(R.id.searchStartLoc);
        sendOffer = findViewById(R.id.sendOffer);
        destinationSearch = findViewById(R.id.searchDestinationLoc);
        driverName = findViewById(R.id.driverNameOfferText);
        carModel = findViewById(R.id.carModelOfferText);
        carPlate = findViewById(R.id.carPlateOfferText);
        price = findViewById(R.id.priceOffer);
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        driverName.setText(sharedPreferences.getString("fullName", null));
        carModel.setText(sharedPreferences.getString("carModel", null));
        carPlate.setText(sharedPreferences.getString("carPlate", null));

        timeAndDate.setOnClickListener(v -> showDateTimePicker());

        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapFragment();
                if(mMap == null){
                    currLoc = true;
                }else{
                    getCurrentLocation();
                }
            }
        });

        Places.initialize(getApplicationContext(), getString(R.string.my_map_apiKey));
        PlacesClient placesClient = Places.createClient(this);

        startSearch.setOnClickListener(v -> openAutocompleteActivity());
        startSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                whoCalled = 0;
                showMapFragment();
                openAutocompleteActivity();
            }
        });
        destinationSearch.setOnClickListener(v -> openAutocompleteActivity());
        destinationSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                whoCalled = 1;
                showMapFragment();
                openAutocompleteActivity();
            }
        });

        sendOffer.setOnClickListener(v -> sendOfferFunction());

    }

    private void sendOfferFunction() {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Ride newRide = new Ride();

        if(carModel.getText() != null && carPlate.getText() != null && price.getText() != null && !timeAndDate.getText().toString().equals("Tap to pick date and time") && startLat != -1 && startLng != -1 && destinationLat != -1 && destinationLng != -1){
            newRide.setDriverId(sharedPreferences.getInt("id", -1));
            newRide.setCarModel(carModel.getText().toString());
            newRide.setCarLicensePlate(carPlate.getText().toString());
            newRide.setCost(Float.parseFloat(price.getText().toString()));
            newRide.setRideDateAndTime(timeAndDate.getText().toString());
            newRide.setDriverStartLat(startLat);
            newRide.setDriverStartLng(startLng);
            newRide.setDriverDestinationLat(destinationLat);
            newRide.setDriverDestinationLng(destinationLng);
            newRide.setStatus("available");

            if(!databaseHelper.addRide(newRide)){
                Toast.makeText(this, "Failed offering a ride. Please try again later", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Ride added successfully!", Toast.LENGTH_LONG).show();
                editor.putBoolean("goToRides", true);
                editor.apply();
                finish();
            }
        }else{
            Toast.makeText(this, "You need to fill everything!", Toast.LENGTH_SHORT).show();
        }

    }

    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    month1 += 1;
                    selectedDate = dayOfMonth + "/" + month1 + "/" + year1;
                    showTimePicker();
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    updateDateTimeText();
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }

    private void updateDateTimeText() {
        String dateTime = "Selected: ";
        if(!selectedDate.isEmpty()){
            dateTime += selectedDate;
        }
        if(!selectedTime.isEmpty()){
            dateTime += " " + selectedTime;
        }
        timeAndDate.setText(dateTime);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void openAutocompleteActivity(){
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, placeFields).build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    String address = place.getAddress();
                    LatLng latLng = place.getLatLng();

                    if (latLng != null) {
                        setMapMarkersLinesAndAddresses(latLng, address);
                    }
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentByTag(SupportMapFragment.class.getSimpleName());

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mapFragmentContainer, mapFragment, SupportMapFragment.class.getSimpleName());
            transaction.commit();
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if(currLoc){
            getCurrentLocation();
            currLoc = false;
        }

        mMap.setOnMapClickListener(latLng -> {
            setMapMarkersLinesAndAddresses(latLng, getLocationName(latLng.latitude, latLng.longitude));
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    private void zoomOnMap(LatLng latLng){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
    }
    private void zoomToFit(LatLng marker1, LatLng marker2){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(marker1);
        builder.include(marker2);
        LatLngBounds bounds = builder.build();
        int padding = 250;

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

        drawLine(marker1, marker2);
    }

    private void getCurrentLocation() {
        Location loc = requestPermissions();

        String addressText = getLocationName(loc.getLatitude(), loc.getLongitude());
        whoCalled = 0;
        setMapMarkersLinesAndAddresses(new LatLng(loc.getLatitude(), loc.getLongitude()), addressText);
    }
    private Location requestPermissions() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (loc == null) {
                loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
            if (loc == null) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                return null;
            }
            return loc;
        }
        return null;
    }

    private String getLocationName(double lat, double lng){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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

    private void setMapMarkersLinesAndAddresses(LatLng latLng, String address){

        if (whoCalled == 0) {
            if (markerStart != null) {
                markerStart.remove();
            }
            startLat = latLng.latitude;
            startLng = latLng.longitude;
            startSearch.setText(address);

            markerStart = mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        } else if (whoCalled == 1) {
            if (markerDestination != null) {
                markerDestination.remove();
            }
            destinationLat = latLng.latitude;
            destinationLng = latLng.longitude;
            destinationSearch.setText(address);
            markerDestination = mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        }
        if (markerStart == null || markerDestination == null) {
            zoomOnMap(latLng);
        } else {
            zoomToFit(markerStart.getPosition(), markerDestination.getPosition());
        }
    }

//    private void getCurrentLocation(){
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
//        }else{
//            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if(loc == null){
//                loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            }
//            if(loc == null){
//                loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//            }
//            if(loc == null){
//                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
//            }else{
//                double myLat = loc.getLatitude();
//                double myLng = loc.getLongitude();
//                startLat = myLat;
//                startLng = myLng;
//
//                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//                try{
//                    List<Address> addresses = geocoder.getFromLocation(myLat, myLng, 1);
//                    if(addresses != null && !addresses.isEmpty()){
//                        Address address = addresses.get(0);
//                        String addressText = address.getAddressLine(0);
//                        startSearch.setText(addressText);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace(); // Log the exception
//                    Toast.makeText(this, "Failed to get address", Toast.LENGTH_SHORT).show();
//                }
//                if(markerStart != null){
//                    markerStart.remove();
//                    markerStart = mMap.addMarker(new MarkerOptions().position(new LatLng(myLat, myLng)).title("Your location"));
//                }else{
//                    markerStart = mMap.addMarker(new MarkerOptions().position(new LatLng(myLat, myLng)).title("Your location"));
//                }
//                if(markerDestination != null){
//                    zoomToFit(markerStart.getPosition(), markerDestination.getPosition());
//                }else{
//                    zoomOnMap(new LatLng(myLat, myLng));
//                }
//            }
//
//        }
//    }

    private void drawLine(LatLng marker1, LatLng marker2){
        if(polyline != null){
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(marker1).add(marker2).width(5).color(Color.RED);
        polyline = mMap.addPolyline(polylineOptions);
    }


}