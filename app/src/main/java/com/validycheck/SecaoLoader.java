package com.validycheck;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

/**
 * Created by Samsung on 25/09/2017.
 */

public class SecaoLoader extends AsyncTaskLoader<ArrayList<Secao>> {

    private static final String LOG_TAG = SecaoLoader.class.getName();

    /** URL da busca */
    private String mUrl;


    public SecaoLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    // esta é uma thread de background
    @Override
    public ArrayList<Secao> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        ArrayList<Secao> secoes = SecaoService.fetchSecaoData(mUrl);
        return secoes;
    }
}
