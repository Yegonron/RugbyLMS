package com.yegonron.rugbylms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yegonron.rugbylms.adapters.TeamCoachAdapter;
import com.yegonron.rugbylms.models.TeamModel;

import java.util.ArrayList;

public class RecordPlayerAttendanceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TeamCoachAdapter teamCoachAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<TeamModel> teamModels = new ArrayList<>();

    Toolbar toolbar;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_player_attendance);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        teamCoachAdapter = new TeamCoachAdapter(this, teamModels);
        recyclerView.setAdapter(teamCoachAdapter);
        teamCoachAdapter.setOnItemClickListener(position -> gotoItemActivity(position));

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

        toolbar = findViewById(R.id.toolbarCoach);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText("Record Attendance");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
        save.setVisibility(View.INVISIBLE);

        back.setOnClickListener(v -> onBackPressed());

    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, CoachAttendanceActivity.class);

        intent.putExtra("teamName", teamModels.get(position).getTeamName());
        intent.putExtra("season", teamModels.get(position).getSeason());
        intent.putExtra("position", position);
        intent.putExtra("tid", teamModels.get(position).getTid());
        startActivity(intent);

    }

}
