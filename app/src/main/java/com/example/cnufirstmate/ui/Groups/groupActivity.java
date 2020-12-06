package com.example.cnufirstmate.ui.Groups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cnufirstmate.ChatGroupRepo;
import com.example.cnufirstmate.R;
import com.example.cnufirstmate.ui.Chat.Chat;
import com.example.cnufirstmate.ui.Chat.ChatsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class groupActivity extends AppCompatActivity {

    public static final String GROUP_ID = "CHAT_ROOM_ID";
    public static final String GROUP_NAME = "CHAT_ROOM_NAME";

    private String groupID;
    private String groupName;
    private RecyclerView chats;
    private ChatsAdapter adapter;

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
        showChatMessages();
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
        chats = findViewById(R.id.rooms);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        chats.setLayoutManager(manager);
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

    private void showChatMessages() {
        chatGroupRepo.getChats(groupID, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("ChatRoomActivity", "Listen failed.", e);
                    return;
                }

                List<Chat> messages = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    messages.add(
                            new Chat(
                                    doc.getId(),
                                    doc.getString("chat_room_id"),
                                    doc.getString("sender_id"),
                                    doc.getString("message"),
                                    doc.getLong("sent")
                            )
                    );
                }

                adapter = new ChatsAdapter(messages, userId);
                chats.setAdapter(adapter);
            }
        });
    }
}
