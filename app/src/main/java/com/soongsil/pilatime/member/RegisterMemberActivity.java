package com.soongsil.pilatime.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.soongsil.pilatime.R;
import com.soongsil.pilatime.center.AdminCalendarActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterMemberActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText idText, passwordText, centerNameText, memberNameText, numberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member);

        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        centerNameText = (EditText) findViewById(R.id.centerNameText);
        memberNameText = (EditText) findViewById(R.id.memberNameText);
        numberText = (EditText) findViewById(R.id.numberText);

        /*Firebase Authentication*/
        mAuth = FirebaseAuth.getInstance();
    }

    public void submitClick(View v) {
        final Intent intent = new Intent(this, MemberCalendarActivity.class); /*ToDo List : MemberCalendar로 변경*/
        final Intent mIntent = this.getIntent();
        /*유효성 Check*/
        if (idText.getText().toString().length()==0) {
            Toast.makeText(RegisterMemberActivity.this,"회원 Email을 입력하세요",Toast.LENGTH_SHORT).show();
            idText.requestFocus();
            return;
        }
        if (passwordText.getText().toString().length()==0) {
            Toast.makeText(RegisterMemberActivity.this,"비밀번호를 입력하세요",Toast.LENGTH_SHORT).show();
            passwordText.requestFocus();
            return;
        }
        if (centerNameText.getText().toString().length()==0) {
            Toast.makeText(RegisterMemberActivity.this,"센터 이름을 입력하세요",Toast.LENGTH_SHORT).show();
            centerNameText.requestFocus();
            return;
        }
        if (memberNameText.getText().toString().length()==0) {
            Toast.makeText(RegisterMemberActivity.this,"회원 이름을 입력하세요",Toast.LENGTH_SHORT).show();
            memberNameText.requestFocus();
            return;
        }
        if (numberText.getText().toString().length()==0) {
            Toast.makeText(RegisterMemberActivity.this,"전화번호를 입력하세요",Toast.LENGTH_SHORT).show();
            numberText.requestFocus();
            return;
        }
        /*센터의 경우 Database에서 존재하는 센터인지 검색*/
        CollectionReference centersRef = db.collection("centers");
        Query query = centersRef.whereEqualTo("name",centerNameText.getText().toString());


        query.get().addOnCompleteListener(this,new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Toast.makeText(RegisterMemberActivity.this,"올바른 센터 이름을 입력하세요.",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error center Name", task.getException());
                        startActivity(mIntent); //TODO : 기존 Activity 의 내용을 유지할 수 있는지 ?
                    }
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }

        });



        /*Firebase Cloud Firestore*/
        Member member = new Member(idText.getText().toString(), memberNameText.getText().toString(), numberText.getText().toString(),centerNameText.getText().toString());
        db.collection("members").document(centerNameText.getText().toString()).collection("member").document(memberNameText.getText().toString()).set(member);


        /*Firebase Authentication Signin*/
        String email = idText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG,"회원가입 Success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    startActivity(intent);
                } else {
                    Log.w(TAG,"회원가입 Error");
                    Toast.makeText(RegisterMemberActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
