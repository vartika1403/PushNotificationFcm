package com.example.vartikasharma.pushnotificationfcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

// to handle messsages
public class MyFirebaseMessagingService extends FirebaseMessagingService {
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
            Log.i(LOG_TAG, "Notification Body: " + message.getNotification().getBody());
            handleNotification(message.getNotification().getBody());
        }

        // check is message contains data payload
        if (message.getData().size() > 0) {
            Log.i(LOG_TAG, "Notification data payload, " + message.getData().toString());
            try {
                JSONObject json = new JSONObject(message.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) {
        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.i(LOG_TAG, "title: " + title);
            Log.i(LOG_TAG, "message: " + message);
            Log.i(LOG_TAG, "isBackground: " + isBackground);
            Log.i(LOG_TAG, "payload: " + payload.toString());
            Log.i(LOG_TAG, "imageUrl: " + imageUrl);
            Log.i(LOG_TAG, "timestamp: " + timestamp);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // show notification with text and image
    private void showNotificationMessageWithBigImage(Context applicationContext, String title,
                                                     String message, String timestamp,
                                                     Intent resultIntent, String imageUrl) {
        notificationUtils = new NotificationUtils(applicationContext);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timestamp, resultIntent, imageUrl);
    }

    // show notification with message only
    private void showNotificationMessage(Context applicationContext, String title,
                                         String message, String timestamp,
                                         Intent resultIntent) {
        notificationUtils = new NotificationUtils(applicationContext);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timestamp, resultIntent);
    }

    private void handleNotification(String body) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", body);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }
}
