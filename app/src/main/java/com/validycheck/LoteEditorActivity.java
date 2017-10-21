package com.validycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.validycheck.domain.Lote;
import com.validycheck.domain.Secao;

import java.util.ArrayList;

public class LoteEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Lote>>{

    private static final String LOG_TAG = "LoteEditorActivity";
    private Lote lote = new Lote();
    LoaderManager loaderManager = getSupportLoaderManager();
    EditText codBarra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lote_editor);

        ArrayList<Secao> secoes = new ArrayList<>();
        secoes.add(0, new Secao(new Long(0),"Lote"));

  /*      final SpinAdapter spinAdapter = new SpinAdapter(this,R.layout.support_simple_spinner_dropdown_item,0,secoes);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinAdapter.initLoaderSecao();
        spinner.setAdapter(spinAdapter);*/
        //cancelar a ação
        Button cancelar = (Button) findViewById(R.id.cancelarLote);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText nomeLote = (EditText) findViewById(R.id.editText_nomeLote);
        codBarra = (EditText) findViewById(R.id.editText_codLote);

        if((Long)getIntent().getLongExtra("codigo",0)!= null){
            Intent intent = getIntent();
            //lote.setNomeLote(intent.getStringExtra("nomeLote"));
            //lote.setCodigo(intent.getLongExtra("codigo",0));
            //lote.setCodBarraLote(intent.getStringExtra("codBarraLote"));
            //nomeLote.setText(lote.getNomeLote());
            //codBarra.setText(lote.getCodBarraLote());
        }

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
              //  lote.setNomeLote(""+nomeLote.getText());
               // lote.setCodBarraLote(""+codBarra.getText());
                //Secao secao = spinAdapter.getItem(spinner.getSelectedItemPosition());
                //lote.setSecao(secao);
                //loaderManager.initLoader(LoteLoader.PRODUTO_LOADER_ID,null,LoteEditorActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if (data!=null ){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    codBarra.setText(""+barcode.displayValue);
                }else {
                    Toast.makeText(this, "Falha ao ler código de barras", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public Loader<ArrayList<Lote>> onCreateLoader(int id, Bundle args) {
        /*if(lote.getCodigo()==null){
            return new LoteLoader(this,lote,LoteLoader.SAVE_PRODUTO);
        }else{
            return new LoteLoader(this,lote,LoteLoader.UPDATE_PRODUTO);
        }*/
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Lote>> loader, ArrayList<Lote> data) {
        /*lote = data.get(data.size()-1);
        Toast.makeText(LoteEditorActivity.this,"Lote Salvo"+lote.getNomeLote()+"\n"
                +lote.getSecao().getNomeSecao()+" - "
                +lote.getSecao().getCodigo(), Toast.LENGTH_SHORT).show();
        lote = null;*/
        finish();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Lote>> loader) {

    }
/*
    public class SpinAdapter extends ArrayAdapter<Secao> implements LoaderManager.LoaderCallbacks<ArrayList<Secao>>{

        private ArrayList<Secao> secoes;


        public SpinAdapter(@NonNull Context context, @LayoutRes int resource,@IdRes int textViewResourceId, ArrayList<Secao> secoes) {
            super(context, resource);
            this.secoes = secoes;
        }

        public int getCount(){
            if(secoes!= null){
                return secoes.size();
            }else {
                return 0;
            }
        }

        public Secao getItem(int position){
            return secoes.get(position);
        }

        public long getItemId(int position){
            return position;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = new TextView(getContext());
            label.setTextColor(Color.BLACK);
            String toLabel = secoes.get(position).getCodigo()+" - "+secoes.get(position).getNomeSecao();
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
            return new SecaoLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Secao>> loader, ArrayList<Secao> data) {
            secoes = data;
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Secao>> loader) {

        }

        public void initLoaderSecao(){
            loaderManager.initLoader(SecaoLoader.SECAO_LOADER_ID,null,this);
        }
    }*/
}