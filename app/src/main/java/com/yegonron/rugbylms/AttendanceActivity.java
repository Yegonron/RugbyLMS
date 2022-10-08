package com.yegonron.rugbylms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AttendanceActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String teamName;
    private String season;
    private int tid;
    private TextView subtitle;

    private AttendanceAdapter adapter;
    private final ArrayList<AttendanceItem> attendanceItems = new ArrayList<>();
    private DbHelper dbHelper;

    MyCalender calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        Intent intent = getIntent();
        teamName = intent.getStringExtra("teamName");
        season = intent.getStringExtra("season");
        int position = intent.getIntExtra("position", -1);
        tid = (int) intent.getLongExtra("tid", -1);

        dbHelper = new DbHelper(this);
        calender = new MyCalender();

        setToolbar();
        loadData();

        RecyclerView recyclerView = findViewById(R.id.attendance_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AttendanceAdapter(this, attendanceItems);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(this::changeStatus);

        loadStatusData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getPlayerTable(tid);
        Log.i("1234567890", "loadData: " + tid);
        attendanceItems.clear();
        while (cursor.moveToNext()) {
            long pid = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.P_ID));
            int roll = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.PLAYER_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.PLAYER_NAME_KEY));
            attendanceItems.add(new AttendanceItem(pid, roll, name));

        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = attendanceItems.get(position).getStatus();

        if (status.equals("P")) status = "A";
        else status = "P";

        attendanceItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);

    }

    private void setToolbar() {

        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);

        ImageButton back = toolbar.findViewById(R.id.backBtn);
        ImageButton save = toolbar.findViewById(R.id.saveBt);

        save.setOnClickListener(v -> saveStatus());

        title.setText(teamName);
        subtitle.setText(season + "|" + calender.getDate());

        back.setOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.menu_attendance);
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);

    }

    private void saveStatus() {
        for (AttendanceItem attendanceItem : attendanceItems) {
            String status = attendanceItem.getStatus();
            if (!status.equals("P")) status = "A";
            long value = dbHelper.addStatus(attendanceItem.getPid(), tid, calender.getDate(), status);

            if (value == -1)
                dbHelper.updateStatus(attendanceItem.getPid(), calender.getDate(), status);

        }

    }

    private void loadStatusData() {
        for (AttendanceItem attendanceItem : attendanceItems) {
            String status = dbHelper.getStatus(attendanceItem.getPid(), calender.getDate());
            if (status != null) attendanceItem.setStatus(status);
            else attendanceItem.setStatus("");
        }
        adapter.notifyDataSetChanged();

    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_player) {
            showAddPlayerDialog();

        } else if (menuItem.getItemId() == R.id.show_calender) {
            showCalender();

        } else if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();

        }
        return true;
    }

    private void openSheetList() {
        long[] idArray = new long[attendanceItems.size()];
        String[] nameArray = new String[attendanceItems.size()];
        int[] rollArray = new int[attendanceItems.size()];

        for (int i = 0; i < idArray.length; i++)
            idArray[i] = attendanceItems.get(i).getPid();

        for (int i = 0; i < rollArray.length; i++)
            rollArray[i] = attendanceItems.get(i).getRoll();

        for (int i = 0; i < nameArray.length; i++)
            nameArray[i] = attendanceItems.get(i).getName();

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
        subtitle.setText(season + "|" + calender.getDate());
        loadStatusData();

    }

    private void showAddPlayerDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show();
        dialog.setListener(this::addPlayer);
    }

    private void addPlayer(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long pid = dbHelper.addPlayer(tid, roll, name);
        AttendanceItem attendanceItem = new AttendanceItem(pid, roll, name);
        attendanceItems.add(attendanceItem);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdatePlayerDialog(item.getGroupId());
                break;
            case 1:
                deletePlayer(item.getGroupId());

        }

        return super.onContextItemSelected(item);
    }

    private void showUpdatePlayerDialog(int position) {
        MyDialog dialog = new MyDialog(attendanceItems.get(position).getRoll(), attendanceItems.get(position).getName());
        dialog.show();
        dialog.setListener((roll_string, name) -> updatePlayer(position, name));

    }

    private void updatePlayer(int position, String name) {
        dbHelper.updatePlayer(attendanceItems.get(position).getPid(), name);
        attendanceItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deletePlayer(int position) {
        dbHelper.deletePlayer(attendanceItems.get(position).getPid());
        attendanceItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}