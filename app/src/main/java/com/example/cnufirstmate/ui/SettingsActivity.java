package com.example.cnufirstmate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.cnufirstmate.ChatGroupWorkRepo;
import com.example.cnufirstmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    String groupID;
    String TAG = "Settings";
    String groupname;
    ChatGroupWorkRepo chatGroupWorkRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatGroupWorkRepo = new ChatGroupWorkRepo(FirebaseFirestore.getInstance());
        setContentView(R.layout.settings_activity);
        Bundle extras = getIntent().getExtras();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(extras.getString("groupName") + " Settings");
        }

        groupID = extras.getString("groupId");

    }
    @Override
    protected void onResume()
    {
        super.onResume();
//        String newMems = getMembers(groupID);
//        TextView m = findViewById(R.id.settingsMems);
//        m.setText(newMems);
        getMembers(groupID);
    }
    public void getMembers(String group){
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Groups").document(group);
        final String[] newMems = new String[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList mems = (ArrayList) document.get("Members");
                        String fin = "Members\n";
                        while(mems.size() > 0){
                            Log.d(TAG, fin);
                            fin += mems.get(mems.size() -1);
                            fin += "\n";
                            mems.remove(mems.size() -1);
                        }
                        TextView m = findViewById(R.id.settingsMems);
                        m.setText(fin);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_group_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.create_room:
                EditText add = findViewById(R.id.memadd);
                String addString = add.getText().toString();
                chatGroupWorkRepo.addMemberToGroup(addString,groupID);
                add.setText("");
                EditText remove = findViewById(R.id.removeMem);
                String remString = remove.getText().toString();
                chatGroupWorkRepo.removeMemberToGroup(remString,groupID);
                remove.setText("");
                getMembers(groupID);
                break;
        }
        return true;
    }


}