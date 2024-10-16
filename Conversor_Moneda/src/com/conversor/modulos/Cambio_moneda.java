package com.conversor.modulos;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Map;

public class Cambio_moneda {
    @SerializedName("conversion_rates")
    private Map<String, Double> Cambio;

    public Map<String, Double> getCambio() {
        return Cambio;
    }

    public ArrayList<String> monedaNombre(){
        return new ArrayList<>(Cambio.keySet());
    }
}
