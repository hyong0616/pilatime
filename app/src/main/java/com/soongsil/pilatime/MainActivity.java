package com.soongsil.pilatime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.soongsil.pilatime.center.AdminCalendarActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn1Click(View v) {
        Intent intent = new Intent(this, AdminCalendarActivity.class);
        startActivity(intent);
    }

    public void btn2Click(View v) {
        Intent intent = new Intent(this,AdminCalendarActivity.class);
        startActivity(intent);
    }
}
