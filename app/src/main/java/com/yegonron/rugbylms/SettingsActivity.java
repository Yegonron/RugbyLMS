package com.yegonron.rugbylms;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {

    private EditText passwordEt, cPasswordEt;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton backBtn = findViewById(R.id.backBtn);
        Button changePasswordBtn = findViewById(R.id.changePasswordBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);

        passwordEt = findViewById(R.id.passwordEt);
//        cPasswordEt = findViewById(R.id.cPasswordEt);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        backBtn.setOnClickListener(v -> onBackPressed());

        changePasswordBtn.setOnClickListener(v -> {
            //change user password
            changeUserPassword();
        });

        deleteBtn.setOnClickListener(view -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting this account will result in completely removing your" +
                    " account from the system and you won't be able to access the app.");
            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    deleteCurrentUser();
                }
            });

            dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        });

    }

    private void changeUserPassword() {
        //input data
        String password = passwordEt.getText().toString().trim();
//        String confirmPassword = cPasswordEt.getText().toString().trim();

        //validate data

//        if (password.length() < 6) {
//            Toast.makeText(this, "Password should be at least 6 characters long...", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (isValidPassword(password)) {
//            Toast.makeText(SettingsActivity.this, " ", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(SettingsActivity.this, "Password should contain at least one capital letter, one number and one symbol ", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!password.equals(confirmPassword)) {
//            Toast.makeText(this, "Password doesn't match...", Toast.LENGTH_SHORT).show();
//            return;
//        }

        progressDialog.setMessage("Changing password...");
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });


    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }


    private void deleteCurrentUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setMessage("Account being deleted");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(null)
                .addOnSuccessListener(avoid -> FirebaseAuth.getInstance().getCurrentUser().delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                startActivity(intent);

                            }
                        }));
    }
}