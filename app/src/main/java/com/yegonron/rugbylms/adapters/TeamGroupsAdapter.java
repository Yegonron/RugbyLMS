package com.yegonron.rugbylms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yegonron.rugbylms.R;
import com.yegonron.rugbylms.models.User;

import java.util.ArrayList;
import java.util.Objects;

public class TeamGroupsAdapter extends RecyclerView.Adapter<TeamGroupsAdapter.TeamListAdapterViewHolder> {
    Context context;
    ArrayList<String> teamGroupList;
    DatabaseReference database;

    public TeamGroupsAdapter(Context context, ArrayList<String> teamGroupList, DatabaseReference database) {
        this.context = context;
        this.teamGroupList = teamGroupList;
        this.database = database;
    }

    @NonNull
    @Override
    public TeamListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_groups_layout, parent, false);
        return new TeamListAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListAdapterViewHolder holder, int position) {

        TeamAdapter teamAdapter;
        ArrayList<User> list = new ArrayList<>();
        teamAdapter = new TeamAdapter(context, list);
        holder.groupName.setText(teamGroupList.get(position));
        database.child("Users").orderByChild("teamname").equalTo(teamGroupList.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = new User((String) dataSnapshot.child("email").getValue(),
                                Objects.requireNonNull(dataSnapshot.child("profileImage").getValue()).toString(), dataSnapshot.child("surname").getValue() + " " + dataSnapshot.child("firstname").getValue() + " " + dataSnapshot.child("lastname").getValue());

                        list.add(user);

                    }
                    teamAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(teamAdapter);
    }

    @Override
    public int getItemCount() {
        return teamGroupList.size();
    }

    public class TeamListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        ImageView imageView;
        CardView cardView;
        RecyclerView recyclerView;
        LinearLayout linearLayout, layout;

        public TeamListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.lbl_user_group);
            cardView = itemView.findViewById(R.id.usersGroupCardView);
            imageView = itemView.findViewById(R.id.ic_down);
            recyclerView = itemView.findViewById(R.id.users_recyclerView);
            linearLayout = itemView.findViewById(R.id.LL2);
            layout = itemView.findViewById(R.id.users);

            cardView.setOnClickListener(v -> switchVisibility(groupName, cardView, imageView, linearLayout, layout, teamGroupList.get(getAdapterPosition()), database, context));
        }
    }

    private void switchVisibility(TextView groupName, CardView cardView, ImageView imageView, LinearLayout linearLayout, LinearLayout layout, String teamGroup, DatabaseReference database, Context context) {
        database.child("Users").orderByChild("teamname").equalTo(teamGroup).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (layout.getVisibility() == View.GONE) {
                        cardView.setElevation(10);
                        groupName.setBackgroundColor(context.getResources().getColor(android.R.color.black));
                        groupName.setTextColor(context.getResources().getColor(android.R.color.white));
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_up));
                        imageView.setBackgroundColor(context.getResources().getColor(android.R.color.black));
                        layout.setVisibility(View.VISIBLE);
                    } else {
                        cardView.setElevation(5);
                        groupName.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                        groupName.setTextColor(context.getResources().getColor(android.R.color.black));
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_down));
                        imageView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                        layout.setVisibility(View.GONE);
                    }
                } else {
                    if (linearLayout.getVisibility() == View.GONE) {
                        cardView.setElevation(10);
                        groupName.setBackgroundColor(context.getResources().getColor(android.R.color.black));
                        groupName.setTextColor(context.getResources().getColor(android.R.color.white));
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_up));
                        imageView.setBackgroundColor(context.getResources().getColor(android.R.color.black));
                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
                        cardView.setElevation(5);
                        groupName.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                        groupName.setTextColor(context.getResources().getColor(android.R.color.black));
                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_down));
                        imageView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                        linearLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
