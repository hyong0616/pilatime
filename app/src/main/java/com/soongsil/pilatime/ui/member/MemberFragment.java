package com.soongsil.pilatime.ui.member;

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

public class MemberFragment extends Fragment {

    private MemberViewModel memberViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        memberViewModel =
                ViewModelProviders.of(this).get(MemberViewModel.class);
        View root = inflater.inflate(R.layout.fragment_member, container, false);
        final TextView textView = root.findViewById(R.id.text_member);
        memberViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                textView.setText(s);
            }
        });
        return root;
    }
}