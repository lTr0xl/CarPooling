package com.example.carpooling;

public class Driver {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String userType;
    private float rating;
    private String carModel;
    private String carLicensePlate;

    public Driver(String fullName, String email, String password, String userType, float rating, String carModel, String carLicensePlate) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.rating = rating;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
    }
    public Driver(int id, String fullName, String email, String userType, float rating, String carModel, String carLicensePlate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
        this.rating = rating;
        this.carModel = carModel;
        this.carLicensePlate = carLicensePlate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
}
