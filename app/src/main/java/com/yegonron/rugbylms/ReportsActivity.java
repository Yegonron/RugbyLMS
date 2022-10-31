package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.yegonron.rugbylms.models.UserModel;

import java.util.Objects;

public class ReportsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent UserModel at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onStart() {
        //
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            //if user is logged in populate the Ui With card views
            updateUserUI(currentUser);
            adapter.startListening();

        }

    }

    private void updateUserUI(FirebaseUser currentUser) {

        //create and initialize an instance of Query that retrieves all Users uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("accountType").equalTo("Player");

        // Create and initialize and instance of Recycler Options passing in your model class and
        //Create a snap shot of your model
        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>().setQuery(query, snapshot -> new UserModel(
                Objects.requireNonNull(snapshot.child("profileImage").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("accountType").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("surname").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("firstname").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("lastname").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("phone").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("age").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("teamname").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("position").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("kitsize").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("bootsize").getValue()).toString())).build();
        // crate a fire base adapter passing in the model, an a View holder
        // Create a  new ViewHolder as a public inner class that extends RecyclerView.Holder, outside the create , start and update the Ui methods.
        //Then implement the methods onCreateViewHolder and onBindViewHolder
        //Complete all the steps in the UserViewHolder before proceeding to  the methods onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<UserModel, ReportsActivity.UserViewHolder>(options) {

            @NonNull
            @Override
            public ReportsActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_items, parent, false);
                return new ReportsActivity.UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ReportsActivity.UserViewHolder holder, int position, @NonNull UserModel model) {
                // very important for you to get the UserModel key since we will use this to set likes and delete a o particular UserModel
                final String user_key = getRef(position).getKey();
                //populate the card views with data
                holder.setProfileImage(getApplicationContext(), model.getProfileImage());
                holder.setName(model.getSurname() + " " + model.getFirstname() + " " + model.getLastname());
                holder.setAge(model.getAge());
                holder.setPhone(model.getPhone());
                holder.setPosition(model.getPosition());
                holder.setKitsize(model.getKitsize());
                holder.setBootsize(model.getBootsize());

                //add  on click listener on the a particular post to  allow opening this post on a different screen
                holder.user_layout.setOnClickListener(view -> {
                    //launch the screen single post activity on clicking a particular cardview item
                    //create this activity using the empty activity template
                    Intent intent = new Intent(ReportsActivity.this, SingleUserActivity.class);
                    intent.putExtra("UserID", user_key);
                    startActivity(intent);
                });
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {

            adapter.stopListening();

        }

    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public final ImageView user_image;
        public final TextView userName;
        public final TextView userAge;
        public final TextView userPhone;
        public final TextView userPosition;
        public final TextView userBootSize;
        public final TextView userKitSize;

        public final LinearLayout user_layout;

        //Declare a string variable to hold  the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth firebaseAuth;

        //create constructor matching super
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            user_image = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.nameTv);
            userAge = itemView.findViewById(R.id.ageTv);
            userPhone = itemView.findViewById(R.id.phoneTv);
            userPosition = itemView.findViewById(R.id.positionTv);
            userBootSize = itemView.findViewById(R.id.bootSizeTv);
            userKitSize = itemView.findViewById(R.id.kitSizeTv);
            user_layout = itemView.findViewById(R.id.linear_layout_user);

        }

        // create yos setters, you will use this setter in you onBindViewHolder method
        // setters
        public void setProfileImage(Context cxt, String profileImage) {
            Picasso.get().load(profileImage).into(user_image);

        }

        public void setName(String name) {

            userName.setText(name);

        }

        public void setAge(String age) {

            userAge.setText("Age: " + age);
        }

        public void setPhone(String phone) {

            userPhone.setText("Phone: " + phone);
        }

        public void setPosition(String position) {

            userPosition.setText("Position: " + position);

        }

        public void setBootsize(String bootsize) {

            userBootSize.setText("Bootsize: " + bootsize);
        }

        public void setKitsize(String kitsize) {

            userKitSize.setText("kitsize: " + kitsize);

        }

    }
}