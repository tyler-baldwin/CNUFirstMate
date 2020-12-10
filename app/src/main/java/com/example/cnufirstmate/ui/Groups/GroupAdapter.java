package com.example.cnufirstmate.ui.Groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cnufirstmate.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ChatRoomViewHolder> {

    private List<Group> groups;
    private OnChatRoomClickListener listener;
    public GroupAdapter(List<Group> groups, OnChatRoomClickListener listener) {
        this.groups = groups;
        this.listener = listener;
    }

    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_chat_room,
                parent,
                false
        );
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatRoomViewHolder holder, int position) {
        holder.bind(groups.get(position));
    }

    public interface OnChatRoomClickListener {
        void onClick(Group group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        Group group;

        public ChatRoomViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_room_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(group);
                }
            });
        }

        public void bind(Group group) {
            this.group = group;
            name.setText(group.getName());
        }
    }
}
