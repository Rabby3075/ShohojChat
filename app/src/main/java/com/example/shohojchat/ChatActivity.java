package com.example.shohojchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shohojchat.Adapter.ChatAdapter;
import com.example.shohojchat.Model.Chat;
import com.example.shohojchat.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    EditText sendMsg;
    ImageView sendBtn,fileAttachment;
    RecyclerView recyclerView;
    String  userId,myId;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    TextView profileName;
CircleImageView userProfileImage;
ChatAdapter chatAdapter;
List<Chat>chatlist;
Button image,audio,video;
BottomSheetBehavior bottomSheetBehavior;
StorageReference storageReference;
final  int Image_Req=1;
final  int Video_Req=2;
final  int Audio_Req = 3;
MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        fileAttachment = findViewById(R.id.attachment);
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            myId = firebaseUser.getUid();
        }
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        chatlist = new ArrayList<>();
    userId = intent.getStringExtra("userId");
        sendMsg = findViewById(R.id.testMessage);
        image = findViewById(R.id.imagesend);
        video = findViewById(R.id.videosend);
        audio = findViewById(R.id.audiosend);
        sendBtn = findViewById(R.id.sendIcon);
        recyclerView = findViewById(R.id.chatting);
        profileName = findViewById(R.id.userName_toolbar);
        userProfileImage = findViewById(R.id.profile_image);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.getStackFromEnd();
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        storageReference = FirebaseStorage.getInstance().getReference("user_upload");
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = sendMsg.getText().toString();
                sendMessage(message);
            }
        });

        View view = findViewById(R.id.nestedscrollview);
        bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(5);
        bottomSheetBehavior.setDraggable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
               startActivityForResult(intent,Image_Req);

            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent,Audio_Req);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent,Video_Req);
            }
        });
        fileAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user!=null)
                {

                    profileName.setText(user.getName());
                    if(user.getImgurl().equals("null"))
                    {
                        userProfileImage.setImageResource(R.mipmap.ic_launcher);
                    }
                    else
                    {
                        Glide.with(ChatActivity.this).load(user.getImgurl()).into(userProfileImage);
                    }
                }
                readMessage(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void readMessage(final User user) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat!=null){
                        if((chat.getSender().equals(myId)&&chat.getReceiver().equals(userId))||(chat.getReceiver().equals(myId)&&chat.getSender().equals(userId)))
                        {
                            chatlist.add(chat);
                        }
                    }
                }
                chatAdapter = new ChatAdapter(ChatActivity.this,chatlist,user.getImgurl());
                recyclerView.setAdapter(chatAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        HashMap<String,Object> chat  = new HashMap<>();
        chat.put("message",message);
        chat.put("sender",myId);
        chat.put("receiver",userId);
        chat.put("image","null");
        chat.put("video","null");
        chat.put("audio","null");
        databaseReference.push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    sendMsg.setText("");

                }


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Image_Req)
        {
            if(resultCode==RESULT_OK)
            {
                if(data!=null)
                {
                    Uri uri = data.getData();
                    final  StorageReference myReference = storageReference.child(userId).child("Img_"+System.currentTimeMillis());
                    assert uri != null;
                    myReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            myReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri myuri = task.getResult();
                                    String myimagepath = String.valueOf(myuri);
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                                    HashMap<String,Object> chat = new HashMap<>();
                                    chat.put("message","null");
                                    chat.put("sender",myId);
                                    chat.put("receiver",userId);
                                    chat.put("image",myimagepath);
                                    chat.put("video","null");
                                    chat.put("audio","null");
                                    databaseReference.push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(ChatActivity.this, "OK ", Toast.LENGTH_SHORT).show();


                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });




                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    });
                }
            }
        }
        else  if(requestCode==Video_Req){
            {
                if(resultCode==RESULT_OK)
                {
                    if(data!=null)
                    {
                        Uri uri = data.getData();
                        final  StorageReference myReference = storageReference.child(userId).child("Video_"+System.currentTimeMillis());
                        assert uri != null;
                        myReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                myReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Uri myuri = task.getResult();
                                        String myvideopath = String.valueOf(myuri);
                                        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                                        HashMap<String,Object> chat = new HashMap<>();
                                        chat.put("message","null");
                                        chat.put("sender",myId);
                                        chat.put("receiver",userId);
                                        chat.put("image","null");
                                        chat.put("video",myvideopath);
                                        chat.put("audio","null");
                                        databaseReference.push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(ChatActivity.this, "OK ", Toast.LENGTH_SHORT).show();


                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });




                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });



                            }
                        });
                    }
                }
            }
        }
        else if(requestCode==Audio_Req){
            {
                Uri uri = data.getData();
                final  StorageReference myReference = storageReference.child(userId).child("Img_"+System.currentTimeMillis());
                assert uri != null;
                myReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        myReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri myuri = task.getResult();
                                String myaudiopath = String.valueOf(myuri);
                                databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
                                HashMap<String,Object> chat = new HashMap<>();
                                chat.put("message","null");
                                chat.put("sender",myId);
                                chat.put("receiver",userId);
                                chat.put("image","null");
                                chat.put("video","null");
                                chat.put("audio",myaudiopath);
                                databaseReference.push().setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(ChatActivity.this, "OK ", Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });




                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChatActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                });
            }
        }

    }


}