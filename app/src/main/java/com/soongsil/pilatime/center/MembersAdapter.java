package com.soongsil.pilatime.center;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soongsil.pilatime.R;

import java.util.ArrayList;

public class MembersAdapter extends BaseAdapter {

    private ArrayList<Members> membersArrayList = new ArrayList<Members>();

    public MembersAdapter() {

    }

    @Override
    public int getCount() {
        return membersArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Members getItem(int position) {
        return membersArrayList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item_member, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.textViewName);
        TextView emailTextView = (TextView) convertView.findViewById(R.id.textViewEmail);

        Members members = membersArrayList.get(position);
        nameTextView.setText(members.getName());
        emailTextView.setText(members.getEmail());

        return convertView;
    }

    public void addItem(String name, String email) {
        Members members = new Members();

        members.setName(name);
        members.setEmail(email);

        membersArrayList.add(members);
    }

}
