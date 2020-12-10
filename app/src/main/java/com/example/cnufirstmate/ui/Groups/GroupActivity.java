package com.example.cnufirstmate.ui.Groups;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cnufirstmate.ChatGroupWorkRepo;
import com.example.cnufirstmate.R;
import com.example.cnufirstmate.ui.Chat.Chat;
import com.example.cnufirstmate.ui.Chat.ChatsAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

public class GroupActivity extends AppCompatActivity {

    public static final String GROUP_ID = "CHAT_ROOM_ID";
    public static final String GROUP_NAME = "CHAT_ROOM_NAME";

    private String groupID;
    private String groupName;
    private RecyclerView chatRecycler;
    private ChatsAdapter adapter;

    private String userId = "";

    private ChatGroupWorkRepo chatGroupWorkRepo;

    private EditText message;
    private ImageButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatGroupWorkRepo = new ChatGroupWorkRepo(FirebaseFirestore.getInstance());

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
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        userId = (account.getEmail());
        return userId;
    }

    private void initUI() {
        message = findViewById(R.id.message_text);
        send = findViewById(R.id.send_message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().isEmpty()) {
                    Toast.makeText(
                            GroupActivity.this,
                            getString(R.string.error_empty_message),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    addMessageToChatRoom();
                }
            }
        });
        chatRecycler = findViewById(R.id.groupRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);

        chatRecycler.setLayoutManager(manager);

    }

    private void addMessageToChatRoom() {
        String chatMessage = message.getText().toString();
        message.setText("");
        send.setEnabled(false);
        chatGroupWorkRepo.addMessageToChatRoom(
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
                                GroupActivity.this,
                                getString(R.string.error_message_failed),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void showChatMessages() {
        chatGroupWorkRepo.getChats(groupID, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("ChatRoomActivity", "Listen failed.", e);
                    return;
                }
                //gets list of chats for this group
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
                //hands over to chatsadapter so it can decide the view for the message
                adapter = new ChatsAdapter(messages, userId);
                chatRecycler.setAdapter(adapter);
            }
        });
    }
}
