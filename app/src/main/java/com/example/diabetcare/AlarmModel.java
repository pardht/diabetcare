package com.example.diabetcare;

public class AlarmModel {
    public int id;
    public int hour;
    public int minute;
    public String keterangan;

    public AlarmModel(int id, int hour, int minute, String keterangan) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.keterangan = keterangan;
    }
}
