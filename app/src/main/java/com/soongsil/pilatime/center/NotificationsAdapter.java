package com.soongsil.pilatime.center;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soongsil.pilatime.R;

import java.util.ArrayList;

public class NotificationsAdapter extends BaseAdapter {

    private ArrayList<Notifications> notificationsList = new ArrayList<Notifications>();

    public NotificationsAdapter(){

    }

    @Override
    public int getCount() {
        return notificationsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Notifications getItem(int position) {
        return notificationsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_notifications, parent, false);
        }

        TextView idxTextView    = (TextView) convertView.findViewById(R.id.textView1);
        TextView dateTextView   = (TextView) convertView.findViewById(R.id.textView2);
        TextView titleTextView  = (TextView) convertView.findViewById(R.id.textView3);
        TextView writerTextView = (TextView) convertView.findViewById(R.id.textView4);

        Notifications notifications = notificationsList.get(position);
        idxTextView.setText(notifications.getIdx());
        dateTextView.setText(notifications.getDate());
        titleTextView.setText(notifications.getTitle());
        writerTextView.setText(notifications.getWriter());

        return convertView;
    }

    public void addItem(String idx, String date, String title, String writer) {

        Notifications notifications = new Notifications();
        notifications.setIdx(idx);
        notifications.setDate(date);
        notifications.setTitle(title);
        notifications.setWriter(writer);

        notificationsList.add(notifications);

    }

}
