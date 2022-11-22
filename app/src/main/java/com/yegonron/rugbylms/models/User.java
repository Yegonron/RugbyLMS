package com.yegonron.rugbylms.models;

public class User {
    String PID, PROFILE_IMAGE, PLAYER_NAME, ROLL;


    public User(String PID, String PROFILE_IMAGE, String PLAYER_NAME) {
        this.PID = PID;
        this.PROFILE_IMAGE = PROFILE_IMAGE;
        this.PLAYER_NAME = PLAYER_NAME;

    }

    public String getPID() {
        return PID;
    }

    public String getPROFILE_IMAGE() {
        return PROFILE_IMAGE;
    }

    public String getPLAYER_NAME() {
        return PLAYER_NAME;
    }

    public String getROLL() {
        return ROLL;
    }

}
