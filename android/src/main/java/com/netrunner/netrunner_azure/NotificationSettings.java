package com.netrunner.netrunner_azure;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;

public class NotificationSettings {
    private String HubName;
    private String HubConnectionString;

    public NotificationSettings(Context context) {
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            HubName = bundle.getString("NotificationHubName");
            Log.d("HubName",HubName);
            HubConnectionString = bundle.getString("NotificationHubConnectionString");
            Log.d("HubConnectionString",HubConnectionString);
        } catch(PackageManager.NameNotFoundException e) {
        }
    }

    public String getHubName() { return HubName; }
    public String getHubConnectionString() { return HubConnectionString; }

}