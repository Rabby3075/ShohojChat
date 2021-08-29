package com.example.shohojchat.Model;

public class Post {
    String status,post_img,user_id,post_id,date;
    public Post() {
    }

    public Post(String status, String post_img, String user_id, String post_id, String date) {
        this.status = status;
        this.post_img = post_img;
        this.user_id = user_id;
        this.post_id = post_id;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public String getPost_img() {
        return post_img;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
