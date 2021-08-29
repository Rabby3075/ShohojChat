package com.example.shohojchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoFullView extends AppCompatActivity {
Intent intent;
VideoView FullVideo;
ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full_view);

        intent = getIntent();
        String videoLink = intent.getStringExtra("video");
        FullVideo = findViewById(R.id.videoFullView);
        FullVideo.setVideoPath(videoLink);
        progressBar = findViewById(R.id.videoLoading);
        FullVideo.setMediaController(new MediaController(this));

        try {
            FullVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    try{
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    catch (Exception ex){
                        Toast.makeText(VideoFullView.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } catch (Exception e) {
            Toast.makeText(VideoFullView.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
            FullVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();

                }
            });
        } catch (Exception e) {
            Toast.makeText(VideoFullView.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}