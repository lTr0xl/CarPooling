package com.example.carpooling;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Rides_RecyclerViewAdapter extends RecyclerView.Adapter<Rides_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<RidesModel> ridesModel;

    public Rides_RecyclerViewAdapter(Context context, ArrayList<RidesModel> ridesModel){
        this.context = context;
        this.ridesModel = ridesModel;
        Log.d("CONSTRUCTOR", String.valueOf(ridesModel.size()));
    }


    @NonNull
    @Override
    public Rides_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new Rides_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Rides_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.rideId.setText(String.valueOf(ridesModel.get(position).getId()));
        holder.driverName.setText(ridesModel.get(position).getDriverName());
        holder.passengerName.setText(ridesModel.get(position).getPassengerName());
        holder.carModel.setText(ridesModel.get(position).getCarModel());
        holder.carPlate.setText(ridesModel.get(position).getCarLicensePlate());
        holder.price.setText(String.valueOf(ridesModel.get(position).getPrice()));

        holder.fromRow.setText(ridesModel.get(position).getStartLoc());
        holder.toRow.setText(ridesModel.get(position).getDestinationLoc());

        holder.ratingFromDriver.setText(String.valueOf(ridesModel.get(position).getDriverRating()));
        holder.ratingFromPassenger.setText(String.valueOf(ridesModel.get(position).getPassengerRating()));
    }

    @Override
    public int getItemCount() {
        return ridesModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView rideId, driverName, passengerName, carModel;
        TextView carPlate, price, fromRow, toRow, ratingFromPassenger, ratingFromDriver;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rideId = itemView.findViewById(R.id.rideIdTextRow);
            driverName = itemView.findViewById(R.id.driverNameRow);
            passengerName = itemView.findViewById(R.id.passengerNameRow);
            carModel = itemView.findViewById(R.id.carModelRow);
            carPlate = itemView.findViewById(R.id.carPlateRow);
            price = itemView.findViewById(R.id.priceRow);
            fromRow = itemView.findViewById(R.id.fromRow);
            toRow = itemView.findViewById(R.id.toRow);
            ratingFromDriver = itemView.findViewById(R.id.driverRatingRow);
            ratingFromPassenger = itemView.findViewById(R.id.passengerRatingRow);

        }
    }
}
