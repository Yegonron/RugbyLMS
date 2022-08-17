package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainPlayerActivity extends AppCompatActivity {

    private TextView nameTv, emailTv,phoneTv;
    private ImageView profileIv;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main_player );

        nameTv = findViewById ( R.id.nameTv );
        emailTv = findViewById ( R.id.emailTv );
        phoneTv = findViewById ( R.id.phoneTv );

        profileIv = findViewById(R.id.profileIv);

        ImageButton logoutBtn = findViewById(R.id.logoutBtn);
        ImageButton editProfileBtn = findViewById(R.id.editProfileBtn);
        ImageButton settingsBtn = findViewById(R.id.settingsBtn);

        logoutBtn.setOnClickListener (view -> {
            //make offline
            //sign out
            //go to login activity
            makeMeOffline();
        });

        editProfileBtn.setOnClickListener(v -> startActivity(new Intent(MainPlayerActivity.this, ProfileEditPlayerActivity.class)));
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(MainPlayerActivity.this, SettingsActivity.class)));

        firebaseAuth = FirebaseAuth.getInstance ();
        progressDialog = new ProgressDialog ( this );
        progressDialog.setTitle ( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside ( false );
        checkUser();
    }

    private void makeMeOffline() {
        // after logging out, make user offline
        progressDialog.setMessage ( "Logging out user..." );

        HashMap<String, Object> hashMap = new HashMap <> (  );
        hashMap.put("online","false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                .addOnSuccessListener (unused -> {
                    // update successfully
                    firebaseAuth.signOut ();
                    checkUser ();
                })
                .addOnFailureListener (e -> {
                    //failed updating
                    progressDialog.dismiss ();
                    Toast.makeText ( MainPlayerActivity.this , ""+e.getMessage () , Toast.LENGTH_SHORT ).show ( );
                });
    }

    private void checkUser() {
        FirebaseUser user= firebaseAuth.getCurrentUser ();
        if (user==null){
            startActivity ( new Intent ( MainPlayerActivity.this, LoginActivity.class ) );
            finish ();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance ().getReference ("Users");
        ref.orderByChild ( "uid" ).equalTo ( firebaseAuth.getUid () )
                .addValueEventListener ( new ValueEventListener( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren ()){
                            String name = ""+ds.child ( "surname" ).getValue ()+" "+ds.child ( "firstname" ).getValue ()+" "+ds.child ( "lastname" ).getValue ();
                            String email = ""+ds.child ( "email" ).getValue ();
                            String phone = ""+ds.child ( "phone" ).getValue ();
                            String profileImage = ""+ds.child ( "profileImage" ).getValue ();

                            nameTv.setText (name);
                            emailTv.setText(email);
                            phoneTv.setText(phone);
                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_gray).into(profileIv);

                            }
                            catch (Exception e){
                                profileIv.setImageResource(R.drawable.ic_person_gray);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
    }
}
