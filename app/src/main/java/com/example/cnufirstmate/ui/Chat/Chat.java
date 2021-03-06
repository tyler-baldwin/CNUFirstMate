package com.example.cnufirstmate.ui.Chat;

public class Chat {
    private String id;
    private String chatRoomId;
    private String senderId;
    private String message;
    private long sent;



    private String nick;

    public Chat(String id, String chatRoomId, String senderId, String message, long sent,String nick) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.message = message;
        this.sent = sent;
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSent() {
        return sent;
    }

    public void setSent(long sent) {
        this.sent = sent;
    }
}
