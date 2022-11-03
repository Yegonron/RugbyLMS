package com.yegonron.rugbylms.models;

public class AttendanceModel {
    //declare the variable
    String profileImage;
    String surname;
    String firstname;
    String lastname;
    String position;

    //create a constructor
    public AttendanceModel(String profileImage, String surname, String firstname, String lastname, String position, String bootsize) {
        this.profileImage = profileImage;
        this.surname = surname;
        this.firstname = firstname;
        this.lastname = lastname;
        this.position = position;

    }

    //requires an empty constructor
    public AttendanceModel() {

    }

    // setters & getters

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
