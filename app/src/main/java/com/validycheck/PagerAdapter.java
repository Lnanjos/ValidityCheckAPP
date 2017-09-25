package com.validycheck;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Samsung on 24/09/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SecaoFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
