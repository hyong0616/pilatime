package com.soongsil.pilatime.ui.goods;

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
import android.widget.Toast;

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
import com.soongsil.pilatime.LoginActivity;
import com.soongsil.pilatime.center.AddGoodsActivity;
import com.soongsil.pilatime.center.AdminCalendarActivity;
import com.soongsil.pilatime.center.Goods;
import com.soongsil.pilatime.center.GoodsAdapter;
import com.soongsil.pilatime.R;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GoodsFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String centerName;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goods, container, false);

        final ListView listView ;
        final GoodsAdapter goodsAdapter = new GoodsAdapter();
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
        /*상품 목록 가져오기
         *
         * 상품들 > 센터 > 상품명 > 상품
         * */
        CollectionReference conRef = db.collection("goods").document(centerName).collection("good");
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
                        Log.d(TAG, "Error center Name", task.getException());
                    }
                    goodsAdapter.notifyDataSetChanged();
                    listView.setAdapter(goodsAdapter);
                } else {
                    Log.d(TAG,"Error getting Documents");
                }
            }
        });

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

                Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
                startActivity(intent);
                /* 데이터 추가
                Goods goods_1 = new Goods("화 목 A Class", "현재 : 6명","시간 : 14:00~15:00","정원 : 8명", "구성 : 30회(15주)", "350,000w");
                db.collection("goods").document(myname).collection("good").document(goods_1.getName()).set(goods_1);
                */

                return true;
            default:
                break;
        }
        return false;
    }

}