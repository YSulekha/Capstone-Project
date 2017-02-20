package com.haryadi.capstone_stage2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haryadi.capstone_stage2.fragments.MapFragment;
import com.haryadi.capstone_stage2.fragments.ProfileFragment;

/**
 * Created by aharyadi on 2/19/17.
 */

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {

    public static final int NUM_PAGES = 2;

    public ScreenSlidePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MapFragment();
            case 1:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}


