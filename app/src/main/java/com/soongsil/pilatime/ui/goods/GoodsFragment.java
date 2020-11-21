package com.soongsil.pilatime.ui.goods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.soongsil.pilatime.R;

public class GoodsFragment extends Fragment {

    private GoodsViewModel goodsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        goodsViewModel =
                ViewModelProviders.of(this).get(GoodsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_goods, container, false);
        final TextView textView = root.findViewById(R.id.text_goods);
        goodsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}