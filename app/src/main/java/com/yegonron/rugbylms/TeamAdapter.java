package com.yegonron.rugbylms;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    final ArrayList<TeamItem> teamItems;
    final Context context;

    private OnItemClickListener onItemClickListener;

    public  interface OnItemClickListener{
        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public TeamAdapter(Context context, ArrayList<TeamItem> teamItems) {
        this.teamItems = teamItems;
        this.context = context;
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        final TextView teamName;
        final TextView season;

        public TeamViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            teamName = itemView.findViewById(R.id.teamNameTv);
            season = itemView.findViewById(R.id.seasonTv);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");

        }
    }
    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_item,parent,false);

        return new TeamViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {

        holder.teamName.setText(teamItems.get(position).getTeamName());
        holder.season.setText(teamItems.get(position).getSeason());

    }

    @Override
    public int getItemCount() {
        return teamItems.size();
    }

}
