package com.example.shohojchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shohojchat.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myProfile extends AppCompatActivity {
    ImageView profile,cover;
    TextView name,email;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    Button update;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        profile = findViewById(R.id.profile_image);
        cover = findViewById(R.id.coverPhoto);
        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
        update = findViewById(R.id.profileEdit);
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            userId = firebaseUser.getUid();
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user!=null)
                    {
                        name.setText(user.getName());
                        email.setText(user.getEmail());
                        if(user.getImgurl().equals("null"))
                        {
                            cover.setImageResource(R.mipmap.ic_launcher);
                            profile.setImageResource(R.mipmap.ic_launcher);

                        }
                        else {

                            Glide.with(myProfile.this).load(user.getImgurl()).into(profile);

                            Glide.with(myProfile.this).load(user.getImgurl()).into(cover);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),editProfile.class));
            }
        });
    }
}