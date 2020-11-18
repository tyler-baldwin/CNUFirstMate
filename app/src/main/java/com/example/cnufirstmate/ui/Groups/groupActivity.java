package com.example.cnufirstmate.ui.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cnufirstmate.R;

public class groupActivity extends AppCompatActivity {

    public static final String GROUP_ID = "CHAT_ROOM_ID";
    public static final String GROUP_NAME = "CHAT_ROOM_NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }
}