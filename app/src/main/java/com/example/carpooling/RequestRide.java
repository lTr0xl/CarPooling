package com.example.carpooling;

public class RequestRide {
    private int id;
    private int rideId;
    private int driverId;
    private int passengerId;
    private String status; // "pending" , "accepted" , "rejected"
    private double passengerStartLat;
    private double passengerStartLng;
    private double passengerDestinationLat;
    private double passengerDestinationLng;

    public RequestRide(){}


    public RequestRide(int id, int rideId, int driverId, int passengerId, String status, double passengerStartLat, double passengerStartLng, double passengerDestinationLat, double passengerDestinationLng) {
        this.id = id;
        this.rideId = rideId;
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.status = status;
        this.passengerStartLat = passengerStartLat;
        this.passengerStartLng = passengerStartLng;
        this.passengerDestinationLat = passengerDestinationLat;
        this.passengerDestinationLng = passengerDestinationLng;
    }

    public RequestRide(int rideId, int driverId, int passengerId, String status, double passengerStartLat, double passengerStartLng, double passengerDestinationLat, double passengerDestinationLng) {
        this.rideId = rideId;
        this.driverId = driverId;
        this.passengerId = passengerId;
        this.status = status;
        this.passengerStartLat = passengerStartLat;
        this.passengerStartLng = passengerStartLng;
        this.passengerDestinationLat = passengerDestinationLat;
        this.passengerDestinationLng = passengerDestinationLng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPassengerStartLat() {
        return passengerStartLat;
    }

    public void setPassengerStartLat(double passengerStartLat) {
        this.passengerStartLat = passengerStartLat;
    }

    public double getPassengerStartLng() {
        return passengerStartLng;
    }

    public void setPassengerStartLng(double passengerStartLng) {
        this.passengerStartLng = passengerStartLng;
    }

    public double getPassengerDestinationLat() {
        return passengerDestinationLat;
    }

    public void setPassengerDestinationLat(double passengerDestinationLat) {
        this.passengerDestinationLat = passengerDestinationLat;
    }

    public double getPassengerDestinationLng() {
        return passengerDestinationLng;
    }

    public void setPassengerDestinationLng(double passengerDestinationLng) {
        this.passengerDestinationLng = passengerDestinationLng;
    }
}
