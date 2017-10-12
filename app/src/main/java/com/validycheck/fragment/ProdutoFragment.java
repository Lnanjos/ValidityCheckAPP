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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.validycheck.R;
import com.validycheck.adapter.ProdutoAdapter;
import com.validycheck.domain.Produto;
import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class ProdutoFragment extends Fragment{

    //Adaptador para lista
    public ProdutoAdapter adapter;

    public ProdutoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container,false);

        ArrayList<Produto> produtos = new ArrayList<>();

        produtos.add(0,new Produto("Coca Cola 2l","12345678900", new Secao("Bebidas")));
        produtos.add(1,new Produto("Sprite","12345678900", new Secao("Bebidas")));
        produtos.add(2,new Produto("Paçoca","12345678900", new Secao("Doces")));
        produtos.add(3,new Produto("Sardinhas","12345678900", new Secao("Conservas")));

        adapter = new ProdutoAdapter(getActivity(),produtos,getActivity().getSupportLoaderManager());
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        return rootView;
    }
}
