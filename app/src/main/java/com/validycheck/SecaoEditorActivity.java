package com.validycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class SecaoEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Secao>>{

    private static final String LOG_TAG = "SecaoEditorActivity";
    private static final int SECAO_LOADER_ID = 1;
    private Secao secao = new Secao();
    LoaderManager loaderManager = getSupportLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secao_editor);


        //cancelar a ação
        Button cancelar = (Button) findViewById(R.id.cancelarSecao);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText nomeSecao = (EditText) findViewById(R.id.editText_nomeSecao);

        if(getIntent().getIntExtra("codigo",0)!= 0){
            Intent intent = getIntent();
            secao.setNomeSecao(intent.getStringExtra("nomeSecao"));
            secao.setCodigo(intent.getIntExtra("codigo",0));
            nomeSecao.setText(secao.getNomeSecao());
        }

        Button salvar = (Button) findViewById(R.id.salvarSecao);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secao.setNomeSecao(""+nomeSecao.getText());
                initLoader();
            }
        });

    }

    public void initLoader(){
        loaderManager.initLoader(SECAO_LOADER_ID, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new SecaoLoader(this,secao);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
        secao = data.get(data.size()-1);
        Toast.makeText(this,"Seção salva:"+secao.getNomeSecao(),Toast.LENGTH_LONG).show();
        secao = null;
        finish();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}