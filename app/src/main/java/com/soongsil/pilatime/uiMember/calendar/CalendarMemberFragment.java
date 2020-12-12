package com.soongsil.pilatime.uiMember.calendar;

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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.soongsil.pilatime.member.AddMemoActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CalendarMemberFragment extends Fragment {

    public CalendarView calendarView;
    public ListView listView;
    public Button addButton;
    String centerName;
    String className;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    /*현재 날짜 가져오기*/
    long now = System.currentTimeMillis();
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
    String date = mFormat.format(new Date());

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_member, container, false);

        calendarView = view.findViewById(R.id.member_calendar);
        addButton = view.findViewById(R.id.btn_add);

        /*Adapter setting*/
        final ClassContentAdapter classContentAdapter = new ClassContentAdapter();
        listView = (ListView) view.findViewById(R.id.list_view);

        /*센터 이름 가져오기*/
        String email = user.getEmail();
        final CollectionReference docRef = db.collection("member");
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
        centerName = document.get(0).getData().get("centerName").toString();
        Log.d(TAG, "CENTERNAME : "+centerName );


        /*수업 이름 가져오기*/
        CollectionReference conRef = db.collection("members")
                .document(centerName).collection("member");
        Query query2 = conRef.whereEqualTo("email",email);
        Task<QuerySnapshot> querySnapshotTask2
                = query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "No Data in init Query", task.getException());
                    }
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });
        //TODO : Task Await 로 변경
        while(!querySnapshotTask2.isComplete()) {
            Log.d(TAG, "Not yet");
        }
        List<DocumentSnapshot> document2 = querySnapshotTask2.getResult().getDocuments();
        className = document2.get(0).getData().get("className").toString();


        /*초기 날짜로 가져오기
        *
        * 센터이름-> 날짜 -> 수업이름 -> 수업내용
        *goods/센터1/good/클래스이름/수업목록
        * contents/센터1/2020.11.28/클래스이름
        * */
        if (!className.equals("")) {

            DocumentReference Ref = db.collection("contents").document(centerName)
                    .collection(date).document(className);
            Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            classContentAdapter.addItem(className, document.getData().get("content").toString());
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                        classContentAdapter.notifyDataSetChanged();
                        listView.setAdapter(classContentAdapter);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        /*날짜가 바뀔때마다 db 내용 가져오기
         *
         *
         * */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String nowDate = String.format("%04d.%02d.%02d",year,month+1,dayOfMonth);
                final ClassContentAdapter dateContentAdapter = new ClassContentAdapter();

                if (!className.equals("")) {
                    DocumentReference Ref = db.collection("contents").document(centerName)
                            .collection(nowDate).document(className);
                    Ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    dateContentAdapter.addItem(className, document.getData().get("content").toString());
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                                dateContentAdapter.notifyDataSetChanged();
                                listView.setAdapter(dateContentAdapter);
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });


        /*메모 추가 버튼 클릭 시*/
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* 데이터 추가
                Goods goods_1 = new Goods("화 목 A Class", "현재 : 6명","시간 : 14:00~15:00","정원 : 8명", "구성 : 30회(15주)", "350,000w");
                db.collection("goods").document(myname).collection("good").document(goods_1.getName()).set(goods_1);
                */
                Intent intent = new Intent(getActivity(), AddMemoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}