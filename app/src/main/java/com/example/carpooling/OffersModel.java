package com.example.carpooling;

public class OffersModel {
    int rideId;
    String driverName;
    String timeAndDate;
    float price;
    String startLocation;
    String destinationLocation;
    String carModel;
    String licensePlate;
    String status;
    float driverRating;
    double driverStartLat;
    double driverStartLng;
    double driverDestLat;
    double driverDestLng;


    public OffersModel() {

    }
    public OffersModel(int rideId, String driverName, String timeAndDate, float price, String startLocation, String destinationLocation, String carModel, String licensePlate, String status) {
        this.rideId = rideId;
        this.driverName = driverName;
        this.timeAndDate = timeAndDate;
        this.price = price;
        this.startLocation = startLocation;
        this.destinationLocation = destinationLocation;
        this.carModel = carModel;
        this.licensePlate = licensePlate;
        this.status = status;
     //   this.driverRating = driverRating;

    }



    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(float driverRating) {
        this.driverRating = driverRating;
    }

    public double getDriverStartLat() {
        return driverStartLat;
    }

    public void setDriverStartLat(double driverStartLat) {
        this.driverStartLat = driverStartLat;
    }

    public double getDriverStartLng() {
        return driverStartLng;
    }

    public void setDriverStartLng(double driverStartLng) {
        this.driverStartLng = driverStartLng;
    }

    public double getDriverDestLat() {
        return driverDestLat;
    }

    public void setDriverDestLat(double driverDestLat) {
        this.driverDestLat = driverDestLat;
    }

    public double getDriverDestLng() {
        return driverDestLng;
    }

    public void setDriverDestLng(double driverDestLng) {
        this.driverDestLng = driverDestLng;
    }
}
