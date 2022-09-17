package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class MainManagerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nameTv, emailTv, phoneTv;
    private ImageView profileIv;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        profileIv = findViewById(R.id.profileIv);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();

        }
    }

    private void makeMeOffline() {
        // after logging out, make user offline
        progressDialog.setMessage("Logging out user...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                .addOnSuccessListener(unused -> {
                    // update successfully
                    firebaseAuth.signOut();
                    checkUser();
                })
                .addOnFailureListener(e -> {
                    //failed updating
                    progressDialog.dismiss();
                    Toast.makeText(MainManagerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainManagerActivity.this, LoginActivity.class));
            finish();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = "" + ds.child("surname").getValue() + " " + ds.child("firstname").getValue() + " " + ds.child("lastname").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();

                            nameTv.setText(name);
                            emailTv.setText(email);
                            phoneTv.setText(phone);

                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_white).into(profileIv);

                            } catch (Exception e) {
                                profileIv.setImageResource(R.drawable.ic_person_white);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profile:
                Intent intent = new Intent(MainManagerActivity.this, ProfileEditManagerActivity.class);
                startActivity(intent);
                break;

            case R.id.reports:
                Intent intent1 = new Intent(MainManagerActivity.this, ReportsActivity.class);
                startActivity(intent1);
                break;

            case R.id.gameFixtures:
                Intent intent2 = new Intent(MainManagerActivity.this, GameFixturesActivity.class);
                startActivity(intent2);
                break;

            case R.id.leagueTable:
                Intent intent3 = new Intent(MainManagerActivity.this, LeagueTableActivity.class);
                startActivity(intent3);
                break;

            case R.id.livestreamGames:
                Intent intent4 = new Intent(MainManagerActivity.this, LivestreamGamesActivity.class);
                startActivity(intent4);
                break;

         /*   case R.id.receive_Game_Notification:
                Intent intent5 = new Intent(MainManagerActivity.this,);
                startActivity(intent5);
                break;
         */
            case R.id.help:
                Intent intent6 = new Intent(MainManagerActivity.this, HelpActivity.class);
                startActivity(intent6);
                break;

            case R.id.About_Us:
                Intent intent7 = new Intent(MainManagerActivity.this, AboutUsActivity.class);
                startActivity(intent7);
                break;

            case R.id.Exit:
                //make offline
                //sign out
                //go to login activity
                makeMeOffline();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
