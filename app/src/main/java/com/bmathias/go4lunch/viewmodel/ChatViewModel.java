package com.bmathias.go4lunch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.bmathias.go4lunch.data.model.ChatMessage;
import com.bmathias.go4lunch.data.repositories.ChatRepository;

import java.util.Collections;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private static final String TAG = "ChatViewModel :";

    private final ChatRepository chatRepository;
    private final String userId;

    private final MutableLiveData<Boolean> _showProgress = new MutableLiveData<>();
    public LiveData<Boolean> showProgress = _showProgress;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    public ChatViewModel(ChatRepository chatRepository, String userId) {
        this.chatRepository = chatRepository;
        this.userId = userId;
    }

    public LiveData<List<ChatMessage>> getMessages() {
        _showProgress.postValue(true);
        return Transformations.map(chatRepository.getChatMessages(userId), result -> {
            _showProgress.postValue(false);

            if (result.isSuccess()) {
                Log.e(TAG, "success");
                _error.postValue(null);
                return result.getData();
            } else {
                Log.e(TAG, result.getError().getMessage());
                _error.postValue(result.getError().getMessage());
                return Collections.emptyList();
            }
        });
    }

    public void addMessage(String textMessage){
        chatRepository.createMessage(textMessage, userId);
    }
}
