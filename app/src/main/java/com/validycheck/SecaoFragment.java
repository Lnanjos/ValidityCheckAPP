/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.validycheck;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class SecaoFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Secao>> {

    /**
     * Valor constante para o ID do loader de Secao. Podemos escolher qualquer inteiro.
     * Isto por utilizar múltiplos loaders.
     */
    private static final int SECAO_LOADER_ID = 1;
    private static final String link = "secao";

    //Adaptador para lista
    private SecaoAdapter adapter;

    public SecaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container,false);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        adapter = new SecaoAdapter(getActivity(),new ArrayList<Secao>());

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();
        // Obtém uma referência ao LoaderManager, a fim de interagir com loaders.

        // Inicializa o loader. Passa um ID constante int definido acima e passa nulo para
        // o bundle. Passa esta activity para o parâmetro LoaderCallbacks (que é válido
        // porque esta activity implementa a interface LoaderCallbacks).
        loaderManager.initLoader(SECAO_LOADER_ID, null, this);


        return rootView;
    }

    @Override
    public Loader<ArrayList<Secao>> onCreateLoader(int id, Bundle args) {
        //Cria um novo Loader
        return new SecaoLoader(getActivity(),link);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
        // Limpa o adapter de dados de earthquake anteriores
        adapter.clear();

        // Se há uma lista válida de {@link Earthquake}s, então os adiciona ao data set do adapter.
        // Isto ativará a atualização da ListView.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Secao>> loader) {
        // Reseta o Loader, então podemos limpar nossos dados existentes.
        adapter.clear();
    }
}
