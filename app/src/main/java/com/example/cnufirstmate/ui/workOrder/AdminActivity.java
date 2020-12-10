package com.example.cnufirstmate.ui.workOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.cnufirstmate.ChatGroupWorkRepo;
import com.example.cnufirstmate.R;
import com.example.cnufirstmate.ui.Groups.GroupActivity;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView workRecycler;
    private WorkOrderAdapter adapter;
    private ChatGroupWorkRepo chatGroupWorkRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        chatGroupWorkRepo = new ChatGroupWorkRepo(FirebaseFirestore.getInstance());
        if (getSupportActionBar() != null) {
            setTitle("Work Order Admin");
        }
        workRecycler = findViewById(R.id.adminRecycler);
        workRecycler.setLayoutManager(new LinearLayoutManager(AdminActivity.this));
        //init work orders
        getWorkOrders();
    }

    //calls the database and asks for all the current workorders
    private void getWorkOrders() {
        chatGroupWorkRepo.getWorkOrders(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Admin Activity", "Listen failed.", e);
                    return;
                }

                List<WorkOrder> workOrders = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
//                    Toast.makeText(getContext(), doc.getString("name"), Toast.LENGTH_LONG).show();
                    String date = doc.getTimestamp("date").toString();
                    workOrders.add(new WorkOrder(doc.getString("building"), date,
                            doc.getString("email"), doc.getString("issue"),
                            doc.getString("name"), doc.getString("room")));
                }

                adapter = new WorkOrderAdapter(workOrders, listener);
                workRecycler.setAdapter(adapter);
            }
        });
    }

    /*This is a method to launch into the actual chat once clicked*/
    WorkOrderAdapter.OnWorkOrderClickListener listener = new WorkOrderAdapter.OnWorkOrderClickListener() {
        @Override
        public void onClick(WorkOrder workOrder) {
            //TODO decide what to do on onCLick
            Intent intent = new Intent(AdminActivity.this, GroupActivity.class);
            //TODO make the work order show??
//            intent.putExtra(GroupActivity.GROUP_ID, group.getId());
//            intent.putExtra(GroupActivity.GROUP_NAME, group.getName());
            startActivity(intent);
        }


    };
}