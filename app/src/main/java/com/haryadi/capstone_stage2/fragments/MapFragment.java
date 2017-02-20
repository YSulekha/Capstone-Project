package com.haryadi.capstone_stage2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haryadi.capstone_stage2.R;

import butterknife.BindView;
import butterknife.ButterKnife;


//Fragment to display map
public class MapFragment extends Fragment {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_map, container, false);
        ButterKnife.bind(this, rootView);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        return rootView;
    }
}
