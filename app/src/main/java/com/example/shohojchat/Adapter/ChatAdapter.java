package com.example.shohojchat.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shohojchat.ImageFullView;
import com.example.shohojchat.Model.Chat;
import com.example.shohojchat.R;
import com.example.shohojchat.VideoFullView;
import com.example.shohojchat.ViewHolders.ChatViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    Context context;
    List<Chat>chats;
    final int MY_ID = 1;
    final int OTHER_ID = 2;
MediaPlayer mediaPlayer;
    FirebaseUser firebaseUser;
    String imageUrl;



    public ChatAdapter(Context context, List<Chat> chats,String imageUrl) {
        this.context = context;
        this.chats = chats;
        this.imageUrl = imageUrl;
    }

    public ChatAdapter() {
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MY_ID)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.my_user,parent,false);
            return new ChatViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.other_user,parent,false);
            return  new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        final Chat chat = chats.get(position);
        holder.message.setText(chat.getMessage());
        if(imageUrl.equals("null"))
        {
            holder.profile.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(context).load(imageUrl).into(holder.profile);
        }
        if (chat.getImage().equals("null")&&chat.getVideo().equals("null")&chat.getAudio().equals("null")) {
            //holder.progressBar.setVisibility(View.GONE);
            //holder.myprogressBar.setVisibility(View.VISIBLE);

            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(chat.getMessage());
           // holder.myprogressBar.setVisibility(View.INVISIBLE);

        } else if(chat.getMessage().equals("null")&&chat.getVideo().equals("null")&&chat.getAudio().equals("null")){

           // holder.myprogressBar.setVisibility(View.VISIBLE);
            if(chat.getImage().equals("null")){
                holder.imagemsg.setVisibility(View.VISIBLE);
                holder.imagemsg.setImageResource(R.mipmap.ic_launcher);



            }
            else {
                holder.imagemsg.setVisibility(View.VISIBLE);
                // holder.progressBar.setVisibility(View.GONE);
                Glide.with(context).load(chat.getImage()).into(holder.imagemsg);
            }


          //  holder.myprogressBar.setVisibility(View.INVISIBLE);


        }else if(chat.getMessage().equals("null")&&chat.getImage().equals("null")&&chat.getAudio().equals("null")){
          //  holder.myprogressBar.setVisibility(View.VISIBLE);

            holder.video.setVisibility(View.VISIBLE);
           // holder.progressBar.setVisibility(View.GONE);
            holder.video.setVideoPath(chat.getVideo());
            //holder.myprogressBar.setVisibility(View.INVISIBLE);

            //holder.video.setMediaController(new MediaController(context));
            holder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();



                }
            });

        }
        else if(chat.getMessage().equals("null")&&chat.getImage().equals("null")&&chat.getVideo().equals("null")){
          // holder.myprogressBar.setVisi bility(View.VISIBLE);

            holder.audiomessage.setVisibility(View.VISIBLE);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            String url = chat.getAudio();

            Uri uri= Uri.parse(url);

            try {
                //  mediaPlayer.setDataSource(url);
                mediaPlayer.setDataSource(context, uri);

                mediaPlayer.prepare();


              //  mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        holder.imagemsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,   ImageFullView.class);
                intent.putExtra("img",chat.getImage());
                context.startActivity(intent);

            }
        });
       holder.video.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, VideoFullView.class);
               intent.putExtra("video",chat.getVideo());
               context.startActivity(intent);
           }
       });
       holder.Play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mediaPlayer.start();
           }
       });
       holder.Pause.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mediaPlayer.pause();
           }
       });
       holder.Stop.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mediaPlayer.stop();
           }
       });


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        int returnType = 0;
        if(firebaseUser!=null) {
            String userId = firebaseUser.getUid();

            if (chats.get(position).getSender().equals(userId)){
                returnType = MY_ID;
            }
            else {
                returnType=OTHER_ID;
            }
        }
        return returnType;
    }

}
