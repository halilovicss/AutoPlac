package com.example.autoplac;

public class MainModel {

    String description, image, model, phone, year, userID, isAdmin;

    MainModel() {

    }

    public MainModel(String description, String image, String model, String phone, String year, String userID, String isAdmin) {
        this.description = description;
        this.image = image;
        this.model = model;
        this.phone = phone;
        this.year = year;
        this.userID = userID;
        this.isAdmin = isAdmin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
