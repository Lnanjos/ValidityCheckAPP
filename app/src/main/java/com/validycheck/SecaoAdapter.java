package com.validycheck;

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

import com.validycheck.domain.Secao;

import java.util.ArrayList;


public class SecaoAdapter extends ArrayAdapter<Secao>{

    LoaderManager loaderManager;
    Secao selectedSecao;


    public SecaoAdapter(@NonNull Context context, @NonNull ArrayList<Secao> objects) {
        super(context, 0, objects);
    }
    public SecaoAdapter(@NonNull Context context, @NonNull ArrayList<Secao> objects, LoaderManager lm) {
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

        final Secao currentSecao = getItem(position);

        TextView idTextView = (TextView) listItemView.findViewById(R.id.id_secao);
        idTextView.setText(""+currentSecao.getCodigo());

        TextView nomeSecao = (TextView) listItemView.findViewById(R.id.nome_secao);
        nomeSecao.setText(currentSecao.getNomeSecao());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),new SecaoEditorActivity().getClass());
                intent.putExtra("codigo",currentSecao.getCodigo());
                intent.putExtra("nomeSecao",currentSecao.getNomeSecao());
                getContext().startActivity(intent);
            }
        });

        final Button delete = (Button) listItemView.findViewById(R.id.delete_secao);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectedSecao = currentSecao;
                    int mPosition = position;
                    DeleDialog dialog = new DeleDialog(selectedSecao,position);
                    dialog.builder.show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        return listItemView;
    }

    public class DeleDialog implements LoaderManager.LoaderCallbacks<ArrayList<Secao>>{

        LoaderManager.LoaderCallbacks loaderCallbacks = this;
        AlertDialog.Builder builder;
        private  Secao secao;
        private int mPosition;
        public DeleDialog(Secao secao,int position) {
            this.secao = secao;
            this.mPosition = position;
            builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Titulo");
            builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    try {
                        loaderManager.initLoader(SecaoLoader.SECAO_LOADER_ID,null,loaderCallbacks);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
            //define um botão como negativo.
            builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(getContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public Loader onCreateLoader(int id, Bundle args) {
            if (secao != null){
                return new SecaoLoader(getContext(),secao,SecaoLoader.DELETE_SECAO);
            }
            return new SecaoLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
            try {
                secao = data.get(data.size()-1);
                Toast.makeText(getContext(),"Seção Deletada:"+secao.getNomeSecao(),Toast.LENGTH_LONG).show();
                remove(mPosition);
                notifyDataSetChanged();
                secao = null;
                loaderManager.destroyLoader(SecaoLoader.SECAO_LOADER_ID);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Secao>> loader) {

        }
    }

    private void remove(int mPosition) {
        remove(getItem(mPosition));
    }
}