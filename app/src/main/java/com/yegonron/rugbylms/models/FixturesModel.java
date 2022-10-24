package com.yegonron.rugbylms.models;

public class FixturesModel {

    //declare the variable
    private String fixtureTitle;
    private String homeTeam;
    private String awayTeam;
    private String fixtureVenue;
    private String fixtureDate;
    private String fixtureTime;
    private String date;
    private String time;

    //create a constructor
    public FixturesModel(String fixtureTitle, String homeTeam, String awayTeam, String fixtureVenue, String fixtureDate, String fixtureTime, String date, String time) {
        this.fixtureTitle = fixtureTitle;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.fixtureVenue = fixtureVenue;
        this.fixtureDate = fixtureDate;
        this.fixtureTime = fixtureTime;
        this.date = date;
        this.time = time;
    }

    //requires an empty constructor

    public FixturesModel() {

    }

    // setters & getters


    public String getFixtureTitle() {
        return fixtureTitle;
    }

    public void setFixtureTitle(String fixtureTitle) {
        this.fixtureTitle = fixtureTitle;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getFixtureVenue() {
        return fixtureVenue;
    }

    public void setFixtureVenue(String fixtureVenue) {
        this.fixtureVenue = fixtureVenue;
    }

    public String getFixtureDate() {
        return fixtureDate;
    }

    public void setFixtureDate(String fixtureDate) {
        this.fixtureDate = fixtureDate;
    }

    public String getFixtureTime() {
        return fixtureTime;
    }

    public void setFixtureTime(String fixtureTime) {
        this.fixtureTime = fixtureTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
