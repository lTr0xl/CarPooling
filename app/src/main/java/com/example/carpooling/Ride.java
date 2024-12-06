package com.example.carpooling;

public class Ride {

    private int id;
    private int passengerId;
    private int driverId;
    private String carModel;
    private String carLicensePlate;
    private float driverRating;
    private float passengerRating;
    private double driverStartLat;
    private double driverStartLng;

    private double driverDestinationLat;
    private double driverDestinationLng;
    private double passengerStartLat;
    private double passengerStartLng;

    private double passengerDestinationLat;
    private double passengerDestinationLng;
    private String rideDateAndTime;
    private String status;  // "available", "pending", "active", "completed"
    private float cost;


    public Ride() {

    }


    public Ride(int passengerId, int driverId, String carModel, String carLicensePlate, float driverRating, float passengerRating, double driverStartLat, double driverStartLng, double driverDestinationLat, double driverDestinationLng, String rideDateAndTime, String status, float cost) {
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
        this.driverRating = driverRating;
        this.passengerRating = passengerRating;
        this.driverStartLat = driverStartLat;
        this.driverStartLng = driverStartLng;
        this.driverDestinationLat = driverDestinationLat;
        this.driverDestinationLng = driverDestinationLng;
        this.rideDateAndTime = rideDateAndTime;
        this.status = status;
        this.cost = cost;
    }

    public Ride(int driverId, String carModel, String carLicensePlate, double driverStartLat, double driverStartLng, double driverDestinationLat, double driverDestinationLng, String rideDateAndTime, String status, float cost) {
        this.driverId = driverId;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
        this.driverStartLat = driverStartLat;
        this.driverStartLng = driverStartLng;
        this.driverDestinationLat = driverDestinationLat;
        this.driverDestinationLng = driverDestinationLng;
        this.rideDateAndTime = rideDateAndTime;
        this.status = status;
        this.cost = cost;
    }

    public Ride(int id, int passengerId, int driverId, String carModel, String carLicensePlate, float driverRating, float passengerRating, double driverStartLat, double driverStartLng, double driverDestinationLat, double driverDestinationLng, double passengerStartLat, double passengerStartLng, double passengerDestinationLat, double passengerDestinationLng, String rideDateAndTime, String status, float cost) {
        this.id = id;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
        this.driverRating = driverRating;
        this.passengerRating = passengerRating;
        this.driverStartLat = driverStartLat;
        this.driverStartLng = driverStartLng;
        this.driverDestinationLat = driverDestinationLat;
        this.driverDestinationLng = driverDestinationLng;
        this.passengerStartLat = passengerStartLat;
        this.passengerStartLng = passengerStartLng;
        this.passengerDestinationLat = passengerDestinationLat;
        this.passengerDestinationLng = passengerDestinationLng;
        this.rideDateAndTime = rideDateAndTime;
        this.status = status;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
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

    public float getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(float driverRating) {
        this.driverRating = driverRating;
    }

    public float getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(float passengerRating) {
        this.passengerRating = passengerRating;
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

    public double getDriverDestinationLat() {
        return driverDestinationLat;
    }

    public void setDriverDestinationLat(double driverDestinationLat) {
        this.driverDestinationLat = driverDestinationLat;
    }

    public double getDriverDestinationLng() {
        return driverDestinationLng;
    }

    public void setDriverDestinationLng(double driverDestinationLng) {
        this.driverDestinationLng = driverDestinationLng;
    }

    public String getRideDateAndTime() {
        return rideDateAndTime;
    }

    public void setRideDateAndTime(String rideDateAndTime) {
        this.rideDateAndTime = rideDateAndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
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
