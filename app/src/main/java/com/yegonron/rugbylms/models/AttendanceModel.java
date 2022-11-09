package com.yegonron.rugbylms.models;

public class AttendanceModel {
    //declare the variable
    String profileImage;
    String surname;
    String firstname;
    String lastname;

    //create a constructor
    public AttendanceModel(String profileImage, String surname, String firstname, String lastname) {
        this.profileImage = profileImage;
        this.surname = surname;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    //requires an empty constructor
    public AttendanceModel() {

    }


    // setters
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    //getters

    public String getProfileImage() {
        return profileImage;
    }

    public String getSurname() {
        return surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

}
