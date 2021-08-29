package com.example.shohojchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.shohojchat.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

public class editProfile extends AppCompatActivity {
    ImageView profileImg;
    EditText nameEdt, emailEdt;
    Button updateBtn;

    final static int PICK_INT = 1;

    StorageReference storageReference;

    DatabaseReference databaseReference;

    String ImageUrl ;


    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storageReference = FirebaseStorage.getInstance().getReference("profileImg");
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        if (firebaseUser != null) {

            userId = firebaseUser.getUid();
            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if(user!=null){
                        nameEdt.setText(user.getName());
                        emailEdt.setText(user.getEmail());

                        if(!user.getImgurl().equals("null")){
                            Glide.with(editProfile.this).load(user.getImgurl()).into(profileImg);
                        }
                        else
                        {
                            profileImg.setImageResource(R.mipmap.ic_launcher);
                        }




                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


        progressDialog = new ProgressDialog(this);

        profileImg = findViewById(R.id.imgPic);
        setContentView(R.layout.activity_edit_profile);
        profileImg = findViewById(R.id.imgPic);

        nameEdt = findViewById(R.id.nameEdit);
        emailEdt = findViewById(R.id.emailEdit);
        updateBtn = findViewById(R.id.profileUpdate);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_INT);


            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nameEdt.getText().toString();
                String email = emailEdt.getText().toString();

                HashMap<String, Object> userMap = new HashMap<>();
                userMap.put("name", name);
                userMap.put("email", email);
                userMap.put("imgurl", ImageUrl);

                databaseReference.child(userId).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();


                        }

                    }
                });


            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_INT) {


            if (resultCode == RESULT_OK) {

                if (data != null) {
                    progressDialog.setMessage("Image Uploading....!");
                    progressDialog.show();
                    Uri uri = data.getData();
                    profileImg.setImageURI(uri);

                    final StorageReference imageReferance = storageReference.child("IMG" + System.currentTimeMillis());


                    assert uri != null;
                    imageReferance.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            imageReferance.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {


                                    if (task.isSuccessful()) {

                                        Uri myUri = task.getResult();

                                        ImageUrl = String.valueOf(myUri);
                                        progressDialog.dismiss();


                                    }


                                }
                            });


                        }
                    });


                }


            }

        }


    }
}