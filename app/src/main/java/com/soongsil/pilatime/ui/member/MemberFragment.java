package com.soongsil.pilatime.ui.member;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.soongsil.pilatime.R;
import com.soongsil.pilatime.SignupActivity;
import com.soongsil.pilatime.center.MembersAdapter;
import com.soongsil.pilatime.member.RegisterMemberActivity;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MemberFragment extends Fragment {

    public Button sucButton, waitButton, submitButton;
    public ListView listView;
    public TextView textView_change;
    String centerName;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_member, container, false);
        sucButton = view.findViewById(R.id.SucButton);
        waitButton = view.findViewById(R.id.WaitButton);
        submitButton = view.findViewById(R.id.SubmitButton);
        listView = view.findViewById(R.id.list_view);

        /*버튼 눌릴때마다 변경되는 textView*/
        textView_change = view.findViewById(R.id.textView_change);

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
        centerName = document.get(0).getData().get("name").toString();

        /*초기의 경우는 승인 완료 회원 가져오기*/
        sucButton.setBackgroundColor(Color.parseColor("#CCCCFF"));
        final MembersAdapter initAdapter = new MembersAdapter();

        /*TODO Y N 쿼리에 따른 분기처리
        *
        * /members/센터1/member/mem1@pila.com
        * */
        CollectionReference conRef = db.collection("members")
                .document(centerName).collection("member");

        conRef.whereEqualTo("ack","Y").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            initAdapter.addItem(document.getData().get("name").toString(), document.getData().get("className").toString(),
                                    Integer.parseInt(document.getData().get("classRemain").toString()));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "No Data in init Query", task.getException());
                    }
                    initAdapter.notifyDataSetChanged();
                    listView.setAdapter(initAdapter);
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });

        /*해당하는 센터의 회원 가져오기*/
        sucButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MembersAdapter membersAdapter = new MembersAdapter();
                sucButton.setBackgroundColor(Color.parseColor("#CCCCFF"));
                waitButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                submitButton.setVisibility(view.INVISIBLE);
                textView_change.setText("출석");

                CollectionReference conRef = db.collection("members")
                        .document(centerName).collection("member");

                conRef.whereEqualTo("ack","Y").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    membersAdapter.addItem(document.getData().get("name").toString(), document.getData().get("className").toString(),
                                            Integer.parseInt(document.getData().get("classRemain").toString()));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d(TAG, "No Data in init Query", task.getException());
                            }
                            membersAdapter.notifyDataSetChanged();
                            listView.setAdapter(membersAdapter);
                        } else {
                            Log.d(TAG,"Error getting Documents");
                        }
                    }
                });
            }
        });

        waitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MembersAdapter waitAdapter = new MembersAdapter();
                submitButton.setVisibility(view.VISIBLE);
                sucButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                waitButton.setBackgroundColor(Color.parseColor("#CCCCFF"));
                textView_change.setText("승인");

                CollectionReference conRef = db.collection("members")
                        .document(centerName).collection("member");

                conRef.whereEqualTo("ack","N").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    waitAdapter.addItem(document.getData().get("name").toString(), document.getData().get("className").toString(),
                                            Integer.parseInt(document.getData().get("classRemain").toString()));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            } else {
                                Log.d(TAG, "No Data in init Query", task.getException());
                            }
                            waitAdapter.notifyDataSetChanged();
                            listView.setAdapter(waitAdapter);
                        } else {
                            Log.d(TAG,"Error getting Documents");
                        }
                    }
                });

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }
}