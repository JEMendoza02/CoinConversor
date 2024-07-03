package com.alura.johanmendoza.conversor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Scanner;

public class MainRequest {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/f11450da711022525ba0917b/latest/";

    public static String obtenerDatosAPI(String codigoMoneda) throws Exception {
        String urlStr = API_URL + codigoMoneda;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new RuntimeException("Error en la conexión: " + responseCode);
        }
    }

    public static double obtenerTasaDeCambio(String codigoMonedaOrigen, String codigoMonedaDestino) throws Exception {
        String respuestaAPI = obtenerDatosAPI(codigoMonedaOrigen);

        // Procesa la respuesta de la API usando Gson
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(respuestaAPI, JsonObject.class);

        // Verifica que la respuesta contenga el objeto conversion_rates
        if (!jsonResponse.has("conversion_rates")) {
            throw new RuntimeException("La respuesta de la API no contiene tasas de cambio.");
        }

        // Extrae la tasa de cambio para la moneda destino
        JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");
        if (!rates.has(codigoMonedaDestino)) {
            throw new RuntimeException("La tasa de cambio para la moneda " + codigoMonedaDestino + " no está disponible.");
        }

        return rates.get(codigoMonedaDestino).getAsDouble();
    }

    public static double convertir(String codigoMonedaOrigen, String codigoMonedaDestino, double cantidad) throws Exception {
        double tasaDeCambio = obtenerTasaDeCambio(codigoMonedaOrigen, codigoMonedaDestino);
        return cantidad * tasaDeCambio;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Pide al usuario los códigos de las monedas y la cantidad
            System.out.print("Ingresa el código de la Moneda de origen: ");
            String codigoMonedaOrigen = scanner.nextLine().toUpperCase();
            System.out.print("Ingresa el código de la Moneda de destino: ");
            String codigoMonedaDestino = scanner.nextLine().toUpperCase();
            System.out.print("Ingresa la cantidad a convertir: ");
            double cantidad = scanner.nextDouble();

            // Realiza la conversión
            double resultado = convertir(codigoMonedaOrigen, codigoMonedaDestino, cantidad);

            // Muestra el resultado
            System.out.println("Conversión: " + cantidad + " " + codigoMonedaOrigen + " a " + codigoMonedaDestino + " = " + resultado);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}