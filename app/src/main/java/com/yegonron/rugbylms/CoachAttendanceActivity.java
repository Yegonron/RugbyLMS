package com.yegonron.rugbylms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yegonron.rugbylms.adapters.PlayerCoachAdapter;
import com.yegonron.rugbylms.models.PlayerModel;

import java.util.ArrayList;

public class CoachAttendanceActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String teamName;
    private String season;
    private int position;
    private long tid;
    private TextView subtitle;

    private RecyclerView recyclerView;
    private PlayerCoachAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PlayerModel> playerModels = new ArrayList<>();

    private MyCalender calender;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_attendance);

        calender = new MyCalender();
        dbHelper = new DbHelper(this);

        Intent intent = getIntent();
        teamName = intent.getStringExtra("teamName");
        season = intent.getStringExtra("season");
        position = intent.getIntExtra("position", -1);
        tid = intent.getLongExtra("tid", -1);

        setToolbar();
        loadData();

        recyclerView = findViewById(R.id.coach_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerCoachAdapter(this, playerModels);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> changeStatus(position));

        loadStatusData();

    }

    private void loadData() {
        Cursor cursor = dbHelper.getPlayerTable(tid);
        Log.i("1234567890", "loadData: " + tid);
        playerModels.clear();
        while (cursor.moveToNext()) {
            long pid = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.P_ID));
            int roll = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.PLAYER_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.PLAYER_NAME_KEY));
            playerModels.add(new PlayerModel(pid, roll, name));

        }
        cursor.close();

    }

    private void changeStatus(int position) {
        String status = playerModels.get(position).getStatus();

        if (status.equals("P")) status = "A";
        else status = "P";

        playerModels.get(position).setStatus(status);
        adapter.notifyItemChanged(position);

    }

    private void setToolbar() {

        toolbar = findViewById(R.id.toolbarCoach);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText(teamName);
        subtitle.setText(season + " | " + calender.getDate());

        back.setOnClickListener(v -> onBackPressed());
        save.setOnClickListener(v -> saveStatus());

        toolbar.inflateMenu(R.menu.coach_attendance_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));

    }

    private void saveStatus() {

        for (PlayerModel playerModel : playerModels) {
            String status = playerModel.getStatus();
            if (status != "P") status = "A";
            long value = dbHelper.addStatus(playerModel.getPid(), tid, calender.getDate(), status);

            if (value == -1)
                dbHelper.updateStatus(playerModel.getPid(), calender.getDate(), status);

        }

    }

    private void loadStatusData() {
        for (PlayerModel playerModel : playerModels) {
            String status = dbHelper.getStatus(playerModel.getPid(), calender.getDate());
            if (status != null) playerModel.setStatus(status);
            else playerModel.setStatus("");
        }
        adapter.notifyDataSetChanged();

    }

    private boolean onMenuItemClick(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.show_calender) {
            showCalender();

        } else if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();

        }
        return true;

    }

    private void openSheetList() {

        long[] idArray = new long[playerModels.size()];
        String[] nameArray = new String[playerModels.size()];
        int[] rollArray = new int[playerModels.size()];

        for (int i = 0; i < idArray.length; i++)
            idArray[i] = playerModels.get(i).getPid();

        for (int i = 0; i < rollArray.length; i++)
            rollArray[i] = playerModels.get(i).getRoll();

        for (int i = 0; i < nameArray.length; i++)
            nameArray[i] = playerModels.get(i).getName();

        Intent intent = new Intent(this, SheetListActivity.class);
        intent.putExtra("tid", tid);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        startActivity(intent);

    }

    private void showCalender() {
        calender.show(getSupportFragmentManager(), "");
        calender.setOnCalenderOkClickListener(this::onCalenderOkClicked);

    }

    private void onCalenderOkClicked(int year, int month, int day) {
        calender.setDate(year, month, day);
        subtitle.setText(season + " | " + calender.getDate());
        loadStatusData();

    }

}