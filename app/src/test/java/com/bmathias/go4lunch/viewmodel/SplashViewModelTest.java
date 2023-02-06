package com.bmathias.go4lunch.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import static org.mockito.Mockito.when;

import com.bmathias.go4lunch.LiveDataTestUtil;
import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.data.repositories.CurrentUserRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;


public class SplashViewModelTest {

    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    private SplashViewModel splashViewModel;

    private final CurrentUserRepository currentUserRepository = Mockito.mock(CurrentUserRepository.class);

    @Before
    public void setup() {
        splashViewModel = new SplashViewModel(currentUserRepository);
    }

    @Test
    public void splashViewModelCheckIfUserIsAuthenticatedSuccess() throws InterruptedException {
        User dataUser = new User();
        dataUser.setUserId("123");
        MutableLiveData<User> authUser = new MutableLiveData<>();
        authUser.postValue(dataUser);

        when(currentUserRepository.getCurrentUser()).thenReturn(authUser);
        splashViewModel.checkIfUserIsAuthenticated();

        User user = LiveDataTestUtil.getOrAwaitValue(splashViewModel.isUserAuthenticatedLiveData);

        Assert.assertEquals(user, authUser.getValue());
        Assert.assertEquals(user.getUserId(), dataUser.getUserId());
    }

   @Test
   public void splashViewModelCheckIfUserIsAuthenticatedNull() throws InterruptedException {
      MutableLiveData<User> authUser = new MutableLiveData<>();
      authUser.postValue(null);

      when(currentUserRepository.getCurrentUser()).thenReturn(authUser);
      splashViewModel.checkIfUserIsAuthenticated();

      User user = LiveDataTestUtil.getOrAwaitValue(splashViewModel.isUserAuthenticatedLiveData);

      Assert.assertNull(authUser.getValue());
      Assert.assertNull(user);
   }
}
