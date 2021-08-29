package com.example.shohojchat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shohojchat.Model.User;
import com.example.shohojchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

ImageView profile,cover;
TextView name,email;
DatabaseReference databaseReference;
FirebaseUser firebaseUser;
FirebaseAuth firebaseAuth;
String userId;

    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        profile = view.findViewById(R.id.profile_image);
        cover = view.findViewById(R.id.coverPhoto);
        name = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);
databaseReference = FirebaseDatabase.getInstance().getReference("User");
firebaseAuth = FirebaseAuth.getInstance();
firebaseUser = firebaseAuth.getCurrentUser();
if(firebaseUser!=null)
{
    userId = firebaseUser.getUid();
    databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            User user = snapshot.getValue(User.class);
            if(user!=null){
                name.setText(user.getName());
                email.setText(user.getEmail());
                if(!user.getImgurl().equals("null")){
                    Glide.with(getContext()).load(user.getImgurl()).into(profile);
                    Glide.with(getContext()).load(user.getImgurl()).into(cover);

                }


            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}


        return view;
    }
}