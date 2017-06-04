package com.example.vartikasharma.pushnotificationfcm;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

//this service get the reg id unigue for device
public class MyFirebaseRegistartionIdService extends FirebaseInstanceIdService {
    private static final String LOG_TAG = MyFirebaseRegistartionIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        //Saving reg id to shared preferences
        storeRegIdInPref(refreshToken);

        //send registration id to server
        sendRegistrationIdToServer(refreshToken);

    }

    private void sendRegistrationIdToServer(String refreshToken) {
        // sending this gcm token to server
        Log.e(LOG_TAG , refreshToken);
    }

    public void storeRegIdInPref(String refreshToken) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("regId", refreshToken);
        editor.apply();
    }
}
