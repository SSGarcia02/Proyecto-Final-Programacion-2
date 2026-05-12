package co.edu.uniquindio.poo.PF.model.patterns.adapter;

import java.util.UUID;

public class PasarelaPSE {
    private String urlPSE;
    private String clientId;

    public PasarelaPSE() {
    }

    public PasarelaPSE(String urlPSE, String clientId) {
        this.urlPSE = urlPSE;
        this.clientId = clientId;
    }

    public String getUrlPSE() { return urlPSE; }
    public void setUrlPSE(String urlPSE) { this.urlPSE = urlPSE; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String iniciarTransaccion(double m) {
        System.out.println("[PSE] Iniciando transaccion por $" + m);
        return "PSE-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String consultarEstado(String id) {
        return "APROBADO";
    }

    public boolean solicitarDevolucion(String id) {
        System.out.println("[PSE] Devolucion solicitada para: " + id);
        return true;
    }
}