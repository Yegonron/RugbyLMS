package com.yegonron.rugbylms;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserReport extends AppCompatActivity {

    private FirebaseRecyclerAdapter adapter;

    private DatabaseReference database;
    private PieChart pieChart;
    private HorizontalBarChart barChart;
    private FirebaseAuth firebaseAuth;

    ArrayList<BarEntry> barArraylist;
    ArrayList<PieEntry> onlineUsers;
    ArrayList<Integer> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        onlineUsers = new ArrayList<>();
        barArraylist = new ArrayList<>();

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());
        database = FirebaseDatabase.getInstance().getReference();

        colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        pieChart = findViewById(R.id.onlineUsersPieChart);

        loadPieChartData();
        setupPieChart();

        barChart = findViewById(R.id.onlineUsersBarChart);

        loadHorizontalBarChartData();
    }

    private void loadHorizontalBarChartData() {
        String[] labels = new String[]{"Admin", "Manager", "Coach", "Player", "Fan"};
        for (int i = 0; i < labels.length; i++) {
            int finalI = i;
            database.child("Users").orderByChild("accountType").equalTo(labels[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        barArraylist.add(new BarEntry((float) finalI, (float) snapshot.getChildrenCount()));

                        BarDataSet barDataSet = new BarDataSet(barArraylist, "User Groups");
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);
                        barChart.invalidate();

                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawAxisLine(false);
                        xAxis.setDrawGridLines(false);
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                        barDataSet.setColors(colors);
                        barData.setDrawValues(true);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize(16f);

                        barChart.setDrawGridBackground(false);
                        barChart.getDescription().setEnabled(false);

                        Legend legend = pieChart.getLegend();
                        legend.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void loadPieChartData() {
        String[] activity = new String[]{"true", "false"};
        String[] activityLabel = new String[]{"Online", "Offline"};
        Map<String, Integer> typeActivityMap = new HashMap<>();
        for (int i = 0; i < activity.length; i++) {
            int finalI = i;
            database.child("Users").orderByChild("online").equalTo(activity[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        typeActivityMap.put(activityLabel[finalI], Integer.parseInt(String.valueOf(snapshot.getChildrenCount())));
                        onlineUsers.add(new PieEntry(Objects.requireNonNull(typeActivityMap.get(activityLabel[finalI])).floatValue(), activityLabel[finalI]));

                        PieDataSet dataSet = new PieDataSet(onlineUsers, "Users Activity");
                        dataSet.setColors(colors);

                        PieData data = new PieData(dataSet);
                        data.setDrawValues(true);
                        data.setValueTextSize(12f);
                        data.setValueTextColor(Color.BLACK);

                        pieChart.setData(data);
                        pieChart.invalidate();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Activity");
        pieChart.setCenterTextSize(12);
        pieChart.getDescription().setEnabled(false);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
    }
}
