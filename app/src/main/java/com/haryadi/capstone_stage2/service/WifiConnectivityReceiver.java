package com.haryadi.capstone_stage2.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

//Broadcast receiver to receive change in wifi settings
public class WifiConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo netInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            int intExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            if (ConnectivityManager.TYPE_WIFI == netInfo.getType() && netInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo1 = wifiManager.getConnectionInfo();
                WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if (!wifiInfo.getSSID().equals("<unknown ssid>")) {
                    //Add code to handle the wifi change
                }
            } else {
                //Add code to add the wifi name to shared preference
            }
        }
    }
}
