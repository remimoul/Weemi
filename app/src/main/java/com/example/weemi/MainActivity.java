package com.example.weemi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button connexion;
    TextView createAccount;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connexion = findViewById(R.id.buttonEntrer);
        createAccount = findViewById(R.id.createAccountTextView);

        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent connect = new Intent(MainActivity.this,ConnectActivity.class);
                startActivity(connect);

            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent create = new Intent(MainActivity.this,CreateAccountActivity.class);
                startActivity(create);

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser == null){
                    startActivity(new Intent(MainActivity.this,ConnectActivity.class));
                }else {
                     startActivity(new Intent(MainActivity.this,MenuActivity.class));
                }
                finish();
            }
        },1000);

    }
}