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
package com.validycheck.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.validycheck.R;
import com.validycheck.adapter.SecaoAdapter;
import com.validycheck.com.validycheck.loader.LoteLoader;
import com.validycheck.com.validycheck.loader.SecaoLoader;
import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class SecaoFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Secao>> {

    private TextView mEmptyStateTextView;
    LoaderManager loaderManager;

    //Adaptador para lista
    public SecaoAdapter adapter;
    private Secao secao;

    public SecaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);
        adapter = new SecaoAdapter(getActivity(), new ArrayList<Secao>(), getActivity().getSupportLoaderManager());
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if(isConnected == true) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(LoteLoader.LOTE_LOADER_ID, null, this);

            setHasOptionsMenu(true);
            setMenuVisibility(true);

            mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
            listView.setEmptyView(mEmptyStateTextView);

        }else {
            mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
            listView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView.setText(R.string.no_internet);
            ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progress);
            bar.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public Loader<ArrayList<Secao>> onCreateLoader(int id, Bundle args) {
        //Cria um novo Loader
        if (secao != null) {
            return new SecaoLoader(getActivity(), secao, SecaoLoader.SAVE_SECAO);
        }
        return new SecaoLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
        // Limpa o adapter de dados anteriores
        adapter.clear();
        secao = null;

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
