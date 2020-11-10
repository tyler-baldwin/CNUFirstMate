package com.example.cnufirstmate.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cnufirstmate.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatFragment extends Fragment {

    private ChatViewModel chatViewModel;
//    CollectionReference cities = db.collection("cities");
    private FirebaseFirestore mFirestore;
    FirebaseFirestore db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                ViewGroup container, Bundle savedInstanceState) {
            chatViewModel =
                    ViewModelProviders.of(this).get(ChatViewModel.class);
            View root = inflater.inflate(R.layout.fragment_chat, container, false);

        initFirestore();
        mFirestore = FirebaseFirestore.getInstance();

//        Do
        return root;
    }

    private void initFirestore() {
        db = FirebaseFirestore.getInstance();
    }




}
