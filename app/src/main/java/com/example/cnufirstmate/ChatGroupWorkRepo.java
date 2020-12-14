package com.example.cnufirstmate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * This Class contains most of the network calls relating the the group,work and chat functionality*/
public class ChatGroupWorkRepo {
    //For logging purposes
    private static final String TAG = "ChatGroupRepo";
    //Reference to DB
    private FirebaseFirestore db;
    private String newmems;
    public ChatGroupWorkRepo(FirebaseFirestore db) {
        this.db = db;
    }
//Creates a group with That group name, a list of people including yourself and some listeners

    public void createGroup(String name, ArrayList<String> arr,
                            final OnSuccessListener<DocumentReference> successCallback,
                            final OnFailureListener failureCallback) {
        Map<String, Object> Group = new HashMap<>();
        Group.put("name", name);
        Group.put("Members", arr);
        db.collection("Groups")
                .add(Group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        successCallback.onSuccess(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureCallback.onFailure(e);
                    }
                });
    }

    public void getRooms(EventListener<QuerySnapshot> listener) {
        db.collection("Groups")
                .whereArrayContains("Members", MainActivity.email)
                .orderBy("name")
                .addSnapshotListener(listener);
    }

    public void addMessageToChatRoom(String groupId,
                                     String senderId,
                                     String message,
                                     final OnSuccessListener<DocumentReference> successCallback,
                                     final OnFailureListener failureCallback) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("group_id", groupId);
        msg.put("sender_id", senderId);
        msg.put("message", message);
        msg.put("sent", System.currentTimeMillis());

        db.collection("message")
                .add(msg)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        successCallback.onSuccess(documentReference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failureCallback.onFailure(e);
                    }
                });
    }

    public void getChats(String roomId, EventListener<QuerySnapshot> listener) {
        db.collection("message")
                .whereEqualTo("group_id", roomId)
                .orderBy("sent", Query.Direction.DESCENDING)
                .addSnapshotListener(listener);
    }

    public void getWorkOrders(EventListener<QuerySnapshot> listener) {
        db.collection("orders")
                .orderBy("date")
                .addSnapshotListener(listener);;
    }

    public void addMemberToGroup(String added,String group){
        DocumentReference addref = db.collection("Groups").document(group);
        addref.update("Members", FieldValue.arrayUnion(added));
    }

    public void removeMemberToGroup(String remmed,String group){
        DocumentReference remref = db.collection("Groups").document(group);
        remref.update("Members", FieldValue.arrayRemove(remmed));
    }

}
