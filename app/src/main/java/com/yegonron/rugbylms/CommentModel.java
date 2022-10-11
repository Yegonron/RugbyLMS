package com.yegonron.rugbylms;

public class CommentModel {
    private String userName, profileImage, comment, time, date;
    //create a constructor

    public CommentModel(String userName, String profileImage, String comment, String time, String date) {
        this.comment = comment;
        this.userName = userName;
        this.profileImage = profileImage;
        this.time = time;
        this.date = date;
    }

    //requires an empty constructor
    public CommentModel() {
    }

    // setters
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //getters
    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

}