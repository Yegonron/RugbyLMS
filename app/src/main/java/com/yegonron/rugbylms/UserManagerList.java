package com.yegonron.rugbylms;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yegonron.rugbylms.adapters.UserGroupsManagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class UserManagerList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    UserGroupsManagerAdapter userGroupsManagerAdapter;
    ArrayList<String> userGroupsManagerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager_list);

        ImageButton backBtn = findViewById(R.id.listBackBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.myUserList);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserManagerList.this));

        userGroupsManagerList = new ArrayList<>(Arrays.asList("Admin", "Manager", "Coach", "Player", "Fan"));
        userGroupsManagerAdapter = new UserGroupsManagerAdapter(UserManagerList.this, userGroupsManagerList, database);
        recyclerView = findViewById(R.id.myUserList);
        recyclerView.setAdapter(userGroupsManagerAdapter);

    }
}
