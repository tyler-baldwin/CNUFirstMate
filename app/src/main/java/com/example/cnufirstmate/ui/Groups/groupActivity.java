package com.example.cnufirstmate.ui.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cnufirstmate.R;

public class groupActivity extends AppCompatActivity {

    public static final String GROUP_ID = "CHAT_ROOM_ID";
    public static final String GROUP_NAME = "CHAT_ROOM_NAME";

    private String groupID;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupID = extras.getString(GROUP_ID, "");
            groupName = extras.getString(GROUP_NAME, "");
        }

        if (getSupportActionBar() != null) {
            setTitle(groupName);
        }
    }

}