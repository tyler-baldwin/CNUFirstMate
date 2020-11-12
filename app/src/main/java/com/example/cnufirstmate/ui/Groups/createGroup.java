package com.example.cnufirstmate.ui.Groups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cnufirstmate.ChatGroupRepo;
import com.example.cnufirstmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class createGroup extends AppCompatActivity {
    private EditText groupName;
    private ArrayList<String> members;

    private ChatGroupRepo chatGroupRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupName = findViewById(R.id.room_name);
        setContentView(R.layout.activity_create_group);

        chatGroupRepo = new ChatGroupRepo(FirebaseFirestore.getInstance());

        setTitle(getString(R.string.create_room));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_close_black_18dp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.create_room:
                if (isRoomEmpty()) {
                    Toast.makeText(this, getString(R.string.error_empty_room), Toast.LENGTH_SHORT).show();
                } else {
                    createGroup();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void createGroup() {
        chatGroupRepo.createGroup(
                groupName.getText().toString(), members,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(createGroup.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private boolean isRoomEmpty() {
        groupName = findViewById(R.id.room_name);
        EditText ed = findViewById(R.id.members);
        String text = ed.getText().toString();
        ArrayList<String> arr = new ArrayList<>();//Assuming no spaces and user is using one comma between numbers
//        int i=0;
        Toast.makeText(createGroup.this, text, Toast.LENGTH_SHORT).show();

        while(text!=null && text.length()>0) {
            if(text.contains(",")){
                arr.add(text.substring(0,text.indexOf(",")));
                text = text.substring(text.indexOf(",")+1);
            }
            else {
                arr.add(text);
                break;
            }
//            i++;
        }
        members = arr;
        return groupName.getText().toString().isEmpty();
    }
}