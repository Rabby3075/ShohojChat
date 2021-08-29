package com.example.shohojchat.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shohojchat.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public ImageView profileImage;
    public TextView name , email;
    public Button chatMe;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImage);
          name = itemView.findViewById(R.id.name);
          email = itemView.findViewById(R.id.userEmail);
          chatMe = itemView.findViewById(R.id.chat);

    }
}
