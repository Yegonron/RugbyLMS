package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageUsersActivity extends AppCompatActivity {

    private EditText createEmailEt, createPasswordEt;
    private EditText enterEmailEt;
    private Button deleteUserBtn;

    final String[] accountTypes = {"Player", "Fan", "Coach", "Manager", "Admin"};

    AutoCompleteTextView accountTypeTv;
    ArrayAdapter<String> adapterItems;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        ImageButton backBtn = findViewById(R.id.backBtn);
        Button createUserBtn = findViewById(R.id.createUserBtn);
        TextView accountTypeTv = findViewById(R.id.accountTypeTv);
        createEmailEt = findViewById(R.id.createEmailEt);
        createPasswordEt = findViewById(R.id.createPasswordEt);

        enterEmailEt = findViewById(R.id.enterEmailEt);
        deleteUserBtn = findViewById(R.id.deleteUserBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        backBtn.setOnClickListener(v -> onBackPressed());

//        createUserBtn.setOnClickListener(v -> {
//            //create user
//            inputData();
//        });

//        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, accountTypes);
////        accountTypeTv.setAdapter(adapterItems);

//        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(ManageUsersActivity.this);
//                dialog.setTitle("Are you sure?");
//                dialog.setMessage("Deleting this account will result in completely removing the account from the system");
//                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        deleteUser();
//                    }
//                });
//
//                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                AlertDialog alertDialog = dialog.create();
//                alertDialog.show();
//            }
//        });

    }

    private String createEmail;
    private String enterEmail;
    private String accountType;

//    private void inputData() {
//        createEmail = createEmailEt.getText().toString().trim();
//        String password = createPasswordEt.getText().toString().trim();
//        accountType = accountTypeTv.getText().toString().trim();
//
//        //validate data
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(createEmail).matches()) {
//            Toast.makeText(this, "Invalid email pattern...", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (password.length() < 6) {
//            Toast.makeText(this, "Password should be at least 6 characters long...", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(accountType)) {
//            Toast.makeText(this, "Enter account type...", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        progressDialog.setMessage("Creating account...");
//        progressDialog.show();
//
//        //create account
//        firebaseAuth.createUserWithEmailAndPassword(createEmail, password).addOnSuccessListener(authResult -> {
//            //account created
//            saveFirebaseData();
//            Toast.makeText(ManageUsersActivity.this, "account created", Toast.LENGTH_SHORT).show();
//
//        }).addOnFailureListener(e -> {
//            // failed creating account
//            progressDialog.dismiss();
//            Toast.makeText(ManageUsersActivity.this, "failed creating account" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        });

//    }
//
//    public boolean isValidPassword(final String password) {
//        Pattern pattern;
//        Matcher matcher;
//
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
//
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher = pattern.matcher(password);
//
//        return matcher.matches();
//    }

//    private void saveFirebaseData() {
//        progressDialog.setMessage("Saving Account info...");
//
//        final String timestamp = "" + System.currentTimeMillis();
//
//        //setup data to save
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("uid", "" + firebaseAuth.getUid());
//        hashMap.put("email", "" + createEmail);
//        hashMap.put("accountType", "" + accountType);
//        hashMap.put("timestamp", "" + timestamp);
//
//        // save to db
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
//        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap).addOnSuccessListener(unused -> {
//                    // db updated
//                    progressDialog.dismiss();
//                    finish();
//                })
//                .addOnFailureListener(e -> {
//                    // failed updating db
//                    progressDialog.dismiss();
//                    finish();
//                });
//    }
//
//    private void deleteUser() {
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please wait...");
//        progressDialog.setMessage("Account being deleted");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Users")
//                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
//                .setValue(null)
//                .addOnSuccessListener(avoid -> FirebaseAuth.getInstance().getCurrentUser().delete()
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                progressDialog.dismiss();
//
//                            }
//                        }));
//
//    }
}
