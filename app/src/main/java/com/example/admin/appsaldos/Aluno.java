package com.example.admin.appsaldos;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

public class Aluno {
    public int getImage() {
        return image;
    }
    public void setImage(int imageN) {
        this.image = imageN;
    }


    public String getMsgType() {
        return MsgType;
    }
    public void setMsgType(String text) {
        this.MsgType = text;
    }



    private int image;
    private String MsgType;

    private String cartao;
    private String password;
    private String school;

    private String cookies;
    private boolean valido = false;

    //Talvez depois por isto em privado
    public String nome;
    public String email;
    public String telefone;
    public String saldo;
    public String info;
    // array de strings

    ArrayList<String> Extrato= new ArrayList<String>();
    ArrayList<String> ListaRefeicoes= new ArrayList<String>();

    public String Valores;
    public String Refeicoes;



    //
    public Aluno(String cartao, String password, String school) throws MalformedURLException, IOException // construtor
    {
        AtualizarDados(cartao,password,school);

        if(this.cartao != null)
        {
            RefreshCookies();
            if(cookies != null)
            {
                valido = true; // o valido começa sempre em falso

                /// afinal pode ficar isto porque quando chamamos aluno.extracto so estamos a chamar esta funçao

            }
        }
    }
    //funçao
    public void AtualizarDados(String cartao, String password, String school) throws IOException /// coloca os dados do alnu
    {
        if(cartao.length() > 0 && password.length() > 0 && school.length() > 0)
        {
            this.cartao = cartao;
            this.password = password;
            this.school = school;
        }
    }

    public boolean Existe()
    {
        return valido;
    }

    public void RefreshSaldo() throws IOException
    {
        Document doc = (Document) Jsoup.parse(GetSource("https://www.giae.pt/cgi-bin/WebGiae.exe/saldo"));
        Element conteudo = doc.getElementById("conteudopagina");
        Elements links = conteudo.getElementsByClass("tdsimples");

        String saldo = null;
        for (Element link : links)
        {
            saldo = link.text();
        }

        if(saldo != null)
        {
            saldo = saldo.replace('', '€');
            this.saldo = saldo;
        }
    }


    //// funçao-aqui vai buscar os dados do aluno

    public void RefreshDadosPessoais() throws IOException
    {
        Document doc = (Document) Jsoup.parse(GetSource("https://www.giae.pt/cgi-bin/WebGiae.exe/dadospessoais"));
        Element conteudo = doc.getElementById("conteudopagina");
        Elements links = conteudo.getElementsByClass("tdBorder");
//qd existem nomes de tags iguais o links é um array e o carlos ta aceder ao i == 0 ,i == 1 , i == 2 ou seja as diferentes posiçoes do array
        int i = 0;
        for (Element link : links)
        {
            if(i == 0)
                nome = link.text();
            if(i == 1)
                telefone = link.text();
            if(i == 2)
                email = link.text();

            i++;
        }

    }
    public void Ementa() throws IOException
    {
        Document doc = (Document) Jsoup.parse(GetSource("https://www.giae.pt/cgi-bin/WebGiae.exe/ementas"));
        Element conteudo = doc.getElementById("conteudopagina");
        Elements links = conteudo.getElementsByTag("td");
        Refeicoes="";
        if(!ListaRefeicoes.isEmpty()){
            ListaRefeicoes.clear();
        }
        int i = 0;
        for (Element link : links)
        {
            if (i == 10)
                i = 0;

            if (i == 0) {
                Refeicoes=link.text().substring(0, 10)+"\n" ;
            }
            else if(i == 5)
                Refeicoes = Refeicoes+link.text() + "\n";
            else if(i == 6)
                Refeicoes = Refeicoes+link.text() + "\n";


            else if(i == 7) {
                Refeicoes = Refeicoes+link.text() + "\n";

            }
            else if(i == 8) {
                Refeicoes = Refeicoes + link.text() + "\n";
                ListaRefeicoes.add(Refeicoes);
            }

            i++;
        }
    }
    public void Extrato() throws IOException
    {
        Document doc = (Document) Jsoup.parse(GetSource("https://www.giae.pt/cgi-bin/WebGiae.exe/extrato"));
        Element conteudo = doc.getElementById("conteudopagina");
        Elements links = conteudo.getElementsByTag("td");
        Valores="";
        if(!Extrato.isEmpty()){
            Extrato.clear();
        }
        for (int i = 11; i < links.size(); i++)
        {
            if((i - 10) % 6 == 0)
            {
                if(links.get(i).html().length() != 0) {

                    Pattern p = Pattern.compile("href=(.*?)");
                    Matcher m = p.matcher(links.get(i).html());
                    String url = null;
                    if (m.find()) {
                        url = m.group(1);
                        Valores = Valores + url + "\n";
                    }
                }

                Extrato.add(Valores);
                Valores = "";

            }
            else
                Valores = Valores + links.get(i).text() + "\n";
        }


    }



    private void RefreshCookies() throws MalformedURLException, IOException
    {
        String cookies = null;

        //Converte os parametros para bytes para
        String urlParameters  = "codEscola="+school+"&numCartao="+cartao+"&acesso="+password;
        byte[] postData = urlParameters.getBytes();
        int postDataLength = postData.length;

        //Cria a conexão e faz set das propriedades http
        URL url = new URL("https://www.giae.pt/cgi-bin/WebGiae.exe/login");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(true);

        //Escreve os dados do post
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

        wr.write(postData);
        wr.close();


        //Faz o post
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) //Se os servidores da giae responderam
        {
            //Get as cookies para uma lista mas depois converte a lista para string
            List<String> cookieList = connection.getHeaderFields().get("Set-Cookie");

            if(cookieList != null)
            {
                cookies = cookieList.toString();
                cookies = cookies.replaceAll(",", ";");
                cookies = cookies.substring(1, cookies.length());
            }
        }

        this.cookies = cookies;
    }

    private String GetSource(String link) throws IOException
    {
        String source = null;
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cookie", cookies);
        connection.getResponseCode(); //Pode ser feito aqui outra verificação do ok do http

        if(connection.getURL().toString().equals("https://www.giae.pt/cgi-bin/webgiae.exe/mapa"))
        {
            //Se retornar para o mapa significa que as cookies estão incorretas, vamos dar refresh das cookies e tentar de novo
            RefreshCookies();
            HttpURLConnection connection2 = (HttpURLConnection) url.openConnection();
            connection2.setRequestMethod("GET");
            connection2.setRequestProperty("Cookie", cookies);
            connection2.getResponseCode();

            if(!connection2.getURL().toString().equals("https://www.giae.pt/cgi-bin/webgiae.exe/mapa")) //Funcionou, as cookies estavam desatualizadas
            {
                Scanner scanner = new Scanner(connection2.getInputStream(),"ISO-8859-1");
                scanner.useDelimiter("\\Z");
                source = scanner.next();
            }
            else
            {
                //A segunda tentativa não funcionou!
                //FALTA FAZER AQUI O ERRO
            }

        }
        else //Funcionou tudo bem a primeira!
        {
            //Get codigo html da página
            Scanner scanner = new Scanner(connection.getInputStream(),"ISO-8859-1");
            scanner.useDelimiter("\\Z");
            source = scanner.next();
        }

        return source;
    }



}
