package com.example.cnufirstmate.ui.Chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnufirstmate.ChatGroupWorkRepo;
import com.example.cnufirstmate.R;
import com.example.cnufirstmate.ui.Groups.GroupAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {


    private FirebaseFirestore mFirestore;
    private RecyclerView recyclerView;
    private Query mQuery;
    private int LIMIT = 100;
    FirebaseFirestore db;
    private ChatGroupWorkRepo chatGroupWorkRepo;
    private GroupAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        chatGroupWorkRepo = new ChatGroupWorkRepo(FirebaseFirestore.getInstance());
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
