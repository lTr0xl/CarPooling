package com.example.carpooling;

public class RequestsModel {
    int requestId;
    String driverName;
    String driverStart;
    String driverDestination;
    float driverRating;
    String passengerName;
    float passengerRating;
    String passengerStart;
    String passengerDestination;
    String timeAndDate;
    float price;
    String status;
    int passengerId;
    double passengerStartLat;
    double passengerDestinationLat;
    double passengerStartLng;
    double passengerDestinationLng;
    int rideId;
    public RequestsModel(){}
    public RequestsModel(int requestId, String driverName, String status, float driverRating, String driverStart, String driverDestination, String passengerName, float passengerRating, String passengerStart, String passengerDestination, String timeAndDate, float price) {
        this.requestId = requestId;
        this.driverName = driverName;
        this.driverStart = driverStart;
        this.driverRating = driverRating;
        this.driverDestination = driverDestination;
        this.passengerName = passengerName;
        this.passengerRating = passengerRating;
        this.passengerStart = passengerStart;
        this.passengerDestination = passengerDestination;
        this.timeAndDate = timeAndDate;
        this.price = price;
    }

    public RequestsModel(int requestId, int rideId, String driverName, String driverStart, String driverDestination, float driverRating, String passengerName, float passengerRating, String passengerStart, String passengerDestination, String timeAndDate, float price, String status, int passengerId, double passengerStartLat, double passengerDestinationLat, double passengerStartLng, double passengerDestinationLng) {
        this.requestId = requestId;
        this.driverName = driverName;
        this.driverStart = driverStart;
        this.driverDestination = driverDestination;
        this.driverRating = driverRating;
        this.passengerName = passengerName;
        this.passengerRating = passengerRating;
        this.passengerStart = passengerStart;
        this.passengerDestination = passengerDestination;
        this.timeAndDate = timeAndDate;
        this.price = price;
        this.status = status;
        this.passengerId = passengerId;
        this.passengerStartLat = passengerStartLat;
        this.passengerDestinationLat = passengerDestinationLat;
        this.passengerStartLng = passengerStartLng;
        this.passengerDestinationLng = passengerDestinationLng;
        this.rideId = rideId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }


    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public float getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(float passengerRating) {
        this.passengerRating = passengerRating;
    }

    public String getPassengerStart() {
        return passengerStart;
    }

    public void setPassengerStart(String passengerStart) {
        this.passengerStart = passengerStart;
    }

    public String getPassengerDestination() {
        return passengerDestination;
    }

    public void setPassengerDestination(String passengerDestination) {
        this.passengerDestination = passengerDestination;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverStart() {
        return driverStart;
    }

    public void setDriverStart(String driverStart) {
        this.driverStart = driverStart;
    }

    public String getDriverDestination() {
        return driverDestination;
    }

    public void setDriverDestination(String driverDestination) {
        this.driverDestination = driverDestination;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(float driverRating) {
        this.driverRating = driverRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public double getPassengerStartLat() {
        return passengerStartLat;
    }

    public void setPassengerStartLat(double passengerStartLat) {
        this.passengerStartLat = passengerStartLat;
    }

    public double getPassengerDestinationLat() {
        return passengerDestinationLat;
    }

    public void setPassengerDestinationLat(double passengerDestinationLat) {
        this.passengerDestinationLat = passengerDestinationLat;
    }

    public double getPassengerStartLng() {
        return passengerStartLng;
    }

    public void setPassengerStartLng(double passengerStartLng) {
        this.passengerStartLng = passengerStartLng;
    }

    public double getPassengerDestinationLng() {
        return passengerDestinationLng;
    }

    public void setPassengerDestinationLng(double passengerDestinationLng) {
        this.passengerDestinationLng = passengerDestinationLng;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }
}
