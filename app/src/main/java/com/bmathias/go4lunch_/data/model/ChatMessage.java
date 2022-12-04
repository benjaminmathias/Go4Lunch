package com.bmathias.go4lunch_.data.model;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {

    private String body;
    private String senderId;
    private String senderName;
    private String receiverId;
    private Date messageTime;
    private boolean isCurrentUserSender;

    public ChatMessage(String messageText, String senderId, String receiverId, Date sentAt) {
        this.body = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageTime = sentAt;
    }

    public ChatMessage() {
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isCurrentUserSender() {
        return isCurrentUserSender;
    }

    public void setCurrentUserSender(boolean currentUserSender) {
        isCurrentUserSender = currentUserSender;
    }
}
