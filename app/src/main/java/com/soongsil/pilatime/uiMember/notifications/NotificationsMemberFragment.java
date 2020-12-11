package com.soongsil.pilatime.uiMember.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.soongsil.pilatime.center.NotificationsAdapter;
import com.soongsil.pilatime.R;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NotificationsMemberFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String centerName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications_member, container, false);

        final ListView listView;
        final NotificationsAdapter notificationsAdapter = new NotificationsAdapter();
        listView = (ListView) view.findViewById(R.id.list_view2);

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
        final String centerName = document.get(0).getData().get("centerName").toString();


        /*공지사항 목록 가져오기
         *
         * 공지사항 > 센터 > 게시글고유번호 > 게시글
         * */
        CollectionReference conRef = db.collection("notifications").document(centerName).collection("notification");
        conRef.orderBy("idx").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        int idx = 1;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            notificationsAdapter.addItem(Integer.toString(idx), document.getData().get("date").toString()
                                    ,document.getData().get("title").toString(), document.getData().get("writer").toString());
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            idx ++;
                        }
                    } else {
                        Log.d(TAG, "Error center Name", task.getException());
                    }
                    notificationsAdapter.notifyDataSetChanged();
                    listView.setAdapter(notificationsAdapter);
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });

        setHasOptionsMenu(true);
        return view;
    }
}