package com.example.shohojchat.ViewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shohojchat.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public ImageView profile,imagemsg;
    public VideoView video;
    public TextView message;
    public ImageButton Play,Pause,Stop;
    public LinearLayout audiomessage;


    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        profile=itemView.findViewById(R.id.profile_images);
        message=itemView.findViewById(R.id.textMessage);
        imagemsg = itemView.findViewById(R.id.imgMessage);
        video = itemView.findViewById(R.id.videoMessage);
        audiomessage = itemView.findViewById(R.id.audiomsg);
        Play = itemView.findViewById(R.id.play);
        Pause = itemView.findViewById(R.id.pause);
        Stop = itemView.findViewById(R.id.stop);



    }
}
