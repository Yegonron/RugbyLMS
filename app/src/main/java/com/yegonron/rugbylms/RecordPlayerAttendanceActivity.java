package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.yegonron.rugbylms.models.AttendanceModel;

import java.util.Objects;

public class RecordPlayerAttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_player_attendance);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent AttendanceModel at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
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
            updateAttendanceUI(currentUser);
            adapter.startListening();

        }

    }

    private void updateAttendanceUI(FirebaseUser currentUser) {

        //create and initialize an instance of Query that retrieves all Users uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("accountType").equalTo("Player");

        // Create and initialize and instance of Recycler Options passing in your model class and
        //Create a snap shot of your model
        FirebaseRecyclerOptions<AttendanceModel> options = new FirebaseRecyclerOptions.Builder<AttendanceModel>().setQuery(query, snapshot -> new AttendanceModel(
                Objects.requireNonNull(snapshot.child("profileImage").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("surname").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("firstname").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("lastname").getValue()).toString())).build();
        // crate a fire base adapter passing in the model, an a View holder
        // Create a  new ViewHolder as a public inner class that extends RecyclerView.Holder, outside the create , start and update the Ui methods.
        //Then implement the methods onCreateViewHolder and onBindViewHolder
        //Complete all the steps in the AttendanceViewHolder before proceeding to  the methods onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<AttendanceModel, RecordPlayerAttendanceActivity.AttendanceViewHolder>(options) {

            @NonNull
            @Override
            public RecordPlayerAttendanceActivity.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_card_items, parent, false);
                return new AttendanceViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecordPlayerAttendanceActivity.AttendanceViewHolder holder, int position, @NonNull AttendanceModel model) {
                // very important for you to get the AttendanceModel key since we will use this to set likes and delete a o particular AttendanceModel
                final String attendance_key = getRef(position).getKey();
                //populate the card views with data
                holder.setProfileImage(getApplicationContext(), model.getProfileImage());
                holder.setName(model.getSurname() + " " + model.getFirstname() + " " + model.getLastname());

                //add  on click listener on the a particular post to  allow opening this post on a different screen
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

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public final ImageView user_image;
        public final TextView userName;

//        public final LinearLayout attendance_layout;

        //Declare a string variable to hold  the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth firebaseAuth;

        //create constructor matching super
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            user_image = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.nameTv);
//            attendance_layout = itemView.findViewById(R.id.linear_layout_attendance);

        }

        // create yos setters, you will use this setter in you onBindViewHolder method
        // setters
        public void setProfileImage(Context cxt, String profileImage) {
            try {
                Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(user_image);
            } catch (Exception e) {
                user_image.setImageResource(R.drawable.profile);
            }

        }

        public void setName(String name) {

            userName.setText(name);

        }

    }


}
