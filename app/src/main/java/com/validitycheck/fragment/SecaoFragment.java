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
package com.validitycheck.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.validitycheck.LoginActivity;
import com.validitycheck.R;
import com.validitycheck.adapter.SecaoAdapter;
import com.validitycheck.com.validycheck.loader.LoteLoader;
import com.validitycheck.com.validycheck.loader.SecaoLoader;
import com.validitycheck.domain.Secao;

import java.util.ArrayList;

public class SecaoFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Secao>> {

    //Adaptador para lista
    public SecaoAdapter adapter;
    String myPrefs = "COM.VALIDYCHECK.PREFERENCES";
    LoaderManager loaderManager;
    private String ip_server = "http://";
    private TextView mEmptyStateTextView;

    public SecaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        ip_server = sharedPref.getString(getString(R.string.ip_server), getString(R.string.ip_server_default));

        adapter = new SecaoAdapter(getActivity(), new ArrayList<Secao>(), getActivity().getSupportLoaderManager(), ip_server);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected == true) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(LoteLoader.LOTE_LOADER_ID, null, this);

            setHasOptionsMenu(true);
            setMenuVisibility(true);

            mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
            listView.setEmptyView(mEmptyStateTextView);

        } else {
            mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
            listView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView.setText(R.string.no_internet);
            ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progress);
            bar.setVisibility(View.GONE);
        }

        return rootView;
    }


    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.optionLote).setVisible(false);
        menu.findItem(R.id.optionProduto).setVisible(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sairOption:
                Intent intent = new Intent(getActivity(), new LoginActivity().getClass());
                startActivity(intent);
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<ArrayList<Secao>> onCreateLoader(int id, Bundle args) {
        //Cria um novo Loader
        return new SecaoLoader(getActivity(), ip_server);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
        ProgressBar bar = (ProgressBar) getView().findViewById(R.id.progress);
        bar.setVisibility(View.GONE);

        // Limpa o adapter de dados anteriores
        adapter.clear();

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
