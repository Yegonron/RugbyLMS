package com.yegonron.rugbylms.models;

public class Member {

    private String Year;
    private String Month;
    private String Day;

    public Member(String year, String month, String day) {
        Year = year;
        Month = month;
        Day = day;
    }

    public Member() {

    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }
}
