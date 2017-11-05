package com.validycheck.com.validycheck.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

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

    public LoteLoader(Context context) {
        super(context);
    }

    public LoteLoader(Context context, Lote lote, int operacao) {
        super(context);
        mLote = lote;
        mOperador = operacao;
    }

    public LoteLoader(Context context, int mOperador, Secao secao, Long dataInicial, Long dataFinal) {
        super(context);
        this.mOperador = mOperador;
        this.secao = secao;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
    }

    public LoteLoader(Context context, int mOperador, Secao secao) {
        super(context);
        this.mOperador = mOperador;
        this.secao = secao;
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
                lotes.add(LoteService.salvar(mLote));
            } else if (mOperador == DELETE_LOTE) {
                lotes.add(LoteService.deletar(mLote));
            } else if (mOperador == UPDATE_LOTE) {
                lotes.add(LoteService.update(mLote));
            }
            return lotes;
        } else if (mOperador == FILTER_LOTE){
            if (secao !=null || dataInicial != new Long(LoteService.EMPTY) || dataFinal != new Long(LoteService.EMPTY)){
                lotes = LoteService.fetchLoteSecaoFiltered(secao,dataInicial,dataFinal);
                return lotes;
            }
        }
        lotes = LoteService.fetchLoteData();
        return lotes;
    }
}
