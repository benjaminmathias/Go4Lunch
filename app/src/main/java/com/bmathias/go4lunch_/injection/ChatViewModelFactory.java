package com.bmathias.go4lunch_.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bmathias.go4lunch_.data.repositories.ChatRepository;
import com.bmathias.go4lunch_.viewmodel.ChatViewModel;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

   private final ChatRepository chatDatasource;
   private final String userId;

   public ChatViewModelFactory(ChatRepository chatDatasource, String userId) {
      this.chatDatasource = chatDatasource;
      this.userId = userId;
   }

   @NonNull
   @Override
   public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
      if (aClass.isAssignableFrom(ChatViewModel.class)){
         return (T) new ChatViewModel(chatDatasource, userId);
      }
      throw new IllegalArgumentException("Unknown ViewModel class");
   }
}
