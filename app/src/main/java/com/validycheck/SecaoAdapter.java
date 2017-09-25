package com.validycheck;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Samsung on 24/09/2017.
 */

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

        Secao currentSecao = getItem(position);

        TextView idSecao = (TextView) listItemView.findViewById(R.id.id_secao);
        idSecao.setText(currentSecao.getIdSecao());

        TextView nameSecao = (TextView) listItemView.findViewById(R.id.nome_secao);
        nameSecao.setText(currentSecao.getNomeSecao());


        return listItemView;
    }
}