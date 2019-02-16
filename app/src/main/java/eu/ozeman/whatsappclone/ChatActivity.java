package eu.ozeman.whatsappclone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eu.ozeman.whatsappclone.Message.Message;
import eu.ozeman.whatsappclone.Message.MessageListAdapter;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView messageListView;
    private RecyclerView.Adapter messageListAdapter;
    private RecyclerView.LayoutManager messageListLayoutManager;
    private ArrayList<Message> messageList;

    private Button sendButton;
    private EditText messageEdit;

    private String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendButton = findViewById(R.id.sendButton);
        messageEdit = findViewById(R.id.messageEdit);
        messageList = new ArrayList<>();

        chatID = getIntent().getExtras().getString("chatID");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        initializeRecycleView();
        getChatMessages();
    }

    private void sendMessage() {
        String text = messageEdit.getText().toString();
        if (!text.isEmpty()) {
            DatabaseReference newMessageDB = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID).push();
            Map newMessageMap = new HashMap<>();
            newMessageMap.put("text", text);
            newMessageMap.put("sender", FirebaseAuth.getInstance().getUid());
            newMessageDB.updateChildren(newMessageMap);
        }
        messageEdit.setText(null);
    }

    private void initializeRecycleView() {
        messageListView = findViewById(R.id.chatMessageList);
        messageListView.setNestedScrollingEnabled(false);
        messageListView.setHasFixedSize(false);
        messageListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        messageListView.setLayoutManager(messageListLayoutManager);
        messageListAdapter = new MessageListAdapter(messageList);
        messageListView.setAdapter(messageListAdapter);
    }

    private void getChatMessages() {
        FirebaseDatabase.getInstance().getReference().child("chat").child(chatID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String text = "", senderID = "";
                    if (dataSnapshot.child("text").getValue() != null) {
                        text = dataSnapshot.child("text").getValue().toString();
                    }
                    if (dataSnapshot.child("sender").getValue() != null) {
                        senderID = dataSnapshot.child("sender").getValue().toString();
                    }
                    messageList.add(new Message(senderID, text, dataSnapshot.getKey()));
                    messageListLayoutManager.scrollToPosition(messageList.size() - 1);
                    messageListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
