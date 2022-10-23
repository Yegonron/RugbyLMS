package com.yegonron.rugbylms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.yegonron.rugbylms.models.FixturesModel;

import java.util.Objects;

public class GameFixturesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    String currentUserID = null;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_fixtures);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        //initialize recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //Reverse  the layout so as to display the most recent FixturesModel at the top
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
            updateFixturesUI(currentUser);
            adapter.startListening();

        }

    }

    private void updateFixturesUI(FirebaseUser currentUser) {

        //create and initialize an instance of Query that retrieves all FixturesModel uploaded
        Query query = FirebaseDatabase.getInstance().getReference().child("FixturesModel");
        // Create and initialize and instance of Recycler Options passing in your model class and
        //Create a snap shot of your model
        FirebaseRecyclerOptions<FixturesModel> options = new FirebaseRecyclerOptions.Builder<FixturesModel>().setQuery(query, snapshot -> new FixturesModel(
                Objects.requireNonNull(snapshot.child("fixtureTitle").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("homeTeam").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("awayTeam").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("fixtureVenue").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("fixtureTime").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("date").getValue()).toString(),
                Objects.requireNonNull(snapshot.child("time").getValue()).toString())).build();
        // crate a fire base adapter passing in the model, an a View holder
        // Create a  new ViewHolder as a public inner class that extends RecyclerView.Holder, outside the create , start and update the Ui methods.
        //Then implement the methods onCreateViewHolder and onBindViewHolder
        //Complete all the steps in the FixturesViewHolder before proceeding to  the methods onCreateViewHolder, and onBindViewHolder
        adapter = new FirebaseRecyclerAdapter<FixturesModel, GameFixturesActivity.FixturesViewHolder>(options) {

            @NonNull
            @Override
            public GameFixturesActivity.FixturesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //inflate the layout where you have the card view items
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fixtures_card_items, parent, false);
                return new GameFixturesActivity.FixturesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull GameFixturesActivity.FixturesViewHolder holder, int position, @NonNull FixturesModel model) {
                // very important for you to get the FixturesModel key since we will use this to set likes and delete a o particular FixturesModel
                final String fixture_key = getRef(position).getKey();
                //populate the card views with data
                holder.setFixtureTitle(model.getFixtureTitle());
                holder.setHomeTeam(model.getHomeTeam());
                holder.setAwayTeam(model.getAwayTeam());
                holder.setAwayTeam(model.getAwayTeam());
                holder.setFixtureVenue(model.getFixtureVenue());
                holder.setFixtureTime(model.getFixtureTime());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());

                //add  on click listener on the a particular post to  allow opening this post on a different screen
                holder.fixture_layout.setOnClickListener(view -> {
                    //launch the screen single post activity on clicking a particular cardview item
                    //create this activity using the empty activity template
                    Intent singleActivity = new Intent(GameFixturesActivity.this, SingleFixtureActivity.class);
                    singleActivity.putExtra("FixtureID", fixture_key);
                    startActivity(singleActivity);
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

    public class FixturesViewHolder extends RecyclerView.ViewHolder {
        //Declare the view objects in the card view
        public final TextView title;
        public final TextView hTeam;
        public final TextView aTeam;
        public final TextView fVenue;
        public final TextView fTime;
        public final TextView pDate;
        public final TextView pTime;

        public final LinearLayout fixture_layout;

        //Declare a string variable to hold  the user ID of currently logged in user
        String currentUserID;
        //Declare an instance of firebase authentication
        FirebaseAuth firebaseAuth;

        //create constructor matching super
        public FixturesViewHolder(@NonNull View itemView) {
            super(itemView);
            //Initialize the card view item objects
            title = itemView.findViewById(R.id.fixtureTitleTv);
            hTeam = itemView.findViewById(R.id.homeTeamTv);
            aTeam = itemView.findViewById(R.id.awayTeamTv);
            fVenue = itemView.findViewById(R.id.fixtureVenueTv);
            fTime = itemView.findViewById(R.id.fixtureTimeTv);
            pDate = itemView.findViewById(R.id.date);
            pTime = itemView.findViewById(R.id.time);
            fixture_layout = itemView.findViewById(R.id.linear_layout_fixture);

        }

        // create yos setters, you will use this setter in you onBindViewHolder method
        // setters
        public void setFixtureTitle(String fixtureTitle) {

            title.setText("Fixture Title: " + fixtureTitle);
        }

        public void setHomeTeam(String homeTeam) {

            hTeam.setText("Home Team: " + homeTeam);
        }

        public void setAwayTeam(String awayTeam) {

            aTeam.setText("Away Team: " + awayTeam);
        }

        public void setFixtureVenue(String fixtureVenue) {

            fVenue.setText("Fixture Venue: " + fixtureVenue);
        }

        public void setFixtureTime(String fixtureTime) {

            fTime.setText("Fixture Time: " + fixtureTime);
        }

        public void setDate(String date) {

            pDate.setText(date);
        }

        public void setTime(String time) {

            pTime.setText(time);
        }

    }

}