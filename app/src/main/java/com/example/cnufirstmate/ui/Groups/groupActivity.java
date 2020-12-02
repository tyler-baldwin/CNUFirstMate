package com.example.cnufirstmate.ui.Groups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cnufirstmate.ChatGroupRepo;
import com.example.cnufirstmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class groupActivity extends AppCompatActivity {

    public static final String GROUP_ID = "CHAT_ROOM_ID";
    public static final String GROUP_NAME = "CHAT_ROOM_NAME";

    private String groupID;
    private String groupName;

    private static final String CURRENT_USER_KEY = "CURRENT_USER_KEY";

    private String userId = "";

    private ChatGroupRepo chatGroupRepo;

    private EditText message;
    private ImageButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatGroupRepo = new ChatGroupRepo(FirebaseFirestore.getInstance());

        userId = getCurrentUserKey();

        setContentView(R.layout.activity_group);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupID = extras.getString(GROUP_ID, "");
            groupName = extras.getString(GROUP_NAME, "");
        }

        if (getSupportActionBar() != null) {
            setTitle(groupName);
        }
        initUI();
    }

    private String getCurrentUserKey() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(CURRENT_USER_KEY, "");
    }

    private void initUI() {
        message = findViewById(R.id.message_text);
        send = findViewById(R.id.send_message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(
                            groupActivity.this,
                            getString(R.string.error_empty_message),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    addMessageToChatRoom();
                }
            }
        });
    }

    private void addMessageToChatRoom() {
        String chatMessage = message.getText().toString();
        message.setText("");
        send.setEnabled(false);
        chatGroupRepo.addMessageToChatRoom(
                groupID,
                userId,
                chatMessage,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        send.setEnabled(true);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        send.setEnabled(true);
                        Toast.makeText(
                                groupActivity.this,
                                getString(R.string.error_message_failed),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

}