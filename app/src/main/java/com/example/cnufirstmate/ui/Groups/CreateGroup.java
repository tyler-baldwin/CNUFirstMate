package com.example.cnufirstmate.ui.Groups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cnufirstmate.ChatGroupRepo;
import com.example.cnufirstmate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateGroup extends AppCompatActivity {
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
                if (isRoomEmptyorInvalid()) {
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
                        Intent intent = new Intent(CreateGroup.this, GroupActivity.class);
                        intent.putExtra(GroupActivity.GROUP_ID, documentReference.getId());
                        intent.putExtra(GroupActivity.GROUP_NAME, groupName.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateGroup.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private boolean isRoomEmptyorInvalid() {
        groupName = findViewById(R.id.room_name);
        EditText ed = findViewById(R.id.members);
        String text = ed.getText().toString();
        String temp;
        ArrayList<String> arr = new ArrayList<>();//Assuming no spaces and user is using one comma between numbers

        while (text != null && text.length() > 0) {
            text = text.replace(" ", "");
            if (text.contains(",")) {
                if (isValidEmail(text.substring(0, text.indexOf(",")))) {
                    arr.add(text.substring(0, text.indexOf(",")));
                    text = text.substring(text.indexOf(",") + 1);
                } else {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return true;
                }
            } else {
                if (isValidEmail(text)) {
                    arr.add(text);
                    break;
                } else {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        //add yourself and then your members
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        arr.add(account.getEmail());
        members = arr;
        return groupName.getText().toString().isEmpty();
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}