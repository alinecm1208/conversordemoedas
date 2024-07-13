import com.google.gson.Gson;
import model.Moeda;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String moedaBase;
        String moedaComparar;
        URI path;
        Moeda moeda;
        Double rate;
        Double valorDigitado;
        HttpResponse<String> respostaAPI;
        var key = "";

        Scanner scanner = new Scanner(System.in);
        boolean loop = true;

        while (loop) {

            System.out.println("***************************************************************");
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("Seja bem-vindo/a ao Conversor de moeda =]");
            System.out.println("1) Dólar =>> Peso argentino"); //dolar
            System.out.println("2) Peso argentino =>> Dólar");
            System.out.println("3) Dólar =>> Real brasileiro"); //dolar
            System.out.println("4) Real brasileiro =>> Dólar");
            System.out.println("5) Dólar =>> Peso comlombiano"); //dolar
            System.out.println("6) Peso comlombiano =>> Dólar");
            System.out.println("7) sair");
            System.out.println("Escolha uma opção válida:");
            System.out.println("***************************************************************");

            //pega o valor que vc digitou entre as opções
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    //Dólar =>> Peso argentino
                    moedaBase = "USD";
                    moedaComparar = "ARS";
                    break;
                case 2:
                    //Peso argentino =>> Dólar
                    moedaBase = "ARS";
                    moedaComparar = "USD";
                    break;
                case 3:
                    //Dólar =>> Real brasileiro
                    moedaBase = "USD";
                    moedaComparar = "BRL";
                    break;
                case 4:
                    //Real brasileiro =>> Dólar
                    moedaBase = "BRL";
                    moedaComparar = "USD";
                    break;
                case 5:
                    //Dólar =>> Peso comlombiano
                    moedaBase = "USD";
                    moedaComparar = "COP";
                    break;
                case 6:
                    //Peso comlombiano =>> Dólar
                    moedaBase = "COP";
                    moedaComparar = "USD";
                    break;
                case 7:
                    loop = false;
                    System.out.println("Saindo do sistema");
                    continue; // Continue para evitar o fechamento do scanner antes do loop terminar
                default:
                    System.out.println("Digite um valor valido que seja entre 1 e 7");
                    continue; // Continue para evitar o fechamento do scanner antes do loop terminar
            }

            System.out.println("Digite o valor que deseja converter:");
            valorDigitado = Double.valueOf(scanner.nextLine());
            //cria a url para se fazer a requisição
            path = getUrl(key, moedaBase);
            // pega a resposta da api aonde vem todos os dados da moeda
            respostaAPI = getStringHttpResponse(path);
            //pega o json da resposta e converte para uma classe chamada moeda
            moeda = apiToModel(respostaAPI);
            //pega o atributo rate que é o valor da moeda ue vai se comparar
            rate = getMoedaParaComparar(moeda, moedaComparar);
            System.out.println("Valor " + valorDigitado + " [" + moedaBase + "] corresponde ao valor final de =>>> " + valorDigitado * rate + " [" + moedaComparar + "]");

        }
        scanner.close();
    }

    private static URI getUrl(String key, String baseFiat) {
        URI path = URI.create("https://v6.exchangerate-api.com/v6/" + key + "/latest/"+baseFiat);
        return path;
    }

    private static Double getMoedaParaComparar(Moeda resp, String searchValue) {
        Double arsRate = resp.getConversion_rates().get(searchValue);
        return arsRate;
    }

    private static Moeda apiToModel(HttpResponse<String> response) {
        Gson gson = new Gson();
        Moeda resp = gson.fromJson(response.body(), Moeda.class);
        return resp;
    }

    private static HttpResponse<String> getStringHttpResponse(URI path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(path)
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient
                    .newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}