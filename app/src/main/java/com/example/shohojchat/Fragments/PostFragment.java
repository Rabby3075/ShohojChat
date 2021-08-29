package com.example.shohojchat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shohojchat.Adapter.PostAdapter;
import com.example.shohojchat.Model.Post;
import com.example.shohojchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class PostFragment extends Fragment {

    public PostFragment() {

    }
RecyclerView recyclerView;
PostAdapter adapter;
List<Post>postList;
DatabaseReference databaseReference;
FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);
       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
       postList = new ArrayList<>();
       recyclerView = view.findViewById(R.id.postRecyleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               postList.clear();
               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   Post post = dataSnapshot.getValue(Post.class);
                   postList.add(post);
               }
               adapter = new PostAdapter(getContext(),postList);
               recyclerView.setAdapter(adapter);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
        return view;
    }
}