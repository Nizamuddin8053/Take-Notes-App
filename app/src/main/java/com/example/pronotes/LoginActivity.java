package com.example.pronotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email_edit_text, password_edit_text;
    Button login_btn;
    ProgressBar progress_bar;
    TextView create_text_view_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        login_btn = findViewById(R.id.login_btn);
        progress_bar = findViewById(R.id.progress_bar);
        create_text_view_btn = findViewById(R.id.create_text_view_btn);

        login_btn.setOnClickListener(View-> loginUser());
        create_text_view_btn.setOnClickListener(View-> startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)) );
    }
    //METHOD FOR LOGIN BUTTON
    void loginUser(){

        String email = email_edit_text.getText().toString();
        String password = password_edit_text.getText().toString();

        boolean isValidate = validateData(email,password);
        if (!isValidate){
            return;
        }
        loginAccountInFirebase(email, password);

    }

    void loginAccountInFirebase(String email, String password){
       FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       changeInProgress(true);
       firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               changeInProgress(false);
               if (task.isSuccessful()){
                   // successfully login
                   if (firebaseAuth.getCurrentUser().isEmailVerified()){
                       // GO TO MAIN ACTIVITY
                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                       finish();
                   }
                   else{
                        Utility.showToast(LoginActivity.this, "Email is not Verified, please " +
                                "verify your email");
                   }
               }
               else{
                   // login is failed
                   Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
               }

           }
       });

    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            //PROGRESS BAR WILL BE VISIBLE
            progress_bar.setVisibility(View.VISIBLE);
            // CREATE ACCOUNT BTN WILL BE GONE
            login_btn.setVisibility(View.GONE);
        }
        else{
            // PROGRESS BAR WILL BE GONE
            progress_bar.setVisibility(View.GONE);
            // CREATE ACCOUNT BTN WILL BE VISIBLE
            login_btn.setVisibility(View.VISIBLE);
        }
    }

    // METHOD TO VALIDATE DATA
    boolean validateData(String email, String password){

        // PATTERN CLASS TO VALIDATE AND MATCH METHOD
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_edit_text.setError("Email is Invalid");
            return false;
        }
        if (password.length()<6){
            password_edit_text.setError("Password is too short");
            return false;

        }
        // IF ALL IF STATEMENT ARE TRUE THEN RETURN TRUE
        return true;

    }
}