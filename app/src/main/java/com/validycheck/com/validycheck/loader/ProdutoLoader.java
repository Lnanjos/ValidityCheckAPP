package com.validycheck.com.validycheck.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.validycheck.domain.Produto;
import com.validycheck.service.ProdutoService;

import java.util.ArrayList;

public class ProdutoLoader extends AsyncTaskLoader<ArrayList<Produto>> {

    public static final int PRODUTO_LOADER_ID = 2;
    public static final int SAVE_PRODUTO = 10;
    public static final int UPDATE_PRODUTO = 20;
    public static final int DELETE_PRODUTO = 30;
    private static final String LOG_TAG = ProdutoLoader.class.getName();
    private Produto mProduto;
    private int mOperador;

    public ProdutoLoader(Context context) {
        super(context);
    }

    public ProdutoLoader(Context context, Produto secao, int operacao) {
        super(context);
        mProduto = secao;
        mOperador = operacao;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    // esta é uma thread de background
    @Override
    public ArrayList<Produto> loadInBackground() {
        ArrayList<Produto> secoes = new ArrayList<Produto>();
        if(mProduto != null){
            if (mOperador == SAVE_PRODUTO){
                secoes.add(ProdutoService.salvar(mProduto));
            }else if (mOperador == DELETE_PRODUTO){
                secoes.add(ProdutoService.deletar(mProduto));
            }else if (mOperador == UPDATE_PRODUTO)

            return secoes;
        }
        secoes = ProdutoService.fetchProdutoData();
        return secoes;
    }
}
