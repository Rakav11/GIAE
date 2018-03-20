package com.example.admin.appsaldos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

import static com.example.admin.appsaldos.IndexActivity.aluno;


public class RefeicoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refeicoes);
        ArrayAdapter<String> itemslistagem;
        basedados db = new basedados(this);


        final ListView ListaRefeicoes = (ListView) findViewById(R.id.listaRefeicoes);



        if (aluno.Existe()) {
            try {
                aluno.Ementa();
            } catch (IOException e) {
                e.printStackTrace();
            }
            itemslistagem = (new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,aluno.ListaRefeicoes));
            ListaRefeicoes.setAdapter(itemslistagem);
        }
    }
}
