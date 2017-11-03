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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.validycheck.R;
import com.validycheck.adapter.ProdutoAdapter;
import com.validycheck.com.validycheck.loader.ProdutoLoader;
import com.validycheck.domain.Produto;

import java.util.ArrayList;

public class ProdutoFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Produto>> {

    //Adaptador para lista
    public ProdutoAdapter adapter;

    public ProdutoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);

        adapter = new ProdutoAdapter(getActivity(), new ArrayList<Produto>(), getActivity().getSupportLoaderManager());
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();

        loaderManager.initLoader(ProdutoLoader.PRODUTO_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new ProdutoLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Produto>> loader, ArrayList<Produto> data) {
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
