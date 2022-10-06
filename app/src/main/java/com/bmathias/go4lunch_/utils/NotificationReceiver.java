package com.bmathias.go4lunch_.utils;

import static com.bmathias.go4lunch_.utils.Constants.TAG;
import static com.bmathias.go4lunch_.utils.Constants.USERS;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bmathias.go4lunch_.R;
import com.bmathias.go4lunch_.data.model.User;
import com.bmathias.go4lunch_.ui.list.DetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class NotificationReceiver extends BroadcastReceiver {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final CollectionReference usersRef = rootRef.collection(USERS);

    @Override
    public void onReceive(Context context, final Intent intent) {

        Log.d("NotificationReceiver", "Alarm received !");

        // Query our current user and check if he selected a restaurant
        usersRef.whereEqualTo("userId", firebaseAuth.getUid())
                .whereNotEqualTo("selectedRestaurantId", null)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.d(TAG, "Listen failed.", error);
                        return;
                    }

                    List<User> users = Objects.requireNonNull(value).toObjects(User.class);

                    // String used to open the corresponding restaurant activity as an extra
                    String placeId = users.get(0).getSelectedRestaurantId();

                    // String used to customize the notification with the selected restaurant name
                    String restaurantName = users.get(0).getSelectedRestaurantName();

                    Intent myIntent = new Intent(context, DetailsActivity.class);
                    myIntent.putExtra("placeId", placeId);

                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyGo4Lunch")
                            .setSmallIcon(R.drawable.ic_go4lunch_icon)
                            .setContentTitle(context.getResources().getString(R.string.notification_title))
                            .setContentText(context.getResources().getString(R.string.notification_text_1) + restaurantName + context.getResources().getString(R.string.notification_text_2))
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                    notificationManager.notify(5, builder.build());
                    Log.d("NotificationReceiver", "Notification fired !");
                });

    }
}

