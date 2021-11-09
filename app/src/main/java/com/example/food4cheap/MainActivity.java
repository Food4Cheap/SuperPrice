package com.example.food4cheap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ParseUser.getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        setContentView(R.layout.activity_main);
    }
}