package com.bmathias.go4lunch_.data.repositories;

import static com.bmathias.go4lunch_.utils.Constants.USERS;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.bmathias.go4lunch_.data.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RunWith(MockitoJUnitRunner.class)
public class UsersRepositoryTest {

    private User user1;
    private User user2;

    public UsersRepository usersRepository;
   // private final List<User> users = new ArrayList<>();

    @Mock
    private CollectionReference colRef;


    @Before
    public void setUp(){
     /*   user1 = new User("uid", "name", "email", "photoUrl", "selectedRestaurantId", "selectedRestaurantName");
        user2 = new User("uid", "name", "email", "photoUrl", "selectedRestaurantId", "selectedRestaurantName");
        users.add(user1);
        users.add(user2);*/

    }

    @Test
    public void getAllUsersFromDatabase() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        // Access private constructor
        Constructor<UsersRepository> usersRepositoryConstructor = UsersRepository.class.getDeclaredConstructor(FirebaseFirestore.class);
        usersRepositoryConstructor.setAccessible(true);

        // Mocks
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        usersRepository = usersRepositoryConstructor.newInstance(mockFirestore);
        colRef = mockFirestore.collection(USERS);

        // Mockito.when(mockFirestore.collection(USERS).get().getResult().toObjects(User.class)).thenReturn(users);

        final List<User> mockUsersList = new ArrayList<>();
        for (User mockUser : mockUsersList){
            colRef.add(mockUser);
        }

        // Get data
        final List<User> actualUsersList = Objects.requireNonNull(usersRepository.getDataUsers().getValue()).getData();
        final List<User> expectedUsersList = mockUsersList;

        assertEquals(actualUsersList, expectedUsersList);
    }

}