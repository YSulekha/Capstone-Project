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

public class CreateWifiFragment extends CreateProfileFragment {

    public CreateWifiFragment() {

    }

    public static CreateWifiFragment newInstance(String triggerPoint, boolean isEdit, Uri uri) {
        CreateWifiFragment frag = new CreateWifiFragment();
        CreateProfileFragment.newInstance(frag,triggerPoint,isEdit,uri);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater,container,savedInstanceState);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isConnect = getString(R.string.text_connect);
        names = new ArrayList<>();
        names = ChangeSettings.getConfiguredWifi(getActivity());
        super.onViewCreated(view, savedInstanceState);
        imageButton.setImageResource(R.drawable.ic_wifi_black_24dp);
        mIsWifiOn.setSelection(0);
        mIsWifiOn.setEnabled(false);
    }

}
