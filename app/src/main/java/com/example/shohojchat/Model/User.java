package com.example.shohojchat.Model;

public class User {
    private String name;
    private String email;
    private String password;
    private String uid;
    private String imgurl;


    public User(String name, String email, String password, String uid, String imgurl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.imgurl = imgurl;
    }

    public User() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
