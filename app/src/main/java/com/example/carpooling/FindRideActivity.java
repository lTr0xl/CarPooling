package com.example.carpooling;

import android.Manifest;
import android.app.Dialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FindRideActivity extends AppCompatActivity implements OnMapReadyCallback, OffersRecyclerViewInterface{

    GoogleMap mMap;
    DatabaseHelper databaseHelper;
    FrameLayout mapFragmentContainer;
    EditText searchStart, searchDestination;
    ConstraintLayout parent;
    RecyclerView recyclerView;
    List<Ride> rides = new ArrayList<>();
    ArrayList<OffersModel> offersModels = new ArrayList<>();
    Offers_RecyclerViewAdapter adapter;
    LatLng start = null, destination = null;
    private int whoCalled = -1;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 2;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private Marker markerStart = null;
    private Marker markerDestination = null;
    private Polyline polyline = null;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_ride);

        databaseHelper = new DatabaseHelper(this);
        mapFragmentContainer = findViewById(R.id.mapFragmentContainerPassenger);
        searchStart = findViewById(R.id.searchStartLocPassenger);
        searchDestination = findViewById(R.id.searchDestinationLocPassenger);
        parent = findViewById(R.id.parentFindRide);
        recyclerView = findViewById(R.id.offersRecyclerView);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);

        showMapFragment();
        Places.initialize(getApplicationContext(), getString(R.string.my_map_apiKey));
        PlacesClient placesClient = Places.createClient(this);

        rides = databaseHelper.getRidesByStatus("available");
        if(rides == null){
            Log.d("rides","null");
        }

        getRidesModelData();
        adapter = new Offers_RecyclerViewAdapter(this, offersModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        searchStart.setOnClickListener(v -> openAutoCompleteActivity());
        searchStart.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                whoCalled = 0;
                mapFragmentContainer.setVisibility(View.VISIBLE);
                openAutoCompleteActivity();
            }
        });
        searchDestination.setOnClickListener(v -> openAutoCompleteActivity());
        searchDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                whoCalled = 1;
                mapFragmentContainer.setVisibility(View.VISIBLE);
                openAutoCompleteActivity();
            }
        });


    }


    private void getRidesModelData() {
        offersModels.clear();
        if(rides != null){
            for(int i=0; i<rides.size(); i++){
                if(databaseHelper.alreadyRequested(rides.get(i).getId(), sharedPreferences.getInt("id", -1))){
                    continue;
                }
                OffersModel model = new OffersModel();
                model.setRideId(rides.get(i).getId());
                model.setCarModel(rides.get(i).getCarModel());
                model.setLicensePlate(rides.get(i).getCarLicensePlate());
                model.setPrice(rides.get(i).getCost());
                model.setTimeAndDate(rides.get(i).getRideDateAndTime());
                model.setStatus(rides.get(i).getStatus());
                model.setStartLocation(getLocationName(rides.get(i).getDriverStartLat(), rides.get(i).getDriverStartLng()));
                model.setDestinationLocation(getLocationName(rides.get(i).getDriverDestinationLat(), rides.get(i).getDriverDestinationLng()));


                String driverN = databaseHelper.getDriverName(rides.get(i).getDriverId());

                model.setDriverName(driverN);

                offersModels.add(model);

            }
        }
    }

    private void openAutoCompleteActivity() {
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

    private void zoomOnMap(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
    }
    private void zoomToFit(LatLng marker1, LatLng marker2) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker1);
        builder.include(marker2);
        LatLngBounds bounds = builder.build();

        int padding = 250;
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        drawLine(marker1, marker2);
    }

    private void drawLine(LatLng marker1, LatLng marker2) {
        if(polyline != null){
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions().add(marker1).add(marker2).width(5).color(Color.RED);
        polyline = mMap.addPolyline(polylineOptions);
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

    private void getCurrentLocation() {
        Location loc = requestPermissions();

        String addressText = getLocationName(loc.getLatitude(), loc.getLongitude());
        whoCalled = 0;
        setMapMarkersLinesAndAddresses(new LatLng(loc.getLatitude(), loc.getLongitude()), addressText);
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
            start = latLng;
            searchStart.setText(address);

            markerStart = mMap.addMarker(new MarkerOptions().position(latLng).title(address));

        } else if (whoCalled == 1) {
            if (markerDestination != null) {
                markerDestination.remove();
            }
            destination = latLng;
            searchDestination.setText(address);
            markerDestination = mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        }
        if (markerStart == null || markerDestination == null) {
            zoomOnMap(latLng);
        } else {
            zoomToFit(markerStart.getPosition(), markerDestination.getPosition());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        getCurrentLocation();


        mMap.setOnMapClickListener(latLng -> {
            setMapMarkersLinesAndAddresses(latLng, getLocationName(latLng.latitude, latLng.longitude));
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void showMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentByTag(SupportMapFragment.class.getSimpleName());

        if(mapFragment == null){
            mapFragment =  SupportMapFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mapFragmentContainerPassenger, mapFragment, SupportMapFragment.class.getSimpleName());
            transaction.commit();
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = getCurrentFocus();

            // Get the views that should not trigger hiding the map
            View[] interactiveViews = {searchStart, searchDestination, mapFragmentContainer};

            // Check if the touch event is outside these views
            if (!isTouchInsideView(event, interactiveViews)) {
                // Hide the map
                mapFragmentContainer.setVisibility(View.GONE);
                // Hide the keyboard if needed
                hideKeyboard();
            }
            if(currentFocus instanceof EditText){
                currentFocus.clearFocus();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Checks if the touch event occurred inside any of the provided views.
     */
    private boolean isTouchInsideView(MotionEvent event, View[] views) {
        for (View view : views) {
            if (view != null && isTouchInsideView(event, view)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the touch event occurred inside the given view.
     */
    private boolean isTouchInsideView(MotionEvent event, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        float x = event.getRawX();
        float y = event.getRawY();

        return x >= location[0] && x <= (location[0] + view.getWidth())
                && y >= location[1] && y <= (location[1] + view.getHeight());
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onItemClick(int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.request_ride_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(this, R.drawable.dialog_background));

        TextView dialogDriverName = dialog.findViewById(R.id.dialogDriverName);
        TextView dialogDriverRating = dialog.findViewById(R.id.dialogDriverRating);
        TextView dialogPrice = dialog.findViewById(R.id.dialogRidePrice);
        TextView dialogStartLocation = dialog.findViewById(R.id.dialogStartLocation);
        TextView dialogDestinationLocation = dialog.findViewById(R.id.dialogDestinationLocation);
        TextView dialogDateTime = dialog.findViewById(R.id.dialogRideDateTime);

        OffersModel offer = offersModels.get(position);

        dialogDriverName.setText(offer.getDriverName());;
        dialogPrice.setText(String.format("$%s", offer.getPrice()));
        dialogDriverRating.setText(String.valueOf(offer.getDriverRating()));
        dialogStartLocation.setText(offer.getStartLocation());
        dialogDestinationLocation.setText(offer.getDestinationLocation());
        dialogDateTime.setText(offer.getTimeAndDate());

        ImageView closeIcon = dialog.findViewById(R.id.closeDialog);
        closeIcon.setOnClickListener(v -> dialog.dismiss());

        Button requestRide = dialog.findViewById(R.id.requestRideButton);
        requestRide.setOnClickListener(v -> {

            if(start == null || destination == null){
                Toast.makeText(this, "Insert start and destination!", Toast.LENGTH_SHORT).show();
                return;
            };

            SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);

            int rideId = rides.get(position).getId();
            int driverId = rides.get(position).getDriverId();
            int passengerId = sharedPreferences.getInt("id", -1);
            String status = "pending";

            RequestRide newRequest = new RequestRide(rideId, driverId, passengerId, status, start.latitude, start.longitude, destination.latitude, destination.longitude);
            boolean result = databaseHelper.addRideRequest(newRequest);
            if(result){
                Toast.makeText(this, "Ride Requested!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "You have already requested this offer!", Toast.LENGTH_SHORT).show();
            }

            getRidesModelData();
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });

        dialog.show();
    }
}