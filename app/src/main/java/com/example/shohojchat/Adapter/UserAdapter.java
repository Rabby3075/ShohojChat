package com.example.shohojchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shohojchat.ChatActivity;
import com.example.shohojchat.Model.User;
import com.example.shohojchat.ProfileActivity;
import com.example.shohojchat.R;
import com.example.shohojchat.ViewHolders.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    Context context;
    List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    public UserAdapter() {
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
final User  user = users.get(position);
if(user!=null)
{
    holder.name.setText(user.getName());
    holder.email.setText(user.getEmail());
    if(user.getImgurl().equals("null"))
    {
        holder.profileImage.setImageResource(R.mipmap.ic_launcher);
    }
    else {
        Glide.with(context).load(user.getImgurl()).into(holder.profileImage);
    }

}
holder.chatMe.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(user!=null){
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("userId",user.getUid());
            context.startActivity(intent);


        }
    }
});
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(user!=null){
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra("userId",user.getUid());
            context.startActivity(intent);


        }

    }
});
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
