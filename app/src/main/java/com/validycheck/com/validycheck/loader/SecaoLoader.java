package com.validycheck.com.validycheck.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.validycheck.domain.Secao;
import com.validycheck.service.SecaoService;

import java.util.ArrayList;

public class SecaoLoader extends AsyncTaskLoader<ArrayList<Secao>> {

    public static final int SECAO_LOADER_ID = 1;
    public static final int SAVE_SECAO = 10;
    public static final int UPDATE_SECAO = 20;
    public static final int DELETE_SECAO = 30;
    private static final String LOG_TAG = SecaoLoader.class.getName();
    private Secao mSecao;
    private int mOperador;

    public SecaoLoader(Context context) {
        super(context);
    }

    public SecaoLoader(Context context,Secao secao,int operacao) {
        super(context);
        mSecao = secao;
        mOperador = operacao;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    // esta é uma thread de background
    @Override
    public ArrayList<Secao> loadInBackground() {
        ArrayList<Secao> secoes = new ArrayList<Secao>();
        if(mSecao != null){
            if (mOperador == SAVE_SECAO){
                secoes.add(SecaoService.salvar(mSecao));
                mSecao = null;
            }else if (mOperador == DELETE_SECAO){
                secoes.add(SecaoService.deletar(mSecao));
                mSecao = null;
            }else if (mOperador == UPDATE_SECAO){
                secoes.add(SecaoService.update(mSecao));
                mSecao = null;
            }
            return secoes;
        }
        secoes = SecaoService.fetchSecaoData();
        return secoes;
    }
}
