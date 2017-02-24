package com.haryadi.capstone_stage2.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haryadi.capstone_stage2.R;
import com.haryadi.capstone_stage2.utils.ChangeSettings;

import java.util.ArrayList;


public class CreateBluetoothFragment extends CreateProfileFragment {

    public CreateBluetoothFragment() {

    }

    public static CreateBluetoothFragment newInstance(String triggerPoint, boolean isEdit, Uri uri) {
        CreateBluetoothFragment frag = new CreateBluetoothFragment();
        CreateProfileFragment.newInstance(frag,triggerPoint,isEdit,uri);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isConnect = getString(R.string.text_connect);
        names = new ArrayList<>();
        names = ChangeSettings.getConfiguredBluetooth(getActivity());
        super.onViewCreated(view, savedInstanceState);
        imageButton.setImageResource(R.drawable.ic_bluetooth_black_24dp);
        mIsBluetoothOn.setSelection(0);
        mIsBluetoothOn.setEnabled(false);
    }
}
