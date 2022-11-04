package com.yegonron.rugbylms;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ProfileEditPlayerActivity extends AppCompatActivity {

    private ImageView profileIv;
    private EditText surNameEt, firstNameEt, lastNameEt, dateOfBirthEt, phoneEt, userNameEt;

    final String[] teams = {"Leos", "KCB", "Oilers"};
    final String[] positions = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    final String[] kitSizes = {"XS", "S", "M", "L", "XL", "XXL"};
    final String[] bootSizes = {"4", "4.5", "5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5",
            "10", "10.5", "11", "11.5", "12", "13", "14", "15", "16", "17"};

    AutoCompleteTextView teamNameTv, positionTv, bootSizeTv, kitSizeTv;
    ArrayAdapter<String> adapterItems;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri image_uri;

    DatePickerDialog.OnDateSetListener setListener;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    private CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_player);

        surNameEt = findViewById(R.id.surNameEt);
        firstNameEt = findViewById(R.id.firstNameEt);
        lastNameEt = findViewById(R.id.lastNameEt);
        dateOfBirthEt = findViewById(R.id.dateOfBirthEt);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        phoneEt = findViewById(R.id.phoneEt);
        userNameEt = findViewById(R.id.userNameEt);
        teamNameTv = findViewById(R.id.teamNameTv);
        positionTv = findViewById(R.id.positionTv);
        bootSizeTv = findViewById(R.id.bootSizeTv);
        kitSizeTv = findViewById(R.id.kitSizeTv);

        phoneEt.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        profileIv = findViewById(R.id.profileIv);
        Button updateBtn = findViewById(R.id.updateBtn);
        ImageButton backBtn = findViewById(R.id.backBtn);

        profileIv.setOnClickListener(v -> showImagePickDialog());
        backBtn.setOnClickListener(v -> onBackPressed());
        updateBtn.setOnClickListener(v -> inputData());

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, teams);
        teamNameTv.setAdapter(adapterItems);

        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, positions);
        positionTv.setAdapter(adapterItems);

        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, kitSizes);
        kitSizeTv.setAdapter(adapterItems);

        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, bootSizes);
        bootSizeTv.setAdapter(adapterItems);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateOfBirthEt.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ProfileEditPlayerActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day);

            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();

        });

        setListener = (datePicker, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = day + "/" + month1 + "/" + year1;
            dateOfBirthEt.setText(date);

        };

    }

    private String surName, firstName, lastName, dateOfBirth, code, phone, userName, teamName, position, bootSize, kitSize;

    private void inputData() {
        surName = surNameEt.getText().toString().trim();
        firstName = firstNameEt.getText().toString().trim();
        lastName = lastNameEt.getText().toString().trim();
        dateOfBirth = dateOfBirthEt.getText().toString().trim();
        code = ccp.getSelectedCountryCode();
        phone = phoneEt.getText().toString().trim();
        userName = userNameEt.getText().toString().trim();
        teamName = teamNameTv.getText().toString().trim();
        position = positionTv.getText().toString().trim();
        bootSize = bootSizeTv.getText().toString().trim();
        kitSize = kitSizeTv.getText().toString().trim();

        updateProfile();
    }

    private void updateProfile() {
        progressDialog.setMessage("Updating profile...");
        progressDialog.show();

        if (image_uri == null) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("surname", "" + surName);
            hashMap.put("firstname", "" + firstName);
            hashMap.put("lastname", "" + lastName);
            hashMap.put("dateofbirth", "" + dateOfBirth);
            hashMap.put("countryCode", "" + code);
            hashMap.put("phone", "" + phone);
            hashMap.put("username", "" + userName);
            hashMap.put("teamname", "" + teamName);
            hashMap.put("position", "" + position);
            hashMap.put("bootsize", "" + bootSize);
            hashMap.put("kitsize", "" + kitSize);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                    .addOnSuccessListener(unused -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditPlayerActivity.this, "Profile updated...", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditPlayerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    });
        } else {
            String filePathAndName = "profile_images/" + "" + firebaseAuth.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(image_uri)
                    .addOnSuccessListener(taskSnapshot -> {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        //noinspection StatementWithEmptyBody
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadImageUri = uriTask.getResult();

                        if (uriTask.isSuccessful()) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("surname", "" + surName);
                            hashMap.put("firstname", "" + firstName);
                            hashMap.put("lastname", "" + lastName);
                            hashMap.put("dateofbirth", "" + dateOfBirth);
                            hashMap.put("countryCode", "" + code);
                            hashMap.put("phone", "" + phone);
                            hashMap.put("username", "" + userName);
                            hashMap.put("teamname", "" + teamName);
                            hashMap.put("position", "" + position);
                            hashMap.put("bootsize", "" + bootSize);
                            hashMap.put("kitsize", "" + kitSize);
                            hashMap.put("profileImage", "" + downloadImageUri);


                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                            ref.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                                    .addOnSuccessListener(unused -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileEditPlayerActivity.this, "Profile updated...", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(ProfileEditPlayerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditPlayerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    });
        }
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String surname = "" + ds.child("surname").getValue();
                            String firstname = "" + ds.child("firstname").getValue();
                            String lastname = "" + ds.child("lastname").getValue();
                            String dateofbirth = "" + ds.child("dateofbirth").getValue();
                            String code = "" + ds.child("countryCode").getValue().toString();
                            String phone = "" + ds.child("phone").getValue();
                            String username = "" + ds.child("username").getValue();
                            String teamname = "" + ds.child("teamname").getValue();
                            String position = "" + ds.child("position").getValue();
                            String bootsize = "" + ds.child("bootsize").getValue();
                            String kitsize = "" + ds.child("kitsize").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();

                            surNameEt.setText(surname);
                            firstNameEt.setText(firstname);
                            lastNameEt.setText(lastname);
                            dateOfBirthEt.setText(dateofbirth);
                            ccp.setCountryForPhoneCode(Integer.parseInt(code));
                            phoneEt.setText(phone);
                            userNameEt.setText(username);
                            teamNameTv.setText(teamname);
                            positionTv.setText(position);
                            bootSizeTv.setText(bootsize);
                            kitSizeTv.setText(kitsize);

                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(profileIv);
                            } catch (Exception e) {
                                profileIv.setImageResource(R.drawable.profile);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image:")
                .setItems(options, (dialog, which) -> {

                    if (which == 0) {
                        if (checkCameraPermission()) {
                            pickFromCamera();
                        } else {
                            requestCameraPermission();
                        }
                    } else {
                        if (checksStoragePermission()) {
                            pickFromGallery();
                        } else {
                            requestStoragePermission();
                        }
                    }
                }).show();
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);

    }

    private boolean checksStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Image Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera permissions are necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permissions is necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = Objects.requireNonNull(data).getData();

                profileIv.setImageURI(image_uri);
            } else if
            (requestCode == IMAGE_PICK_CAMERA_CODE) {
                profileIv.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}