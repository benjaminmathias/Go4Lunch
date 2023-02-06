package com.bmathias.go4lunch.viewmodel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch.LiveDataTestUtil;
import com.bmathias.go4lunch.data.model.ChatMessage;
import com.bmathias.go4lunch.data.network.model.DataResult;
import com.bmathias.go4lunch.data.repositories.ChatRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class ChatViewModelTest {

   @Rule
   public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

   private ChatViewModel chatViewModel;

   private final ChatRepository chatRepository = Mockito.mock(ChatRepository.class);

   private final String userId = "123";


   @Before
   public void setup(){
      chatViewModel = new ChatViewModel(chatRepository, userId);
   }

   @Test
   public void getMessagesShouldReturnResultWhenRepoSuccess() throws InterruptedException {
      List<ChatMessage> chatMessages = new ArrayList<>();
      DataResult<List<ChatMessage>> dataResult = new DataResult<>(chatMessages);
      MutableLiveData<DataResult<List<ChatMessage>>> _chatMessages = new MutableLiveData<>(dataResult);

      when(chatRepository.getChatMessages(userId)).thenReturn(_chatMessages);
      List<ChatMessage> result = LiveDataTestUtil.getOrAwaitValue(chatViewModel.getMessages());

      Boolean progress = LiveDataTestUtil.getOrAwaitValue(chatViewModel.showProgress);

      Assert.assertNotEquals(null, result);
      Assert.assertFalse(progress);
   }

   @Test
   public void getMessagesShouldReturnErrorWhenRepoReturnException() throws InterruptedException {
      String userId = "123";
      TimeoutException exception = new TimeoutException("I'm sorry");
      DataResult<List<ChatMessage>> dataResult = new DataResult<>(exception);
      MutableLiveData<DataResult<List<ChatMessage>>> _chatMessages = new MutableLiveData<>(dataResult);

      when(chatRepository.getChatMessages(userId)).thenReturn(_chatMessages);
      List<ChatMessage> result = LiveDataTestUtil.getOrAwaitValue(chatViewModel.getMessages());

      Boolean progress = LiveDataTestUtil.getOrAwaitValue(chatViewModel.showProgress);

      Assert.assertEquals(result, Collections.emptyList());
      Assert.assertEquals("I'm sorry",
              Objects.requireNonNull(chatRepository.getChatMessages(userId).getValue()).getError().getMessage());
      Assert.assertFalse(progress);
   }
}
