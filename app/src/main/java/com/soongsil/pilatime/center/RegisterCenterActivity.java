package com.soongsil.pilatime.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.soongsil.pilatime.R;

public class RegisterCenterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText idText, passwordText, center_nameText, center_addressText, center_numberText;
    private Button center_registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_center);



        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        center_nameText = (EditText) findViewById(R.id.centernameText);
        center_addressText = (EditText) findViewById(R.id.centeraddressText);
        center_numberText = (EditText) findViewById(R.id.numberText);
        center_registerButton = (Button)findViewById(R.id.registerButton);

        /*Firebase Authentication*/
        mAuth = FirebaseAuth.getInstance();

    }

    public void submitClick(View v) {
        final Intent intent = new Intent(this, AdminCalendarActivity.class);

        /*유효성 Check*/
        if (idText.getText().toString().length()==0) {
            Toast.makeText(RegisterCenterActivity.this,"관리자 Email을 입력하세요",Toast.LENGTH_SHORT).show();
            idText.requestFocus();
            return;
        }
        if (passwordText.getText().toString().length()==0) {
            Toast.makeText(RegisterCenterActivity.this,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
            passwordText.requestFocus();
            return;
        }
        if (center_nameText.getText().toString().length()==0) {
            Toast.makeText(RegisterCenterActivity.this,"센터 이름을 입력하세요",Toast.LENGTH_SHORT).show();
            center_nameText.requestFocus();
            return;
        }
        if (center_addressText.getText().toString().length()==0) {
            Toast.makeText(RegisterCenterActivity.this,"센터 주소를 입력하세요",Toast.LENGTH_SHORT).show();
            center_addressText.requestFocus();
            return;
        }
        if (center_numberText.getText().toString().length()==0) {
            Toast.makeText(RegisterCenterActivity.this,"센터 번호를 입력하세요",Toast.LENGTH_SHORT).show();
            center_numberText.requestFocus();
            return;
        }

        /*Firebase Cloud Firestore*/
        Center center = new Center(center_nameText.getText().toString(), idText.getText().toString(), center_addressText.getText().toString(), center_numberText.getText().toString());
        db.collection("centers").document(center_nameText.getText().toString()).set(center);


        /*Firebase Authentication Signin*/
        String email = idText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG,"센터 회원가입 Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    startActivity(intent);
                } else {
                    Log.w(TAG,"센터 회원가입 Error");
                    Toast.makeText(RegisterCenterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}