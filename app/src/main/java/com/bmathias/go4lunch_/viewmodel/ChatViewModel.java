package com.bmathias.go4lunch_.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch_.data.model.ChatMessage;
import com.bmathias.go4lunch_.data.repositories.ChatRepository;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private static final String TAG = "ChatViewModel :";

    private final ChatRepository chatRepository;
    private final String userId;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;
    public LiveData<String> error;

    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>();

    public ChatViewModel(ChatRepository chatRepository, String userId) {
        this.chatRepository = chatRepository;
        this.userId = userId;
        loadMessages();
    }

    public void loadMessages() {
        _showProgress.postValue(true);
        error = Transformations.map(chatRepository.getChatMessages(userId), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                Log.e(TAG, "success");
                messages.postValue(result.getData());
                return null;
            } else {
                Log.e(TAG, result.getError().getMessage());
                return result.getError().getMessage();
            }
        });
    }

    public void addMessage(String textMessage){
        chatRepository.createMessage(textMessage, userId);
    }

    public LiveData<List<ChatMessage>> getMessages(){
        return messages;
    }
}
