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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.soongsil.pilatime.center.AdminCalendarActivity;
import com.soongsil.pilatime.member.MemberCalendarActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        final Intent Adminintent = new Intent(this, AdminCalendarActivity.class);
        final Intent Memberintent = new Intent(this,AdminCalendarActivity.class); /*TODO : 회원 Activity로 변경 */

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

                            adminCheck(email);

                        } else {
                            Log.w(TAG, "login Failure",task.getException());
                            Toast.makeText(LoginActivity.this, "로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*TODO : 관리자별, 회원별 분기 처리 */
    public void adminCheck(String id) {
        final Intent Adminintent = new Intent(this,AdminCalendarActivity.class);
        final Intent Memberintent = new Intent(this, MemberCalendarActivity.class); /*TODO : 회원 Activity로 변경 */

        CollectionReference centersRef = db.collection("centers");
        Query query = centersRef.whereEqualTo("email",idText.getText().toString());

        query.get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    /*센터 아이디 목록에 있는 경우에 따른 분기 처리*/
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "센터 로그인 : "+document.getId() + " => " + document.getData());
                            startActivity(Adminintent);
                        }
                    } else {
                        Log.d(TAG, "회원 로그인 :");
                        startActivity(Memberintent); //TODO : 기존 Activity 의 내용을 유지할 수 있는지 ?
                    }
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });
    }
}
