package com.soongsil.pilatime.uiMember.goods;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.soongsil.pilatime.center.AdminCalendarActivity;
import com.soongsil.pilatime.center.Goods;
import com.soongsil.pilatime.member.GoodsAdapter;
import com.soongsil.pilatime.R;
import com.soongsil.pilatime.member.MemberCalendarActivity;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GoodsMemberFragment extends Fragment {

    public Button submitButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String centerName, email;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_member, container, false);

        final ListView listView ;
        final GoodsAdapter goodsAdapter = new GoodsAdapter();
        listView = (ListView) view.findViewById(R.id.list_view);
        submitButton = (Button) view.findViewById(R.id.SubmitButton);

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

        /*상품 목록 가져오기
         *
         * 상품들 > 센터 > 상품명 > 상품
         * */
        CollectionReference conRef = db.collection("goods").document(centerName).collection("good");
        Log.d(TAG,conRef.getPath());
        conRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            goodsAdapter.addItem(document.getData().get("name").toString(), document.getData().get("status").toString()
                                    ,document.getData().get("time").toString(), document.getData().get("capacity").toString()
                                    ,document.getData().get("count").toString(), document.getData().get("cost").toString());
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "상품 없음", task.getException());
                    }
                    goodsAdapter.notifyDataSetChanged();
                    listView.setAdapter(goodsAdapter);
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String btnText = submitButton.getText().toString();
                int count = goodsAdapter.getCount();
                int checked =-1;
                if (count > 0) {

                    // 현재 선택된 아이템의 position 획득.
                    checked = listView.getCheckedItemPosition();
                    if (checked > -1 && checked < count) {

                        if (btnText.equals("신청")) {
                            submitButton.setText("변경");
                        } else {
                            submitButton.setText("신청");
                        }

                        Goods goods = new Goods();
                        goods = goodsAdapter.getItem(checked); //선택한 상품 정보
                        Log.d(TAG, goods.getName() + " => " + goods.getTime());

                        Goods nowgoods = new Goods("클래스 A", "3", "10:00-12:00 월 수 금", "5", "30", "2,000,000");

                        /*수업 정보 update*/
                        DocumentReference docRef =  db.collection("members")
                                .document(centerName).collection("member").document(email);

                        docRef.update(
                                "className", nowgoods.getName(),                                            //수업이름
                                "classCapacity", Integer.parseInt(nowgoods.getCapacity()),//전체정원
                                "classStatus", Integer.parseInt(nowgoods.getStatus()) ,                     //현재정원
                                "classTime",nowgoods.getTime(),                                             //수업 시간
                                "classTotal",Integer.parseInt(nowgoods.getCount()),                         //전체 수업 회차
                                "classRemain",Integer.parseInt(nowgoods.getCount()),                        //남은 수업 회차
                                "classCost",nowgoods.getCost()                            //가격
                                )

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "수강 신청 버튼 : DocumentSnapshot successfully updated!");
                                        Intent intent = new Intent(getActivity(), MemberCalendarActivity.class);
                                        intent.putExtra("particularFragment","classManage");
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "수강 신청 버튼 : Error updating document", e);
                                    }
                                });
                    }
                }
            }
        });

        return view;
    }
}