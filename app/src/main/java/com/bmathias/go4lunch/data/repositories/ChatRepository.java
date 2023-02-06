package com.bmathias.go4lunch.data.repositories;

import static androidx.lifecycle.Transformations.switchMap;
import static com.bmathias.go4lunch.utils.Constants.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch.data.model.ChatMessage;
import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.data.network.model.DataResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class ChatRepository {

    private static final String MESSAGE_COLLECTION = "messages";
    private static final String USERS_COLLECTION = "users";
    private static volatile ChatRepository instance;
    private final CurrentUserRepository currentUserRepository;
    private final CollectionReference messagesRef;
    private final CollectionReference usersRef;

    private ChatRepository(FirebaseFirestore firebaseFirestore, CurrentUserRepository currentUserRepository) {
        messagesRef = firebaseFirestore.collection(MESSAGE_COLLECTION);
        usersRef = firebaseFirestore.collection(USERS_COLLECTION);
        this.currentUserRepository = currentUserRepository;
    }

    public static ChatRepository getInstance(FirebaseFirestore firebaseFirestore, CurrentUserRepository currentUserRepository) {
        ChatRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ChatRepository.class) {
            if (instance == null) {
                instance = new ChatRepository(firebaseFirestore, currentUserRepository);
            }
            return instance;
        }
    }

    public LiveData<DataResult<List<ChatMessage>>> getChatMessages(String toUserId) {
        return switchMap(getWorkmateUsernameFromUserIdDataResult(toUserId), dataResult -> {
            if (!dataResult.isSuccess()) {
                return new MutableLiveData<>(new DataResult<>(dataResult.getError()));
            }
            String senderName = dataResult.getData();
            return getChatMessagesData(toUserId, senderName);
        });
    }

    private LiveData<DataResult<List<ChatMessage>>> getChatMessagesData(String toUserId, String senderName) {

        MutableLiveData<DataResult<List<ChatMessage>>> _messages = new MutableLiveData<>();

        messagesRef.whereIn("receiverId", Arrays.asList(toUserId, currentUserRepository.getCurrentUserId()))
                .orderBy("messageTime", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d(TAG, "Listen failed.", error);
                        return;
                    }
                    List<ChatMessage> messages = Objects.requireNonNull(value).toObjects(ChatMessage.class);
                    List<ChatMessage> filteredMessages = new ArrayList<>();
                    for (ChatMessage message : messages) {
                        if (message.getSenderId().equals(toUserId) || message.getSenderId().equals(currentUserRepository.getCurrentUserId())) {
                            if (message.getSenderId().equals(toUserId)) {
                                message.setSenderName(senderName);
                            } else {
                                message.setCurrentUserSender(true);
                            }
                            filteredMessages.add(message);
                        }
                    }
                    _messages.postValue(new DataResult<>(filteredMessages));
                });
        return _messages;
    }

    private LiveData<DataResult<String>> getWorkmateUsernameFromUserIdDataResult(String userId) {
        MutableLiveData<DataResult<String>> _userName = new MutableLiveData<>();

        usersRef.document(userId)
                .get()
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        DocumentSnapshot document = userTask.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            assert user != null;
                            _userName.setValue(new DataResult<>(user.getUserName()));
                        } else {
                            Exception exception = userTask.getException();
                            DataResult<String> dataResult = new DataResult<>(exception);
                            Log.e("onError", "======================================================");
                            Log.e("ChatRepository", "Error getting documents: ", userTask.getException());
                            Objects.requireNonNull(exception).printStackTrace();
                            Log.e("onError", "======================================================");
                            _userName.postValue(dataResult);
                        }
                    }
                });
        return _userName;
    }

    public void createMessage(String textMessage, String toUserId) {
        Date date = new Date(System.currentTimeMillis());
        ChatMessage message = new ChatMessage(textMessage, currentUserRepository.getCurrentUserId(), toUserId, date);

        messagesRef.add(message)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }
}
