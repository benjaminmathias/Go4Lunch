package com.bmathias.go4lunch_.viewmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.LiveDataTestUtil;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.repositories.CurrentUserRepository;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

public class WorkmatesViewModelTest {

    @Rule
    InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();
    private WorkmatesViewModel viewModel;
    private User user1;
    private User user2;
    private LiveData<DataResult<List<User>>> result;
    private MutableLiveData<DataResult<List<User>>> _users = new MutableLiveData<>();
    @Mock
    private UsersRepository usersRepository = mock(UsersRepository.class);

    @Mock
    private CurrentUserRepository currentUserRepository = mock(CurrentUserRepository.class);


    @Before
    public void setup(){
      /*  MockitoAnnotations.initMocks(this);
        user1 = new User("uid", "name", "email", "photoUrl", "selectedRestaurantId", "selectedRestaurantName");
        user2 = new User("uid", "name", "email", "photoUrl", "selectedRestaurantId", "selectedRestaurantName");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        _users.postValue(users);
        when(usersRepository.getDataUsers()).thenReturn(result);*/
        //viewModel = new WorkmatesViewModel(usersRepository);
    }



    @Test
    public void workmatesViewModelGetUsersSuccess() throws InterruptedException {
        List<User> users = new ArrayList<>();
        DataResult<List<User>> dataResult = new DataResult<>(users);

       // MutableLiveData<DataResult<List<User>>> usersList = new MutableLiveData<>();

        when(usersRepository.getDataUsers().getValue()).thenReturn(dataResult);
        DataResult<List<User>> result = LiveDataTestUtil.getOrAwaitValue(usersRepository.getDataUsers());

        viewModel.getDataUsersFromDatabase();
        assertTrue(result.isSuccess());
        assertNotEquals(null, viewModel.getUsers());
        assertNull(viewModel.error.getValue());
    }

    @Test
    public void workmatesViewModelGetUsersError(){

        viewModel.getDataUsersFromDatabase().getValue();

    }

}