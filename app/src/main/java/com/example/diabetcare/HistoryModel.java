package com.example.diabetcare;

import java.util.ArrayList;
import java.util.List;

public class HistoryModel {
    public String tanggal;
    public List<String> responList;

    public HistoryModel(String tanggal) {
        this.tanggal = tanggal;
        this.responList = new ArrayList<String>();
    }

    public static class ResponObat {
        public int idAlarm;
        public String status;
        public String waktuKonfirmasi;

        public ResponObat(int idAlarm, String status, String waktuKonfirmasi) {
            this.idAlarm = idAlarm;
            this.status = status;
            this.waktuKonfirmasi = waktuKonfirmasi;
        }
    }

}
