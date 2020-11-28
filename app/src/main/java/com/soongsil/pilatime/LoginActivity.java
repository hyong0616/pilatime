package com.soongsil.pilatime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText idText, passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        final Button LoginBotton = (Button)findViewById(R.id.loginButton);
        TextView registerButton = (TextView) findViewById(R.id.registerButton);

        /*Firebase Authentication*/
        mAuth = FirebaseAuth.getInstance();

        /*회원가입 버튼*/
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });
    }

    public void loginClick(View v) {
        final Intent Adminintent = new Intent(this,AdminCalendarActivity.class);
        final Intent Memberintent = new Intent(this,AdminCalendarActivity.class); /*Todo List : 회원 Activity로 변경 */

        /*유효성 Check*/
        if (idText.getText().toString().length()==0) {
            Toast.makeText(LoginActivity.this,"Email을 입력하세요",Toast.LENGTH_SHORT).show();
            idText.requestFocus();
            return;
        }
        if (passwordText.getText().toString().length()==0) {
            Toast.makeText(LoginActivity.this,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
            passwordText.requestFocus();
            return;
        }
        final String email = idText.getText().toString();
        final String password = passwordText.getText().toString();

        /*Firebase 로그인*/
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG,"login Success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (adminCheck(email)) {
                                startActivity(Adminintent);
                            } else {
                                startActivity(Memberintent);
                            }
                        } else {
                            Log.w(TAG, "login Failure",task.getException());
                            Toast.makeText(LoginActivity.this, "로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*Todo List : 관리자별, 회원별 분기 처리 */
    public boolean adminCheck(String id) {

        return true;
    }
}
