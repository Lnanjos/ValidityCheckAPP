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

import com.validycheck.LoteEditorActivity;
import com.validycheck.R;
import com.validycheck.com.validycheck.loader.LoteLoader;
import com.validycheck.domain.Lote;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LoteAdapter extends ArrayAdapter<Lote> {

    public static final long FIFTEEN_DAYS = 1296000000;
    LoaderManager loaderManager;
    Lote selectedLote;
    private String ip_server = (String) getContext().getText(R.string.ip_server_default);


    public LoteAdapter(@NonNull Context context, @NonNull ArrayList<Lote> objects, String ip_server) {
        super(context, 0, objects);
        this.ip_server = ip_server;
    }

    public LoteAdapter(@NonNull Context context, @NonNull ArrayList<Lote> objects, LoaderManager lm, String ip_server) {
        super(context, 0, objects);
        this.loaderManager = lm;
        this.ip_server = ip_server;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_lote_item, parent, false);
        }

        final Lote currentLote = getItem(position);

        TextView idTextView = (TextView) listItemView.findViewById(R.id.produto);
        idTextView.setText(currentLote.getProduto().getNomeProduto());

        TextView nomeLote = (TextView) listItemView.findViewById(R.id.codBarra);
        nomeLote.setText(currentLote.getProduto().getCodBarraProduto());

        TextView secao = (TextView) listItemView.findViewById(R.id.secao);
        secao.setText(currentLote.getProduto().getSecao().getNomeSecao());

        Locale brasil = new Locale("pt", "BR");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DATE_FIELD, brasil);

        TextView validade = (TextView) listItemView.findViewById(R.id.validade);
        validade.setText("" + dateFormat.format(currentLote.getValidade()));
        validade.setTextColor(getValideColor(currentLote.getValidade()));


        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), new LoteEditorActivity().getClass());
                intent.putExtra("codigo", currentLote.getCodigo());
                intent.putExtra("validade", currentLote.getValidade().getTime());
                intent.putExtra("produtoNome", currentLote.getProduto().getNomeProduto());
                intent.putExtra("codBarraProduto", currentLote.getProduto().getCodBarraProduto());
                intent.putExtra("produtoCodigo", currentLote.getProduto().getCodigo());
                intent.putExtra("secaoCodigo", currentLote.getProduto().getSecao().getCodigo());
                intent.putExtra("secaoNome", currentLote.getProduto().getSecao().getNomeSecao());
                getContext().startActivity(intent);
            }
        });

        final Button delete = (Button) listItemView.findViewById(R.id.delete_secao);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectedLote = currentLote;
                    int mPosition = position;
                    DeleDialog dialog = new DeleDialog(selectedLote, position);
                    dialog.builder.show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        return listItemView;
    }

    private int getValideColor(Date date) {
        if (date.getTime() < new Date().getTime()) {
            return getContext().getResources().getColor(R.color.vencido);
        }
        if (date.getTime() < new Date().getTime() + FIFTEEN_DAYS) {
            return getContext().getResources().getColor(R.color.vencendoQuinzeDias);
        }
        if (date.getTime() < new Date().getTime() + FIFTEEN_DAYS + FIFTEEN_DAYS) {
            return getContext().getResources().getColor(R.color.vencendoMes);
        }
        return getContext().getResources().getColor(R.color.colorPrimaryText);
    }

    private void remove(int mPosition) {
        remove(getItem(mPosition));
    }

    public class DeleDialog implements LoaderManager.LoaderCallbacks<ArrayList<Lote>> {

        LoaderManager.LoaderCallbacks loaderCallbacks = this;
        AlertDialog.Builder builder;
        private Lote lote;
        private int mPosition;

        public DeleDialog(Lote Lote, int position) {
            this.lote = Lote;
            this.mPosition = position;
            builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Deseja excluir \"" + lote.getProduto().getNomeProduto() + "\"" +
                    "\nValidade:" + lote.getValidade() + "?");
            builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    try {
                        loaderManager.initLoader(LoteLoader.LOTE_LOADER_ID, null, loaderCallbacks);
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
            if (lote != null) {
                return new LoteLoader(getContext(), lote, LoteLoader.DELETE_LOTE, ip_server);
            }
            return new LoteLoader(getContext(), ip_server);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Lote>> loader, ArrayList<Lote> data) {
            try {
                lote = data.get(data.size() - 1);
                Toast.makeText(getContext(), "Lote deletado:" + lote.getProduto().getNomeProduto()
                        + "\nValidade:" + lote.getValidade(), Toast.LENGTH_LONG).show();
                remove(mPosition);
                notifyDataSetChanged();
                lote = null;

                loaderManager.destroyLoader(LoteLoader.LOTE_LOADER_ID);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Lote>> loader) {

        }
    }
}