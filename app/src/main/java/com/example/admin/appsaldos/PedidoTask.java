package com.example.admin.appsaldos;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Admin on 17/09/2017.
 */

public class PedidoTask extends AsyncTask<String, String, String> {
    @Override
    protected String  doInBackground(String... params) {
        final String[] source = {null};

        final int[] count = {0};
        final int distritoId = 0;
        final URL[] url = {null};
        final int[] arrayIds = new int[99999];
        try {


            ////verificar se esta bem
            url[0] = new URL( params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url[0].openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Get codigo html da página
        Scanner scanner = null;
        try {
            scanner = new Scanner(connection.getInputStream(), "ISO-8859-1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter("\\Z");
        source[0] = scanner.next();

//JSOUP
        Document doc = (Document) Jsoup.parse(source[0]);
        Element conteudo = doc.getElementById("cbescolas");
        Elements links = conteudo.getElementsByTag("script");

        String arrayEscolas = null;
        for (Element link : links) {
            for (DataNode node : link.dataNodes())
                arrayEscolas = node.getWholeData();
        }
        return arrayEscolas;
    }
}
