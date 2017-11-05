package com.validycheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.validycheck.adapter.PagerAdapter;

public class MainActivity extends AppCompatActivity {


    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Conecte o layout da aba com o view pager. Isto irá
        //   1. Atualizar o layout da aba quando o view pager for deslizado
        //   2. Atualizar o view pager quando uma aba for selecionada
        //   3. Definir os nomes da aba do layout da aba com os títulos do adapter do view pager
        //      chamando onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == PagerAdapter.SECAO_FRAG) {
                    Intent intent = new Intent(MainActivity.this, SecaoEditorActivity.class);
                    startActivity(intent);
                } else if (viewPager.getCurrentItem() == PagerAdapter.PRODUTO_FRAG) {
                    Intent intent = new Intent(MainActivity.this, ProdutoEditorActivity.class);
                    startActivity(intent);
                } else if (viewPager.getCurrentItem() == PagerAdapter.LOTE_FRAG) {
                    Intent intent = new Intent(MainActivity.this, LoteEditorActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        menu.setGroupVisible(R.id.menuLote,false);
        menu.setGroupVisible(R.id.menuProduto,false);
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

}
