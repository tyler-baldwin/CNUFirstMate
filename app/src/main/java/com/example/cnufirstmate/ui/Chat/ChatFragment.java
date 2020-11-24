package com.example.cnufirstmate.ui.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnufirstmate.ChatGroupRepo;
import com.example.cnufirstmate.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {


    private FirebaseFirestore mFirestore;
    private RecyclerView recyclerView;
    private Query mQuery;
    private int LIMIT = 100;
    FirebaseFirestore db;
    private ChatGroupRepo chatGroupRepo;
    private ChatRoomAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        chatGroupRepo = new ChatGroupRepo(FirebaseFirestore.getInstance());
        initFirestore();
        mFirestore = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
//        recyclerView = getView().findViewById(R.id.rooms);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        getChatRooms();
        return view;
    }

//    private void getChatRooms() {
//        chatGroupRepo.getRooms(new EventListener<QuerySnapshot>() {
//
//            @Override
//            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.e("ChatFrag", "Listen failed.", e);
//                    return;
//                }
//
//                List<ChatRoom> rooms = new ArrayList<>();
//                for (QueryDocumentSnapshot doc : snapshots) {
//                    Toast.makeText(getContext(), doc.getString("name"), Toast.LENGTH_LONG).show();
//                    rooms.add(new ChatRoom(doc.getId(), doc.getString("name")));
//                }
//
//                adapter = new ChatRoomAdapter(rooms);
//                recyclerView.setAdapter(adapter);
//            }
//        });
//    }


    private void initFirestore() {
//        db = FirebaseFirestore.getInstance();
//        mQuery = mFirestore.collection("groups")
//                .orderBy("sentat", Query.Direction.DESCENDING)
//                .limit(LIMIT);
//        db.collection("groups"
    }


}
