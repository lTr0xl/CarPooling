package com.example.carpooling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Requests_RecyclerViewAdapter extends RecyclerView.Adapter<Requests_RecyclerViewAdapter.MyViewHolder>{
    private final RequestsRecyclerViewInterface requestsRecyclerViewInterface;
    Context context;
    ArrayList<RequestsModel> requestsModel;

    public Requests_RecyclerViewAdapter(RequestsRecyclerViewInterface requestsRecyclerViewInterface, Context context, ArrayList<RequestsModel> requestsModel) {
        this.requestsRecyclerViewInterface = requestsRecyclerViewInterface;
        this.context = context;
        this.requestsModel = requestsModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.request_recycler_view_row, parent, false);
        return new MyViewHolder(view, requestsRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.requestId.setText(String.valueOf(requestsModel.get(position).getRequestId()));
        holder.driverName.setText(requestsModel.get(position).getDriverName());
        String price = "$"+String.valueOf(requestsModel.get(position).getPrice());
        holder.price.setText(price);
        String startLoc = "From: " + requestsModel.get(position).getDriverStart();
        holder.driverFrom.setText(startLoc);
        String destLoc = "To: " + requestsModel.get(position).getDriverDestination();
        holder.driverTo.setText(destLoc);
        holder.timeAndDate.setText(requestsModel.get(position).getTimeAndDate());
        holder.passengerName.setText(String.valueOf(requestsModel.get(position).getPassengerName()));
        startLoc = "From: " + requestsModel.get(position).getPassengerStart();
        holder.passengerFrom.setText(startLoc);
        destLoc = "To: " + requestsModel.get(position).getPassengerDestination();
        holder.passengerTo.setText(destLoc);
        holder.driverRating.setText(String.valueOf(requestsModel.get(position).getDriverRating()));
        holder.passengerRating.setText(String.valueOf(requestsModel.get(position).getPassengerRating()));
        holder.requestStatus.setText(requestsModel.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return requestsModel.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView requestId, requestStatus, driverName, driverFrom, driverTo, timeAndDate, driverRating, price;
        TextView passengerName, passengerFrom, passengerTo, passengerRating;


        public MyViewHolder(@NonNull View itemView, RequestsRecyclerViewInterface requestsRecyclerViewInterface) {
            super(itemView);
            requestStatus = itemView.findViewById(R.id.requestStatus);
            requestId = itemView.findViewById(R.id.requestId);
            driverName = itemView.findViewById(R.id.requestDriverName);
            driverFrom = itemView.findViewById(R.id.requestDriverFrom);
            driverTo = itemView.findViewById(R.id.requestDriverTo);
            timeAndDate = itemView.findViewById(R.id.requestTimeAndDate);
            driverRating = itemView.findViewById(R.id.requestDriverRating);
            price = itemView.findViewById(R.id.requestPrice);
            passengerName = itemView.findViewById(R.id.requestPassengerName);
            passengerFrom = itemView.findViewById(R.id.requestPassengerStart);
            passengerTo = itemView.findViewById(R.id.requestPassengerDestination);
            passengerRating = itemView.findViewById(R.id.requestPassengerRating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(requestsRecyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            requestsRecyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
