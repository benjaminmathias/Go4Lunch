package com.bmathias.go4lunch_.ui.workmates;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.databinding.ActivityChatBinding;
import com.bmathias.go4lunch_.injection.ChatViewModelFactory;
import com.bmathias.go4lunch_.injection.Injection;
import com.bmathias.go4lunch_.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private ChatViewModel chatViewModel;
    private ChatAdapter adapter;
    private ActivityChatBinding chatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
        String userId = getIntent().getExtras().getString("userId");
        this.setupChatViewModel(userId);
        this.setupRecyclerView();
        observeLiveData();
        setupSendButton();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupChatViewModel(String userId) {
        ChatViewModelFactory chatViewModelFactory = Injection.provideChatViewModelFactory(userId);
        this.chatViewModel = new ViewModelProvider(this, chatViewModelFactory).get(ChatViewModel.class);
    }


    private void observeLiveData() {
        chatViewModel.getMessages().observe(this, messages -> {
            adapter.setChatMessageItems(messages);
            if (adapter.getItemCount() == 0) {
                chatBinding.emptyView.getRoot().setVisibility(View.VISIBLE);
                chatBinding.emptyView.title.setText(R.string.no_messages);
                chatBinding.listOfMessages.setVisibility(View.GONE);
            }
        });

        chatViewModel.error.observe(this, error -> {
            if (error == null) return;
            chatBinding.emptyView.getRoot().setVisibility(View.VISIBLE);
            chatBinding.emptyView.title.setText(R.string.messages_error);
            chatBinding.emptyView.subTitle.setText(R.string.empty_dataset_error_description);
            chatBinding.listOfMessages.setVisibility(View.GONE);
            chatBinding.progressbar.setVisibility(View.GONE);
        });

        chatViewModel.showProgress.observe(this, isVisible -> chatBinding.progressbar.setVisibility(isVisible ? View.VISIBLE : View.GONE));
    }

    private void setupRecyclerView() {
        adapter = new ChatAdapter(new ArrayList<>());
        chatBinding.listOfMessages.setAdapter(adapter);
        chatBinding.listOfMessages.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSendButton() {
        chatBinding.input.setHint(R.string.message_hint);

        chatBinding.buttonSendMessage.setOnClickListener(v -> {
            if (!chatBinding.input.getText().toString().equals("")) {
                chatViewModel.addMessage(chatBinding.input.getText().toString());
                chatBinding.input.setText("");
                chatBinding.listOfMessages.smoothScrollToPosition(Objects.requireNonNull(chatBinding.listOfMessages.getAdapter()).getItemCount() - 1);
            }
        });
    }
}