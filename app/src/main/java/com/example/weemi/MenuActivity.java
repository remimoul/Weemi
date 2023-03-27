package com.example.weemi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuActivity extends AppCompatActivity {

    Button picture,profile,deconnect,history;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        picture = findViewById(R.id.takePictureButton);
        profile = findViewById(R.id.profileButton);
        deconnect = findViewById(R.id.deconnectButton);
        history = findViewById(R.id.HistoryBtn);


        history.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this,HistoryActivity.class))));

        picture.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this,TakePhotoActivity.class))));

        profile.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this,ProfileActivity.class))));

        deconnect.setOnClickListener((v -> startActivity(new Intent(MenuActivity.this,MenuActivity.class))));

    }
}