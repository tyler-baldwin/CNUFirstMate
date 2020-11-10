package com.example.cnufirstmate.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnufirstmate.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {


//    CollectionReference cities = db.collection("cities");
    private FirebaseFirestore mFirestore;
    private RecyclerView recyclerView;
    private Query mQuery;
    private int LIMIT = 100;
    FirebaseFirestore db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                ViewGroup container, Bundle savedInstanceState) {

        initFirestore();
        mFirestore = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    private void initFirestore() {
        db = FirebaseFirestore.getInstance();
//        mQuery = mFirestore.collection("groups")
//                .orderBy("sentat", Query.Direction.DESCENDING)
//                .limit(LIMIT);
//        db.collection("groups"
    }




}
