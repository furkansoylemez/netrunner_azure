package com.netrunner.netrunner_azure;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import android.R;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.net.Uri;
import android.media.AudioAttributes;
import android.app.Notification;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.app.NotificationCompat.DEFAULT_ALL;
import static androidx.core.app.NotificationCompat.DEFAULT_SOUND;
import static androidx.core.app.NotificationCompat.DEFAULT_VIBRATE;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

public class NotificationService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CHANNEL_ID = "azure_notificationhubs_flutter";
    public static final String NOTIFICATION_CHANNEL_NAME = "Azure Notification Hubs Channel";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Azure Notification Hubs Channel";

    public static final String ACTION_REMOTE_MESSAGE =
            "com.ovidos.pakettaxi.driverNOTIFICATION";
    public static final String ACTION_TOKEN = "com.ovidos.pakettaxi.driverTOKEN";
    public static final String EXTRA_REMOTE_MESSAGE =
            "com.ovidos.pakettaxi.driverNOTIFICATION_DATA";
    public static final String EXTRA_TOKEN = "com.ovidos.pakettaxi.driverTOKEN_DATA";

    private NotificationManager mNotificationManager;
    private static Context ctx;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.d("MESSAGE RECEIVED",message.toString());
        Map<String, Object> content = parseRemoteMessage(message);
        Intent intent = new Intent(ACTION_REMOTE_MESSAGE);
        intent.putExtra(EXTRA_REMOTE_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        sendNotification(content);
    }

    private void sendNotification(Map<String, Object> content) {
        ctx = getApplicationContext();
        Class mainActivity;
        try {
            String packageName = "com.ovidos.pakettaxi.driver";
            Log.d("PACKAGE NAME",packageName);
            Intent launchIntent = ctx.getPackageManager().getLaunchIntentForPackage(packageName);
            String activityName = launchIntent.getComponent().getClassName();
            mainActivity = Class.forName(activityName);
            Intent intent = new Intent(ctx, mainActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
            Resources resources = ctx.getPackageManager().getResourcesForApplication(packageName);
            int resId = resources.getIdentifier("shopping_bag", "drawable", packageName);
            Drawable icon = resources.getDrawable(resId);
            int soundRawId = resources.getIdentifier("kisapakettaxi", "raw", packageName);
            Uri soundUri = Uri.parse("android.resource://" +"com.ovidos.pakettaxi.driver"+ "/" + soundRawId);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                    ctx,
                    NOTIFICATION_CHANNEL_ID)
                .setContentTitle(("PaketTaxi"))
                .setContentText(((Map) content.get("data")).get("message").toString())
                .setPriority(PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_menu_manage)
                .setSound(soundUri)
                .setLargeIcon(BitmapFactory.decodeResource(resources, resId))
                .setContentIntent(contentIntent)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setAutoCancel(true);
                
            Notification notification = notificationBuilder.build();
            int m = (int)((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            mNotificationManager.notify(m, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createChannelAndHandleNotifications(Context context) {
        ctx = context;
        Uri soundUri = Uri.parse("android.resource://"+ "com.ovidos.pakettaxi.driver" + "/" + "raw/kisapakettaxi");
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            channel.enableVibration(true);
            channel.setSound(soundUri,audioAttributes);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    public static Map<String, Object> parseRemoteMessage(RemoteMessage message) {
        Log.d("REMOTE MESSAGE",message.toString());
        Map<String, Object> content = new HashMap<>();
        content.put("data", message.getData());
        return content;
    }
}