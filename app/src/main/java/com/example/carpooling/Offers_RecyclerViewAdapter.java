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

public class Offers_RecyclerViewAdapter extends RecyclerView.Adapter<Offers_RecyclerViewAdapter.MyViewHolder> {

    private final OffersRecyclerViewInterface offersRecyclerViewInterface;
    Context context;
    ArrayList<OffersModel> offersModel;

    public Offers_RecyclerViewAdapter(Context context, ArrayList<OffersModel> offersModel, OffersRecyclerViewInterface offersRecyclerViewInterface){
        this.offersRecyclerViewInterface = offersRecyclerViewInterface;
        this.context = context;
        this.offersModel = offersModel;
    }


    @NonNull
    @Override
    public Offers_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offer_recycler_view_row, parent, false);
        return new MyViewHolder(view, offersRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Offers_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.rideId.setText(String.valueOf(offersModel.get(position).getRideId()));
        holder.driverName.setText(offersModel.get(position).getDriverName());
        String price = "$"+String.valueOf(offersModel.get(position).getPrice());
        holder.price.setText(price);
        holder.carModel.setText(offersModel.get(position).getCarModel());
        holder.licensePlate.setText(offersModel.get(position).getLicensePlate());
        holder.status.setText(offersModel.get(position).getStatus());
        String startLoc = "From: " + offersModel.get(position).getStartLocation();
        holder.startLoc.setText(startLoc);
        String destLoc = "To: " + offersModel.get(position).getDestinationLocation();
        holder.destinationLoc.setText(destLoc);
        holder.timeAndDate.setText(offersModel.get(position).getTimeAndDate());

    }

    @Override
    public int getItemCount() {
        return offersModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rideId, driverName, price, carModel, licensePlate, startLoc, destinationLoc, status, timeAndDate;
        TextView driverRating;
        public MyViewHolder(@NonNull View itemView, OffersRecyclerViewInterface offersRecyclerViewInterface) {
            super(itemView);

            rideId = itemView.findViewById(R.id.offerRideId);
            driverName = itemView.findViewById(R.id.offerDriverName);
            price = itemView.findViewById(R.id.offerPrice);
            carModel = itemView.findViewById(R.id.offerCarModel);
            licensePlate = itemView.findViewById(R.id.offerLicensePlate);
            startLoc = itemView.findViewById(R.id.offerStartLocation);
            destinationLoc = itemView.findViewById(R.id.offerDestinationLocation);
            status = itemView.findViewById(R.id.offerStatus);
            timeAndDate = itemView.findViewById(R.id.offerTimeDate);
            driverRating = itemView.findViewById(R.id.offerDriverRating);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(offersRecyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            offersRecyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });



        }
    }
}
