package com.validitycheck.com.validycheck.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.validitycheck.R;
import com.validitycheck.domain.Produto;
import com.validitycheck.domain.Secao;
import com.validitycheck.service.ProdutoService;

import java.util.ArrayList;

public class ProdutoLoader extends AsyncTaskLoader<ArrayList<Produto>> {

    public static final int PRODUTO_LOADER_ID = 2;
    public static final int SAVE_PRODUTO = 10;
    public static final int UPDATE_PRODUTO = 20;
    public static final int DELETE_PRODUTO = 30;
    public static final int FILTER_SECAO = 40;
    private static final String LOG_TAG = ProdutoLoader.class.getName();
    private Secao secao;
    private Produto mProduto;
    private int mOperador;
    private String ip_server = (String) getContext().getText(R.string.ip_server_default);

    public ProdutoLoader(Context context, String ipServer) {
        super(context);
        this.ip_server = ipServer;
    }

    public ProdutoLoader(Context context, Produto produto, int operacao, String ipServer) {
        super(context);
        mProduto = produto;
        mOperador = operacao;
        ip_server = ipServer;
    }

    public ProdutoLoader(Context context, Secao secao, Produto mProduto, int mOperador, String ip_server) {
        super(context);
        this.secao = secao;
        this.mProduto = mProduto;
        this.mOperador = mOperador;
        this.ip_server = ip_server;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    // esta é uma thread de background
    @Override
    public ArrayList<Produto> loadInBackground() {
        ArrayList<Produto> produtos = new ArrayList<Produto>();
        if (mProduto != null) {
            if (mOperador == SAVE_PRODUTO) {
                produtos.add(ProdutoService.salvar(ip_server, mProduto));
            } else if (mOperador == DELETE_PRODUTO) {
                produtos.add(ProdutoService.deletar(ip_server, mProduto));
            } else if (mOperador == UPDATE_PRODUTO) {
                produtos.add(ProdutoService.update(ip_server, mProduto));
            }
            return produtos;
        } else if (mOperador == FILTER_SECAO && secao != null) {
            produtos = ProdutoService.fetchProdutoSecaoFiltered(ip_server, secao);
            return produtos;
        }
        produtos = ProdutoService.fetchProdutoData(ip_server);
        return produtos;
    }
}
