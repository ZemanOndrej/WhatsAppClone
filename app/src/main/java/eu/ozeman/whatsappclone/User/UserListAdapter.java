package eu.ozeman.whatsappclone.User;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import eu.ozeman.whatsappclone.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {


    private final ArrayList<User> userList;

    public UserListAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }


    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        return new UserListViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder userListViewHolder, final int position) {
        userListViewHolder.name.setText(userList.get(position).getName());
        userListViewHolder.phone.setText(userList.get(position).getPhone());
        userListViewHolder.layout.setOnClickListener(v -> {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            String key = db.child("chat").push().getKey();
            db.child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
            db.child("user").child(userList.get(position).getUid()).child("chat").child(key).setValue(true);

        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class UserListViewHolder extends RecyclerView.ViewHolder {

        public TextView name, phone;
        public LinearLayout layout;

        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);

        }
    }
}
