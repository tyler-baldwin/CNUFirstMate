package com.example.cnufirstmate.ui.Groups;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnufirstmate.ChatGroupWorkRepo;
import com.example.cnufirstmate.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupsHome extends Fragment {
    private FloatingActionButton createGroup;
    private int LIMIT = 100;

    private ChatGroupWorkRepo chatGroupWorkRepo;
    //These are essential to fill out the list of groups in the database
    private RecyclerView grouplist;
    private GroupAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //inflates appropriate view
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        //allows retrieval of info with helper class
        chatGroupWorkRepo = new ChatGroupWorkRepo(FirebaseFirestore.getInstance());
        return view;
    }

    //Sets the adapter for the recycler view, calls the method to fill it out
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        createGroup = getView().findViewById(R.id.newgroup);

        //gets the recycler view and sets a manager to fill it
        grouplist = getView().findViewById(R.id.grouplist);
        grouplist.setLayoutManager(new LinearLayoutManager(getActivity()));
        getChatRooms();
        initUI();
    }

    //calls the database and asks for all the current groups
    private void getChatRooms() {
        chatGroupWorkRepo.getRooms(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("Group Frag", "Listen failed.", e);
                    return;
                }

                List<Group> rooms = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
//                    Toast.makeText(getContext(), doc.getString("name"), Toast.LENGTH_LONG).show();
                    rooms.add(new Group(doc.getId(), doc.getString("name")));
                }

                adapter = new GroupAdapter(rooms,listener);
                grouplist.setAdapter(adapter);
            }
        });
    }

    /*This is a method to launch into the actual chat once clicked*/
    GroupAdapter.OnChatRoomClickListener listener = new GroupAdapter.OnChatRoomClickListener() {
        @Override
        public void onClick(Group group) {
            Intent intent = new Intent(getContext(), GroupActivity.class);
            intent.putExtra(GroupActivity.GROUP_ID, group.getId());
            intent.putExtra(GroupActivity.GROUP_NAME, group.getName());
            startActivity(intent);
        }
    };



    //listens for the creategroup activity launch
    private void initUI() {
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Groups", "Launch create a room screen");
                Intent intent = new Intent(getActivity(), CreateGroup.class);
                startActivity(intent);
            }
        });
    }

}
