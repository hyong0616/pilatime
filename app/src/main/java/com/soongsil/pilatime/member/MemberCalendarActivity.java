package com.soongsil.pilatime.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soongsil.pilatime.R;


public class MemberCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_calendar);
        BottomNavigationView navView2 = findViewById(R.id.nav_view2);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_calendar_member, R.id.navigation_classManage, R.id.navigation_goods_member, R.id.navigation_notifications_member)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_member_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView2, navController);

        /*특정 fragment로 이동하는 intent filter*/
        String str = getIntent().getStringExtra("particularFragment");
        if(str !=null)
        {
            if (str.equals("classManage"))
            {
                navController.navigate(R.id.action_navigation_calendar_member_to_navigation_classManage);
            } else if (str.equals("notification"))
            {
                navController.navigate(R.id.action_navigation_calendar_member_to_navigation_notifications_member);
            } else if (str.equals("good"))
            {
                navController.navigate(R.id.action_navigation_calendar_member_to_navigation_goods_member);
            }
        }
    }

}
