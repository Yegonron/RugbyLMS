package com.yegonron.rugbylms.models;

public class UserModel {
    //declare the variable
    String profileImage;
    String accountType;
    String surname;
    String firstname;
    String lastname;
    String phone;
    String age;
    String teamname;
    String position;
    String kitsize;
    String bootsize;

    //create a constructor
    public UserModel(String profileImage, String accountType, String surname, String firstname, String lastname, String phone, String age, String teamname, String position, String kitsize, String bootsize) {
        this.profileImage = profileImage;
        this.accountType = accountType;
        this.surname = surname;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.age = age;
        this.teamname = teamname;
        this.position = position;
        this.kitsize = kitsize;
        this.bootsize = bootsize;

    }
    //requires an empty constructor
    public UserModel() {

    }

    // setters & getters
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getKitsize() {
        return kitsize;
    }

    public void setKitsize(String kitsize) {
        this.kitsize = kitsize;
    }

    public String getBootsize() {
        return bootsize;
    }

    public void setBootsize(String bootsize) {
        this.bootsize = bootsize;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
