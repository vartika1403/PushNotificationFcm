package com.example.vartikasharma.pushnotificationfcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

// to handle messsages
public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String LOG_TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(LOG_TAG, "From:  " + message.getFrom());

        if (message == null) {
            return;
        }

        // Check if message contains a notification payload
        if (message.getNotification() != null) {
            Log.i(LOG_TAG,"Notification Body: " + message.getNotification().getBody());
            handleNotification(message.getNotification().getBody());
        }
    }

    private void handleNotification(String body) {
    }

}
