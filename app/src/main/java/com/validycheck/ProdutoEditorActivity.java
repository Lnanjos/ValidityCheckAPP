package com.validycheck;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.validycheck.domain.Produto;
import com.validycheck.domain.Secao;
import java.util.ArrayList;

public class ProdutoEditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ProdutoEditorActivity";
    private Produto produto = new Produto();
    LoaderManager loaderManager = getSupportLoaderManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_editor);

        String [] spinnerlist = {"Bebidas","Latcineos","Frios"};
        ArrayList<Secao> secoes = new ArrayList<Secao>();
        secoes.add(0,new Secao(new Long(0),"Bebidas"));
        secoes.add(1,new Secao(new Long(1),"frios"));
        secoes.add(2,new Secao(new Long(2),"Latcíneos"));

        final SpinAdapter spinAdapter = new SpinAdapter(this,R.layout.support_simple_spinner_dropdown_item,0,secoes);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(spinAdapter);
        //cancelar a ação
        Button cancelar = (Button) findViewById(R.id.cancelarProduto);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final EditText nomeProduto = (EditText) findViewById(R.id.editText_nomeProduto);
        final EditText codBarra = (EditText) findViewById(R.id.editText_codProduto);

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
                produto.setCodBarraProduto(""+codBarra.getText());
                Secao secao = spinAdapter.getItem(spinner.getSelectedItemPosition());
                produto.setSecao(secao);
                Toast.makeText(ProdutoEditorActivity.this,""+produto.getNomeProduto()+"\n"
                        +produto.getSecao().getNomeSecao()+" - "
                        +produto.getSecao().getCodigo(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class SpinAdapter extends ArrayAdapter<Secao>{

        private ArrayList<Secao> secoes;

        public SpinAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, ArrayList<Secao> secoes) {
            super(context, resource, textViewResourceId);
            this.secoes = secoes;
        }

        public int getCount(){
            return secoes.size();
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
            label.setText(secoes.get(position).getNomeSecao());

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
    }
}