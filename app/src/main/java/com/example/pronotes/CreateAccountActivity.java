package com.example.pronotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateAccountActivity extends AppCompatActivity {

    EditText email_edit_text, password_edit_text,confirm_password_edit_text;
    Button create_account_btn;
    ProgressBar progress_bar;
    TextView login_text_view_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        confirm_password_edit_text = findViewById(R.id.confirm_password_edit_text);
        create_account_btn = findViewById(R.id.create_account_btn);
        progress_bar = findViewById(R.id.progress_bar);
        login_text_view_btn = findViewById(R.id.login_text_view_btn);

        // ONCLICK ON CREATE ACCOUNT BTN
        create_account_btn.setOnClickListener(view->creatAccount());
        login_text_view_btn.setOnClickListener(view ->finish());

    }
   // LOGIC FOR CREATE_ACCOUNT_BTN
    void creatAccount(){
        String email = email_edit_text.getText().toString();
        String password = password_edit_text.getText().toString();
        String confirmPassword = confirm_password_edit_text.getText().toString();
        boolean isValidate = validateData(email,password,confirmPassword);
        if (!isValidate){
            return;
        }
        createAccountInFirebase(email, password);
    }
// ACCOUNT WILL BE CREATED IN FIREBASE
    void createAccountInFirebase(String email, String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            // TOAST WILL COME FROM UTILITY CLASS
                            Utility.showToast(CreateAccountActivity.this, "your account is created successfully, to varify check email");
                            // CREATE ACCOUNT IS DONE

                            // AN EMAIL WILL BE SEND TO VARIFY USER
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();

                        }
                        else{
                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                            // FAILURE

                        }
                    }
                });

    }
    void changeInProgress(boolean inProgress){
        if (inProgress){
            //PROGRESS BAR WILL BE VISIBLE
            progress_bar.setVisibility(View.VISIBLE);
            // CREATE ACCOUNT BTN WILL BE GONE
            create_account_btn.setVisibility(View.GONE);
        }
        else{
            // PROGRESS BAR WILL BE GONE
            progress_bar.setVisibility(View.GONE);
            // CREATE ACCOUNT BTN WILL BE VISIBLE
            create_account_btn.setVisibility(View.VISIBLE);
        }
    }

    // METHOD TO VALIDATE DATA
    boolean validateData(String email, String password, String confirmPassword){

        // PATTERN CLASS TO VALIDATE AND MATCH METHOD
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_edit_text.setError("Email is Invalid");
            return false;
        }
        if (password.length()<6){
            password_edit_text.setError("Password is too short");
            return false;

        }
        if (!password.equals(confirmPassword)){
            confirm_password_edit_text.setError("password is not matched");
            return false;
        }
        // IF ALL IF STATEMENT ARE TRUE THEN RETURN TRUE
        return true;

    }
}