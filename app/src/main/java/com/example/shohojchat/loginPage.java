package com.example.shohojchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginPage extends AppCompatActivity {
EditText email,pass;
Button login,gotoregister;
ProgressDialog progressDialog;
FirebaseAuth firebaseAuth;
SharedPreferences sharedPreferences;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passwordLogin);
        login = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        gotoregister = findViewById(R.id.registerhere);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        String savedEmail = sharedPreferences.getString("email","");
        String savedPassword = sharedPreferences.getString("password","");
        gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),registration.class));
                finish();
            }
        });
        if(!savedEmail.equals("")&&!savedPassword.equals("")){
            email.setText(savedEmail);
            pass.setText(savedPassword);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = pass.getText().toString().trim();
                if(!Email.equals("")&& !Password.equals(""))
                {
                    progressDialog.setTitle("Validating Email....");
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    Login(Email,Password);
                }
                else {
                    email.setError("Registration Error");
                    pass.setError("Registration Error");
                }
            }
        });


    }

    private void Login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                    startActivity(intent);

                    Toast.makeText(loginPage.this,"Login Succcessfully..!",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(loginPage.this,"Login failed..!",Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}