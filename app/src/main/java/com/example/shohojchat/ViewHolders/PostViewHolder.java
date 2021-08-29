package com.example.shohojchat.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shohojchat.R;

import de.hdodenhof.circleimageview.CircleImageView;



public class PostViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profilePicture;
    public TextView captionText,user;
    public ImageView post_image;
    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        profilePicture = itemView.findViewById(R.id.profile_image);
        user = itemView.findViewById(R.id.userName);
        captionText =itemView.findViewById(R.id.caption);
        post_image = itemView.findViewById(R.id.postImage);

    }
}
