package com.bmathias.go4lunch.utils;

import static com.bmathias.go4lunch.utils.Constants.TAG;
import static com.bmathias.go4lunch.utils.Constants.USERS;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bmathias.go4lunch.R;
import com.bmathias.go4lunch.data.model.User;
import com.bmathias.go4lunch.ui.list.DetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "10000";

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    private final CollectionReference usersRef = rootRef.collection(USERS);

    @SuppressLint({"UnsafeProtectedBroadcastReceiver", "UnspecifiedImmutableFlag"})
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

                    Intent notificationIntent = new Intent(context, DetailsActivity.class);
                    notificationIntent.putExtra("placeId", placeId);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    PendingIntent notificationPendingIntent;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        notificationPendingIntent = PendingIntent.getActivity(context,
                                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    } else {
                        notificationPendingIntent = PendingIntent.getActivity(context,
                                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyGo4Lunch")
                            .setSmallIcon(R.drawable.ic_go4lunch_icon)
                            .setContentTitle(context.getResources().getString(R.string.notification_title))
                            .setContentText(context.getResources().getString(R.string.notification_text_1) + restaurantName + context.getResources().getString(R.string.notification_text_2))
                            .setContentIntent(notificationPendingIntent)
                            .setAutoCancel(true)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        builder.setChannelId(CHANNEL_ID);
                    }

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(
                                CHANNEL_ID,
                                "NotificationDemo",
                                NotificationManager.IMPORTANCE_DEFAULT
                        );
                        notificationManager.createNotificationChannel(channel);
                    }
                    notificationManager.notify(0, builder.build());
                    Log.d("NotificationReceiver", "Notification fired !");
                });
    }
}


