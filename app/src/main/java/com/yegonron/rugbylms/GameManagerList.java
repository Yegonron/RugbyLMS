package com.yegonron.rugbylms;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yegonron.rugbylms.adapters.GameGroupsManagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class GameManagerList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    GameGroupsManagerAdapter gameGroupsManagerAdapter;
    ArrayList<String> gameGroupsManagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager_list);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.myUserList);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameGroupsManagerList = new ArrayList<>(Arrays.asList("Homeboyz", "Impala", "Kabras", "KCB", "Leos", "Mwamba", "Nakuru", "Nondies", "Oilers", "Quins"));
        gameGroupsManagerAdapter = new GameGroupsManagerAdapter(this, gameGroupsManagerList, database);
        recyclerView = findViewById(R.id.myUserList);
        recyclerView.setAdapter(gameGroupsManagerAdapter);

    }
}