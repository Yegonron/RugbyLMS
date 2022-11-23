
package com.yegonron.rugbylms.adapters;

import android.content.Context;
import android.graphics.Color;
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

public class PlayerCoachAdapter extends RecyclerView.Adapter<PlayerCoachAdapter.PlayerViewHolder> {

    final ArrayList<PlayerModel> playerItems;
    final Context context;

    private PlayerCoachAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(PlayerCoachAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PlayerCoachAdapter(Context context, ArrayList<PlayerModel> playerItems) {
        this.playerItems = playerItems;
        this.context = context;

    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        final TextView roll;
        final TextView name;
        final TextView status;
        final CardView cardView;

        public PlayerViewHolder(@NonNull View itemView, PlayerCoachAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));

        }

    }

    @NonNull
    @Override
    public PlayerCoachAdapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        return new PlayerCoachAdapter.PlayerViewHolder(itemView, onItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull PlayerCoachAdapter.PlayerViewHolder holder, int position) {
        holder.roll.setText(playerItems.get(position).getRoll() + "");
        holder.name.setText(playerItems.get(position).getName());
        holder.status.setText(playerItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));

    }

    private int getColor(int position) {
        String status = playerItems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.present)));

        else if (status.equals("A"))
            return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.absent)));

        return Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(context, R.color.normal)));


    }

    @Override
    public int getItemCount() {
        return playerItems.size();
    }


}