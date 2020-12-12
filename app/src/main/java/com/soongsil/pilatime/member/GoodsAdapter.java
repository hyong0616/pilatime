package com.soongsil.pilatime.member;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soongsil.pilatime.R;
import com.soongsil.pilatime.center.Goods;

import java.util.ArrayList;

public class GoodsAdapter extends BaseAdapter {

    private ArrayList<Goods> goodsList = new ArrayList<Goods>();

    public GoodsAdapter() {

    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Goods getItem(int position) {
        return goodsList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_mem_item_goods, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView statusTextView = (TextView) convertView.findViewById(R.id.textView2);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.textView3);
        TextView capacityTextView = (TextView) convertView.findViewById(R.id.textView4);
        TextView countTextView = (TextView) convertView.findViewById(R.id.textView5);
        TextView costTextView = (TextView) convertView.findViewById(R.id.textView6);

        Goods goods = goodsList.get(position);
        nameTextView.setText(goods.getName());
        statusTextView.setText("현재 : "+goods.getStatus()+"명");
        timeTextView.setText("시간 : "+goods.getTime());
        capacityTextView.setText("정원 : "+goods.getCapacity()+"명");
        countTextView.setText("구성 : "+goods.getCount()+"회");
        costTextView.setText("가격 : "+goods.getCost()+"원");

        return convertView;
    }

    public void addItem(String name, String status,String time, String capacity, String count, String cost) {
        Goods goods = new Goods();

        goods.setName(name);
        goods.setStatus(status);
        goods.setTime(time);
        goods.setCapacity(capacity);
        goods.setCount(count);
        goods.setCost(cost);
        goodsList.add(goods);
    }
}
