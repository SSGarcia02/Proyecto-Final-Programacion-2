package co.edu.uniquindio.poo.PF.model.patterns.adapter;

import java.util.UUID;

public class CajaEfectivo {
    private String idCaja;
    private String operador;

    public CajaEfectivo() {
    }

    public CajaEfectivo(String idCaja, String operador) {
        this.idCaja = idCaja;
        this.operador = operador;
    }

    public String getIdCaja() { return idCaja; }
    public void setIdCaja(String idCaja) { this.idCaja = idCaja; }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String registrarPago(double m) {
        System.out.println("[Caja] Pago en efectivo $" + m + " registrado.");
        return "EFE-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String emitirRecibo(String id) { return "Recibo#" + id; }

    public boolean anularPago(String id) {
        System.out.println("[Caja] Anulando pago: " + id);
        return true;
    }
}