package com.yegonron.rugbylms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yegonron.rugbylms.R;
import com.yegonron.rugbylms.models.TeamModel;

import java.util.ArrayList;

public class TeamCoachAdapter extends RecyclerView.Adapter<TeamCoachAdapter.TeamViewHolder> {

    final ArrayList<TeamModel> teamModels;
    final Context context;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TeamCoachAdapter(Context context, ArrayList<TeamModel> teamModels) {
        this.teamModels = teamModels;
        this.context = context;

    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        final TextView teamName;
        final TextView season;

        public TeamViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            teamName = itemView.findViewById(R.id.team_tv);
            season = itemView.findViewById(R.id.season_tv);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));

        }

    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        holder.teamName.setText(teamModels.get(position).getTeamName());
        holder.season.setText(teamModels.get(position).getSeason());

    }

    @Override
    public int getItemCount() {
        return teamModels.size();
    }


}