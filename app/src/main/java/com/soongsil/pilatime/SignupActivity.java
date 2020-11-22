package com.soongsil.pilatime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final Button center_Botton = (Button)findViewById(R.id.CenterButton);
        final Button member_Botton = (Button)findViewById(R.id.MemberButton);

        center_Botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent centerIntent = new Intent(SignupActivity.this, RegisterCenterActivity.class);
                SignupActivity.this.startActivity(centerIntent);
            }
        });

        member_Botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memberIntent = new Intent(SignupActivity.this, RegisterMemberActivity.class);
                SignupActivity.this.startActivity(memberIntent);
            }
        });



    }
}

