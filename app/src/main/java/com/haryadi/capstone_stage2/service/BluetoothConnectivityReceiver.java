package com.haryadi.capstone_stage2.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//Broadcast receiver to receive change in bluetooth settings
public class BluetoothConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            if (state == BluetoothAdapter.STATE_OFF) {
                //Add code to handle bluetooth disable
            }
        }
        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
            //Add code to handle bluetooth connection
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            //Add code to handle bluetooth disconnection
        }
    }
}
