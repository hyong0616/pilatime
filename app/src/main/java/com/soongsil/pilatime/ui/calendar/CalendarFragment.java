package com.soongsil.pilatime.ui.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.soongsil.pilatime.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;
    public CalendarView calendarView;
    public TextView dateTextView, contentTextView;

    //현재 날짜
    long now = System.currentTimeMillis();
    Date mDate = new Date(now);
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd");
    String date = mFormat.format(mDate);

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.admin_calendar);
        dateTextView = view.findViewById(R.id.textView_date);
        contentTextView = view.findViewById(R.id.textView_content);
        dateTextView.setText(date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dateTextView.setText(String.format("%d/%d/%d",year,month+1,dayOfMonth));
                dateTextView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }



}