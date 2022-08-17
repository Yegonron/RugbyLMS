package com.yegonron.rugbylms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> onBackPressed());

    }
}