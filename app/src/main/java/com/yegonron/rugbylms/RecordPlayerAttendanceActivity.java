package com.yegonron.rugbylms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecordPlayerAttendanceActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    TeamAdapter teamAdapter;
    RecyclerView.LayoutManager layoutManager;
    final ArrayList<TeamItem> teamItems = new ArrayList<>();
    Toolbar toolbar;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_player_attendance);

        ImageButton backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> onBackPressed());

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> {
            showDialog();
        });

        loadData();

        dbHelper = new DbHelper(this);

        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        teamAdapter = new TeamAdapter(this, teamItems);
        recyclerView.setAdapter(teamAdapter);
        teamAdapter.setOnItemClickListener(this::gotoItemActivity);

        setToolbar();

    }

    private void loadData() {
        dbHelper = new DbHelper(this);

        Cursor cursor = dbHelper.getTeamTable();

        teamItems.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.T_ID));
            String teamName = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.TEAM_NAME_KEY));
            String season = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.SEASON_NAME_KEY));

            teamItems.add(new TeamItem(id, teamName, season));

        }

    }

    private void setToolbar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.backBtn);
        ImageButton save = toolbar.findViewById(R.id.saveBt);

        title.setText("Attendance");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, AttendanceActivity.class);

        intent.putExtra("teamName", teamItems.get(position).getTeamName());
        intent.putExtra("season", teamItems.get(position).getSeason());
        intent.putExtra("position", position);
        intent.putExtra("tid", teamItems.get(position).getTid());
        startActivity(intent);

    }

    private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.TEAM_ADD_DIALOG);
        dialog.setListener(this::addTeam);

    }

    private void addTeam(String teamName, String season) {
        long tid = dbHelper.addTeam(teamName, season);

        TeamItem teamItem = new TeamItem(tid, teamName, season);
        teamItems.add(teamItem);
        teamAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteTeam(item.getGroupId());
        }

        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.TEAM_UPDATE_DIALOG);
        dialog.setListener((teamName, season) -> updateTeam(position, teamName, season));
    }

    private void updateTeam(int position, String teamName, String season) {
        dbHelper.updateTeam(teamItems.get(position).getTid(), teamName, season);
        teamItems.get(position).setTeamName(teamName);
        teamItems.get(position).setSeason(season);
        teamAdapter.notifyItemChanged(position);
    }

    private void deleteTeam(int position) {
        dbHelper.deleteTeam(teamItems.get(position).getTid());
        teamItems.remove(position);
        teamAdapter.notifyItemRemoved(position);
    }
}