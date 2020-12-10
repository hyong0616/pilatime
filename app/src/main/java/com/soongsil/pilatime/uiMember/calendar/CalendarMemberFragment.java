package com.soongsil.pilatime.uiMember.calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.soongsil.pilatime.center.ClassContent;
import com.soongsil.pilatime.center.ClassContentAdapter;
import com.soongsil.pilatime.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CalendarMemberFragment extends Fragment {

    public CalendarView calendarView;
    public TextView dateTextView, contentTextView;
    public ListView listView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    /*현재 날짜 가져오기*/
    long now = System.currentTimeMillis();
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd");
    String date = mFormat.format(new Date());

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_member, container, false);

        calendarView = view.findViewById(R.id.member_calendar);
        dateTextView = view.findViewById(R.id.textView_date2);
        /*현재 날짜로 setting*/
        dateTextView.setText(date);

        /*Adapter setting*/
        final ClassContentAdapter classContentAdapter = new ClassContentAdapter();
        listView = (ListView) view.findViewById(R.id.list_view);

        /*센터 이름 가져오기*/
        String email = user.getEmail();
        final CollectionReference docRef = db.collection("members");
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
        final String centerName = document.get(0).getData().get("centerName").toString();
        final String className = document.get(0).getData().get("className").toString();

        /*초기 날짜로 가져오기
        *
        * 센터이름-> 날짜 -> 수업이름 -> 수업내용
        *goods/센터1/good/클래스이름/수업목록
        * */
        if (!className.equals("")) {

            DocumentReference Ref = db.collection("goods").document(centerName)
                    .collection("good").document(className).collection("dates").document(date);
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
                dateTextView.setText(String.format("%04d.%02d.%02d",year,month+1,dayOfMonth));
                dateTextView.setVisibility(View.VISIBLE);
                final ClassContentAdapter dateContentAdapter = new ClassContentAdapter();

                if (!className.equals("")) {
                    DocumentReference Ref = db.collection("goods").document(centerName)
                            .collection("good").document(className).collection("dates").document(date);
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
            }
        });
        return view;
    }
}