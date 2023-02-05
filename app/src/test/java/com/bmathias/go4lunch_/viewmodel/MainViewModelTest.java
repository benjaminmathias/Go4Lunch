package com.bmathias.go4lunch_.viewmodel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.LiveDataTestUtil;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class MainViewModelTest {

   @Rule
   public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

   private MainViewModel mainViewModel;

   private final CurrentUserRepository currentUserRepository = Mockito.mock(CurrentUserRepository.class);

   @Before
   public void setup(){
      mainViewModel = new MainViewModel(currentUserRepository);
   }

   @Test
   public void getUserFromDatabaseShouldReturnUserWhenRepoReturnSuccess() throws InterruptedException{
      User dataUser = new User();
      dataUser.setUserId("123");
      MutableLiveData<User> authUser = new MutableLiveData<>();
      authUser.postValue(dataUser);

      when(currentUserRepository.getCurrentUser()).thenReturn(authUser);
      mainViewModel.getUserFromDatabase();

      User user = LiveDataTestUtil.getOrAwaitValue(mainViewModel.currentUser);

      Assert.assertEquals(user, authUser.getValue());
      Assert.assertEquals(user.getUserId(), dataUser.getUserId());
   }

   @Test
   public void getUserFromDatabaseShouldReturnNullWhenRepoReturnError() throws InterruptedException{
      MutableLiveData<User> authUser = new MutableLiveData<>();
      authUser.postValue(null);

      when(currentUserRepository.getCurrentUser()).thenReturn(authUser);
      mainViewModel.getUserFromDatabase();

      User user = LiveDataTestUtil.getOrAwaitValue(mainViewModel.currentUser);

      Assert.assertNull(authUser.getValue());
      Assert.assertNull(user);
   }
}
