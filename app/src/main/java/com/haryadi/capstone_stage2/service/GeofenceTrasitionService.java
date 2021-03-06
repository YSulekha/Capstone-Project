package com.haryadi.capstone_stage2.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.haryadi.capstone_stage2.R;
import com.haryadi.capstone_stage2.TriggerActivity;
import com.haryadi.capstone_stage2.data.TriggerContract;
import com.haryadi.capstone_stage2.utils.ChangeSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aharyadi on 3/2/17.
 */

public class GeofenceTrasitionService extends IntentService {

    private static final String TAG = GeofenceTrasitionService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID = 0;
    public static final String SHARED_LAST_WIFI = "wifi_name";
    public static final String SHARED_LAST_BLUETOOTH = "bluetooth_name";
    public static final String SHARED_LAST_TRIGGER = "last_trigger";

    public GeofenceTrasitionService() {
        super("GeofenceTrasitionService");
    }

    public GeofenceTrasitionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve the Geofencing intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = ChangeSettings.getErrorString(this,geofencingEvent.getErrorCode());
            Log.e(TAG, errorMsg);
            return;
        }

        // Retrieve GeofenceTrasition
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            // Create a detail message with Geofences received
            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences);
            // Send notification details as a String
            sendNotification(geofenceTransitionDetails);
            checkDatabase(triggeringGeofences, geoFenceTransition);
        }
    }

    public void checkDatabase(List<Geofence> trigger, int transition) {
        for (Geofence geofence : trigger) {
            String name = geofence.getRequestId();
            String connect = "Connect";
            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                connect = getString(R.string.text_connect);
            } else {
                connect = getString(R.string.text_disconnect);
            }
            Uri uri = TriggerContract.TriggerEntry.CONTENT_URI;
            String where = TriggerContract.TriggerEntry.TABLE_NAME + "." +
                    TriggerContract.TriggerEntry.COLUMN_NAME + " = ?" + " AND " +
                    TriggerContract.TriggerEntry.COLUMN_CONNECT + " = ?";
            String[] args = new String[]{name, connect};
            Cursor c = getContentResolver().query(uri, ChangeSettings.TRIGGER_COLUMNS, where, args, null);
            if (c.moveToNext()) {
                if (c != null) {
                    String bluetooth = c.getString(ChangeSettings.INDEX_ISBLUETOOTHON);
                    String wifi = c.getString((ChangeSettings.INDEX_ISWIFION));
                    if (bluetooth.equals(getString(R.string.turn_on))) {
                        ChangeSettings.changeBluetoothSetting(this, true);
                    } else if (bluetooth.equals(getString(R.string.turn_off))) {
                        ChangeSettings.changeBluetoothSetting(this, false);
                    }
                    if (wifi.equals(getString(R.string.turn_on))) {
                        ChangeSettings.changeWifiSettings(this, true);
                    } else if (wifi.equals(getString(R.string.turn_off))) {
                        ChangeSettings.changeWifiSettings(this, false);
                    }

                    ChangeSettings.changeMediaVolume(this, c.getInt(ChangeSettings.INDEX_MEDIAVOL));
                    ChangeSettings.changeRingVolume(this, c.getInt(ChangeSettings.INDEX_RINGVOL));
                    ChangeSettings.writeToSharedPref(this, c.getLong(ChangeSettings.INDEX_TRIGGER_ID));
                    ChangeSettings.notifyWidgets(this);
                }
                c.close();
            }
        }
    }

    // Create a detail message with Geofences received
    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesList.add(geofence.getRequestId());
        }
        String status = null;
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            status = "Entering ";
        else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            status = "Exiting ";
        return status + TextUtils.join(", ", triggeringGeofencesList);
    }

    // Send a notification
    private void sendNotification(String msg) {
        Log.i(TAG, "sendNotification: " + msg);

        // Intent to start the main Activity
        Intent notificationIntent = new Intent(this, TriggerActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TriggerActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating and sending Notification
        NotificationManager notificatioMng =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificatioMng.notify(
                GEOFENCE_NOTIFICATION_ID,
                createNotification(msg, notificationPendingIntent));
    }

    // Create a notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_location_on_black_24dp)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("Geofence Notification!")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder.build();
    }
    
}
