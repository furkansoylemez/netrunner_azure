package com.netrunner.netrunner_azure;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.microsoft.windowsazure.messaging.NotificationHub;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "ANH_FLUTTER";
    private static String FCM_token = null;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String resultString = null;
        String regID = null;
        String userId="08da9af0-f457-ae45-3bc7-1219c1fb49f1";
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener < InstanceIdResult > () {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    FCM_token = instanceIdResult.getToken();
                }
            });
            TimeUnit.SECONDS.sleep(1);
            String sha1 = "";
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.reset();
                digest.update(FCM_token.getBytes("utf8"));
                sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] tags = {
                "device:" + sha1
            };
            NotificationSettings nhSettings = new NotificationSettings(getApplicationContext());
            if (((regID = sharedPreferences.getString("registrationID", null)) == null)) {
                NotificationHub hub = new NotificationHub(nhSettings.getHubName(),
                        nhSettings.getHubConnectionString(), this);
                regID = hub.register(FCM_token, "android",userId).getRegistrationId();
                resultString = "New NH Registration Successfully - RegId : " + regID;
                Log.d(TAG, resultString);
                sharedPreferences.edit().putString("registrationID", regID).apply();
                sharedPreferences.edit().putString("FCMtoken", FCM_token).apply();
            }

            // Check to see if the token has been compromised and needs refreshing.
            else if ((sharedPreferences.getString("FCMtoken", "")) != FCM_token) {
                NotificationHub hub = new NotificationHub(nhSettings.getHubName(),
                        nhSettings.getHubConnectionString(), this);
                regID = hub.register(FCM_token, "android",userId).getRegistrationId();
                resultString = "New NH Registration Successfully - RegId : " + regID;
                Log.d(TAG, resultString);
                sharedPreferences.edit().putString("registrationID", regID).apply();
                sharedPreferences.edit().putString("FCMtoken", FCM_token).apply();
            } else {
                resultString = "Previously Registered Successfully - RegId : " + regID;
            }
            Intent tIntent = new Intent(NotificationService.ACTION_TOKEN);
            tIntent.putExtra(NotificationService.EXTRA_TOKEN, "device:" + sha1);
            LocalBroadcastManager.getInstance(this).sendBroadcast(tIntent);
        } catch (Exception e) {
            Log.e(TAG, resultString = "Failed to complete registration", e);
            // TODO: attempt the update at a later time if an error occurs during registration
        }
    }
}