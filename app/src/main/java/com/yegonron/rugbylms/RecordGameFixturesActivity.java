package com.yegonron.rugbylms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecordGameFixturesActivity extends AppCompatActivity {

    // Declare the view objects
    private EditText fixtureTitleEt, homeTeamEt, awayTeamEt, fixtureVenueEt, fixtureTimeEt;

    //Declare an Instance of the Storage reference where we will upload the post photo
    private StorageReference mStorageRef;
    //Declare an Instance of the database reference  where we will be saving the post details
    private DatabaseReference databaseRef;
    //Declare an Instance of the database reference  where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a Instance of currently logged in user
    private FirebaseUser mCurrentUser;
    // Declare  and initialize  a private final static int  that will serve as our request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_game_fixtures);

        fixtureTitleEt = findViewById(R.id.fixtureTitleEt);
        homeTeamEt = findViewById(R.id.homeTeamEt);
        awayTeamEt = findViewById(R.id.awayTeamEt);
        fixtureVenueEt = findViewById(R.id.fixtureVenueEt);
        fixtureTimeEt = findViewById(R.id.fixtureTimeEt);
        Button recordGameFixtureBtn = findViewById(R.id.recordGameFixtureBtn);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        //Initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Initialize the database reference/node where you will be storing posts
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Fixtures");
        //Initialize an instance of  Firebase Authentication
        //Declare an Instance of firebase authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = firebaseAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        // posting to Firebase
        recordGameFixtureBtn.setOnClickListener(view -> {

            Toast.makeText(RecordGameFixturesActivity.this, "Posting...", Toast.LENGTH_LONG).show();
            //get title and desc from the edit texts
            final String fixtureTitle = fixtureTitleEt.getText().toString().trim();
            final String homeTeam = homeTeamEt.getText().toString().trim();
            final String awayTeam = awayTeamEt.getText().toString().trim();
            final String fixtureVenue = fixtureVenueEt.getText().toString().trim();
            final String fixtureTime = fixtureTimeEt.getText().toString().trim();

            //get the date and time of the post
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            final String saveCurrentDate = currentDate.format(calendar.getTime());

            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calendar1.getTime());

            // do a check for empty fields
            if (!TextUtils.isEmpty(homeTeam) && !TextUtils.isEmpty(awayTeam) && !TextUtils.isEmpty(fixtureVenue) && !TextUtils.isEmpty(fixtureTime)) {

                // call the method push() to add values on the database reference
                final DatabaseReference newFixture = databaseRef.push();
                //adding post contents to database reference
                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        newFixture.child("uid").setValue(mCurrentUser.getUid());
                        newFixture.child("fixtureTitle").setValue(fixtureTitle);
                        newFixture.child("homeTeam").setValue(homeTeam);
                        newFixture.child("awayTeam").setValue(awayTeam);
                        newFixture.child("fixtureVenue").setValue(fixtureVenue);
                        newFixture.child("fixtureTime").setValue(fixtureTime);
                        newFixture.child("date").setValue(saveCurrentDate);
                        newFixture.child("time").setValue(saveCurrentTime);

                        //get username of the person posting fixtures
                        newFixture.child("username").setValue(dataSnapshot.child("username").getValue()).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                                //launch the main activity after posting
                                Intent intent = new Intent(RecordGameFixturesActivity.this, GameFixturesActivity.class);
                                startActivity(intent);

                                showNotification("fixtureTitle: " + fixtureTitle, "fixtureTime: " + fixtureTime);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void showNotification(String title, String msg) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Desc");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID").setSmallIcon(R.drawable.icon).setContentTitle(title).setContentText(msg).setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        notificationManager.notify(0, builder.build());
    }

}