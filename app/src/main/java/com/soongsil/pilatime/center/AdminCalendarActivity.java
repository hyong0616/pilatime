package com.soongsil.pilatime.center;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soongsil.pilatime.R;
import com.soongsil.pilatime.ui.calendar.CalendarFragment;
import com.soongsil.pilatime.ui.goods.GoodsFragment;
import com.soongsil.pilatime.ui.member.MemberFragment;
import com.soongsil.pilatime.ui.notifications.NotificationsFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class AdminCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_calendar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_calendar, R.id.navigation_member, R.id.navigation_goods, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /*특정 fragment로 이동하는 intent filter*/
        String str = getIntent().getStringExtra("particularFragment");
        if(str !=null)
        {
            if (str.equals("good"))
            {
                navController.navigate(R.id.action_navigation_calendar_to_navigation_goods);
            } else if (str.equals("notification"))
            {
                navController.navigate(R.id.action_navigation_calendar_to_navigation_notifications);
            } else if (str.equals("member"))
            {
                navController.navigate(R.id.action_navigation_calendar_to_navigation_member);
            }
        }
    }
}
