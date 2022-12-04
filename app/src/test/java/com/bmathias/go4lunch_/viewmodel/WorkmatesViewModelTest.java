package com.bmathias.go4lunch_.viewmodel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.data.network.model.DataResult;
import com.bmathias.go4lunch_.data.repositories.UsersRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest {

    private WorkmatesViewModel viewModel;
    private User user1;
    private User user2;
    private LiveData<DataResult<List<User>>> result;
    // private MutableLiveData<DataResult<List<User>>> _users = new MutableLiveData<>();

    @Mock
    private UsersRepository usersRepository;

    @Rule
    InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
      /*  user1 = new User("uid", "name", "email", "photoUrl", "selectedRestaurantId", "selectedRestaurantName");
        user2 = new User("uid", "name", "email", "photoUrl", "selectedRestaurantId", "selectedRestaurantName");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        _users.postValue(users);*/
        when(usersRepository.getDataUsers()).thenReturn(result);
        //viewModel = new WorkmatesViewModel(usersRepository);
    }

    @Test
    public void getUsersFromDatabase(){
        viewModel.getDataUsersFromDatabase().getValue();

    }

}