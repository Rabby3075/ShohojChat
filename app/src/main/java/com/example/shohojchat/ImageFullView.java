package com.example.shohojchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageFullView extends AppCompatActivity {
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_view);

        intent = getIntent();

        String imgLink = intent.getStringExtra("img");


        ImageView imgView = findViewById(R.id.imgView);

        Glide.with(getApplicationContext()).load(imgLink).into(imgView);
    }
}