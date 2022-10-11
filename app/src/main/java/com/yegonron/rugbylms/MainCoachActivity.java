package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class MainCoachActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView nameTv, emailTv, phoneTv;
    private ImageView profileIv;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private RecyclerView recyclerView;
    private DatabaseReference likesRef;
    // private FirebaseUser mCurrentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Boolean likeChecker = false;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coach);

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        profileIv = findViewById(R.id.profileIv);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent post at the top
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        //get the database reference where you will fetch posts
        // mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        //Initialize the database reference where you will store likes
        likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);
        checkUser();

    }

    @Override
    protected void onStart() {
        //
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            //if user is logged in populate the Ui With card views
            updateUI(currentUser);
            adapter.startListening();

        }

    }

    private void updateUI(FirebaseUser currentUser) {

        //create and initialize an instance of Query that retrieves all posts uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("Posts");
        // Create and initialize and instance of Recycler Options passing in your model class and
        //Create a snap shot of your model
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(query, snapshot -> new Post(
                Objects.requireNonNull(snapshot.child("title").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("desc").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("postImage").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("username").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("profileImage").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("time").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("date").getValue()).toString())).build();
        // crate a fire base adapter passing in the model, an a View holder
        // Create a  new ViewHolder as a public inner class that extends RecyclerView.Holder, outside the create , start and update the Ui methods.
        //Then implement the methods onCreateViewHolder and onBindViewHolder
        //Complete all the steps in the PostViewHolder before proceeding to  the methods onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<Post, MainCoachActivity.PostViewHolder>(options) {

            @NonNull
            @Override
            public MainCoachActivity.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card_items, parent, false);
                return new MainCoachActivity.PostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MainCoachActivity.PostViewHolder holder, int position, @NonNull Post model) {
                // very important for you to get the post key since we will use this to set likes and delete a o particular post
                final String post_key = getRef(position).getKey();
                //populate the card views with data
                holder.setTitle(model.getTitle());
                holder.setDesc(model.getDesc());
                holder.setPostImage(getApplicationContext(), model.getPostImage());
                holder.setUserName(model.getUserName());
                holder.setProfileImage(getApplicationContext(), model.getProfileImage());
                //set a like on a particular post

                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
                holder.setLikeButtonStatus(post_key);
                holder.commentPostButton.setOnClickListener(v -> {
                    Intent singleActivity = new Intent(MainCoachActivity.this, SinglePostActivity.class);
                    singleActivity.putExtra("PostID", post_key);
                    startActivity(singleActivity);

                });
                //add  on click listener on the a particular post to  allow opening this post on a different screen
                holder.post_layout.setOnClickListener(view -> {
                    //launch the screen single post activity on clicking a particular cardview item
                    //create this activity using the empty activity template
                    Intent singleActivity = new Intent(MainCoachActivity.this, SinglePostActivity.class);
                    singleActivity.putExtra("PostID", post_key);
                    startActivity(singleActivity);
                });
                // set the onclick listener on the button for liking a post
                holder.likePostButton.setOnClickListener(v -> {
                    // initialize the like checker to true, we are using this boolean variable to determine if a post has been liked or dislike
                    // we declared this variable on to of our activity class
                    likeChecker = true;
                    //check the currently logged in user using his/her ID
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        currentUserID = user.getUid();
                    } else {
                        Toast.makeText(MainCoachActivity.this, "please login", Toast.LENGTH_SHORT).show();

                    }
                    //Listen to changes in the likes database reference
                    likesRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (likeChecker.equals(true)) {
                                // if the current post has a like, associated to the current logged and the user clicks on it again, remove the like
                                //basically the user is disliking
                                if (dataSnapshot.child(Objects.requireNonNull(post_key)).hasChild(currentUserID)) {
                                    likesRef.child(post_key).child(currentUserID).removeValue();
                                    likeChecker = false;
                                } else {
                                    //here the user is liking, set value on the like
                                    likesRef.child(post_key).child(currentUserID).setValue(true);
                                    likeChecker = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();

        }
    }

    private void makeMeOffline() {
        // after logging out, make user offline
        progressDialog.setMessage("Logging out user...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                .addOnSuccessListener(unused -> {
                    // update successfully
                    firebaseAuth.signOut();
                    checkUser();
                })
                .addOnFailureListener(e -> {
                    //failed updating
                    progressDialog.dismiss();
                    Toast.makeText(MainCoachActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainCoachActivity.this, LoginActivity.class));
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
                            String name = "" + ds.child("surname").getValue() + " " + ds.child("firstname").getValue() + " " + ds.child("lastname").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();

                            nameTv.setText(name);
                            emailTv.setText(email);
                            phoneTv.setText(phone);
                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_white).into(profileIv);

                            } catch (Exception e) {
                                profileIv.setImageResource(R.drawable.ic_person_white);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public final TextView post_title;
        public final TextView post_desc;
        public final ImageView post_image;
        public final TextView postUserName;
        public final ImageView user_image;
        public final TextView postTime;
        public final TextView postDate;
        public final LinearLayout post_layout;
        public final ImageButton likePostButton;
        public final ImageButton commentPostButton;
        public final TextView displayLikes;

        //Declare an int variable to hold the count  of likes
        int countLikes;
        //Declare a string variable to hold  the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth mAuth;
        //Declare a database reference where you are saving  the likes
        final DatabaseReference likesRef;

        //create constructor matching super
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            post_title = itemView.findViewById(R.id.post_title_txtview);
            post_desc = itemView.findViewById(R.id.post_desc_txtview);
            post_image = itemView.findViewById(R.id.post_image);
            postUserName = itemView.findViewById(R.id.post_user);
            user_image = itemView.findViewById(R.id.userImage);
            postTime = itemView.findViewById(R.id.time);
            postDate = itemView.findViewById(R.id.date);
            post_layout = itemView.findViewById(R.id.linear_layout_post);
            likePostButton = itemView.findViewById(R.id.like_button);
            commentPostButton = itemView.findViewById(R.id.comment);
            displayLikes = itemView.findViewById(R.id.likes_display);

            //Initialize a database reference where you will store  the likes
            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        }

        // create yos setters, you will use this setter in you onBindViewHolder method
        public void setTitle(String title) {

            post_title.setText(title);
        }

        public void setDesc(String desc) {

            post_desc.setText(desc);
        }

        public void setPostImage(Context ctx, String postImage) {

            Picasso.get().load(postImage).into(post_image);
        }

        public void setUserName(String userName) {

            postUserName.setText(userName);
        }

        public void setProfileImage(Context context, String profileImage) {
            Picasso.get().load(profileImage).into(user_image);

        }

        public void setTime(String time) {
            postTime.setText(time);
        }

        public void setDate(String date) {
            postDate.setText(date);
        }

        public void setLikeButtonStatus(final String post_key) {
            //we want to know who has like a particular post, so let's get the user using their user_ID
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                currentUserID = user.getUid();
            } else {
                Toast.makeText(MainCoachActivity.this, "please login", Toast.LENGTH_SHORT).show();
            }

            // Listen to changes in the database reference of Likes
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //define post_key in the in the onBindViewHolder method
                    //check if a particular post has been liked
                    if (dataSnapshot.child(post_key).hasChild(currentUserID)) {
                        //if liked get the number of likes
                        countLikes = (int) dataSnapshot.child(post_key).getChildrenCount();
                        //check the image from initiali sislike to like
                        likePostButton.setImageResource(R.drawable.like);
                        // count the like and display them in the textView for likes
                        displayLikes.setText(Integer.toString(countLikes));
                    } else {
                        //If disliked, get the current number of likes
                        countLikes = (int) dataSnapshot.child(post_key).getChildrenCount();
                        // set the image resource as disliked
                        likePostButton.setImageResource(R.drawable.dislike);
                        //display the current number of likes
                        displayLikes.setText(Integer.toString(countLikes));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.action_add:
                Intent postIntent = new Intent(this, PostActivity.class);
                startActivity(postIntent);
                // on clicking log out, log the user out
                break;

            case R.id.profile:
                Intent intent = new Intent(MainCoachActivity.this, ProfileEditCoachActivity.class);
                startActivity(intent);
                break;

            case R.id.recordPlayerAttendance:
                Intent intent1 = new Intent(MainCoachActivity.this, RecordPlayerAttendanceActivity.class);
                startActivity(intent1);
                break;

            case R.id.gameFixtures:
                Intent intent2 = new Intent(MainCoachActivity.this, GameFixturesActivity.class);
                startActivity(intent2);
                break;

            case R.id.leagueTable:
                Intent intent3 = new Intent(MainCoachActivity.this, LeagueTableActivity.class);
                startActivity(intent3);
                break;

            case R.id.livestreamGames:
                Intent intent4 = new Intent(MainCoachActivity.this, LivestreamGamesActivity.class);
                startActivity(intent4);
                break;

            case R.id.settings:
                Intent intent5 = new Intent(MainCoachActivity.this, SettingsActivity.class);
                startActivity(intent5);
                break;

            case R.id.help:
                Intent intent6 = new Intent(MainCoachActivity.this, HelpActivity.class);
                startActivity(intent6);
                break;

            case R.id.About_Us:
                Intent intent7 = new Intent(MainCoachActivity.this, AboutUsActivity.class);
                startActivity(intent7);
                break;

            case R.id.Exit:
                //make offline
                //sign out
                //go to login activity
                makeMeOffline();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
