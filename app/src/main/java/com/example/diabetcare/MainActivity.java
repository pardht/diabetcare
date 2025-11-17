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

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnSchedule, btnLog;

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
        });



        btnSchedule = findViewById(R.id.btn_schedule);
        btnLog = findViewById(R.id.btn_log);

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {schedule();}
        });
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicineLog();
            }
        });

        Button btnCheck1 = findViewById(R.id.btn_check_alarm1);
        Button btnCheck2 = findViewById(R.id.btn_check_alarm2);

        DbHelper dbHelper = new DbHelper(this);
        dbHelper.initializeDefaultAlarms(8, 0, 20, 0); // atau gunakan input dari pengguna
        List<AlarmModel> alarms = dbHelper.getAllAlarms();

        for (AlarmModel alarm : alarms) {
            boolean inWindow = isWithinWindow(alarm.hour, alarm.minute);
            boolean hasResponded = dbHelper.hasRespondedToday(alarm.id);
            Button targetBtn = (alarm.id == 1) ? btnCheck1 : btnCheck2;

            if (hasResponded) {
                targetBtn.setEnabled(false);
                targetBtn.setText("Sudah dikonfirmasi (" + String.format("%02d:%02d", alarm.hour, alarm.minute) + ")");
            } else if (inWindow) {
                targetBtn.setEnabled(true);
                targetBtn.setText("Cek Obat (" + String.format("%02d:%02d", alarm.hour, alarm.minute) + ")");
                targetBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MedicineCheck.class);
                    intent.putExtra("alarm_id", alarm.id);
                    intent.putExtra("jadwal_jam", alarm.hour);
                    intent.putExtra("jadwal_menit", alarm.minute);
                    startActivity(intent);
                });
            } else {
                targetBtn.setEnabled(false);
                targetBtn.setText("Diluar waktu cek (" + String.format("%02d:%02d", alarm.hour, alarm.minute) + ")");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCheckButtons();
    }

    private void updateCheckButtons() {
        Button btnCheck1 = findViewById(R.id.btn_check_alarm1);
        Button btnCheck2 = findViewById(R.id.btn_check_alarm2);

        DbHelper dbHelper = new DbHelper(this);
        List<AlarmModel> alarms = dbHelper.getAllAlarms();

        for (AlarmModel alarm : alarms) {
            boolean inWindow = isWithinWindow(alarm.hour, alarm.minute);
            boolean hasResponded = dbHelper.hasRespondedToday(alarm.id);
            Button targetBtn = (alarm.id == 1) ? btnCheck1 : btnCheck2;

            if (hasResponded) {
                targetBtn.setEnabled(false);
                targetBtn.setText("Sudah dikonfirmasi (" + String.format("%02d:%02d", alarm.hour, alarm.minute) + ")");
            } else if (inWindow) {
                targetBtn.setEnabled(true);
                targetBtn.setText("Cek Obat (" + String.format("%02d:%02d", alarm.hour, alarm.minute) + ")");
                targetBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(this, MedicineCheck.class);
                    intent.putExtra("alarm_id", alarm.id);
                    intent.putExtra("jadwal_jam", alarm.hour);
                    intent.putExtra("jadwal_menit", alarm.minute);
                    startActivity(intent);
                });
            } else {
                targetBtn.setEnabled(false);
                targetBtn.setText("Diluar waktu cek (" + String.format("%02d:%02d", alarm.hour, alarm.minute) + ")");
            }
        }
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