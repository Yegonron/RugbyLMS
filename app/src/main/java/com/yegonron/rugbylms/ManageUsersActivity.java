package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageUsersActivity extends AppCompatActivity {

    private EditText createEmailEt;
    private EditText createPasswordEt;
    final String[] accountTypes = {"Player", "Fan", "Coach", "Manager", "Admin"};

    AutoCompleteTextView accountTypeTv;
    ArrayAdapter<String> adapterItems;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        ImageButton backBtn = findViewById(R.id.backBtn);
        Button createUserBtn = findViewById(R.id.createUserBtn);
        createEmailEt = findViewById(R.id.createEmailEt);
        createPasswordEt = findViewById(R.id.createPasswordEt);
        accountTypeTv = findViewById(R.id.accountTypeTv);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        backBtn.setOnClickListener(v -> onBackPressed());

        createUserBtn.setOnClickListener(v -> {
            //create user
            inputData();
        });

        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, accountTypes);
        accountTypeTv.setAdapter(adapterItems);

    }

    private String createEmail;
    private String accountType;

    private void inputData() {
        createEmail = createEmailEt.getText().toString().trim();
        String password = createPasswordEt.getText().toString().trim();
        accountType = accountTypeTv.getText().toString().trim();

        //validate data

        if (!Patterns.EMAIL_ADDRESS.matcher(createEmail).matches()) {
            Toast.makeText(this, "Invalid email pattern...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters long...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isValidPassword(password)) {
            Toast.makeText(ManageUsersActivity.this, " ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ManageUsersActivity.this, "Password should contain at least one capital letter, one number and one symbol ", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(accountType)) {
            Toast.makeText(this, "Enter account type...", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        //create account
        firebaseAuth.createUserWithEmailAndPassword(createEmail, password).addOnSuccessListener(authResult -> {
            //account created
            saveFirebaseData();
            Toast.makeText(ManageUsersActivity.this, "Account created", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            // failed creating account
            progressDialog.dismiss();
            Toast.makeText(ManageUsersActivity.this, "Failed creating account" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void saveFirebaseData() {
        progressDialog.setMessage("Saving Account info...");

        final String timestamp = "" + System.currentTimeMillis();

        //setup data to save
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", "" + firebaseAuth.getUid());
        hashMap.put("email", "" + createEmail);
        hashMap.put("accountType", "" + accountType);
        hashMap.put("timestamp", "" + timestamp);

        // save to db

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap).addOnSuccessListener(unused -> {
                    // db updated
                    progressDialog.dismiss();

                })
                .addOnFailureListener(e -> {
                    // failed updating db
                    progressDialog.dismiss();


                });
    }
}
