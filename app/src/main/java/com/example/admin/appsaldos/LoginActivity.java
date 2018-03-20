package com.example.admin.appsaldos;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import static com.example.admin.appsaldos.IndexActivity.aluno;

public class LoginActivity extends AppCompatActivity {
    basedados db = new basedados(this);


        EditText etUser;
        EditText  etPass;
    String Escola;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUser = (EditText) findViewById(R.id.etUser);
         etPass = (EditText) findViewById(R.id.etPass);

         Escola = getIntent().getStringExtra("keyescola");

        Button Entrar = (Button) findViewById(R.id.btnEntrar);

        Entrar.setOnClickListener(new View.OnClickListener() {
            String UtilizadorSTR =  etUser.getText().toString().trim();
            String PassSTR = etPass.getText().toString().trim();

            @Override
            public void onClick(View view) {
                final String ETuser = ((EditText) findViewById(R.id.etUser)).getText().toString().trim();
                final String ETpass = ((EditText) findViewById(R.id.etPass)).getText().toString().trim();

                new Thread(new Runnable() {
                    public void run() {


                        // vamos chamar esta funçao para ir buscar o saldo e os dados do aluno
                        try {
                            aluno = new Aluno(ETuser, ETpass, Escola);
                        } catch (IOException e) {
                            e.printStackTrace();

                        }

                        if (aluno.Existe()) {
                            db.addUser(new User(ETuser, ETpass , Escola));

                            Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent2);
                            /// criar o novo utilizador e vai depois para a main activity
                        }
                    }
                } ).start();               //Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                //startActivity(intent);
            }
        });


    }


}
