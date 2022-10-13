package com.yegonron.rugbylms;

public class Post {
    //declare the variable
    private String title, desc, postImage, userName, profileImage, time, date;
    //create a constructor

    public Post(String title, String desc, String postImage, String userName, String profileImage, String time, String date) {
        this.title = title;
        this.desc = desc;
        this.postImage = postImage;
        this.userName = userName;
        this.profileImage = profileImage;
        this.time = time;
        this.date = date;
    }

    //requires an empty constructor
    public Post() {

    }

    // setters
    public void setPostImage(String postImage) {
        this.postImage = postImage;

    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getPostImage() {
        return postImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
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
