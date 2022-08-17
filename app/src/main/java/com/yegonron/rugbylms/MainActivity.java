package com.yegonron.rugbylms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = findViewById(R.id.loginBtn);
        Button signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        signupBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));
    }
}