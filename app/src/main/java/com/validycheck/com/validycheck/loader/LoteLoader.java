package com.validycheck.com.validycheck.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.validycheck.R;
import com.validycheck.domain.Lote;
import com.validycheck.domain.Secao;
import com.validycheck.service.LoteService;

import java.util.ArrayList;

public class LoteLoader extends AsyncTaskLoader<ArrayList<Lote>> {

    public static final int LOTE_LOADER_ID = 3;
    public static final int SAVE_LOTE = 10;
    public static final int UPDATE_LOTE = 20;
    public static final int DELETE_LOTE = 30;
    public static final int FILTER_LOTE = 40;
    private static final String LOG_TAG = LoteLoader.class.getName();
    private Lote mLote;
    private int mOperador;
    private Secao secao;
    private Long dataInicial = LoteService.EMPTY;
    private Long dataFinal = LoteService.EMPTY;
    private String ip_server = (String) getContext().getText(R.string.ip_server_default);


    public LoteLoader(Context context,String ip_server) {
        super(context);
        this.ip_server = ip_server;
    }

    public LoteLoader(Context context, Lote lote, int operacao, String ipServer) {
        super(context);
        mLote = lote;
        mOperador = operacao;
        ip_server = ipServer;
    }

    public LoteLoader(Context context, int mOperador, Secao secao, Long dataInicial, Long dataFinal, String ip_server) {
        super(context);
        this.mOperador = mOperador;
        this.secao = secao;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.ip_server = ip_server;
    }

    public LoteLoader(Context context, int mOperador, Secao secao, String ip_server) {
        super(context);
        this.mOperador = mOperador;
        this.secao = secao;
        this.ip_server = ip_server;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    // esta é uma thread de background
    @Override
    public ArrayList<Lote> loadInBackground() {
        ArrayList<Lote> lotes = new ArrayList<Lote>();

        if (mLote != null) {
            if (mOperador == SAVE_LOTE) {
                lotes.add(LoteService.salvar(ip_server,mLote));
            } else if (mOperador == DELETE_LOTE) {
                lotes.add(LoteService.deletar(ip_server,mLote));
            } else if (mOperador == UPDATE_LOTE) {
                lotes.add(LoteService.update(ip_server,mLote));
            }
            return lotes;
        } else if (mOperador == FILTER_LOTE){
            if (secao !=null || dataInicial != new Long(LoteService.EMPTY) || dataFinal != new Long(LoteService.EMPTY)){
                lotes = LoteService.fetchLoteSecaoFiltered(ip_server,secao,dataInicial,dataFinal);
                return lotes;
            }
        }
        lotes = LoteService.fetchLoteData(ip_server);
        return lotes;
    }
}
