package com.bmathias.go4lunch.viewmodel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch.LiveDataTestUtil;
import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.data.repositories.AuthRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Objects;

public class AuthViewModelTest {

   @Rule
   public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

   private AuthViewModel authViewModel;

   private final AuthRepository authRepository = Mockito.mock(AuthRepository.class);

   @Before
   public void setUp(){
      authViewModel = new AuthViewModel(authRepository);
   }

   @Test
   public void signWithAuthCredentialShouldReturnSuccessWhenRepoReturnUser() throws InterruptedException {
      User dataUser = new User();
      dataUser.setUserId("123");
      MutableLiveData<User> authUser = new MutableLiveData<>(dataUser);

      when(authRepository.firebaseSignIn(Mockito.any())).thenReturn(authUser);
      authViewModel.signWithAuthCredential(Mockito.any());

      User user = LiveDataTestUtil.getOrAwaitValue(authViewModel.authenticatedUserLiveData);

      Assert.assertEquals(dataUser, user);
      Assert.assertEquals(user.getUserId(), Objects.requireNonNull(authUser.getValue()).getUserId());
   }

   @Test
   public void signWithAuthCredentialShouldReturnErrorWhenRepoDoesntReturnUser() throws InterruptedException {
      MutableLiveData<User> authUser = new MutableLiveData<>(null);

      when(authRepository.firebaseSignIn(Mockito.any())).thenReturn(authUser);
      authViewModel.signWithAuthCredential(Mockito.any());

      User user = LiveDataTestUtil.getOrAwaitValue(authViewModel.authenticatedUserLiveData);

      Assert.assertNull(user);
   }
}
