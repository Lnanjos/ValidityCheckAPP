package com.validycheck.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.validycheck.ProdutoEditorActivity;
import com.validycheck.R;
import com.validycheck.com.validycheck.loader.ProdutoLoader;
import com.validycheck.domain.Produto;
import java.util.ArrayList;

public class ProdutoAdapter extends ArrayAdapter<Produto>{

    LoaderManager loaderManager;
    Produto selectedProduto;


    public ProdutoAdapter(@NonNull Context context, @NonNull ArrayList<Produto> objects) {
        super(context, 0, objects);
    }
    public ProdutoAdapter(@NonNull Context context, @NonNull ArrayList<Produto> objects, LoaderManager lm) {
        super(context, 0, objects);
        this.loaderManager = lm;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Produto currentProduto = getItem(position);

        TextView idTextView = (TextView) listItemView.findViewById(R.id.main_text);
        idTextView.setText(currentProduto.getNomeProduto());

        TextView nomeProduto = (TextView) listItemView.findViewById(R.id.below_main);
        nomeProduto.setText(currentProduto.getCodBarraProduto());

        TextView secaoProduto = (TextView) listItemView.findViewById(R.id.second_text);
        secaoProduto.setText(currentProduto.getSecao().getNomeSecao());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),new ProdutoEditorActivity().getClass());
                intent.putExtra("codigo",currentProduto.getCodigo());
                intent.putExtra("nomeProduto",currentProduto.getNomeProduto());
                intent.putExtra("codBarraProduto",currentProduto.getCodBarraProduto());
                intent.putExtra("nomeSecao",currentProduto.getSecao().getNomeSecao());
                intent.putExtra("codigoSecao",currentProduto.getSecao().getCodigo());
                getContext().startActivity(intent);
            }
        });

        final Button delete = (Button) listItemView.findViewById(R.id.delete_secao);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectedProduto = currentProduto;
                    int mPosition = position;
                    DeleDialog dialog = new DeleDialog(selectedProduto,position);
                    dialog.builder.show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        return listItemView;
    }

    public class DeleDialog implements LoaderManager.LoaderCallbacks<ArrayList<Produto>>{

        LoaderManager.LoaderCallbacks loaderCallbacks = this;
        AlertDialog.Builder builder;
        private  Produto Produto;
        private int mPosition;
        public DeleDialog(Produto Produto,int position) {
            this.Produto = Produto;
            this.mPosition = position;
            builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Deseja excluir \""+Produto.getNomeProduto()+"\"?");
            builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    try {
                        loaderManager.initLoader(ProdutoLoader.PRODUTO_LOADER_ID,null,loaderCallbacks);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
            //define um botão como negativo.
            builder.setNegativeButton("não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    try {
                        DeleDialog.this.finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
        }

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            if (Produto != null){
                return new ProdutoLoader(getContext(),Produto,ProdutoLoader.DELETE_PRODUTO);
            }
            return new ProdutoLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Produto>> loader, ArrayList<Produto> data) {
            try {
                Produto = data.get(data.size()-1);
                Toast.makeText(getContext(),"Produto deletado:"+Produto.getNomeProduto(),Toast.LENGTH_LONG).show();
                remove(mPosition);
                notifyDataSetChanged();
                Produto = null;

                loaderManager.destroyLoader(ProdutoLoader.PRODUTO_LOADER_ID);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Produto>> loader) {

        }
    }

    private void remove(int mPosition) {
        remove(getItem(mPosition));
    }
}