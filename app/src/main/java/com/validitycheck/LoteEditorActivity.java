package com.validitycheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.validitycheck.com.validycheck.loader.LoteLoader;
import com.validitycheck.com.validycheck.loader.ProdutoLoader;
import com.validitycheck.domain.Lote;
import com.validitycheck.domain.Produto;
import com.validitycheck.domain.Secao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LoteEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Lote>> {

    private static final String LOG_TAG = "LoteEditorActivity";
    LoaderManager loaderManager = getSupportLoaderManager();
    AutoCompleteTextView nomeLote;
    String myPrefs = "COM.VALIDYCHECK.PREFERENCES";
    private Lote lote = new Lote();
    private String ip_server = "http://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lote_editor);

        SharedPreferences sharedPref = getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        ip_server = sharedPref.getString(getString(R.string.ip_server), getString(R.string.ip_server_default));

        ArrayList<Secao> produtos = new ArrayList<>();
        produtos.add(0, new Secao(new Long(0), "Lote"));

        //cancelar a ação
        Button cancelar = (Button) findViewById(R.id.cancelarLote);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setMinDate(new Date().getTime());


        final Calendar c = Calendar.getInstance();

        ImageButton scan = (ImageButton) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoteEditorActivity.this, ScanBarcodeActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        Button salvar = (Button) findViewById(R.id.salvarLote);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                lote.setValidade(c.getTime());
                if (lote.getProduto().getCodBarraProduto() != null) {
                    loaderManager.initLoader(LoteLoader.LOTE_LOADER_ID, null, LoteEditorActivity.this);
                } else {
                    Toast.makeText(LoteEditorActivity.this, "Produto não encontrado", Toast.LENGTH_LONG).show();
                }
            }
        });


        ArrayList<Produto> produtosarray = new ArrayList<>();
        AutoProdutoAdapter completeAdapter = new AutoProdutoAdapter(this, R.layout.produto_auto, produtosarray);
        completeAdapter.initLoaderProduto();

        nomeLote = (AutoCompleteTextView) findViewById(R.id.editText_nomeLote);
        nomeLote.setAdapter(completeAdapter);

        nomeLote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lote.setProduto((Produto) nomeLote.getAdapter().getItem(position));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nomeLote.getWindowToken(), 0);
            }
        });

        Produto produto = null;
        if ((Long) getIntent().getLongExtra("codigo", 0) != null) {
            Intent intent = getIntent();
            lote.setCodigo(intent.getLongExtra("codigo", 0));
            lote.setValidade(new Date(intent.getLongExtra("validade", 0)));
            produto = new Produto(
                    intent.getLongExtra("produtoCodigo", 0),
                    intent.getStringExtra("produtoNome"),
                    intent.getStringExtra("codBarraProduto"),
                    new Secao(intent.getLongExtra("secaoCodigo", 0), intent.getStringExtra("secaoNome"))
            );
            lote.setProduto(produto);
            c.setTime(lote.getValidade());
            datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            if (produto.getCodBarraProduto() != null) {
                nomeLote.setText(produto.getCodBarraProduto() + " - " + produto.getNomeProduto());
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    nomeLote.setText("" + barcode.displayValue);
                } else {
                    Toast.makeText(this, "Falha ao ler código de barras", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public Loader<ArrayList<Lote>> onCreateLoader(int id, Bundle args) {
        if (lote.getCodigo() == null) {
            return new LoteLoader(this, lote, LoteLoader.SAVE_LOTE, ip_server);
        } else {
            return new LoteLoader(this, lote, LoteLoader.UPDATE_LOTE, ip_server);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Lote>> loader, ArrayList<Lote> data) {
        lote = data.get(data.size() - 1);
        Toast.makeText(LoteEditorActivity.this, "Lote Salvo" + lote.getProduto().getNomeProduto() + "\n"
                + " - "
                + lote.getValidade(), Toast.LENGTH_SHORT).show();
        lote = null;
        finish();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Lote>> loader) {

    }

    public class AutoProdutoAdapter extends ArrayAdapter<Produto> implements LoaderManager.LoaderCallbacks<ArrayList<Produto>> {
        private ArrayList<Produto> items;
        private ArrayList<Produto> itemsAll;
        private ArrayList<Produto> suggestions;
        Filter nameFilter = new Filter() {

            @Override
            public String convertResultToString(Object resultValue) {
                String str = ((Produto) (resultValue)).getCodBarraProduto() + " - " + ((Produto) (resultValue)).getNomeProduto();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (Produto produto : itemsAll) {
                        if (produto.getNomeProduto().toLowerCase().contains(constraint.toString().toLowerCase())
                                || produto.getCodBarraProduto().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(produto);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<Produto> filteredList = (ArrayList<Produto>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (Produto c : filteredList) {
                        add(c);
                    }
                    notifyDataSetChanged();
                }
            }
        };
        private int viewResourceId;

        public AutoProdutoAdapter(Context context, int viewResourceId, ArrayList<Produto> items) {
            super(context, viewResourceId, items);
            this.items = items;
            this.itemsAll = (ArrayList<Produto>) items.clone();
            this.suggestions = new ArrayList<>();
            this.viewResourceId = viewResourceId;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(viewResourceId, null);
            }
            Produto produto = items.get(position);
            if (produto != null) {
                TextView produtoNameLabel = (TextView) v.findViewById(R.id.produtoNameLabel);
                if (produtoNameLabel != null) {
                    produtoNameLabel.setText(produto.getCodBarraProduto() + " - " + produto.getNomeProduto());
                }
            }
            return v;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        @Override
        public Loader<ArrayList<Produto>> onCreateLoader(int id, Bundle args) {
            return new ProdutoLoader(getContext(), ip_server);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Produto>> loader, ArrayList<Produto> data) {
            itemsAll = data;
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Produto>> loader) {

        }

        public void initLoaderProduto() {
            loaderManager.initLoader(ProdutoLoader.PRODUTO_LOADER_ID, null, this);
        }

    }

}