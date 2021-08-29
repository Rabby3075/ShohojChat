package com.example.shohojchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {

    EditText email, password, userName;
    Button registration,gotoregister;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    CheckBox remember;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    //CircleImageView picture;
    //f/inal static int PICK_INT = 1;
   // String imageurl = "null";
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);
        gotoregister = findViewById(R.id.registerhere);

        //picture = findViewById(R.id.imageUpload);
       // addpicture = findViewById(R.id.imgclick);
        userName = findViewById(R.id.userInput);
        registration = findViewById(R.id.register);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        storageReference = FirebaseStorage.getInstance().getReference("profileImg");
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        remember = findViewById(R.id.rememberMe);


        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),loginPage.class));
                finish();
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Pass = password.getText().toString().trim();
                String User = userName.getText().toString().trim();
                if (!Email.equals("") && !Pass.equals("")) {
                    progressDialog.setTitle("Validating Email....");
                    progressDialog.setTitle("Please Wait...");
                    progressDialog.show();
                    registerNow(User, Email, Pass);

                } else {
                    email.setError("Registration Error");
                    password.setError("Registration Error");
                }
            }
        });

    }


    private void registerNow(final String User, final String Email, final String Password) {
        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(registration.this, "Verification email send", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registration.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                String Uid = "";
                if (firebaseUser != null) {
                    Uid = firebaseUser.getUid();
                }

                if (task.isSuccessful()) {


                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("name", User);
                    userMap.put("email", Email);
                    userMap.put("password", Password);
                    userMap.put("uid", Uid);
                    userMap.put("imgurl", "null");
                    databaseReference.child(Uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            startActivity(new Intent(registration.this, loginPage.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });


                  if(remember.isChecked()){
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.putString("email",Email);
                      editor.putString("password",Password);
                      editor.apply();
                      progressDialog.dismiss();
                      startActivity(new Intent(getApplicationContext(),loginPage.class));
                  }
                  else {
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.putString("email","");
                      editor.putString("password","");
                      editor.apply();
                      progressDialog.dismiss();
                     // startActivity(new Intent(getApplicationContext(),loginPage.class));
                  }



                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(registration.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}