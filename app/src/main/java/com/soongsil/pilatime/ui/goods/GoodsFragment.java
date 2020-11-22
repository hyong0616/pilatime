package com.soongsil.pilatime.ui.goods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.soongsil.pilatime.GoodsAdapter;
import com.soongsil.pilatime.R;

public class GoodsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods, container, false);

        ListView listView ;
        GoodsAdapter goodsAdapter = new GoodsAdapter();

        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(goodsAdapter);

        goodsAdapter.addItem("화/목 A Class", "현재 6명/8명","시간 : 14:00~15:00","정원 : 8명", "구성 : 30회(15주)", "350,000w");
        goodsAdapter.addItem("월/수 B Class","현재 2명/6명", "시간 : 14:00~15:00","정원 : 6명", "구성 : 20회(10주)", "200,000w");
        goodsAdapter.addItem("월/수/금 C Class","현재 6명/10명", "시간 : 10:00~11:00","정원 : 10명", "구성 : 30회(10주)", "400,000w");

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);

    }
}