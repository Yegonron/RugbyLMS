package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yegonron.rugbylms.models.CommentModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class SinglePostActivity extends AppCompatActivity {

    private ImageView singleImage;
    private TextView singleTitle;
    private TextView singleDesc;
    String post_key = null;
    private DatabaseReference mDatabase, commentRef, mDatabaseUsers;
    private Button deleteBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mCurrentUser;

    EditText makeComment;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;

    //String currentUserID =null;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        singleImage = findViewById(R.id.singleImageview);
        singleTitle = findViewById(R.id.singleTitle);
        singleDesc = findViewById(R.id.singleDesc);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);

        //initialize recyclerview
        recyclerView = findViewById(R.id.comment_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        TextView postComment = findViewById(R.id.postComment);
        makeComment = findViewById(R.id.editTextcomment);

        post_key = getIntent().getExtras().getString("PostID");

        //Initialize the database reference/node where you will be storing posts
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
        //Initialize an instance of  Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        //Initialize the instance of the firebase user
        mCurrentUser = firebaseAuth.getCurrentUser();
        //Get currently logged in user
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        deleteBtn = findViewById(R.id.deleteBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        deleteBtn.setVisibility(View.INVISIBLE);

        deleteBtn.setOnClickListener(view -> {

            AlertDialog.Builder dialog = new AlertDialog.Builder(SinglePostActivity.this);
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting this post will completely remove it" +
                    " from the system and you won't be able to access it.");
            dialog.setPositiveButton("Delete", (dialogInterface, i) -> mDatabase.child(post_key).removeValue());

            dialog.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        });

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("postImage").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                singleTitle.setText(post_title);
                singleDesc.setText(post_desc);
                Picasso.get().load(post_image).into(singleImage);
                if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid().equals(post_uid)) {

                    deleteBtn.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postComment.setOnClickListener(v -> {
            //Lis
            Toast.makeText(SinglePostActivity.this, "Posting...", Toast.LENGTH_LONG).show();
            //get the comment from the edit texts
            final String comment = makeComment.getText().toString().trim();
            //get the date and time of the post

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
            final String saveCurrentDate = currentDate.format(calendar.getTime());

            Calendar calendar1 = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calendar1.getTime());
            // do a check for empty fields
            if (!TextUtils.isEmpty(comment)) {
                final DatabaseReference newComment = commentRef.push();
                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        newComment.child("uid").setValue(mCurrentUser.getUid());
                        newComment.child("comment").setValue(comment);
                        newComment.child("time").setValue(saveCurrentTime);
                        newComment.child("date").setValue(saveCurrentDate);
                        newComment.child("profileImage").setValue(dataSnapshot.child("profileImage").getValue());
                        newComment.child("username").setValue(dataSnapshot.child("username").getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });
    }

    @Override
    protected void onStart() {
        //
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            //if user is logged in populate the Ui With card views
            updateUI();
            adapter.startListening();

        }

    }

    private void updateUI() {
        //create and initialize an instance of Query that retrieves all posts uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("Comments").child(post_key);
        // Create and initialize and instance of Recycler Options passing in your model class and
        //Create a snap shot of your model
        FirebaseRecyclerOptions<CommentModel> options = new FirebaseRecyclerOptions.Builder<CommentModel>().
                setQuery(query, snapshot -> new CommentModel(
                        Objects.requireNonNull(snapshot.child("profileImage").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("comment").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("time").getValue()).toString(),
                        Objects.requireNonNull(snapshot.child("date").getValue()).toString()))
                .build();
        // crate a fire base adapter passing in the model, an a View holder
        // Create a  new ViewHolder as a public inner class that extends RecyclerView.Holder, outside the create , start and update the Ui methods.
        //Then implement the methods onCreateViewHolder and onBindViewHolder
        //Complete all the steps in the AtticViewHolder before proceeding to  the methods onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<CommentModel, commentModelViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull commentModelViewHolder holder, int i, @NonNull CommentModel model) {
                holder.setProfileImage(model.getProfileImage());
                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
                holder.setComment(model.getComment());

            }

            @NonNull
            @Override
            public commentModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
                return new commentModelViewHolder(view);
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

    public static class commentModelViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view

        public final ImageView commenterimage;
        public final TextView commentTime;
        public final TextView commentDate;
        public final TextView the_comment;

        //create constructor matching super
        public commentModelViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects

            commenterimage = itemView.findViewById(R.id.commenterImage);
            commentTime = itemView.findViewById(R.id.commentTime);
            commentDate = itemView.findViewById(R.id.commentDate);
            the_comment = itemView.findViewById(R.id.the_comment);

        }

        // create yos setters, you will use this setter in you onBindViewHolder method
        public void setComment(String comment) {

            the_comment.setText(comment);
        }

        public void setProfileImage(String profileImage) {
            Picasso.get().load(profileImage).into(commenterimage);

        }

        public void setTime(String time) {
            commentTime.setText(time);
        }

        public void setDate(String date) {
            commentDate.setText(date);
        }

    }

}
