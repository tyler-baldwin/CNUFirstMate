package com.example.cnufirstmate.ui.workOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cnufirstmate.ChatGroupWorkRepo;
import com.example.cnufirstmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView workRecycler;
    private WorkOrderAdapter adapter;
    private ChatGroupWorkRepo chatGroupWorkRepo;
    private List<WorkOrder> workOrders;

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

                workOrders = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
//                    Toast.makeText(AdminActivity.this, doc.getTimestamp("date").toString(), Toast.LENGTH_LONG).show();

                    workOrders.add(new WorkOrder(doc.getString("building"), doc.getString("date"),
                            doc.getString("email"), doc.getString("issue"),
                            doc.getString("name"), doc.getString("room"), doc.getId()));
                }

                adapter = new WorkOrderAdapter(workOrders, listener);
                workRecycler.setAdapter(adapter);
            }
        });
    }

    /*This is a method to launch into the actual chat once clicked*/
    WorkOrderAdapter.OnWorkOrderClickListener listener = new WorkOrderAdapter.OnWorkOrderClickListener() {
        @Override
        public void onClick(final WorkOrder workOrder) {
            new AlertDialog.Builder(AdminActivity.this)
                    .setTitle("Work Order " + workOrder.getId())
                    .setMessage(workOrder.getBuilding() + " " + workOrder.getRoom()+ "\n" +workOrder.getIssue()
                            + "\n" +workOrder.getName() + " " + workOrder.getEmail())
                    /*This should be used by workers or admin to complete work orders*/
                    .setPositiveButton("Delete & Notify", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Start email intent*/
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                            String[] emails = {workOrder.getEmail()};
                            intent.putExtra(Intent.EXTRA_EMAIL, emails);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Work Order " + workOrder.getId() + " Complete");
                            String nowTime = Calendar.getInstance().getTime().toString();
                            intent.putExtra(Intent.EXTRA_TEXT, workOrder.getName() + ", "
                                    + workOrder.getIssue() + " has been completed on " + nowTime + " \n Work Done: ");
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                            /*Start Database delete*/
                            FirebaseFirestore db;
                            db = FirebaseFirestore.getInstance();
                            db.collection("orders").document(workOrder.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("delete", "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("delete", "Error deleting document", e);
                                        }
                                    });

                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


    };
}