package com.validycheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.validycheck.com.validycheck.loader.ProdutoLoader;
import com.validycheck.com.validycheck.loader.SecaoLoader;
import com.validycheck.domain.Produto;
import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class ProdutoEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Produto>> {

    private static final String LOG_TAG = "ProdutoEditorActivity";
    LoaderManager loaderManager = getSupportLoaderManager();
    EditText codBarra;
    private Produto produto = new Produto();
    private String ip_server = "http://";
    String myPrefs = "COM.VALIDYCHECK.PREFERENCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_editor);

        SharedPreferences sharedPref = getSharedPreferences(myPrefs,Context.MODE_PRIVATE);
        ip_server = sharedPref.getString(getString(R.string.ip_server),getString(R.string.ip_server_default));

        //cancelar a ação
        Button cancelar = (Button) findViewById(R.id.cancelarProduto);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText nomeProduto = (EditText) findViewById(R.id.editText_nomeProduto);
        codBarra = (EditText) findViewById(R.id.editText_codProduto);

        Secao secao = new Secao();

        if ((Long) getIntent().getLongExtra("codigo", 0) != null) {
            Intent intent = getIntent();
            produto.setNomeProduto(intent.getStringExtra("nomeProduto"));
            produto.setCodigo(intent.getLongExtra("codigo", 0));
            produto.setCodBarraProduto(intent.getStringExtra("codBarraProduto"));
            secao = new Secao(intent.getLongExtra("codigoSecao", 0), intent.getStringExtra("nomeSecao"));
            produto.setSecao(secao);
            nomeProduto.setText(produto.getNomeProduto());
            codBarra.setText(produto.getCodBarraProduto());
        }

        ArrayList<Secao> secoes = new ArrayList<>();
        secoes.add(secao);

        final SpinAdapter spinAdapter = new SpinAdapter(this, R.layout.support_simple_spinner_dropdown_item, 0, secoes);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinAdapter.initLoaderSecao();
        spinner.setAdapter(spinAdapter);


        ImageButton scan = (ImageButton) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProdutoEditorActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        Button salvar = (Button) findViewById(R.id.salvarProduto);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                produto.setNomeProduto("" + nomeProduto.getText());
                produto.setCodBarraProduto("" + codBarra.getText());
                Secao secao = spinAdapter.getItem(spinner.getSelectedItemPosition());
                produto.setSecao(secao);
                loaderManager.initLoader(ProdutoLoader.PRODUTO_LOADER_ID, null, ProdutoEditorActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    codBarra.setText("" + barcode.displayValue);
                } else {
                    Toast.makeText(this, "Falha ao ler código de barras", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public Loader<ArrayList<Produto>> onCreateLoader(int id, Bundle args) {
        if (produto.getCodigo() == null) {
            return new ProdutoLoader(this, produto, ProdutoLoader.SAVE_PRODUTO, ip_server);
        } else {
            return new ProdutoLoader(this, produto, ProdutoLoader.UPDATE_PRODUTO, ip_server);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Produto>> loader, ArrayList<Produto> data) {
        produto = data.get(data.size() - 1);
        Toast.makeText(ProdutoEditorActivity.this, "Produto Salvo" + produto.getNomeProduto() + "\n"
                + produto.getSecao().getNomeSecao() + " - "
                + produto.getSecao().getCodigo(), Toast.LENGTH_SHORT).show();
        produto = null;
        finish();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Produto>> loader) {

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

        public int posicao(Secao secao) {
            return secoes.indexOf(secao);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = new TextView(getContext());
            label.setTextColor(Color.BLACK);
            String toLabel;
            if (secoes.get(position).getNomeSecao() != null) {
                toLabel = secoes.get(position).getNomeSecao();
            } else {
                toLabel = "Selecione uma Seção";
            }
            label.setText(toLabel);

            return label;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = new TextView(getContext());
            label.setTextColor(Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                label.setTextAppearance(R.style.TextAppearance_AppCompat_Headline);
            }
            label.setText(secoes.get(position).getCodigo() + " - " + secoes.get(position).getNomeSecao());

            return label;
        }

        @Override
        public Loader<ArrayList<Secao>> onCreateLoader(int id, Bundle args) {
            return new SecaoLoader(getContext(),ip_server);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
            secoes = data;
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Secao>> loader) {

        }

        public void initLoaderSecao() {
            loaderManager.initLoader(SecaoLoader.SECAO_LOADER_ID, null, this);
        }
    }
}