package com.example.diabetcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MedicineCheck extends AppCompatActivity {

    Button btnYes, btnNo;
    int alarmId;
    TextView txtJadwal;
    DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_check);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        alarmId = getIntent().getIntExtra("alarm_id", -1); // gunakan variabel global
        int jadwalJam = getIntent().getIntExtra("jadwal_jam", -1);
        int jadwalMenit = getIntent().getIntExtra("jadwal_menit", -1);

        txtJadwal = findViewById(R.id.txt_jadwal);
        txtJadwal.setText("id :" + alarmId + "Jadwal: " + String.format("%02d:%02d", jadwalJam, jadwalMenit));

        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        dbHelper = new DbHelper(this);

        btnYes.setOnClickListener(v -> simpanRiwayat("sudah"));
        btnNo.setOnClickListener(v -> simpanRiwayat("belum"));
    }

    private void simpanRiwayat(String status) {
        String tanggal = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String waktu = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        dbHelper.insertRiwayat(alarmId, tanggal, status, waktu);
        Toast.makeText(this, "Riwayat disimpan", Toast.LENGTH_SHORT).show();
        finish();
    }
}
