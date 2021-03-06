package eu.ozeman.whatsappclone.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import eu.ozeman.whatsappclone.ChatActivity;
import eu.ozeman.whatsappclone.R;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    private final ArrayList<Chat> chatList;

    public ChatListAdapter(ArrayList<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new ChatListAdapter.ChatListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder viewHolder, final int position) {
        viewHolder.title.setText(chatList.get(position).getChatId());
        viewHolder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("chatID", chatList.get(viewHolder.getAdapterPosition()).getChatId());
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public LinearLayout layout;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            title = itemView.findViewById(R.id.title);

        }
    }
}
