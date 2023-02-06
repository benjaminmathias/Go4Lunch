package com.bmathias.go4lunch.data.repositories;

import static com.bmathias.go4lunch.utils.Constants.LIKED_RESTAURANTS;
import static com.bmathias.go4lunch.utils.Constants.TAG;
import static com.bmathias.go4lunch.utils.Constants.USERS;

import android.util.Log;

import com.bmathias.go4lunch.data.model.LikedRestaurant;
import com.bmathias.go4lunch.data.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class FirestoreUserDatasource implements UserDatasource {

    private final CurrentUserRepository currentUserRepository;
    private final CollectionReference usersRef;
    private final CollectionReference likedRestaurantsRef;

    public FirestoreUserDatasource(
            CurrentUserRepository currentUserRepository,
            FirebaseFirestore firebaseFirestore
    ) {
        this.currentUserRepository = currentUserRepository;
        usersRef = firebaseFirestore.collection(USERS);
        likedRestaurantsRef = firebaseFirestore.collection(LIKED_RESTAURANTS);
    }

    // Retrieve a list of all known selected restaurants IDs by users
    @Override
    public Observable<List<String>> getNonDistinctSelectedRestaurantIds() {
        BehaviorSubject<List<String>> peopleEatingObservable = BehaviorSubject.create();

        usersRef.whereNotEqualTo("selectedRestaurantId", null)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d(TAG, "Listen failed.", error);
                        return;
                    }
                    List<User> users = Objects.requireNonNull(value).toObjects(User.class);
                    List<String> restaurantIds = new ArrayList<>();

                    for (User user : users) {
                        restaurantIds.add(user.getSelectedRestaurantId());
                    }
                    peopleEatingObservable.onNext(restaurantIds);
                });

        return peopleEatingObservable;
    }

    // Retrieve a list of all known favorites restaurants IDs by users
    @Override
    public Observable<List<String>> getNonDistinctFavoriteRestaurantIds() {
        BehaviorSubject<List<String>> restaurantLikesObservable = BehaviorSubject.create();

        likedRestaurantsRef.whereNotEqualTo("restaurantId", null)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d(TAG, "Listen failed.", error);
                        return;
                    }
                    List<LikedRestaurant> likedRestaurants = Objects.requireNonNull(value).toObjects(LikedRestaurant.class);
                    List<String> restaurantLikes = new ArrayList<>();
                    for (LikedRestaurant likedRestaurant : likedRestaurants) {
                        restaurantLikes.add(likedRestaurant.getRestaurantId());
                    }
                    restaurantLikesObservable.onNext(restaurantLikes);
                });
        return restaurantLikesObservable;
    }

    // Retrieve a boolean to check if the current RestaurantDetails is users favorite
    @Override
    public Observable<Boolean> getCurrentUserFavoriteObservable(String placeId) {
        BehaviorSubject<Boolean> currentUserFavoriteObservable = BehaviorSubject.create();

        likedRestaurantsRef.whereEqualTo("restaurantId", placeId)
                .whereEqualTo("userId", currentUserRepository.getCurrentUserId())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d(TAG, "Listen failed.", error);
                        return;
                    }
                    boolean currentUserFavorite = !Objects.requireNonNull(value).getDocuments().isEmpty();
                    currentUserFavoriteObservable.onNext(currentUserFavorite);
                });

        return currentUserFavoriteObservable;
    }

    // Update user selected restaurant
    public void updateSelectedRestaurant(String placeId, String placeName) {
        if (currentUserRepository.getCurrentUserId() != null) {
            DocumentReference userRef = usersRef.document(currentUserRepository.getCurrentUserId());
            userRef.update("selectedRestaurantId", placeId).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantId successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
            userRef.update("selectedRestaurantName", placeName).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantName successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        }
    }

    // Delete user selected restaurant
    public void deleteSelectedRestaurant() {
        if (currentUserRepository.getCurrentUserId() != null) {
            DocumentReference userRef = usersRef.document(currentUserRepository.getCurrentUserId());
            userRef.update("selectedRestaurantId", null).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantId successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
            userRef.update("selectedRestaurantName", null).addOnSuccessListener(aVoid -> Log.d(TAG, "selectedRestaurantName successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        }
    }

    // Add a user favorite restaurant
    public void addFavoriteRestaurant(String placeId) {
        if (currentUserRepository.getCurrentUserId() != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("restaurantId", placeId);
            data.put("userId", currentUserRepository.getCurrentUserId());

            likedRestaurantsRef.add(data)
                    .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        }
    }

    // Remove a specific user favorite restaurant
    public void removeFavoriteRestaurant(String placeId) {
        if (currentUserRepository.getCurrentUserId() != null) {
            Query query = likedRestaurantsRef.whereEqualTo("restaurantId", placeId)
                    .whereEqualTo("userId", currentUserRepository.getCurrentUserId());

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        likedRestaurantsRef.document(document.getId()).delete();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }
}
