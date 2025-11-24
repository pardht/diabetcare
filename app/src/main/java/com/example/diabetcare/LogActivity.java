package com.example.diabetcare;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.diabetcare.databinding.ActivityLogBinding;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.Arrays;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private ActivityLogBinding binding;



    private final List<BarEntry> barEntries = Arrays.asList(
            new BarEntry(0,10),
            new BarEntry(1,5),
            new BarEntry(2,6),
            new BarEntry(3,0),
            new BarEntry(4,1),
            new BarEntry(5,8),
            new BarEntry(6,2)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log);
        binding = ActivityLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBarChart();



        ListView listView = findViewById(R.id.list_of_history);
        DbHelper dbHelper = new DbHelper(this);
        List<HistoryModel> history = dbHelper.getHistoryGroupedByDate();

        HistoryAdapter adapter = new HistoryAdapter(this, history);
        listView.setAdapter(adapter);
    }

    private void setupBarChart() {
        BarDataSet dataSet = new BarDataSet(barEntries, "Bar Data");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));
        BarData data = new BarData(dataSet);
        binding.barChart.setData(data);
        binding.barChart.invalidate();

    }
}