package com.yegonron.rugbylms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yegonron.rugbylms.R;
import com.yegonron.rugbylms.models.FixturesModel;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {
    final Context context;
    final ArrayList<FixturesModel> list;

    public GameAdapter(Context context, ArrayList<FixturesModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fixtures_report_card_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        FixturesModel fixturesModel = list.get(position);
        holder.title.setText(fixturesModel.getFixtureTitle());
        holder.fVenue.setText(fixturesModel.getFixtureVenue());
        holder.fDate.setText(fixturesModel.getFixtureDate());
        holder.fTime.setText(fixturesModel.getFixtureTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView fVenue;
        final TextView fDate;
        final TextView fTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.fixtureTitleTv);
            fVenue = itemView.findViewById(R.id.fixtureVenueTv);
            fDate = itemView.findViewById(R.id.fixtureDateTv);
            fTime = itemView.findViewById(R.id.fixtureTimeTv);

        }
    }
}
