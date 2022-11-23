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
import com.yegonron.rugbylms.adapters.TeamAdminAdapter;
import com.yegonron.rugbylms.models.TeamModel;

import java.util.ArrayList;

public class UpdatePlayerAttendanceActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    TeamAdminAdapter teamAdminAdapter;
    RecyclerView.LayoutManager layoutManager;
    final ArrayList<TeamModel> teamModels = new ArrayList<>();

    Toolbar toolbar;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_player_attendance);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        teamAdminAdapter = new TeamAdminAdapter(this, teamModels);
        recyclerView.setAdapter(teamAdminAdapter);
        teamAdminAdapter.setOnItemClickListener(this::gotoItemActivity);

        dbHelper = new DbHelper(this);

        loadData();
        setToolbar();

    }

    private void loadData() {
        dbHelper = new DbHelper(this);

        Cursor cursor = dbHelper.getTeamTable();

        teamModels.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.T_ID));
            String teamName = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.TEAM_NAME_KEY));
            String season = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.SEASON_NAME_KEY));

            teamModels.add(new TeamModel(id, teamName, season));

        }

    }

    private void setToolbar() {

        toolbar = findViewById(R.id.toolbarAdmin);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);

        title.setText("Update Attendance");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);

        back.setOnClickListener(v -> onBackPressed());

    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, AdminAttendanceActivity.class);

        intent.putExtra("teamName", teamModels.get(position).getTeamName());
        intent.putExtra("season", teamModels.get(position).getSeason());
        intent.putExtra("position", position);
        intent.putExtra("tid", teamModels.get(position).getTid());
        startActivity(intent);

    }

    private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.TEAM_ADD_DIALOG);
        dialog.setListener(this::addTeam);

    }

    private void addTeam(String teamName, String season) {
        long tid = dbHelper.addTeam(teamName, season);
        TeamModel teamModel = new TeamModel(tid, teamName, season);
        teamModels.add(teamModel);
        teamAdminAdapter.notifyDataSetChanged();

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
        dbHelper.updateTeam(teamModels.get(position).getTid(), teamName, season);
        teamModels.get(position).setTeamName(teamName);
        teamModels.get(position).setSeason(season);
        teamAdminAdapter.notifyItemChanged(position);

    }

    private void deleteTeam(int position) {
        dbHelper.deleteTeam(teamModels.get(position).getTid());
        teamModels.remove(position);
        teamAdminAdapter.notifyItemRemoved(position);

    }

}