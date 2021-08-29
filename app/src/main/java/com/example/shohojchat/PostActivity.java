package com.example.shohojchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    ImageView statusImg;
    EditText statusText;
    Button postNow;

    final static int IMAGE_REQ = 1;

    StorageReference storageReference;

    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String myId;

    String myUrl="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        if (firebaseUser != null) {
            myId = firebaseUser.getUid();

        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        storageReference = FirebaseStorage.getInstance().getReference("Story");


        statusImg = findViewById(R.id.statusImage);
        statusText = findViewById(R.id.statusText);
        postNow = findViewById(R.id.postNow);

        statusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("image/*");

                startActivityForResult(intent, IMAGE_REQ);


            }
        });


        postNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status = statusText.getText().toString();
                String uiid = UUID.randomUUID().toString().replace("_", "");

                uiid = uiid + System.currentTimeMillis();


                HashMap<String, Object> postMap = new HashMap<>();
                postMap.put("status", status);
                postMap.put("post_img", myUrl);
                postMap.put("user_id", myId);
                postMap.put("post_id", uiid);

                databaseReference.child(uiid).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(PostActivity.this, "Your Status Posted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            startActivity(intent);

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


        if (requestCode == IMAGE_REQ) {

            if (resultCode == RESULT_OK) {

                if (data != null) {

                    Uri uri = data.getData();

                    statusImg.setImageURI(uri);

                    final StorageReference myRef = storageReference.child(myId).child("Img_Stories" + System.currentTimeMillis());
                    myRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            myRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    if (task.isSuccessful()) {
                                        {

                                            Uri myUri = task.getResult();

                                            myUrl = String.valueOf(myUri);



                                        }
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