package com.haryadi.capstone_stage2.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.location.GeofenceStatusCodes;
import com.haryadi.capstone_stage2.R;
import com.haryadi.capstone_stage2.data.TriggerContract;
import com.haryadi.capstone_stage2.service.GeofenceTrasitionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ChangeSettings {

    //Projections for database query
    public static final String[] TRIGGER_COLUMNS = {
            TriggerContract.TriggerEntry._ID,
            TriggerContract.TriggerEntry.COLUMN_TRIGGER_NAME,
            TriggerContract.TriggerEntry.COLUMN_NAME,
            TriggerContract.TriggerEntry.COLUMN_TRIGGER_POINT,
            TriggerContract.TriggerEntry.COLUMN_CONNECT,
            TriggerContract.TriggerEntry.COLUMN_ISWIFION,
            TriggerContract.TriggerEntry.COLUMN_ISBLUETOOTHON,
            TriggerContract.TriggerEntry.COLUMN_MEDIAVOL,
            TriggerContract.TriggerEntry.COLUMN_RINGVOL
    };

    public static final int INDEX_TRIGGER_ID = 0;
    public static final int INDEX_TRIGGER_NAME = 1;
    public static final int INDEX_NAME = 2;
    public static final int INDEX_TRIGGER_POINT = 3;
    public static final int INDEX_CONNECT = 4;
    public static final int INDEX_ISWIFION = 5;
    public static final int INDEX_ISBLUETOOTHON = 6;
    public static final int INDEX_MEDIAVOL = 7;
    public static final int INDEX_RINGVOL = 8;

    public static final String ACTION_DATA_UPDATED = "com.haryadi.trigger.ACTION_DATA_UPDATED";

    //Method to enable/disable Bluetooth setting
    public static void changeBluetoothSetting(Context context, Boolean enable) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (enable) {
            if (!bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.enable();
            }
        } else {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    //Method to enable/disable Wifi setting
    public static void changeWifiSettings(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.v("IsWifiEnabled", String.valueOf(wifiManager.isWifiEnabled()));
        String name = wifiInfo.getSSID();
        Log.v("wifi name", name);
        wifiManager.setWifiEnabled(enable);
    }

    //Method to change media volume
    public static void changeMediaVolume(Context context, int progress) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
    }

    //Method to change ring volume
    public static void changeRingVolume(Context context, int progress) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
    }

    //Method to get all saved wifi names
    public static ArrayList<String> getConfiguredWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ArrayList<String> wifiNames = new ArrayList<>();
        List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        if (configurations != null) {
            for (WifiConfiguration configuration : configurations) {
                wifiNames.add(configuration.SSID);
            }
        }
        return wifiNames;
    }

    //Method to get all saved bluetooth names
    public static ArrayList<String> getConfiguredBluetooth(Context context) {
        ArrayList<String> blueToothNames = new ArrayList<>();
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : pairedDevices) {
                blueToothNames.add(bluetoothDevice.getName());
            }
        }
        return blueToothNames;
    }

    public static void addWifiNameToSharedPreference(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String name = wifiInfo.getSSID();
        if(!name.equals("<unknown ssid>")){
            Log.v("ChangeSe",name);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = prefs.edit();
          //  edit.putString(GeofenceTrasitionService.SHARED_LAST_WIFI, name);
            edit.commit();
        }
    }

    public static void writeToSharedPref(Context context, long id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(GeofenceTrasitionService.SHARED_LAST_TRIGGER, id);
        edit.commit();
    }

    //Method to notify widgets
    public static void notifyWidgets(Context mContext) {
        Intent intent = new Intent(ACTION_DATA_UPDATED);
        mContext.sendBroadcast(intent);
    }

    //Get the error string for Geofence
    public static String getErrorString(Context mContext, int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return mContext.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return mContext.getString(R.string.geofence_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return mContext.getString(R.string.geofence_many_intents);
            default:
                return mContext.getString(R.string.geofence_unknown_errors);
        }
    }
}
