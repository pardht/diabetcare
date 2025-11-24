package com.example.diabetcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "alarms";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";
    public static final String COLUMN_KETERANGAN = "keterangan";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RIWAYAT_TABLE = "CREATE TABLE riwayat (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_alarm INTEGER," +
                "tanggal TEXT," +
                "status TEXT," +
                "waktu_konfirmasi TEXT," +
                "FOREIGN KEY(id_alarm) REFERENCES alarms(id))";
        db.execSQL(CREATE_RIWAYAT_TABLE);

        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_HOUR + " INTEGER," +
                COLUMN_MINUTE + " INTEGER," +
                "keterangan TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS riwayat");
        onCreate(db);
    }

    public void resetToDefaultAlarms() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        insertAlarm(new AlarmModel(1, 8, 0, "Pagi hari"));
        insertAlarm(new AlarmModel(2, 20, 0, "Malam hari"));
        db.close();
    }

    public void insertRiwayat(int idAlarm, String tanggal, String status, String waktuKonfirmasi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_alarm", idAlarm);
        values.put("tanggal", tanggal);
        values.put("status", status);
        values.put("waktu_konfirmasi", waktuKonfirmasi);
        db.insert("riwayat", null, values);
        db.close();
    }

    public void insertAlarm(AlarmModel alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, alarm.id);
        values.put(COLUMN_HOUR, alarm.hour);
        values.put(COLUMN_MINUTE, alarm.minute);
        values.put("keterangan", alarm.keterangan);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<AlarmModel> getAllAlarms() {
        List<AlarmModel> alarms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int hour = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE));
                String keterangan = cursor.getString(cursor.getColumnIndexOrThrow("keterangan"));
                alarms.add(new AlarmModel(id, hour, minute, keterangan));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alarms;
    }

    public void updateAlarm(AlarmModel alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOUR, alarm.hour);
        values.put(COLUMN_MINUTE, alarm.minute);
        values.put("keterangan", alarm.keterangan);
        db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(alarm.id)});
        db.close();
    }

    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean hasRespondedToday(int alarmId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM riwayat WHERE id_alarm = ? AND tanggal = ?",
                new String[]{String.valueOf(alarmId), today}
        );

        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0;
        }

        cursor.close();
        db.close();
        return exists;
    }

    public List<HistoryModel> getHistoryGroupedByDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<HistoryModel> result = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT tanggal, id_alarm, status, waktu_konfirmasi FROM riwayat ORDER BY tanggal DESC, waktu_konfirmasi ASC",
                null
        );

        Map<String, HistoryModel> map = new LinkedHashMap<>();

        if (cursor.moveToFirst()) {
            do {
                String tanggal = cursor.getString(0);
                int idAlarm = cursor.getInt(1);
                String status = cursor.getString(2);
                String waktu = cursor.getString(3);

                if (!map.containsKey(tanggal)) {
                    map.put(tanggal, new HistoryModel(tanggal));
                }

                String detail = "Jadwal " + idAlarm + " → " + status + ", " + waktu;
                map.get(tanggal).responList.add(detail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        result.addAll(map.values());
        return result;
    }
}
