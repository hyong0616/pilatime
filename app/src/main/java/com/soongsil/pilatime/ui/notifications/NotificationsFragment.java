package com.soongsil.pilatime.ui.notifications;

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

import com.soongsil.pilatime.Notifications;
import com.soongsil.pilatime.NotificationsAdapter;
import com.soongsil.pilatime.R;

public class NotificationsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        ListView listView;
        NotificationsAdapter notificationsAdapter = new NotificationsAdapter();
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(notificationsAdapter);

        notificationsAdapter.addItem("1","2020/11/26", "첫번째 게시물 제목입니다.", "ADMIN");
        notificationsAdapter.addItem("2","2020/11/26", "두번째 게시물 제목입니다.", "ADMIN");
        notificationsAdapter.addItem("3","2020/11/26", "세번째 게시물 제목입니다.", "ADMIN");
        notificationsAdapter.addItem("4","2020/11/26", "네번째 게시물 제목입니다.", "ADMIN");
        notificationsAdapter.addItem("5","2020/11/26", "다섯번째 게시물 제목입니다.", "ADMIN");
        notificationsAdapter.addItem("6","2020/11/26", "여섯번째 게시물 제목입니다.", "ADMIN");

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);

    }
}