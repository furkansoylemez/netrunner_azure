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

            HubName = "pakettaxi";
            Log.d("HubName",HubName);
            HubConnectionString = "Endpoint=sb://pakettaxi.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=SOwyAFhwfc7cX/tvAnOzT8nGMJoW1ZGYg3EVvxoyNBk=";
            Log.d("HubConnectionString",HubConnectionString);
        } catch(Exception e) {
        }
    }

    public String getHubName() { return HubName; }
    public String getHubConnectionString() { return HubConnectionString; }

}