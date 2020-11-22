package com.soongsil.pilatime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText idText = (EditText) findViewById(R.id.idText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);

        final Button LoginBotton = (Button)findViewById(R.id.loginButton);
        TextView registerButton = (TextView) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }

    public void loginClick(View v) {
        Intent intent = new Intent(this,AdminCalendarActivity.class);
        startActivity(intent);
    }


}
