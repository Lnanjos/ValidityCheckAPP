package com.validycheck.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.validycheck.fragment.LoteFragment;
import com.validycheck.fragment.ProdutoFragment;
import com.validycheck.fragment.SecaoFragment;

/**
 * Created by Samsung on 24/09/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public static final int SECAO_FRAG = 2;
    public static final int PRODUTO_FRAG = 1;
    public static final int LOTE_FRAG = 0;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case SECAO_FRAG:
                return new SecaoFragment();
            case PRODUTO_FRAG:
                return new ProdutoFragment();
            case LOTE_FRAG:
                return new LoteFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case SECAO_FRAG:
                return "SEÇÕES";
            case PRODUTO_FRAG:
                return "PRODUTO";
            case LOTE_FRAG:
                return "LOTE";
            default:
                return null;
        }
    }
}
