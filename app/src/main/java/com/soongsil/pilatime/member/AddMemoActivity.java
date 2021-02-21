package com.soongsil.pilatime.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.soongsil.pilatime.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMemoActivity extends AppCompatActivity {
    public Button plusButton, closeButton;
    public EditText contentText;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        /*Activity 팝업 화면 처럼 구성*/
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9);
        int height = (int) (display.getHeight() * 0.7);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        plusButton = (Button) findViewById(R.id.btn_plus);
        closeButton = (Button) findViewById(R.id.btn_close);
        contentText = (EditText) findViewById(R.id.editTextMemo);



        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMemoActivity.this, MemberCalendarActivity.class);

                /*현재 날짜 가져오기*/
                long now = System.currentTimeMillis();
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
                String date = mFormat.format(new Date());

                /*데이터 넣기
                 * contents/센터이름/2020.11.17/클래스이름/클래스 내용
                 * */
                Memo memo = new Memo(date,contentText.getText().toString());
                db.collection("member").document(user.getEmail()).
                        collection("memo").document(date).set(memo);
                startActivity(intent);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
    }
}
