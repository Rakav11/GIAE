package com.example.admin.appsaldos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static com.example.admin.appsaldos.IndexActivity.aluno;


public class MainActivity extends AppCompatActivity {

    /// se retirar o new basedados obtenho um erro diferente
basedados db = new basedados(this);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView texto = (TextView) findViewById(R.id.textinicial);
        // CRIOU UM COISA NOVA É UMA CLONE FIESTA NAS TREADS SE HOUVER ALGUM PROBLEMA VER : FOMOS VER ISTO DEVIDO A UM ERRO DE INTERNET
        // "https://stackoverflow.com/questions/19740604/how-to-fix-networkonmainthreadexception-in-android"
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        if(aluno.Existe())
        {
            try {
                aluno.RefreshSaldo();
                aluno.RefreshDadosPessoais();
            } catch (IOException e) {
                e.printStackTrace();
            }
            User teste = db.getUser(1);
            texto.setText(aluno.nome+"\n"+aluno.telefone+"\n"+aluno.email+"\n"+aluno.saldo+"\n"+teste._utilizador+"");

        }
        else
        {
            texto.setText("Dados incorrectos");
        }

        final Button botao = (Button) findViewById(R.id.botao);
                botao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RefeicoesActivity.class);
                startActivity(intent);
            }
        });
        final Button botao2 = (Button) findViewById(R.id.btnindex);
        botao2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IndexActivity.class);
                startActivity(intent);
            }
        });
        final Button botaoextrato = (Button) findViewById(R.id.button);
        botaoextrato.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExtractoActivity.class);
                startActivity(intent);
            }
        });



    }

}
