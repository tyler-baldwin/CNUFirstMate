package com.example.cnufirstmate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.cnufirstmate.ChatGroupWorkRepo;
import com.example.cnufirstmate.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {
    String groupID;
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
                break;
        }
        return true;
    }


}