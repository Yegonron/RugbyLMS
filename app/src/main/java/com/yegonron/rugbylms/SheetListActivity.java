package com.yegonron.rugbylms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    private ListView sheetList;

    private ArrayList<String> listItems = new ArrayList<>();
    private long tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        tid = getIntent().getLongExtra("tid", -1);
        Log.i("1234567890", "onCreate: " + tid);
        loadListItems();
        sheetList = findViewById(R.id.sheetList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.sheet_list, R.id.date_list_item, listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));
    }

    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        Intent intent = new Intent(this, SheetActivity.class);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        intent.putExtra("month", listItems.get(position));

        startActivity(intent);

    }

    private void loadListItems() {
        Cursor cursor = new DbHelper(this).getDistinctMonths(tid);

        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.DATE_KEY));
            listItems.add(date.substring(3));

        }
    }
}