package com.yegonron.rugbylms;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

@SuppressWarnings("ALL")
public class RegisterManagerActivity extends AppCompatActivity {

    private ImageView profileIv;
    private EditText surNameEt, firstNameEt, lastNameEt, phoneEt, userNameEt, emailEt, passwordEt, cPasswordEt;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;
    //image picked uri
    private Uri image_uri;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_manager);

        //init UI views
        ImageButton backBtn = findViewById(R.id.backBtn);
        profileIv = findViewById(R.id.profileIv);
        Button registerBtn = findViewById(R.id.registerBtn);
        TextView noAccountManagerTv = findViewById(R.id.noAccountManagerTv);

        surNameEt = findViewById(R.id.surNameEt);
        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        phoneEt = findViewById(R.id.phoneEt);
        userNameEt = findViewById(R.id.userNameEt);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        cPasswordEt = findViewById(R.id.cPasswordEt);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        // permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        backBtn.setOnClickListener(v -> onBackPressed());

        profileIv.setOnClickListener(v -> {
            //pick image
            showImagePickDialog();
        });

        registerBtn.setOnClickListener(v -> {
            //register user
            inputData();
        });

        noAccountManagerTv.setOnClickListener(v -> startActivity(new Intent(RegisterManagerActivity.this, SignUpActivity.class)));

    }

    private String surName, firstName, lastName, phoneNo, userName, email, password;

    private void inputData() {
        //input data
        surName = surNameEt.getText().toString().trim();
        firstName = firstNameEt.getText().toString().trim();
        lastName = lastNameEt.getText().toString().trim();
        phoneNo = phoneEt.getText().toString().trim();
        userName = userNameEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        String confirmPassword = cPasswordEt.getText().toString().trim();

        //validate data

        if (TextUtils.isEmpty(surName)) {
            Toast.makeText(this, "Enter surname...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Enter first name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Enter last name...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(this, "Enter phone number...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Enter username...", Toast.LENGTH_SHORT).show();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email pattern...", Toast.LENGTH_SHORT).show();
        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 characters long...", Toast.LENGTH_SHORT).show();
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password doesn't match...", Toast.LENGTH_SHORT).show();
        }

        createAccount();
    }

    private void createAccount() {
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            //account created
            saveFirebaseData();
            Toast.makeText(RegisterManagerActivity.this, "account created", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            // failed creating account
            progressDialog.dismiss();
            Toast.makeText(RegisterManagerActivity.this, "failed creating account" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void saveFirebaseData() {
        progressDialog.setMessage("Saving Account info...");

        final String timestamp = "" + System.currentTimeMillis();

        if (image_uri == null) {
            //save info without image

            //setup data to save
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid", "" + firebaseAuth.getUid());
            hashMap.put("email", "" + email);
            hashMap.put("surname", "" + surName);
            hashMap.put("firstname", "" + firstName);
            hashMap.put("lastname", "" + lastName);
            hashMap.put("phone", "" + phoneNo);
            hashMap.put("username", "" + userName);
            hashMap.put("timestamp", "" + timestamp);
            hashMap.put("accountType", "Manager");
            hashMap.put("online", "true");
            hashMap.put("profileImage", "");

            // save to db

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap).addOnSuccessListener(unused -> {
                        // db updated
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterManagerActivity.this, LoginActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // failed updating db
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterManagerActivity.this, RegisterManagerActivity.class));
                        finish();
                    });
        } else {
            //save info with image

            // name and path of image
            String filepathAndName = "profile_images/" + "" + firebaseAuth.getUid();
            //upload image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filepathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // get url of uploaded image
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        //noinspection StatementWithEmptyBody
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadImageUri = uriTask.getResult();

                        if (uriTask.isSuccessful()) {

                            //setup data to save
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("uid", "" + firebaseAuth.getUid());
                            hashMap.put("email", "" + email);
                            hashMap.put("surname", "" + surName);
                            hashMap.put("firstname", "" + firstName);
                            hashMap.put("lastname", "" + lastName);
                            hashMap.put("phone", "" + phoneNo);
                            hashMap.put("username", "" + userName);
                            hashMap.put("timestamp", "" + timestamp);
                            hashMap.put("accountType", "Manager");
                            hashMap.put("online", "true");
                            hashMap.put("profileImage", "" + downloadImageUri);

                            // save to db

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap).addOnSuccessListener(unused -> {
                                        // db updated
                                        progressDialog.dismiss();
                                        startActivity(new Intent(RegisterManagerActivity.this, LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        // failed updating db
                                        progressDialog.dismiss();
                                        startActivity(new Intent(RegisterManagerActivity.this, RegisterManagerActivity.class));
                                        finish();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterManagerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showImagePickDialog() {
        //options to display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        //camera clicked
                        if (checkCameraPermission()) {
                            //camera permissions allowed
                            pickFromCamera();

                        } else {
                            //not allowed, request
                            requestCameraPermission();

                        }

                    } else {
                        //gallery clicked
                        if (checkStoragePermission()) {
                            //storage permissions allowed
                            pickFromGallery();

                        } else {
                            //not allowed, request
                            requestStoragePermission();

                        }

                    }

                })
                .show();

    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission() {

        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);


    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        //permission allowed
                        pickFromCamera();

                    } else {
                        //permission denied
                        Toast.makeText(this, "Camera permissions are necessary...", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //permission allowed
                        pickFromGallery();

                    } else {
                        //permission denied
                        Toast.makeText(this, "Storage permission is necessary...", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                image_uri = Objects.requireNonNull(data).getData();

                profileIv.setImageURI(image_uri);

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                profileIv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
}
