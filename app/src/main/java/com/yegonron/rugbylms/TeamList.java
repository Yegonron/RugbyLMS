package com.yegonron.rugbylms;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yegonron.rugbylms.adapters.TeamGroupsAdapter;

import java.util.ArrayList;
import java.util.Arrays;


public class TeamList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    TeamGroupsAdapter teamGroupsAdapter;
    ArrayList<String> teamGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.myTeamList);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        teamGroupsList = new ArrayList<>(Arrays.asList("Homeboyz", "Impala", "Kabras", "KCB", "Leos", "Mwamba", "Nakuru", "Nondies", "Oilers", "Quins"));
        teamGroupsAdapter = new TeamGroupsAdapter(this, teamGroupsList, database);
        recyclerView = findViewById(R.id.myTeamList);
        recyclerView.setAdapter(teamGroupsAdapter);


    }
}