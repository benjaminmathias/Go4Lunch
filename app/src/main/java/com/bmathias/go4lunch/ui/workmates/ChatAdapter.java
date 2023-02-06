package com.bmathias.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.model.ChatMessage;
import com.bmathias.go4lunch.databinding.MessageBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    //FOR DATA
    private final List<ChatMessage> chatMessageList;

    public ChatAdapter(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setChatMessageItems(List<ChatMessage> chatMessages) {
        this.chatMessageList.clear();
        this.chatMessageList.addAll(chatMessages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(MessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatMessage message = chatMessageList.get(position);

        // User name
        if (message.isCurrentUserSender()) {
            holder.binding.messageUser.setTextColor(Color.BLUE);
            holder.binding.line.setBackgroundColor(Color.BLUE);
            holder.binding.messageUser.setText(R.string.chat_activity_username);
        } else {
            holder.binding.messageUser.setTextColor(Color.RED);
            holder.binding.line.setBackgroundColor(Color.RED);
            holder.binding.messageUser.setText(message.getSenderName());
        }

        // Update message TextView
        holder.binding.messageText.setText(message.getBody());

        // Update date TextView
        if (message.getMessageTime() != null)
            holder.binding.messageTime.setText(this.convertDateToHour(message.getMessageTime()));

    }

    private String convertDateToHour(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }

    @Override
    public int getItemCount() {
        return chatMessageList == null ? 0 : chatMessageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MessageBinding binding;

        public ViewHolder(MessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
