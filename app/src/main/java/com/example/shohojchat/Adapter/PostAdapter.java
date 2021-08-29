package com.example.shohojchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shohojchat.ImageFullView;
import com.example.shohojchat.Model.Post;
import com.example.shohojchat.Model.User;
import com.example.shohojchat.ProfileActivity;
import com.example.shohojchat.R;
import com.example.shohojchat.ViewHolders.PostViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    Context context;
    List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    public PostAdapter() {
    }
    FirebaseUser firebaseUser;

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = postList.get(position);
        if(post!=null && firebaseUser!=null)
        {
            if(post.getStatus().equals("null")){
                holder.captionText.setVisibility(View.GONE);
            }
            else {
                holder.captionText.setText(post.getStatus());
            }
            if (post.getPost_img().equals("null")){
                holder.post_image.setVisibility(View.GONE);
            }
            else {
                Glide.with(context).load(post.getPost_img()).into(holder.post_image);
            }
            getAuthor(holder.profilePicture,holder.user,post.getUser_id());
            holder.post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,   ImageFullView.class);
                    intent.putExtra("img",post.getPost_img());
                    context.startActivity(intent);
                }
            });

        }




    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void getAuthor(final CircleImageView propic ,  final TextView userName , String userId)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final User user = snapshot.getValue(User.class);
                if(user!=null){
                    userName.setText(user.getName());
                    if(user.getImgurl().equals("null"))
                    {
                        propic.setImageResource(R.mipmap.ic_launcher);
                    }
                    else {
                        Glide.with(context).load(user.getImgurl()).into(propic);
                    }
                    propic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ProfileActivity.class);
                            intent.putExtra("userId",user.getUid());
                            context.startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
