package com.example.diabetcare;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {
    private List<HistoryModel> data;
    private Context context;

    public HistoryAdapter(Context context, List<HistoryModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() { return data.size(); }

    @Override
    public Object getItem(int position) { return data.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txt;
        if (convertView == null) {
            txt = new TextView(context);
            txt.setPadding(16, 16, 16, 16);
        } else {
            txt = (TextView) convertView;
        }

        HistoryModel item = data.get(position);
        StringBuilder sb = new StringBuilder();
        sb.append("Tanggal: ").append(item.tanggal).append("\n");
        for (String detail : item.responList) {
            sb.append("  - ").append(detail).append("\n");
        }

        txt.setText(sb.toString().trim());
        return txt;
    }

}
