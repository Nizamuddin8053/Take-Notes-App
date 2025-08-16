package com.example.pronotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView aboutContent;
    ImageButton aboutBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        aboutContent = findViewById(R.id.about_content);
        aboutBackBtn = findViewById(R.id.about_back_btn);
        aboutBackBtn.setOnClickListener((v)->{
            startActivity(new Intent(AboutActivity.this, MainActivity.class));
            onBackPressed();
        });
        String content  = "Welcome to the our Notes Making App . Our aim is to create a user-friendly\n" +
                "applications that will help you organize your notes efficiently. With this app, you can easily\n" +
                "create, edit and manage notes of all kinds – whether it’s for personal or professional use.\n" +
                "The aims to provide a comprehensive notes making app that can improve productivity\n" +
                "and organization for users. With this app, you can keep all your notes in one place and access\n" +
                "them whenever and wherever you need them.\n"+ "\n"+
               "Objectives:\n" +
                "1. Create a user- friendly notes making application that can be easily used by anyone.\n" +
                "2. Allow users to Create, Edit , Search and Manage notes.\n" +
                "3. Continuously improve the app based on user feedback and suggestions to enhance user\n" +
                "experience and functionality.";
                ;
        aboutContent.setText(content);

    }
}