package com.yegonron.rugbylms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
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

public class PostActivity extends AppCompatActivity implements FetchAddressTask.onTaskCompleted {

    //Views
    private TextView mLocationTextView;

    //location classes
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mTrackingLocation;
    private LocationCallback mLocationCallBack;
    // Constants
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TRACKING_LOCATION_KEY = "tracking location";
    private final boolean isContinue = false;
    private final boolean isGPS = false;

    // Declare the view objects
    private ImageButton imageBtn;
    private EditText textTitle;
    private EditText textDesc;

    //Declare an Instance of the Storage reference where we will upload the post photo
    private StorageReference mStorageRef;
    //Declare an Instance of the database reference  where we will be saving the post details
    private DatabaseReference databaseRef;
    //Declare an Instance of the database reference  where we have user details
    private DatabaseReference mDatabaseUsers;
    //Declare a Instance of currently logged in user
    private FirebaseUser mCurrentUser;
    // Declare  and initialize  a private final static int  that will serve as our request code

    private static final int GALLERY_REQUEST_CODE = 2;
    // Declare an Instance of URI for getting the image from our phone, initialize it to null
    private Uri uri = null;

    @Override
    protected void onStart() {
        getLocation();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // mLocationTextView = findViewById(R.id.textview_location);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // initializing  view objects
        Button postBtn = findViewById(R.id.postBtn);
        textDesc = findViewById(R.id.textDesc);
        textTitle = findViewById(R.id.textTitle);
        mLocationTextView = findViewById(R.id.location);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        //Initialize the storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Initialize the database reference/node where you will be storing posts
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        //Initialize an instance of  Firebase Authentication
        //Declare an Instance of firebase authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = firebaseAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        // initialize the image button
        imageBtn = findViewById(R.id.imgBtn);
        //picking image from gallery
        imageBtn.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        //getLocation();

        // posting to Firebase
        postBtn.setOnClickListener(view -> {

            Toast.makeText(PostActivity.this, "POSTING...", Toast.LENGTH_LONG).show();
            //get title and desc from the edit texts
            final String PostTitle = textTitle.getText().toString().trim();
            final String PostDesc = textDesc.getText().toString().trim();
            final String mLocation = mLocationTextView.getText().toString().trim();
            //get the date and time of the post

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            final String saveCurrentDate = currentDate.format(calendar.getTime());

            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calendar1.getTime());

            // do a check for empty fields
            if (!TextUtils.isEmpty(PostDesc) && !TextUtils.isEmpty(PostTitle)) {

                //create Storage reference node, inside pOST_image storage reference where you will save the post image
                StorageReference filepath = mStorageRef.child("post_images").child(uri.getLastPathSegment());
                //call the putFile() method passing the post image the user set on the storage reference where you are uploading the image
                //further call addOnSuccessListener on the reference to listen if the upload task was successful,and get a snapshot of the task
                filepath.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                    //if the upload of the post image was successful get the download url
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            //get the download url from your storage use the methods getStorage() and getDownloadUrl()
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            //call the method addOnSuccessListener to determine if we got the download url
                            result.addOnSuccessListener(uri -> {
                                //convert the uri to a string on success
                                final String imageUrl = uri.toString();

                                Toast.makeText(getApplicationContext(), "Succesfully Uploaded", Toast.LENGTH_SHORT).show();
                                // call the method push() to add values on the database reference
                                final DatabaseReference newPost = databaseRef.push();
                                //adding post contents to database reference
                                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        newPost.child("title").setValue(PostTitle);
                                        newPost.child("desc").setValue(PostDesc);
                                        newPost.child("postImage").setValue(imageUrl);
                                        newPost.child("uid").setValue(mCurrentUser.getUid());
                                        newPost.child("time").setValue(saveCurrentTime);
                                        newPost.child("date").setValue(saveCurrentDate);
                                        newPost.child("location").setValue(mLocation);
                                        //get the profile photo and display name of the person posting
                                        newPost.child("profileImage").setValue(dataSnapshot.child("profileImage").getValue());
                                        newPost.child("username").setValue(dataSnapshot.child("username").getValue()).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                //launch the main activity after posting
                                                Intent intent = new Intent(PostActivity.this, MainAdminActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            });
                        }
                    }
                });
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("TAG", "getLocation: permissions granted");
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            // Start the reverse geocode AsyncTask
            new FetchAddressTask(PostActivity.this, PostActivity.this).execute(location);
        });
        mLocationTextView.setText(getString(R.string.address_text, getString(R.string.loading), System.currentTimeMillis()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {// If the permission is granted, get the location,
            // otherwise, show a Toast
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        //update ui
        // mLocationTextView.setVisibility(true);
        mLocationTextView.setText(getString(R.string.address_text, result, System.currentTimeMillis()));


    }

    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            //get the image selected by the user
            uri = data.getData();
            //set the image
            imageBtn.setImageURI(uri);
        }
    }

}
