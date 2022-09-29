package com.yegonron.rugbylms;

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

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    final ArrayList<AttendanceItem> attendanceItems;
    final Context context;

    private OnItemClickListener onItemClickListener;

    public  interface OnItemClickListener{
        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AttendanceAdapter(Context context, ArrayList<AttendanceItem> attendanceItems) {
        this.attendanceItems = attendanceItems;
        this.context = context;
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        final TextView roll;
        final TextView name;
        final TextView status;
        final CardView cardView;

        public AttendanceViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(getAdapterPosition(),0,0,"Edit");
            menu.add(getAdapterPosition(),1,0,"Delete");

        }
    }
    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item,parent,false);

        return new AttendanceViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {

        holder.roll.setText(attendanceItems.get(position).getRoll()+"");
        holder.name.setText(attendanceItems.get(position).getName());
        holder.status.setText(attendanceItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status = attendanceItems.get(position).getStatus();
        if (status.equals("P"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));

        else if(status.equals("A"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));

        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));

    }

    @Override
    public int getItemCount() {
        return attendanceItems.size();
    }

}
