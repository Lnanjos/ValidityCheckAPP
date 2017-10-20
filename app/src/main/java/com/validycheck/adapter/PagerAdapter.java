package com.validycheck.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.validycheck.fragment.ProdutoFragment;
import com.validycheck.fragment.SecaoFragment;

/**
 * Created by Samsung on 24/09/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public static final int SECAO_FRAG = 0;
    public static final int PRODUTO_FRAG = 1;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case SECAO_FRAG:
                return new SecaoFragment();
            case PRODUTO_FRAG:
                return new ProdutoFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
