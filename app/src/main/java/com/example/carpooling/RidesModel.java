package com.example.carpooling;

public class RidesModel {
    int id;
    String driverName;
    String passengerName;
    float price;
    String startLoc;
    String destinationLoc;
    String carModel;
    String carLicensePlate;
    String rideDateAndTime;
    String status;
    float passengerRating;
    float driverRating;

    public RidesModel(){}

    public RidesModel(int id, String status, String driverName, String passengerName, float price, String startLoc, String destinationLoc, String carModel, String carLicensePlate, String rideDateAndTime) {
        this.id = id;
        this.driverName = driverName;
        this.passengerName = passengerName;
        this.price = price;
        this.startLoc = startLoc;
        this.destinationLoc = destinationLoc;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
        this.rideDateAndTime = rideDateAndTime;
        this.status = status;
    }
    public RidesModel(String driverName, String status, String passengerName, float price, String startLoc, String destinationLoc, String carModel, String carLicensePlate, String rideDateAndTime) {
        this.driverName = driverName;
        this.passengerName = passengerName;
        this.price = price;
        this.startLoc = startLoc;
        this.destinationLoc = destinationLoc;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
        this.rideDateAndTime = rideDateAndTime;
        this.status = status;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStartLoc() {
        return startLoc;
    }

    public void setStartLoc(String startLoc) {
        this.startLoc = startLoc;
    }

    public String getDestinationLoc() {
        return destinationLoc;
    }

    public void setDestinationLoc(String destinationLoc) {
        this.destinationLoc = destinationLoc;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarLicensePlate() {
        return carLicensePlate;
    }

    public void setCarLicensePlate(String carLicensePlate) {
        this.carLicensePlate = carLicensePlate;
    }

    public String getRideDateAndTime() {
        return rideDateAndTime;
    }

    public void setRideDateAndTime(String rideDateAndTime) {
        this.rideDateAndTime = rideDateAndTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(float passengerRating) {
        this.passengerRating = passengerRating;
    }

    public float getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(float driverRating) {
        this.driverRating = driverRating;
    }
}



