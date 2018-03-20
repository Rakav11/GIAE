package com.example.admin.appsaldos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexActivity extends AppCompatActivity {
    public static Aluno aluno = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


       final basedados db = new basedados(this);


            new Thread(new Runnable() {
                public void run() {
                    if (db.checkUser(1)) {

                        User utilizador = db.getUser(1);
                        String user = utilizador._utilizador;
                        String password = utilizador._pass;
                        String school = (utilizador._escola);

                        // vamos chamar esta funçao para ir buscar o saldo e os dados do aluno
                        try {
                            aluno = new Aluno(user, password, school);
                            Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }

                }
            } ).start();


            final String[] source = {null};

            final int[] count = {0};
            final int distritoId = 0;
            final URL[] url = {null};
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(IndexActivity.this, android.R.layout.select_dialog_item);
            final ArrayAdapter<String> arrayAdapterDistritos = new ArrayAdapter<String>(IndexActivity.this, android.R.layout.select_dialog_item);

            final int[] arrayIds = new int[99999];

            final Button btnescola = (Button) findViewById(R.id.btnEscolas);
            Button btndistrito = (Button) findViewById(R.id.btnDistritos);
            btnescola.setVisibility(View.INVISIBLE);
            arrayAdapterDistritos.add("Aveiro");
            arrayAdapterDistritos.add("Beja");
            arrayAdapterDistritos.add("Braga");
            arrayAdapterDistritos.add("Bragança");
            arrayAdapterDistritos.add("Castelo Branco");
            arrayAdapterDistritos.add("Coimbra");
            arrayAdapterDistritos.add("Évora");
            arrayAdapterDistritos.add("Faro");
            arrayAdapterDistritos.add("Guarda");
            arrayAdapterDistritos.add("Leiria");
            arrayAdapterDistritos.add("Lisboa");
            arrayAdapterDistritos.add("Portalegre");
            arrayAdapterDistritos.add("Porto");
            arrayAdapterDistritos.add("Santarém");
            arrayAdapterDistritos.add("Setúbal");
            arrayAdapterDistritos.add("Viana do Castelo");
            arrayAdapterDistritos.add("Vila Real");
            arrayAdapterDistritos.add("Viseu");


            TextView texto = (TextView) findViewById(R.id.textView);


            btndistrito.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(IndexActivity.this);
                    builderSingle.setIcon(R.drawable.ic_launcher);
                    builderSingle.setTitle("Escolha");
                    builderSingle.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapterDistritos, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            arrayAdapter.clear();
                            btnescola.setVisibility(View.VISIBLE);
                            int posicao = which + 1;


                            String url = "https://www.giae.pt/cgi-bin/WebGiae.exe/mapa?codDistrito=" + posicao;


/// ver pode SER DAQUI O  ERRO
                            String arrayEscolas = null;
                            try {

                                arrayEscolas = new PedidoTask().execute(url).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Position :" + arrayEscolas, Toast.LENGTH_LONG).show();
                            int contador = 0;
                            ArrayList<Escola> escolas = new ArrayList<>();
                            Escola escola = new Escola();
                            arrayEscolas = arrayEscolas.replace("\\'", "`");


                            for (int i = 0; i < arrayEscolas.length(); i++) {
                                if (arrayEscolas.charAt(i) == '\'' && arrayEscolas.charAt(i - 1) == '=') {
                                    int charContador = 1;
                                    String str = "";
                                    while (arrayEscolas.charAt(i + charContador) != '\'') {
                                        str = str + arrayEscolas.charAt(i + charContador);
                                        charContador++;
                                    }

                                    i += charContador;

                                    switch (contador) {
                                        case 0:
                                            escola.id = Integer.parseInt(str);
                                            contador++;
                                            break;
                                        case 1:
                                            escola.nome = str;
                                            contador++;
                                            break;
                                        case 2:
                                            escola.estado = Integer.parseInt(str);
                                            contador++;
                                            break;
                                        case 4:
                                            escolas.add(escola);
                                            escola = new Escola();
                                            contador = 0;
                                            break;
                                        default:
                                            contador++;
                                            break;
                                    }
                                }
                            }

                            for (Escola es : escolas) {

                                if (es.estado != 0) {

                                    arrayIds[count[0]] = es.id;
                                    arrayAdapter.add(es.nome);
                                    count[0] = count[0] + 1;

                                }


                            }


                        }
                    });
                    builderSingle.show();


                }
            });
            btnescola.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(IndexActivity.this);
                    builderSingle.setIcon(R.drawable.ic_launcher);
                    builderSingle.setTitle("Escolha");
                    builderSingle.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            intent.putExtra("keyescola", String.valueOf(arrayIds[which]));
                            startActivity(intent);

                            Toast.makeText(getApplicationContext(), "Position :" + arrayIds[which], Toast.LENGTH_LONG).show();

                        }
                    });
                    builderSingle.show();


                }
            });
        }
    }

