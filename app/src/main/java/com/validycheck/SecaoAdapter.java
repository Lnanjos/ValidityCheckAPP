package com.validycheck;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.validycheck.domain.Secao;

import java.util.ArrayList;


public class SecaoAdapter extends ArrayAdapter<Secao> {

    public SecaoAdapter(@NonNull Context context, @NonNull ArrayList<Secao> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

        return listItemView;
    }
}