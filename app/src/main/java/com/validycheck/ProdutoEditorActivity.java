package com.validycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.validycheck.domain.Produto;

public class ProdutoEditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProdutoEditorActivity";
    private Produto produto = new Produto();
    LoaderManager loaderManager = getSupportLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_editor);


        String [] spinnerlist = {"Bebidas","Latcineos","Frios"};

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,spinnerlist);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        //cancelar a ação
        Button cancelar = (Button) findViewById(R.id.cancelarProduto);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText nomeProduto = (EditText) findViewById(R.id.editText_nomeProduto);

        if((Long)getIntent().getLongExtra("codigo",0)!= null){
            Intent intent = getIntent();
            produto.setNomeProduto(intent.getStringExtra("nomeProduto"));
            produto.setCodigo(intent.getLongExtra("codigo",0));
            nomeProduto.setText(produto.getNomeProduto());
        }

        Button salvar = (Button) findViewById(R.id.salvarProduto);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                produto.setNomeProduto(""+nomeProduto.getText());
            }
        });
    }
}