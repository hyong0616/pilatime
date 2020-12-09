package com.soongsil.pilatime.center;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.soongsil.pilatime.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AddGoodsActivity extends AppCompatActivity {
    public Button plusButton, closeButton;
    public EditText nameText, timeText, capacityText, countText,costText;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 0.9);
        int height = (int) (display.getHeight() * 0.7);

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        plusButton = (Button) findViewById(R.id.btn_plus);
        closeButton = (Button) findViewById(R.id.btn_close);
        nameText = (EditText) findViewById(R.id.editText_ClassName);
        timeText = (EditText) findViewById(R.id.editText_ClassTime);
        capacityText = (EditText) findViewById(R.id.editText_ClassCapacity);
        countText = (EditText) findViewById(R.id.editText_ClassCount);
        costText = (EditText) findViewById(R.id.editText_ClassCost);
        /*센터 이름 가져오기*/
        String email = user.getEmail();
        final CollectionReference docRef = db.collection("centers");
        Query query = docRef.whereEqualTo("email",email);
        Task<QuerySnapshot> querySnapshotTask
                = query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error center Name", task.getException());
                    }
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });
        //TODO : Task Await 로 변경
        while(!querySnapshotTask.isComplete()) {
            Log.d(TAG, "Not yet");
        }
        List<DocumentSnapshot> document = querySnapshotTask.getResult().getDocuments();
        final String centerName = document.get(0).getData().get("name").toString();


        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddGoodsActivity.this, AdminCalendarActivity.class);
                intent.putExtra("particularFragment","good");
                /*현재 시간 가져오기*/
                long now = System.currentTimeMillis();
                Date mDate = new Date(now);
                SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
                String date = mFormat.format(mDate);

                /*데이터 넣기
                 * goods/센터1/good/개인레슨 A Class
                 * */
                Goods goods = new Goods(nameText.getText().toString(), "현재 : 0명",timeText.getText().toString(),capacityText.getText().toString(), countText.getText().toString(), costText.getText().toString());
                db.collection("goods").document(centerName).collection("good").document(goods.getName()).set(goods);

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
