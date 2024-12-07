package com.example.carpooling;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RidesFragment extends Fragment implements RequestsRecyclerViewInterface{

    private enum Filter{
        AVAILABLE,
        AWAITING,
        PREVIOUS,
        REQUESTS
    }
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    Button showAvailable, showPrevious, showRequests, showAwaiting;
    LinearLayout activeRideButton;
    RecyclerView recyclerView;
    List<Ride> rides = new ArrayList<>();
    List<Ride> awaitingRides = new ArrayList<>();
    List<RequestRide> requests = new ArrayList<>();
    ArrayList<RidesModel> ridesModel = new ArrayList<>();
    ArrayList<RequestsModel> requestsModel = new ArrayList<>();
    Rides_RecyclerViewAdapter adapter;
    Requests_RecyclerViewAdapter requestsAdapter;
    private Filter filterBy;
    private String type;

    public RidesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_rides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = new DatabaseHelper(getActivity());
        sharedPreferences = requireActivity().getSharedPreferences("session", MODE_PRIVATE);
        recyclerView = requireActivity().findViewById(R.id.ridesRecyclerView);
        showAvailable = requireActivity().findViewById(R.id.showAvailableButton);
        showPrevious = requireActivity().findViewById(R.id.showPreviousButton);
        showRequests = requireActivity().findViewById(R.id.showRequestsButton);
        showAwaiting = requireActivity().findViewById(R.id.showAwaitingButton);
        activeRideButton = requireActivity().findViewById(R.id.activeRideButton);

        type = sharedPreferences.getString("type", null);

        //initial
        if(type.equals("Driver")){
            showAvailable.setVisibility(View.VISIBLE);
            showAvailable.setBackgroundColor(getResources().getColor(R.color.gray));
            rides = databaseHelper.getRidesByDriverId(sharedPreferences.getInt("id", -1));
            requests = databaseHelper.getDriverRequests(sharedPreferences.getInt("id", -1));
            filterBy = Filter.AVAILABLE;
            updateModel();
        } else if (type.equals("Passenger")) {
            showPrevious.setBackgroundColor(getResources().getColor(R.color.gray));
            rides = databaseHelper.getRidesByPassengerId(sharedPreferences.getInt("id", -1));
            requests = databaseHelper.getPassengerRequests(sharedPreferences.getInt("id", -1));
            filterBy = Filter.PREVIOUS;
            updateModel();
        }

        requestsAdapter = new Requests_RecyclerViewAdapter(this, requireActivity(), requestsModel);
        //set model before adapter
        adapter = new Rides_RecyclerViewAdapter(requireActivity(), ridesModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        if(isThereAwaitingRide()){
            showAwaiting.setVisibility(View.VISIBLE);
        }
        if (isThereActiveRide()) {
            activeRideButton.setVisibility(View.VISIBLE);
        }

        activeRideButton.setOnClickListener(v->{
            Intent intent = new Intent(requireActivity(), ActiveRideActivity.class);
            for(Ride ride : rides){
                if(ride.getStatus().equals("active")){
                    intent.putExtra("rideId", ride.getId());
                }
            }

            startActivity(intent);
        });

        showAwaiting.setOnClickListener(v -> {
            if(filterBy == Filter.AWAITING){
                return;
            }
            showAwaiting.setBackgroundColor(getResources().getColor(R.color.gray));
            showAvailable.setBackgroundColor(getResources().getColor(R.color.black));
            showPrevious.setBackgroundColor(getResources().getColor(R.color.black));
            showRequests.setBackgroundColor(getResources().getColor(R.color.black));

            if(recyclerView.getAdapter() != requestsAdapter){
                recyclerView.setAdapter(requestsAdapter);
            }

            filterBy = Filter.AWAITING;
            updateAwaitingModel();
            requestsAdapter.notifyDataSetChanged();

        });

        showAvailable.setOnClickListener(v -> {
            if(filterBy == Filter.AVAILABLE){
                return;
            }
            showAwaiting.setBackgroundColor(getResources().getColor(R.color.black));
            showAvailable.setBackgroundColor(getResources().getColor(R.color.gray));
            showPrevious.setBackgroundColor(getResources().getColor(R.color.black));
            showRequests.setBackgroundColor(getResources().getColor(R.color.black));

            if(recyclerView.getAdapter() != adapter){
                recyclerView.setAdapter(adapter);
            }

            filterBy = Filter.AVAILABLE;
            updateModel();
            adapter.notifyDataSetChanged();

        });

        showPrevious.setOnClickListener(v -> {
            if(filterBy == Filter.PREVIOUS){
                return;
            }
            showAwaiting.setBackgroundColor(getResources().getColor(R.color.black));
            showAvailable.setBackgroundColor(getResources().getColor(R.color.black));
            showPrevious.setBackgroundColor(getResources().getColor(R.color.gray));
            showRequests.setBackgroundColor(getResources().getColor(R.color.black));

            if(recyclerView.getAdapter() != adapter){
                recyclerView.setAdapter(adapter);
            }

            filterBy = Filter.PREVIOUS;
            updateModel();
            adapter.notifyDataSetChanged();

        });

        showRequests.setOnClickListener(v -> {
            if(filterBy == Filter.REQUESTS){
                return;
            }
            showAwaiting.setBackgroundColor(getResources().getColor(R.color.black));
            showAvailable.setBackgroundColor(getResources().getColor(R.color.black));
            showPrevious.setBackgroundColor(getResources().getColor(R.color.black));
            showRequests.setBackgroundColor(getResources().getColor(R.color.gray));

            if(recyclerView.getAdapter() != requestsAdapter){
                recyclerView.setAdapter(requestsAdapter);
            }

            filterBy = Filter.REQUESTS;
            updateRequestModel();
            requestsAdapter.notifyDataSetChanged();
        });




    }


    private boolean isThereActiveRide() {
        if(rides != null) {
            for (Ride ride : rides) {
                if (ride.getStatus().equals("active")) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean isThereAwaitingRide() {
        if(rides != null) {
            awaitingRides.clear();
            for(Ride ride : rides){
                if(ride.getStatus().equals("pending")){
                    awaitingRides.add(ride);
                }
            }
        }

        return !awaitingRides.isEmpty();
    }

    private void updateAwaitingModel() {
        requestsModel.clear();
        if(awaitingRides != null){
            for(int i=0; i<awaitingRides.size(); i++){
                RequestsModel model = new RequestsModel();
                model.setRideId(awaitingRides.get(i).getId());
                model.setStatus(awaitingRides.get(i).getStatus());
                model.setPassengerStart(getLocationName(awaitingRides.get(i).getPassengerStartLat(),awaitingRides.get(i).getPassengerStartLng()));
                model.setPassengerDestination(getLocationName(awaitingRides.get(i).getPassengerDestinationLat(),awaitingRides.get(i).getPassengerDestinationLng()));
                model.setDriverStart(getLocationName(awaitingRides.get(i).getDriverStartLat(),awaitingRides.get(i).getDriverStartLng()));
                model.setDriverDestination(getLocationName(awaitingRides.get(i).getDriverDestinationLat(),awaitingRides.get(i).getDriverDestinationLng()));
                model.setPrice(awaitingRides.get(i).getCost());
                model.setTimeAndDate(awaitingRides.get(i).getRideDateAndTime());

                String driverN = databaseHelper.getDriverName(awaitingRides.get(i).getDriverId());
                model.setDriverName(driverN);
                String passengerN = (databaseHelper.getPassengerName(awaitingRides.get(i).getPassengerId()));
                model.setPassengerName(passengerN);

                float driverRating = databaseHelper.getDriverRating(awaitingRides.get(i).getDriverId());
                float passengerRating = databaseHelper.getPassengerRating(awaitingRides.get(i).getPassengerId());

                model.setPassengerRating(passengerRating);
                model.setDriverRating(driverRating);

                requestsModel.add(model);
            }
        }

    }


    private void updateRequestModel() {
        requestsModel.clear();
        if(requests != null){
            for(int i=0; i<requests.size(); i++){
                if(!requests.get(i).getStatus().equals("pending") && !(type.equals("Passenger") && requests.get(i).getStatus().equals("rejected"))){
                    continue;
                }
                RequestsModel model = new RequestsModel();
                Ride ride = databaseHelper.getRideById(requests.get(i).getRideId());
                model.setRideId(requests.get(i).getRideId());
                model.setRequestId(requests.get(i).getId());
                model.setStatus(requests.get(i).getStatus());
                if(requests.get(i).getPassengerId() == 0){
                    Log.d("ERROR", "ID NULL");
                }else{
                    Log.d("OKEJ", "id e: "+String.valueOf(requests.get(i).getPassengerId()));
                }
                model.setPassengerId(requests.get(i).getPassengerId());
                model.setPassengerStartLat(requests.get(i).getPassengerStartLat());
                model.setPassengerStartLng(requests.get(i).getPassengerStartLng());
                model.setPassengerDestinationLat(requests.get(i).getPassengerDestinationLat());
                model.setPassengerDestinationLng(requests.get(i).getPassengerDestinationLng());


                model.setPassengerStart(getLocationName(requests.get(i).getPassengerStartLat(), requests.get(i).getPassengerStartLng()));
                model.setPassengerDestination(getLocationName(requests.get(i).getPassengerDestinationLat(), requests.get(i).getPassengerDestinationLng()));

                model.setDriverStart(getLocationName(ride.getDriverStartLat(), ride.getDriverStartLng()));
                model.setDriverDestination(getLocationName(ride.getDriverDestinationLat(), ride.getDriverDestinationLng()));
                model.setPrice(ride.getCost());
                model.setTimeAndDate(ride.getRideDateAndTime());

                String driverN = databaseHelper.getDriverName(requests.get(i).getDriverId());
                model.setDriverName(driverN);
                String passengerN = (databaseHelper.getPassengerName(requests.get(i).getPassengerId()));
                model.setPassengerName(passengerN);

                float driverRating = databaseHelper.getDriverRating(requests.get(i).getDriverId());
                float passengerRating = databaseHelper.getPassengerRating(requests.get(i).getPassengerId());

                model.setPassengerRating(passengerRating);
                model.setDriverRating(driverRating);


                requestsModel.add(model);
            }
        }
    }


    public void updateModel(){
        ridesModel.clear();
        if(rides != null){
            for(int i=0; i<rides.size(); i++){
                if(filterBy == Filter.AVAILABLE && !rides.get(i).getStatus().equals("available")){
                    continue;
                }else if(filterBy == Filter.PREVIOUS && !rides.get(i).getStatus().equals("completed")){
                    Log.d("NE E", "HALLO");
                    continue;
                }
                RidesModel model = new RidesModel();
                model.setId(rides.get(i).getId());
                model.setCarModel(rides.get(i).getCarModel());
                model.setCarLicensePlate(rides.get(i).getCarLicensePlate());
                model.setPrice(rides.get(i).getCost());
                model.setRideDateAndTime(rides.get(i).getRideDateAndTime());
                model.setStatus(rides.get(i).getStatus());

                model.setStartLoc(getLocationName(rides.get(i).getDriverStartLat(), rides.get(i).getDriverStartLng()));
                model.setDestinationLoc(getLocationName(rides.get(i).getDriverDestinationLat(), rides.get(i).getDriverDestinationLng()));

                String driverN = databaseHelper.getDriverName(rides.get(i).getDriverId());
                String passengerN = (databaseHelper.getPassengerName(rides.get(i).getPassengerId()));

                model.setPassengerName(passengerN);
                model.setDriverName(driverN);

                model.setDriverRating(rides.get(i).getPassengerRating());
                model.setPassengerRating(rides.get(i).getDriverRating());


                ridesModel.add(model);

            }
        }
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




    @Override
    public void onItemClick(int position) {
        if(type.equals("Driver") && filterBy == Filter.REQUESTS){
            Dialog dialog = new Dialog(requireActivity());
            dialog.setContentView(R.layout.accept_ride_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireActivity(), R.drawable.dialog_background));

            TextView dialogPassengerName = dialog.findViewById(R.id.dialogPassengerName);
            TextView dialogPassengerRating = dialog.findViewById(R.id.dialogPassengerRating);
            TextView dialogStartLocation = dialog.findViewById(R.id.dialogStartLocation);
            TextView dialogDestinationLocation = dialog.findViewById(R.id.dialogDestinationLocation);

            RequestsModel request = requestsModel.get(position);

            dialogPassengerName.setText(request.getPassengerName());;
            dialogPassengerRating.setText(String.valueOf(request.getPassengerRating()));
            dialogStartLocation.setText(request.getPassengerStart());
            dialogDestinationLocation.setText(request.getPassengerDestination());

            ImageView closeIcon = dialog.findViewById(R.id.closeDialog);
            closeIcon.setOnClickListener(v -> dialog.dismiss());

            Button acceptRequest = dialog.findViewById(R.id.acceptRequestButton);
            Button rejectRequest = dialog.findViewById(R.id.rejectRequestButton);

            acceptRequest.setOnClickListener(v -> {
                int rideId = requestsModel.get(position).getRideId();
                int passId = requestsModel.get(position).getPassengerId();
                Log.d("passId", String.valueOf(passId));
                LatLng start = new LatLng( requestsModel.get(position).getPassengerStartLat(),  requestsModel.get(position).getPassengerStartLng());
                LatLng destination = new LatLng( requestsModel.get(position).getPassengerDestinationLat(),  requestsModel.get(position).getPassengerDestinationLng());

                databaseHelper.addPassengerToRide(rideId, passId, start ,destination);
                databaseHelper.updateRideRequest(requestsModel.get(position).getRequestId(), "accepted");
                Toast.makeText(requireActivity(), "Accepted request!", Toast.LENGTH_SHORT).show();

                rides.clear();
                rides = databaseHelper.getRidesByDriverId(sharedPreferences.getInt("id", -1));
                requests.clear();
                requests = databaseHelper.getDriverRequests(sharedPreferences.getInt("id", -1));

                if(isThereAwaitingRide()){
                    showAwaiting.setVisibility(View.VISIBLE);
                }

                updateRequestModel();
                requestsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });

            rejectRequest.setOnClickListener(v -> {
                databaseHelper.updateRideRequest(requestsModel.get(position).getRequestId(), "rejected");

                requests.clear();
                requests = databaseHelper.getDriverRequests(sharedPreferences.getInt("id", -1));

                updateRequestModel();
                requestsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });


            dialog.show();


        }else if(type.equals("Driver") && filterBy == Filter.AWAITING){

            Dialog dialog = new Dialog(requireActivity());
            dialog.setContentView(R.layout.start_ride_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(requireActivity(), R.drawable.dialog_background));

            TextView dialogPassengerName = dialog.findViewById(R.id.dialogPassengerName);
            TextView dialogPassengerRating = dialog.findViewById(R.id.dialogPassengerRating);
            TextView dialogStartLocation = dialog.findViewById(R.id.dialogStartLocation);
            TextView dialogDestinationLocation = dialog.findViewById(R.id.dialogDestinationLocation);

            RequestsModel ride = requestsModel.get(position);

            dialogPassengerName.setText(ride.getPassengerName());;
            dialogPassengerRating.setText(String.valueOf(ride.getPassengerRating()));
            dialogStartLocation.setText(ride.getPassengerStart());
            dialogDestinationLocation.setText(ride.getPassengerDestination());

            ImageView closeIcon = dialog.findViewById(R.id.closeDialog);
            closeIcon.setOnClickListener(v -> dialog.dismiss());

            Button startRideButton = dialog.findViewById(R.id.startRideButton);

            startRideButton.setOnClickListener(v -> {
                if(isThereActiveRide()){
                    Toast.makeText(requireActivity(), "You have already started a ride!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                databaseHelper.updateRideStatus(ride.getRideId(), "active");
                Toast.makeText(requireActivity(), "Ride started!", Toast.LENGTH_SHORT).show();
                activeRideButton.setVisibility(View.VISIBLE);


                rides.clear();
                awaitingRides.clear();
                rides = databaseHelper.getRidesByDriverId(sharedPreferences.getInt("id", -1));
                if(!isThereAwaitingRide()){
                    showAwaiting.setVisibility(View.GONE);
                }

                updateAwaitingModel();
                requestsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });

            dialog.show();
        }
    }

}