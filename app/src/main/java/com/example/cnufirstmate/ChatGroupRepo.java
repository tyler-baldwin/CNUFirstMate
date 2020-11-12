package com.example.cnufirstmate;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
}
