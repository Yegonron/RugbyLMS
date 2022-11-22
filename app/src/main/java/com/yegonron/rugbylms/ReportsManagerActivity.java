package com.yegonron.rugbylms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ReportsManagerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;

    private FirebaseAuth firebaseAuth;

    ArrayList barArraylist;

    Button button, btnUserList, btnTeamList, btnTeamReport, btnGameList, btnGameReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_reports);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        button = findViewById(R.id.userReport);
        btnUserList = findViewById(R.id.userList);
        btnTeamList = findViewById(R.id.teamList);
        btnTeamReport = findViewById(R.id.teamReport);
        btnGameList = findViewById(R.id.gameList);
        btnGameReport = findViewById(R.id.gameReport);

        button.setOnClickListener(v -> {
            Intent int1 = new Intent(ReportsManagerActivity.this, UserManagerReport.class);
            startActivity(int1);

        });

        btnUserList.setOnClickListener(v -> {
            Intent int2 = new Intent(ReportsManagerActivity.this, UserManagerList.class);
            startActivity(int2);
        });

        btnTeamList.setOnClickListener(v -> {
            Intent int3 = new Intent(ReportsManagerActivity.this, TeamManagerList.class);
            startActivity(int3);
        });

        btnTeamReport.setOnClickListener(v -> {
            Intent int4 = new Intent(ReportsManagerActivity.this, TeamManagerReport.class);
            startActivity(int4);
        });
        btnGameList.setOnClickListener(v -> {
            Intent int5 = new Intent(ReportsManagerActivity.this, GameManagerList.class);
            startActivity(int5);
        });
        btnGameReport.setOnClickListener(v -> {
            Intent int6 = new Intent(ReportsManagerActivity.this, GameManagerReport.class);
            startActivity(int6);
        });


    }

    private void getData() {
        barArraylist = new ArrayList();
        barArraylist.add(new BarEntry(2f, 10));
        barArraylist.add(new BarEntry(3f, 20));
        barArraylist.add(new BarEntry(4f, 30));
        barArraylist.add(new BarEntry(5f, 40));
        barArraylist.add(new BarEntry(6f, 50));
    }

}