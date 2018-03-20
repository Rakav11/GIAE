import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Admin on 19/08/2017.
 */

public class Aluno {

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
                    RefreshDadosPessoais();
                    RefreshSaldo();
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
// funçao
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
            saldo = saldo.replace('', '€');
            this.saldo = saldo;
        }


    //// funçao-aqui vai buscar os dados do aluno

        public void RefreshDadosPessoais() throws IOException
        {
            Document doc = (Document) Jsoup.parse(GetSource("https://www.giae.pt/cgi-bin/WebGiae.exe/dadospessoais"));
            Element conteudo = doc.getElementById("conteudopagina");
            Elements links = conteudo.getElementsByClass("tdBorder");

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
                    Scanner scanner = new Scanner(connection.getInputStream(),"ISO-8859-1");
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
