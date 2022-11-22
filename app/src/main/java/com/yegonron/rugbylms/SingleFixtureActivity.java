package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class SingleFixtureActivity extends AppCompatActivity {

    // Declare the view objects
    private TextView fixtureTitle, homeTeam, awayTeam, fixtureVenue, fixtureDate, fixtureTime, date, time, homeTeamScoreTv, awayTeamScoreTv;
    EditText hScore, aScore;
    private RelativeLayout scores;

    String fixture_key = null;

    private DatabaseReference mDatabase, scoresRef, mDatabaseUsers;
    private Button updateBtn, deleteBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mCurrentUser;

    AlertDialog dialog;
    private FirebaseRecyclerAdapter adapter;
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
        homeTeamScoreTv = findViewById(R.id.homeTeamScoreTv);
        awayTeamScoreTv = findViewById(R.id.awayTeamScoreTv);

        scores = findViewById(R.id.scoresRl);
        hScore = findViewById(R.id.homeTeamScoreEt);
        aScore = findViewById(R.id.awayTeamScoreEt);

        firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        fixture_key = getIntent().getExtras().getString("FixtureID");

        //Initialize the database reference/node where you will be storing comments
        scoresRef = FirebaseDatabase.getInstance().getReference().child("Scores").child(fixture_key);
        //Initialize an instance of  Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = firebaseAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(mCurrentUser).getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Fixtures");

        deleteBtn = findViewById(R.id.deleteBtn);
        updateBtn = findViewById(R.id.updateBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);
        updateBtn.setVisibility(View.INVISIBLE);
        scores.setVisibility(View.INVISIBLE);

        updateBtn.setOnClickListener(v -> {
            Toast.makeText(SingleFixtureActivity.this, "Posting scores...", Toast.LENGTH_LONG).show();
            //get the scores from the edit texts
            final String hTeamScore = hScore.getText().toString().trim();
            final String aTeamScore = aScore.getText().toString().trim();
            //get the date and time of the post

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            final String saveCurrentDate = currentDate.format(calendar.getTime());

            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calendar1.getTime());
            // do a check for empty fields
            if (!TextUtils.isEmpty(hTeamScore) && !TextUtils.isEmpty(aTeamScore)) {
                final DatabaseReference newScores = scoresRef.push();
                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        newScores.child("uid").setValue(mCurrentUser.getUid());
                        newScores.child("homeTeamScore").setValue(hTeamScore);
                        newScores.child("awayTeamScore").setValue(aTeamScore);
                        newScores.child("time").setValue(saveCurrentTime);
                        newScores.child("date").setValue(saveCurrentDate);
                        newScores.child("profileImage").setValue(dataSnapshot.child("profileImage").getValue());
                        newScores.child("username").setValue(dataSnapshot.child("username").getValue());

                        newScores.child("fixtureTitle").setValue(dataSnapshot.child("fixtureTitle").getValue());
                        newScores.child("fixtureDate").setValue(dataSnapshot.child("fixtureDate").getValue());

                        Toast.makeText(SingleFixtureActivity.this, "Recorded", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SingleFixtureActivity.this, GameFixturesActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });


        deleteBtn.setOnClickListener(view -> {

            mDatabase.child(fixture_key).removeValue();
            Toast.makeText(SingleFixtureActivity.this, "Fixture Deleted", Toast.LENGTH_LONG).show();
            onBackPressed();

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
                    scores.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loadScores();

    }

    private void loadScores() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Scores");
        ref.orderByChild("uid").equalTo(fixture_key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String hScore = "" + ds.child("homeTeamScore").getValue();
                            String aScore = "" + ds.child("awayTeamScore").getValue();

                            homeTeamScoreTv.setText(hScore);
                            awayTeamScoreTv.setText(aScore);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}