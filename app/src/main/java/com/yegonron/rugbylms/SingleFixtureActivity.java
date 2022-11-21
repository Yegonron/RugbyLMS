package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SingleFixtureActivity extends AppCompatActivity {

    // Declare the view objects
    private TextView fixtureTitle, homeTeam, awayTeam, fixtureVenue, fixtureDate, fixtureTime, date, time;

    String fixture_key = null;

    private DatabaseReference mDatabase;
    private Button updateBtn, deleteBtn;
    private FirebaseAuth firebaseAuth;

    AlertDialog dialog;

    String currentUserID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fixture);

        fixtureTitle = findViewById(R.id.fixtureTitleTv);
        homeTeam = findViewById(R.id.homeTeamTv);
        awayTeam = findViewById(R.id.awayTeamTv);
        fixtureVenue = findViewById(R.id.fixtureVenueTv);
        fixtureDate = findViewById(R.id.fixtureDateTv);
        fixtureTime = findViewById(R.id.fixtureTimeTv);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);

        firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        fixture_key = getIntent().getExtras().getString("FixtureID");

        //Initialize an instance of  Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        FirebaseUser mCurrentUser = firebaseAuth.getCurrentUser();
        //Get currently logged in user
        DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(mCurrentUser).getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Fixtures");

        deleteBtn = findViewById(R.id.deleteBtn);
        updateBtn = findViewById(R.id.updateBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        updateBtn.setVisibility(View.INVISIBLE);

        deleteBtn.setOnClickListener(view -> {

            AlertDialog.Builder dialog = new AlertDialog.Builder(SingleFixtureActivity.this);
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting this fixture will completely remove it" + " from the system and you won't be able to access it.");
            dialog.setPositiveButton("Delete", (dialogInterface, i) -> mDatabase.child(fixture_key).removeValue());

            dialog.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        });

        mDatabase.child(fixture_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fixture_title = (String) dataSnapshot.child("fixtureTitle").getValue();
                String home_team = (String) dataSnapshot.child("homeTeam").getValue();
                String away_team = (String) dataSnapshot.child("awayTeam").getValue();
                String fixture_venue = (String) dataSnapshot.child("fixtureVenue").getValue();
                String fixture_date = (String) dataSnapshot.child("fixtureDate").getValue();
                String fixture_time = (String) dataSnapshot.child("fixtureTime").getValue();
                String post_date = (String) dataSnapshot.child("date").getValue();
                String post_time = (String) dataSnapshot.child("time").getValue();
                String fixture_uid = (String) dataSnapshot.child("uid").getValue();

                fixtureTitle.setText(fixture_title);
                homeTeam.setText("Home Team: " + home_team);
                awayTeam.setText("Away Team: " + away_team);
                fixtureVenue.setText("Fixture Venue: " + fixture_venue);
                fixtureDate.setText("Fixture Date: " + fixture_date);
                fixtureTime.setText("Fixture Time: " + fixture_time);
                date.setText(post_date);
                time.setText(post_time);

                if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid().equals(fixture_uid)) {

                    updateBtn.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
