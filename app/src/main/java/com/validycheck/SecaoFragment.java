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

import com.validycheck.com.validycheck.loader.SecaoLoader;
import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class SecaoFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Secao>> {

    private Secao secao;

    //Adaptador para lista
    public SecaoAdapter adapter;


    public SecaoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container,false);
        adapter = new SecaoAdapter(getActivity(),new ArrayList<Secao>(),getActivity().getSupportLoaderManager());
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(SecaoLoader.SECAO_LOADER_ID, null,this);

        return rootView;
    }

    @Override
    public Loader<ArrayList<Secao>> onCreateLoader(int id, Bundle args) {
        //Cria um novo Loader
        if (secao!=null){
            return new SecaoLoader(getActivity(),secao,SecaoLoader.SAVE_SECAO);
        }
        return new SecaoLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
        // Limpa o adapter de dados anteriores
        adapter.clear();
        secao = null;

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
        secao = null;
    }
}
