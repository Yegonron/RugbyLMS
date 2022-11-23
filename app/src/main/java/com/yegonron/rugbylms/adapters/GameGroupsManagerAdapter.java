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
import com.yegonron.rugbylms.models.FixturesModel;

import java.util.ArrayList;

public class GameGroupsManagerAdapter extends RecyclerView.Adapter<GameGroupsManagerAdapter.GameGroupsAdapterViewHolder> {
    final Context context;
    final ArrayList<String> userGroupManagerList;
    final DatabaseReference database;

    public GameGroupsManagerAdapter(Context context, ArrayList<String> userGroupManagerList, DatabaseReference database) {
        this.context = context;
        this.userGroupManagerList = userGroupManagerList;
        this.database = database;
    }

    @NonNull
    @Override
    public GameGroupsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_groups_layout, parent, false);
        return new GameGroupsAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GameGroupsAdapterViewHolder holder, int position) {

        GameAdapter gameAdapter;
        ArrayList<FixturesModel> list = new ArrayList<>();
        gameAdapter = new GameAdapter(context, list);
        holder.groupName.setText(userGroupManagerList.get(position));
        database.child("Fixtures").orderByChild("homeTeam").equalTo(userGroupManagerList.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FixturesModel fixtureModel = new FixturesModel((String) dataSnapshot.child("fixtureTitle").getValue(), (String) dataSnapshot.child("homeTeam").getValue(), (String) dataSnapshot.child("awayTeam").getValue(), (String) dataSnapshot.child("fixtureVenue").getValue(), (String) dataSnapshot.child("fixtureDate").getValue(), (String) dataSnapshot.child("fixtureTime").getValue(), (String) dataSnapshot.child("date").getValue(), (String) dataSnapshot.child("time").getValue());

                        list.add(fixtureModel);

                    }
                    gameAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(gameAdapter);
    }

    @Override
    public int getItemCount() {
        return userGroupManagerList.size();
    }

    public class GameGroupsAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView groupName;
        final ImageView imageView;
        final CardView cardView;
        final RecyclerView recyclerView;
        final LinearLayout linearLayout;
        final LinearLayout layout;

        public GameGroupsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.lbl_user_group);
            cardView = itemView.findViewById(R.id.usersGroupCardView);
            imageView = itemView.findViewById(R.id.ic_down);
            recyclerView = itemView.findViewById(R.id.users_recyclerView);
            linearLayout = itemView.findViewById(R.id.LL2);
            layout = itemView.findViewById(R.id.users);

            cardView.setOnClickListener(v -> switchVisibility(groupName, cardView, imageView, linearLayout, layout, userGroupManagerList.get(getAdapterPosition()), database, context));
        }
    }

    private void switchVisibility(TextView groupName, CardView cardView, ImageView imageView, LinearLayout linearLayout, LinearLayout layout, String fixtureGroup, DatabaseReference database, Context context) {
        database.child("Fixtures").orderByChild("homeTeam").equalTo(fixtureGroup).addListenerForSingleValueEvent(new ValueEventListener() {
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
