package com.conversor.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Conexion_api {
    private String direccion;

    public Conexion_api(){
        direccion ="https://v6.exchangerate-api.com/v6/9efa50df1cfc691d31bbebe1/latest/";
    }

    public void referencia(String moneda){
        this.direccion += moneda;
    }

    public String response_json() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

}
