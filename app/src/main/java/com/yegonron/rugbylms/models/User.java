package com.yegonron.rugbylms.models;

public class User {
    String PID, PLAYER_NAME, ROLL;


    public User(String PID, String PLAYER_NAME) {
        this.PID = PID;
        this.PLAYER_NAME = PLAYER_NAME;
    }

    public String getPID() {
        return PID;
    }

    public String getPLAYER_NAME() {
        return PLAYER_NAME;
    }

    public String getROLL() {
        return ROLL;
    }

}
