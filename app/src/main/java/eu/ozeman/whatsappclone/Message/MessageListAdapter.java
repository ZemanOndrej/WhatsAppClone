package eu.ozeman.whatsappclone.Message;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import eu.ozeman.whatsappclone.R;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {

    private ArrayList<Message> messageList;

    public MessageListAdapter(ArrayList<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageListAdapter.MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new MessageListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder viewHolder, int position) {
        viewHolder.text.setText(messageList.get(position).getText());
        viewHolder.username.setText(messageList.get(position).getAuthorName());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder {

        public TextView username, text;
        public LinearLayout layout;

        public MessageListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            username = itemView.findViewById(R.id.messageUsername);
            text = itemView.findViewById(R.id.messageText);

        }
    }
}
