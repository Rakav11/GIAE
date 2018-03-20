package com.example.admin.appsaldos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.admin.appsaldos.IndexActivity.aluno;

public class ExtractoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracto);
        ArrayAdapter<String> itemslistagem;
        basedados db = new basedados(this);


        final ListView ListaExtrato = (ListView) findViewById(R.id.lista);




        if (aluno.Existe()) {

            try {
                aluno.Extrato();
            } catch (IOException e) {
                e.printStackTrace();
            }
            itemslistagem = (new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,aluno.Extrato));
            ListaExtrato.setAdapter(itemslistagem);
        }

    }
}
