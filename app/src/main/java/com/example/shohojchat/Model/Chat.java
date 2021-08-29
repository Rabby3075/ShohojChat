package com.example.shohojchat.Model;

public class Chat {
    String message, receiver, sender,image,video,audio;

    public Chat(String message, String receiver, String sender, String image, String video,String audio) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.image = image;
        this.video = video;
        this.audio = audio;
    }

    public Chat() {
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
