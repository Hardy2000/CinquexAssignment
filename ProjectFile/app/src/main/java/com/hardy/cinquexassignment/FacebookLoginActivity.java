package com.hardy.cinquexassignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FacebookLoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {

        }

    }
}