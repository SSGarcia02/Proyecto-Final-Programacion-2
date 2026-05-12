package co.edu.uniquindio.poo.PF.model.patterns.adapter;

import java.util.UUID;

public class PasarelaTarjeta {
    private String endpointBanco;
    private String apiKey;

    public PasarelaTarjeta() {
    }

    public PasarelaTarjeta(String endpointBanco, String apiKey) {
        this.endpointBanco = endpointBanco;
        this.apiKey = apiKey;
    }

    public String getEndpointBanco() { return endpointBanco; }
    public void setEndpointBanco(String endpointBanco) { this.endpointBanco = endpointBanco; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String cobrar(double m, String tarjeta) {
        System.out.println("[Tarjeta] Cobrando $" + m + " a tarjeta " + tarjeta);
        return "TAR-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean reversar(String id) {
        System.out.println("[Tarjeta] Reversando: " + id);
        return true;
    }

    public String consultarTrans(String id) { return "APROBADO"; }
}