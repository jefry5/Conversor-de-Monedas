package com.conversor.gestor;

import com.conversor.modulos.Cambio_moneda;
import com.conversor.util.Conexion_api;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Gestionador {
    private Cambio_moneda cambio;
    private ArrayList<String> monedas;
    private Conexion_api api;
    private Scanner entrada;
    private int indice; //Cuenta los indice del menu de opciones

    public Gestionador(){
        cambio = new Cambio_moneda();
        api = new Conexion_api();
        entrada = new Scanner(System.in);
        this.indice = 0;
        llenarCambio();
    }

    private void llenarCambio(){
        Gson gson = new Gson();
        String ref = menuReferencia();
        api.referencia(ref);
        try {
            cambio = gson.fromJson(api.response_json(), Cambio_moneda.class);
            this.monedas = cambio.monedaNombre();
        } catch (NullPointerException e) {
            System.out.println("Moneda de referencia no encontrada");
        } catch (InterruptedException e){
            System.out.println("Ocurrio una interrupción");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Ocurrio un error inesperado");
            System.out.println(e.getMessage());
        }
    }

    private String menuReferencia(){
        String monedaR;
        System.out.println("Digite la moneda de referencia: ");
        System.out.println("Ejm: USD");
        System.out.println("Nota: Si presiona Enter se pondrá por defecto USD");
        monedaR = entrada.nextLine();
        if (monedaR.isEmpty())
            monedaR = "USD";
        return monedaR.toUpperCase();
    }

    public void menu(){
        int opcion = -1; //El indice inicia como 0, entonces le pongo un numero diferente para empezar la iteración
        while(opcion != indice){
            menuOpcion();
            System.out.print("Seleccione una opcion: ");
            opcion = entrada.nextInt();
            entrada.nextLine(); //Consumir salto de línea

            if(opcion > 0 && opcion < indice){
                System.out.println(convertirMonedaMenu(opcion) + "\n");
                System.out.print("Presiona Enter para continuar...");
                entrada.nextLine();
            }
        }
    }

    private void menuOpcion(){
        indice = 1;
        System.out.println("***CONVERSOR DE MONEDAS***");
        for(int i=0; i<monedas.size() - 1; i++){
            //Se crea el menu de conversión (ejm: USD <==> PEN)
            System.out.println(indice++ + ". " + monedas.get(0) + " <==> " + monedas.get(i+1));
        }
        System.out.println(indice + ". " + "Salir");
    }

    private void menuOpcionCambio(int opcion){
        System.out.println("**Cambio de " + monedas.get(0) + " <==> " + monedas.get(opcion) + "**");
        System.out.println("1. " + monedas.get(0) + " ==> " + monedas.get(opcion));
        System.out.println("2. " + monedas.get(opcion) + " ==> " + monedas.get(0));
    }

    private String convertirMonedaMenu(int opcion){
        String mensaje = "";
        int opcionConvertir = 0;
        double valor = 0.0, valorConvertido = 0.0;
        while(opcionConvertir < 1 || opcionConvertir > 2){
            menuOpcionCambio(opcion);
            System.out.print("Seleccione una opcion: ");
            opcionConvertir = entrada.nextInt();
            entrada.nextLine(); //Consumir salto de línea
        }

        //Se encarga del mensaje y la conversión de la moneda
        System.out.print("Digite la cantidad a convertir: ");
        valor = entrada.nextDouble();
        entrada.nextLine(); //Consumir salto de línea
        valorConvertido = convertirMoneda(opcionConvertir, opcion, valor);
        switch (opcionConvertir){
            case 1: mensaje = "El valor de " + valor + "[" + monedas.get(0) + "] corresponde al valor final de =>>> " +
                                valorConvertido + "[" + monedas.get(opcion) + "]";
                    break;
            case 2: mensaje = "El valor de " + valor + "[" + monedas.get(opcion) + "] corresponde al valor final de =>>> " +
                                valorConvertido + "[" + monedas.get(0) + "]";
                    break;
        }

        return mensaje;
    }

    private double convertirMoneda(int opcionConvertir, int opcion, double valor){
        double valorConvertido = 0.0;
        switch (opcionConvertir){
            case 1: valorConvertido = valor * cambio.getCambio().get(monedas.get(opcion));
                break;
            case 2: valorConvertido = valor / cambio.getCambio().get(monedas.get(opcion));
                break;
        }
        return valorConvertido;
    }

}
