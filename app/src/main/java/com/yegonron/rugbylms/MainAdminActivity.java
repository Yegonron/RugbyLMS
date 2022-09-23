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

public class MainAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_main_admin);

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
                    Toast.makeText(MainAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainAdminActivity.this, LoginActivity.class));
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
                Intent intent = new Intent(MainAdminActivity.this, ProfileEditAdminActivity.class);
                startActivity(intent);
                break;

            case R.id.manage_users:
                Intent intent1 = new Intent(MainAdminActivity.this, ManageUsersActivity.class);
                startActivity(intent1);
                break;

            case R.id.reports:
                Intent intent2 = new Intent(MainAdminActivity.this, ReportsActivity.class);
                startActivity(intent2);
                break;

            case R.id.recordGameFixtures:
                Intent intent3 = new Intent(MainAdminActivity.this, RecordGameFixturesActivity.class);
                startActivity(intent3);
                break;

            case R.id.gameFixtures:
                Intent intent4 = new Intent(MainAdminActivity.this, GameFixturesActivity.class);
                startActivity(intent4);
                break;

            case R.id.leagueTable:
                Intent intent5 = new Intent(MainAdminActivity.this, LeagueTableActivity.class);
                startActivity(intent5);
                break;

            case R.id.updateLeagueTable:
                Intent intent6 = new Intent(MainAdminActivity.this, UpdateLeagueTableActivity.class);
                startActivity(intent6);
                break;

            case R.id.livestreamGames:
                Intent intent7 = new Intent(MainAdminActivity.this, LivestreamGamesActivity.class);
                startActivity(intent7);
                break;

            case R.id.settings:
                Intent intent8 = new Intent(MainAdminActivity.this, SettingsActivity.class);
                startActivity(intent8);
                break;

            case R.id.help:
                Intent intent9 = new Intent(MainAdminActivity.this, HelpActivity.class);
                startActivity(intent9);
                break;

            case R.id.About_Us:
                Intent intent10 = new Intent(MainAdminActivity.this, AboutUsActivity.class);
                startActivity(intent10);
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