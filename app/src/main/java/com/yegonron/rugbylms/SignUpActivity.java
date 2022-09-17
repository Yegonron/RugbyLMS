package com.yegonron.rugbylms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button playerBtn = findViewById(R.id.playerBtn);
        Button fanBtn = findViewById(R.id.fanBtn);
        Button coachBtn = findViewById(R.id.coachBtn);
        Button managerBtn = findViewById(R.id.managerBtn);
        Button adminBtn = findViewById(R.id.adminBtn);

        TextView noSignupTv = findViewById(R.id.noSignupTv);

        playerBtn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, RegisterPlayerActivity.class)));
        fanBtn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, RegisterFanActivity.class)));
        coachBtn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, RegisterCoachActivity.class)));
        managerBtn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, RegisterManagerActivity.class)));
        adminBtn.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, RegisterAdminActivity.class)));
        noSignupTv.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));

    }
}