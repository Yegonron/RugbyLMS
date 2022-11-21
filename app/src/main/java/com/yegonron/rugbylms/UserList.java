package com.yegonron.rugbylms;


import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yegonron.rugbylms.adapters.UserGroupsAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class UserList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    UserGroupsAdapter userGroupsAdapter;
    ArrayList<String> userGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ImageButton backBtn = findViewById(R.id.listBackBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.myUserList);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userGroupsList = new ArrayList<>(Arrays.asList(new String[]{"Admin", "Manager", "Coach", "Player", "Fan"}));
        userGroupsAdapter = new UserGroupsAdapter(this, userGroupsList, database);
        recyclerView = findViewById(R.id.myUserList);
        recyclerView.setAdapter(userGroupsAdapter);


    }
}
