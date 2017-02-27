package com.haryadi.capstone_stage2.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haryadi.capstone_stage2.R;

import java.util.ArrayList;

public class CreateLocationFragment extends CreateProfileFragment {

    public CreateLocationFragment() {

    }

    public static CreateLocationFragment newInstance(String triggerPoint, boolean isEdit, Uri uri) {
        CreateLocationFragment frag = new CreateLocationFragment();
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
        names = new ArrayList<>();
        super.onViewCreated(view, savedInstanceState);
        imageButton.setImageResource(R.drawable.ic_location_on_black_24dp);
        mWifiName.setVisibility(View.INVISIBLE);
        locationName.setVisibility(View.VISIBLE);
    }

}
