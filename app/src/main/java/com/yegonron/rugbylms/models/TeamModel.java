package com.yegonron.rugbylms.models;

public class TeamModel {
    private long tid;
    private String teamName;
    private String season;

    public TeamModel(String teamName, String season) {
        this.teamName = teamName;
        this.season = season;
    }

    public TeamModel(long tid, String teamName, String season) {
        this.tid = tid;
        this.teamName = teamName;
        this.season = season;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}