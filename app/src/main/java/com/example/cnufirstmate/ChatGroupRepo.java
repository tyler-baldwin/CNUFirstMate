package com.example.cnufirstmate;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatGroupRepo {
    private static final String TAG = "ChatGroupRepo";

    private FirebaseFirestore db;

    public ChatGroupRepo(FirebaseFirestore db) {
        this.db = db;
    }


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
//                .orderBy("name")
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
}
