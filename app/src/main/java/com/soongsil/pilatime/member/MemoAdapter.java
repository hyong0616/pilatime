package com.soongsil.pilatime.member;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soongsil.pilatime.R;

import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {

    private ArrayList<Memo> memoList = new ArrayList<Memo>();

    public MemoAdapter() {}

    @Override
    public int getCount() {
        return memoList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Memo getItem(int position) {
        return memoList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_memo, parent, false);
        }

        TextView dateTextView = (TextView) convertView.findViewById(R.id.textViewDate);
        TextView contentTextView = (TextView) convertView.findViewById(R.id.textViewContent);

        Memo memo = memoList.get(position);
        dateTextView.setText(memo.getDate());
        contentTextView.setText(memo.getContent());

        return convertView;
    }

    public void addItem(String date, String content) {

        Memo memo = new Memo();
        memo.setDate(date);
        memo.setContent(content);
        memoList.add(memo);
    }
}
