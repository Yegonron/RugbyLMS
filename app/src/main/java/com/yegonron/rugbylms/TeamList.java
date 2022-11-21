package com.yegonron.rugbylms;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class TeamList extends AppCompatActivity {

    EditText editText;
    Button submit, fetchButton;
    DatabaseReference rootRef, demoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        editText = findViewById(R.id.etValue);
        submit = findViewById(R.id.btnSubmit);
        ImageButton backBtn = findViewById(R.id.backBtn);

        fetchButton = findViewById(R.id.btnFetch);
        final TextView fetchedText = findViewById(R.id.tvValue);

        // Database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();

        // Database reference pointing to demo node
        //demoRef = rootRef.child("PLAYER_TABLE");

        backBtn.setOnClickListener(v -> onBackPressed());

        submit.setOnClickListener(v -> {
            String value = editText.getText().toString();

            // Push creates a unique id in database
            demoRef.setValue(value);
        });

        fetchButton.setOnClickListener(v -> {
            demoRef = FirebaseDatabase.getInstance().getReference().child("Users").child("1");
            demoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    fetchedText.setText(name);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }
}