package com.yegonron.rugbylms;

public class AttendanceItem {
    private final long pid;
    private int roll;
    private String name;
    private String status;

    public AttendanceItem(long pid,int roll, String name) {
        this.pid = pid;
        this.roll = roll;
        this.name = name;
        status = "";
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

    public long getPid() {
        return pid;
    }
}
