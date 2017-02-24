package com.haryadi.capstone_stage2.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aharyadi on 2/22/17.
 */

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
}
