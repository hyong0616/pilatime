package com.soongsil.pilatime.uiMember.member;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.soongsil.pilatime.R;
import com.soongsil.pilatime.member.ModifyInfoActivity;

import org.w3c.dom.Text;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ClassManageFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView classNameTextView, classStatusTextView, classTimeTextView, classCapacityTextView, classCountTextView, classRemainTextView,classCostTextView;
    TextView nameTextView, phoneTextView, centerTextView;
    String centerName, email,className;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_manage, container, false);

        classNameTextView = (TextView) view.findViewById(R.id.textView1);
        classStatusTextView= (TextView) view.findViewById(R.id.textView2);
        classTimeTextView= (TextView) view.findViewById(R.id.textView3);
        classCapacityTextView= (TextView) view.findViewById(R.id.textView4);
        classCountTextView= (TextView) view.findViewById(R.id.textView5);
        classRemainTextView= (TextView) view.findViewById(R.id.textView7);
        classCostTextView= (TextView) view.findViewById(R.id.textView6);;
        nameTextView= (TextView) view.findViewById(R.id.textView10);
        phoneTextView= (TextView) view.findViewById(R.id.textView11);
        centerTextView= (TextView) view.findViewById(R.id.textView12);

        /*센터 이름 가져오기*/
        email = user.getEmail();
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


        CollectionReference conRef = db.collection("members")
                .document(centerName).collection("member");
        Query query2 = conRef.whereEqualTo("email", email);
        Task<QuerySnapshot> querySnapshotTask2 = query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                }
                else {
                Log.d(TAG, "Error getting Documents");
                }
            }
        });
        //TODO : Task Await 로 변경
        while (!querySnapshotTask2.isComplete()) {
            Log.d(TAG, "Not yet");
        }
        List<DocumentSnapshot> document2 = querySnapshotTask2.getResult().getDocuments();
        className = document2.get(0).getData().get("className").toString();

        if (!className.equals("")) {
            classNameTextView.setText(className);
            classStatusTextView.setText("현재 수강 인원 : "+document2.get(0).getData().get("className").toString()+"명");
            classTimeTextView.setText("시간 : "+document2.get(0).getData().get("classTime").toString());
            classCapacityTextView.setText("정원 : "+document2.get(0).getData().get("classCapacity").toString()+"명");
            classCountTextView.setText("구성 : "+document2.get(0).getData().get("classTotal").toString()+"회");
            classRemainTextView.setText("남은 수강 횟수 : "+document2.get(0).getData().get("classRemain").toString()+"회");
            classCostTextView.setText("가격 : "+document2.get(0).getData().get("classCost").toString()+"원");

            nameTextView.setText("내 이름 : "+document2.get(0).getData().get("name").toString());
            phoneTextView.setText("전화번호 : "+document2.get(0).getData().get("phone").toString());
            centerTextView.setText(" 센터명 : "+centerName);

        }
        /*등록된 수업 정보가 없는 경우*/
        else {
            classNameTextView.setText("수강중인 수업이 없습니다....");
            classStatusTextView.setText("수강신청을 진행해 주세요...");
            classTimeTextView.setText("");
            classCapacityTextView.setText("");
            classCountTextView.setText("");
            classRemainTextView.setText("");
            classCostTextView.setText("");
            nameTextView.setText("내 이름 : "+document2.get(0).getData().get("name").toString());
            phoneTextView.setText("전화번호 : "+document2.get(0).getData().get("phone").toString());
            centerTextView.setText(" 센터명 : "+centerName);

        }
        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add:

                /* 데이터 추가
                Notifications notifications2 = new Notifications("2","2020/11/26", "두번째 게시물 제목입니다.", "ADMIN");
                db.collection("notifications").document(myname).collection("notification").document().set(notifications2);
                */
                Intent intent = new Intent(getActivity(), ModifyInfoActivity.class);
                startActivity(intent);
                return true;

            default:
                break;
        }
        return false;
    }
}