package com.validycheck;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.validycheck.domain.Secao;
import com.validycheck.service.SecaoService;

import java.util.ArrayList;

public class SecaoLoader extends AsyncTaskLoader<ArrayList<Secao>> {

    private static final String LOG_TAG = SecaoLoader.class.getName();
    private Secao mSecao;

    public SecaoLoader(Context context) {
        super(context);
    }

    public SecaoLoader(Context context,Secao secao) {
        super(context);
        mSecao = secao;
     }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    // esta é uma thread de background
    @Override
    public ArrayList<Secao> loadInBackground() {
        if(mSecao != null){
            SecaoService.salvar(mSecao);
        }
        ArrayList<Secao> secoes = SecaoService.fetchSecaoData();
        return secoes;
    }
}
