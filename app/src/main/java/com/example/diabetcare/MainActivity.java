package com.example.diabetcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnSchedule, btnLog;
    private RecyclerView recyclerCheck;
    private DbHelper dbHelper;

    private boolean isWithinWindow(int hour, int minute) {
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, hour);
        target.set(Calendar.MINUTE, minute);
        target.set(Calendar.SECOND, 0);
        target.set(Calendar.MILLISECOND, 0);

        long diff = Math.abs(now.getTimeInMillis() - target.getTimeInMillis());
        return diff <= 2 * 60 * 60 * 1000; // 2 jam
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });btnSchedule = findViewById(R.id.btn_schedule);
        btnLog = findViewById(R.id.btn_log);
        recyclerCheck = findViewById(R.id.recyclerCheck);
        dbHelper = new DbHelper(this);

        btnSchedule.setOnClickListener(v -> schedule());
        btnLog.setOnClickListener(v -> medicineLog());

        List<AlarmModel> alarms = dbHelper.getAllAlarms();
        recyclerCheck.setLayoutManager(new LinearLayoutManager(this));
        recyclerCheck.setAdapter(new CheckAlarmAdapter(this, alarms, dbHelper));


    }

    @Override
    protected void onResume() {
        super.onResume();
        List<AlarmModel> alarms = dbHelper.getAllAlarms();
        recyclerCheck.setAdapter(new CheckAlarmAdapter(this, alarms, dbHelper));
    }

    private void medicineLog() {
        Intent goLog = new Intent(this, LogActivity.class);
        startActivity(goLog);
    }

    private void schedule() {
        Intent goSchedule = new Intent(this, ScheduleActivity.class);
        startActivity(goSchedule);
    }
}