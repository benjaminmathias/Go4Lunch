package com.bmathias.go4lunch_.viewmodel;

import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.LiveDataTestUtil;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;

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

public class WorkmatesViewModelTest {
    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();
    private WorkmatesViewModel workmatesViewModel;
    private final UsersRepository usersRepository = Mockito.mock(UsersRepository.class);
    private final CurrentUserRepository currentUserRepository = Mockito.mock(CurrentUserRepository.class);

    @Before
    public void setup() {
        workmatesViewModel = new WorkmatesViewModel(usersRepository, currentUserRepository);
    }

    @Test
    public void workmatesViewModelGetDataUsersFromDatabaseSuccess() throws InterruptedException {
        List<User> users = new ArrayList<>();
        DataResult<List<User>> dataResult = new DataResult<>(users);

        MutableLiveData<DataResult<List<User>>> _users = new MutableLiveData<>();
        _users.postValue(dataResult);

        when(usersRepository.getDataUsers()).thenReturn(_users);
        List<User> result = LiveDataTestUtil.getOrAwaitValue(workmatesViewModel.getUsers());

        Boolean progress = LiveDataTestUtil.getOrAwaitValue(workmatesViewModel.showProgress);

        Assert.assertNotEquals(null, result);
        Assert.assertFalse(progress);
    }

    @Test
    public void workmatesViewModelGetDataUsersFromDatabaseError() throws InterruptedException {
        TimeoutException exception = new TimeoutException("I'm sorry");
        DataResult<List<User>> dataResult = new DataResult<>(exception);

        MutableLiveData<DataResult<List<User>>> _users = new MutableLiveData<>();
        _users.postValue(dataResult);

        when(usersRepository.getDataUsers()).thenReturn(_users);
        List<User> result = LiveDataTestUtil.getOrAwaitValue(workmatesViewModel.getUsers());

        Boolean progress = LiveDataTestUtil.getOrAwaitValue(workmatesViewModel.showProgress);

        Assert.assertEquals(result, Collections.emptyList());
        Assert.assertEquals("I'm sorry",
                Objects.requireNonNull(usersRepository.getDataUsers().getValue()).getError().getMessage());
        Assert.assertFalse(progress);
    }

    @Test
    public void workmatesViewModelGetCurrentUserIdSuccess() {
        String userId = "123";

        when(currentUserRepository.getCurrentUserId()).thenReturn(userId);
        String currentUserId = workmatesViewModel.getCurrentUserId();

        Assert.assertEquals(userId, currentUserId);
    }
}