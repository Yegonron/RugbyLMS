package com.yegonron.rugbylms;

public class TeamItem {
    private long tid;

    public TeamItem(long tid, String teamName, String season) {
        this.tid = tid;
        this.teamName = teamName;
        this.season = season;
    }

    private String teamName;

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

    private String season;

    public TeamItem(String teamName, String season) {
        this.teamName = teamName;
        this.season = season;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
