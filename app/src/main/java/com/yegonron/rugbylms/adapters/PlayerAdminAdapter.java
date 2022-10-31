package com.yegonron.rugbylms.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yegonron.rugbylms.R;
import com.yegonron.rugbylms.models.PlayerModel;

import java.util.ArrayList;

public class PlayerAdminAdapter extends RecyclerView.Adapter<PlayerAdminAdapter.PlayerViewHolder> {

    ArrayList<PlayerModel> playerItems;
    Context context;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public PlayerAdminAdapter(Context context, ArrayList<PlayerModel> playerItems) {
        this.playerItems = playerItems;
        this.context = context;

    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView roll;
        TextView name;
        TextView status;
        CardView cardView;

        public PlayerViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardView);
//            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 0, 0, "Edit");
            menu.add(getAdapterPosition(), 1, 0, "Delete");

        }
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        return new PlayerViewHolder(itemView, onItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        holder.roll.setText(playerItems.get(position).getRoll() + "");
        holder.name.setText(playerItems.get(position).getName());
        holder.status.setText(playerItems.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return playerItems.size();
    }


}
