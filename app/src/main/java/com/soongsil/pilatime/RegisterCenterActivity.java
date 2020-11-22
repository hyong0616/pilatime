package com.soongsil.pilatime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_center);

        EditText idText = (EditText) findViewById(R.id.idText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);
        EditText center_nameText = (EditText) findViewById(R.id.centernameText);
        EditText center_addressText = (EditText) findViewById(R.id.centeraddressText);
        EditText center_emailText = (EditText) findViewById(R.id.emailText);
        Button center_registerButton = (Button)findViewById(R.id.registerButton);
    }
}