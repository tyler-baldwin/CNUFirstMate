package com.example.cnufirstmate.ui.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cnufirstmate.R;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private List<ChatRoom> chatRooms;
    private OnChatRoomClickListener listener;
    public ChatRoomAdapter(List<ChatRoom> chatRooms, OnChatRoomClickListener listener) {
        this.chatRooms = chatRooms;
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
        holder.bind(chatRooms.get(position));
    }

    public interface OnChatRoomClickListener {
        void onClick(ChatRoom chatRoom);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ChatRoom chatRoom;

        public ChatRoomViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_chat_room_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(chatRoom);
                }
            });
        }

        public void bind(ChatRoom chatRoom) {
            this.chatRoom = chatRoom;
            name.setText(chatRoom.getName());
        }
    }
}
