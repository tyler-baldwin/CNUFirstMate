package com.example.cnufirstmate.ui.Groups;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.cnufirstmate.R;

public class createGroup extends AppCompatActivity {
    private EditText groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupName = findViewById(R.id.room_name);
        setContentView(R.layout.activity_create_group);
    }
}