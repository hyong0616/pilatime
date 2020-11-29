package com.soongsil.pilatime.center;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soongsil.pilatime.R;

import java.util.ArrayList;

public class ClassContentAdapter extends BaseAdapter {

    private ArrayList<ClassContent> classContentArrayList = new ArrayList<ClassContent>();

    public ClassContentAdapter() {

    }

    @Override
    public int getCount() {
        return classContentArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ClassContent getItem(int position) {
        return classContentArrayList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_class_content, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.textViewClass);
        TextView contentTextView = (TextView) convertView.findViewById(R.id.textViewContent);

        ClassContent classContent = classContentArrayList.get(position);
        nameTextView.setText(classContent.getName());
        contentTextView.setText(classContent.getContent());

        return convertView;
    }

    public void addItem(String name, String content) {
        ClassContent classContent = new ClassContent();

        classContent.setName(name);
        classContent.setContent(content);

        classContentArrayList.add(classContent);
    }


}
