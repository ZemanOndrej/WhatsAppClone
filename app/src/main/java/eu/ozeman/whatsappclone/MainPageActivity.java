package eu.ozeman.whatsappclone;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import eu.ozeman.whatsappclone.Chat.Chat;
import eu.ozeman.whatsappclone.Chat.ChatListAdapter;


public class MainPageActivity extends AppCompatActivity {

    private RecyclerView chatListView;
    private RecyclerView.Adapter chatListAdapter;
    private RecyclerView.LayoutManager chatListLayoutManager;
    private ArrayList<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button logout = findViewById(R.id.logout);
        Button findUsers = findViewById(R.id.find_users);
        chatList = new ArrayList<>();
        findUsers.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), FindUserActivity.class)));
        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        getPermissions();
        initializeRecycleView();
        getUserChats();
    }

    private void getUserChats() {
        DatabaseReference userChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
        userChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childChat : dataSnapshot.getChildren()) {
                        Chat newChat = new Chat(childChat.getKey());
                        if (!chatList.contains(newChat)) {
                            chatList.add(newChat);
                            chatListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeRecycleView() {
        chatListView = findViewById(R.id.chatList);
        chatListView.setNestedScrollingEnabled(false);
        chatListView.setHasFixedSize(false);
        chatListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        chatListView.setLayoutManager(chatListLayoutManager);
        chatListAdapter = new ChatListAdapter(chatList);
        chatListView.setAdapter(chatListAdapter);
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        }
    }
}
