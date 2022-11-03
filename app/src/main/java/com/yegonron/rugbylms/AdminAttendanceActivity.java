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

import com.yegonron.rugbylms.adapters.PlayerAdminAdapter;
import com.yegonron.rugbylms.models.PlayerModel;

import java.util.ArrayList;

public class AdminAttendanceActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String teamName;
    private String season;
    private int position;
    private long tid;
    private TextView subtitle;

    private RecyclerView recyclerView;
    private PlayerAdminAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PlayerModel> playerModels = new ArrayList<>();

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance);

        dbHelper = new DbHelper(this);

        Intent intent = getIntent();
        teamName = intent.getStringExtra("teamName");
        season = intent.getStringExtra("season");
        position = intent.getIntExtra("position", -1);
        tid = intent.getLongExtra("tid", -1);

        setToolbar();
        loadData();

        recyclerView = findViewById(R.id.admin_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayerAdminAdapter(this, playerModels);
        recyclerView.setAdapter(adapter);

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

    private void setToolbar() {

        toolbar = findViewById(R.id.toolbarAdmin);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);

        title.setText(teamName);
        subtitle.setText(season);

        back.setOnClickListener(v -> onBackPressed());

        toolbar.inflateMenu(R.menu.admin_attendance_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));

    }

    private boolean onMenuItemClick(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.add_player) {
            showAddPlayerDialog();

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

    private void showAddPlayerDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.PLAYER_ADD_DIALOG);
        dialog.setListener((roll, name) -> addPlayer(roll, name));

    }

    private void addPlayer(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long pid = dbHelper.addPlayer(tid, roll, name);
        PlayerModel playerModel = new PlayerModel(pid, roll, name);
        playerModels.add(playerModel);
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
        MyDialog dialog = new MyDialog(playerModels.get(position).getRoll(), playerModels.get(position).getName());
        dialog.show(getSupportFragmentManager(), MyDialog.PLAYER_UPDATE_DIALOG);
        dialog.setListener((roll_string, name) -> updatePlayer(position, name));

    }

    private void updatePlayer(int position, String name) {
        dbHelper.updatePlayer(playerModels.get(position).getPid(), name);
        playerModels.get(position).setName(name);
        adapter.notifyItemChanged(position);

    }

    private void deletePlayer(int position) {
        dbHelper.deletePlayer(playerModels.get(position).getPid());
        playerModels.remove(position);
        adapter.notifyItemRemoved(position);

    }
}