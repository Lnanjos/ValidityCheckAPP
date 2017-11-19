package com.validycheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.validycheck.com.validycheck.loader.SecaoLoader;
import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class SecaoEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Secao>> {

    private static final String LOG_TAG = "SecaoEditorActivity";
    LoaderManager loaderManager = getSupportLoaderManager();
    String myPrefs = "COM.VALIDYCHECK.PREFERENCES";
    private Secao secao = new Secao();
    private String ip_server = "http://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secao_editor);

        SharedPreferences sharedPref = getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        ip_server = sharedPref.getString(getString(R.string.ip_server), getString(R.string.ip_server_default));

        //cancelar a ação
        Button cancelar = (Button) findViewById(R.id.cancelarSecao);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText nomeSecao = (EditText) findViewById(R.id.editText_nomeSecao);

        if ((Long) getIntent().getLongExtra("codigo", 0) != null) {
            Intent intent = getIntent();
            secao.setNomeSecao(intent.getStringExtra("nomeSecao"));
            secao.setCodigo(intent.getLongExtra("codigo", 0));
            nomeSecao.setText(secao.getNomeSecao());
        }

        Button salvar = (Button) findViewById(R.id.salvarSecao);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secao.setNomeSecao("" + nomeSecao.getText());
                initLoader();
            }
        });

    }

    public void initLoader() {
        loaderManager.initLoader(SecaoLoader.SECAO_LOADER_ID, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (secao.getCodigo() == null) {
            return new SecaoLoader(this, secao, SecaoLoader.SAVE_SECAO, ip_server);
        } else {
            return new SecaoLoader(this, secao, SecaoLoader.UPDATE_SECAO, ip_server);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
        secao = data.get(data.size() - 1);
        Toast.makeText(this, "Seção salva:" + secao.getNomeSecao(), Toast.LENGTH_LONG).show();
        secao = null;
        finish();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}