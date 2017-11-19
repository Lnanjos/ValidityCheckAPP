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

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.validycheck.LoginActivity;
import com.validycheck.R;
import com.validycheck.adapter.ProdutoAdapter;
import com.validycheck.com.validycheck.loader.LoteLoader;
import com.validycheck.com.validycheck.loader.ProdutoLoader;
import com.validycheck.com.validycheck.loader.SecaoLoader;
import com.validycheck.domain.Produto;
import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class ProdutoFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Produto>> {

    //Adaptador para lista
    public ProdutoAdapter adapter;
    String myPrefs = "COM.VALIDYCHECK.PREFERENCES";
    LoaderManager loaderManager;
    private String ip_server = "http://";
    private TextView mEmptyStateTextView;
    private Secao secaoFilter;

    public ProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        ip_server = sharedPref.getString(getString(R.string.ip_server), getString(R.string.ip_server_default));

        adapter = new ProdutoAdapter(getActivity(), new ArrayList<Produto>(), getActivity().getSupportLoaderManager(), ip_server);
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
        menu.findItem(R.id.optionProduto).setVisible(true);
        menu.findItem(R.id.optionLote).setVisible(false);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new ProdutoLoader(getActivity(), ip_server);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionProduto:
                ExibirDialog(getView());
                return true;
            case R.id.sairOption:
                Intent intent = new Intent(getActivity(), new LoginActivity().getClass());
                startActivity(intent);
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        menu.clear();
    }

    public void ExibirDialog(View v) {

        final Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.dialog_lote_filter);

        dialog.setTitle("Filtrar Lote");

        Button filtrar = (Button) dialog.findViewById(R.id.filtrar);

        ArrayList<Secao> secoes = new ArrayList<>();

        if (secaoFilter != null) {
            secoes.add(secaoFilter);
        } else {
            Secao secao = new Secao();
            secoes.add(secao);
        }

        TextView dataI = (TextView) dialog.findViewById(R.id.dataInicialText);
        dataI.setVisibility(View.GONE);

        TextView dataF = (TextView) dialog.findViewById(R.id.dataFinalText);
        dataF.setVisibility(View.GONE);


        DatePicker dI = (DatePicker) dialog.findViewById(R.id.dataInicial);
        DatePicker df = (DatePicker) dialog.findViewById(R.id.dataFinal);

        dI.setVisibility(View.GONE);
        df.setVisibility(View.GONE);

        final SpinAdapter spinAdapter =
                new SpinAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, 0, secoes);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.filterSpinner);
        spinAdapter.initLoaderSecao();
        spinner.setAdapter(spinAdapter);

        filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem().getClass() != String.class) {
                    secaoFilter = (Secao) spinner.getSelectedItem();
                    if (secaoFilter.getCodigo().equals(new Long(0))) {
                        secaoFilter = null;
                    }
                } else {
                    secaoFilter = null;
                }
                initLoader();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void initLoader() {
        loaderManager.restartLoader(LoteLoader.LOTE_LOADER_ID, null, this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Produto>> loader, ArrayList<Produto> data) {
        ProgressBar bar = (ProgressBar) getView().findViewById(R.id.progress);
        bar.setVisibility(View.GONE);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public class SpinAdapter extends ArrayAdapter<Secao> implements LoaderManager.LoaderCallbacks<ArrayList<Secao>> {

        private ArrayList<Secao> secoes;


        public SpinAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, ArrayList<Secao> secoes) {
            super(context, resource);
            this.secoes = secoes;
        }

        public int getCount() {
            if (secoes != null) {
                return secoes.size();
            } else {
                return 0;
            }
        }

        public Secao getItem(int position) {
            return secoes.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = new TextView(getContext());
            label.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                label.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
            }
            String toLabel;
            if (secoes.get(position).getNomeSecao() != null) {
                toLabel = secoes.get(position).getNomeSecao();
            } else {
                toLabel = "Selecione uma seção";
            }
            label.setText(toLabel);

            return label;
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = new TextView(getContext());
            label.setTextColor(Color.BLACK);
            label.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
            label.setText(secoes.get(position).getNomeSecao());
            return label;
        }

        @Override
        public Loader<ArrayList<Secao>> onCreateLoader(int id, Bundle args) {
            return new SecaoLoader(getContext(), ip_server);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
            secoes.clear();
            secoes.add(0, new Secao(new Long(0), "Selecione uma seção"));
            secoes.addAll(data);
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Secao>> loader) {

        }

        public void initLoaderSecao() {
            loaderManager.initLoader(SecaoLoader.SECAO_LOADER_ID, null, this);
        }
    }
}
