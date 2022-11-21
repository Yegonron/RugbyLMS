package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SingleUserActivity extends AppCompatActivity {

    // Declare the view objects
    public ImageView userImage;
    public TextView name;
    public TextView age;
    public TextView phone;
    public TextView position;
    public TextView bootSize;
    public TextView kitSize;

    String user_key = null;

    String currentUserID = null;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);

        userImage = findViewById(R.id.userImage);
        name = findViewById(R.id.nameTv);
        age = findViewById(R.id.ageTv);
        phone = findViewById(R.id.phoneTv);
        position = findViewById(R.id.positionTv);
        kitSize = findViewById(R.id.kitSizeTv);
        bootSize = findViewById(R.id.bootSizeTv);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        user_key = getIntent().getExtras().getString("UserID");

        //Initialize an instance of  Firebase Authentication
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        FirebaseUser mCurrentUser = firebaseAuth.getCurrentUser();
        //Get currently logged in user
        DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(mCurrentUser).getUid());

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase.child(user_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_surname = (String) dataSnapshot.child("surname").getValue();
                String user_firstname = (String) dataSnapshot.child("firstname").getValue();
                String user_lastname = (String) dataSnapshot.child("lastname").getValue();
                String user_code = (String) dataSnapshot.child("countryCode").getValue();
                String user_phone = (String) dataSnapshot.child("phone").getValue();
                String user_age = (String) dataSnapshot.child("age").getValue();
                String user_position = (String) dataSnapshot.child("position").getValue();
                String user_kitsize = (String) dataSnapshot.child("kitsize").getValue();
                String user_bootsize = (String) dataSnapshot.child("bootsize").getValue();
                String user_Image = "" + dataSnapshot.child("profileImage").getValue();

                String user_uid = (String) dataSnapshot.child("uid").getValue();

                name.setText("Name: " + user_surname + " " + user_firstname + " " + user_lastname);
                phone.setText("Phone: +" + user_code + " " + user_phone);
                age.setText("Age: " + user_age);
                position.setText("Position: " + user_position);
                kitSize.setText("Kit Size: " + user_kitsize);
                bootSize.setText("Boot Size: " + user_bootsize);

                try {
                    Picasso.get().load(user_Image).placeholder(R.drawable.ic_person_white).into(userImage);

                } catch (Exception e) {
                    userImage.setImageResource(R.drawable.ic_person_white);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
