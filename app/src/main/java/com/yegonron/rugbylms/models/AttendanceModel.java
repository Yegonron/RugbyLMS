package com.yegonron.rugbylms.models;

public class AttendanceModel {

    //declare the variable
    String profileImage;
    String surname;
    String firstname;
    String lastname;
    String teamname;
    String position;

    public AttendanceModel(String profileImage, String surname, String firstname, String lastname, String teamname, String position) {
        this.profileImage = profileImage;
        this.surname = surname;
        this.firstname = firstname;
        this.lastname = lastname;
        this.teamname = teamname;
        this.position = position;
    }

    public AttendanceModel() {

    }

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

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

