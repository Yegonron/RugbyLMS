package com.yegonron.rugbylms.models;

public class PlayerModel {

    private long pid;
    private int roll;
    private String name;
    private String status;

    public PlayerModel(long pid, int roll, String name) {
        this.pid = pid;
        this.roll = roll;
        this.name = name;
        status = "";

    }

    public PlayerModel() {

    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}