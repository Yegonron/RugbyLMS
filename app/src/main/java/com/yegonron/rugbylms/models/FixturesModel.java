package com.yegonron.rugbylms.models;

public class FixturesModel {

    //declare the variable
    private String fixtureTitle;
    private String homeTeam;
    private String awayTeam;
    private String fixtureVenue;
    private String fixtureTime;
    private String date;
    private String time;

    //create a constructor
    public FixturesModel(String fixtureTitle, String homeTeam, String awayTeam, String fixtureVenue, String fixtureTime, String time, String date) {
        this.fixtureTitle = fixtureTitle;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.fixtureVenue = fixtureVenue;
        this.fixtureTime = fixtureTime;
        this.date = date;
        this.time = time;

    }

    //requires an empty constructor
    public FixturesModel() {


    }

    // setters
    public void setFixtureTitle(String fixtureTitle) {
        this.fixtureTitle = fixtureTitle;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setFixtureVenue(String fixtureVenue) {
        this.fixtureVenue = fixtureVenue;
    }

    public void setFixtureTime(String fixtureTime) {
        this.fixtureTime = fixtureTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //getters
    public String getFixtureTitle() {
        return fixtureTitle;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getFixtureVenue() {
        return fixtureVenue;
    }

    public String getFixtureTime() {
        return fixtureTime;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
