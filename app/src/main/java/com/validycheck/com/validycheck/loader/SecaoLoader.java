package com.validycheck.com.validycheck.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.validycheck.R;
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
    private String ip_server = (String) getContext().getText(R.string.ip_server_default);

    public SecaoLoader(Context context, String ipServer) {
        super(context);
        this.ip_server = ipServer;
    }

    public SecaoLoader(Context context, Secao secao, int operacao, String ipServer) {
        super(context);
        mSecao = secao;
        mOperador = operacao;
        ip_server = ipServer;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    // esta é uma thread de background
    @Override
    public ArrayList<Secao> loadInBackground() {
        ArrayList<Secao> secoes = new ArrayList<Secao>();
        if (mSecao != null) {
            if (mOperador == SAVE_SECAO) {
                secoes.add(SecaoService.salvar(ip_server,mSecao));
                mSecao = null;
            } else if (mOperador == DELETE_SECAO) {
                secoes.add(SecaoService.deletar(ip_server,mSecao));
                mSecao = null;
            } else if (mOperador == UPDATE_SECAO) {
                secoes.add(SecaoService.update(ip_server,mSecao));
                mSecao = null;
            }
            return secoes;
        }
        secoes = SecaoService.fetchSecaoData(ip_server);
        return secoes;
    }
}
