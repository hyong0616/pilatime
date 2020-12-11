package com.soongsil.pilatime.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.soongsil.pilatime.center.AddContextActivity;
import com.soongsil.pilatime.center.ClassContent;
import com.soongsil.pilatime.center.ClassContentAdapter;
import com.soongsil.pilatime.R;
import com.soongsil.pilatime.member.RegisterMemberActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CalendarFragment extends Fragment {

    public CalendarView calendarView;
    public TextView dateTextView, contentTextView;
    public ListView listView;
    public Button addButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //현재 날짜
    long now = System.currentTimeMillis();
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
    String date = mFormat.format(new Date());

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.admin_calendar);
        dateTextView = view.findViewById(R.id.textView_date);
        contentTextView = view.findViewById(R.id.textView_content);
        addButton = view.findViewById(R.id.btn_add);
        /*현재 날짜로 setting*/
        dateTextView.setText(date);

        /*Adapter setting*/
        final ClassContentAdapter classContentAdapter = new ClassContentAdapter();
        listView = (ListView) view.findViewById(R.id.list_view);

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

        /*초기 날짜로 가져오기*/
        CollectionReference conRef = db.collection("contents").document(centerName).collection(date);
        conRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            classContentAdapter.addItem(document.getData().get("name").toString(), document.getData().get("content").toString());
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error center Name", task.getException());
                    }
                    classContentAdapter.notifyDataSetChanged();
                    listView.setAdapter(classContentAdapter);
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });



        /*날짜가 바뀔때마다 db 내용 가져오기
        *
        * 센터>날짜>수업내용
        * */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String nowDate = String.format("%04d.%02d.%02d",year,month+1,dayOfMonth);
                dateTextView.setText(String.format("%04d.%02d.%02d",year,month+1,dayOfMonth));
                dateTextView.setVisibility(View.VISIBLE);
                final ClassContentAdapter dateContentAdapter = new ClassContentAdapter();

                /*
                ClassContent classContent = new ClassContent("클래스 C", "브릿지 10회 5세트 \n OOO동작 OO몇회 세트");
                db.collection("contents").document(centerName).
                        collection(String.format("%d.%d.%d",year,month+1,dayOfMonth)).document("클래스 C").set(classContent);
                */

                /*해당하는 날짜의 Class 가져오기*/
                CollectionReference conRef = db.collection("contents").document(centerName).collection(nowDate);
                conRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    dateContentAdapter.addItem(document.getData().get("name").toString(), document.getData().get("content").toString());
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d(TAG, "Error center Name", task.getException());
                            }
                            dateContentAdapter.notifyDataSetChanged();
                            listView.setAdapter(dateContentAdapter);
                        } else {
                            Log.d(TAG,"Error getting Documents");
                        }
                    }
                });
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* 데이터 추가
                Goods goods_1 = new Goods("화 목 A Class", "현재 : 6명","시간 : 14:00~15:00","정원 : 8명", "구성 : 30회(15주)", "350,000w");
                db.collection("goods").document(myname).collection("good").document(goods_1.getName()).set(goods_1);
                */
                Intent intent = new Intent(getActivity(), AddContextActivity.class);
                startActivity(intent);
            }
        });
        ;

        return view;
    }
}