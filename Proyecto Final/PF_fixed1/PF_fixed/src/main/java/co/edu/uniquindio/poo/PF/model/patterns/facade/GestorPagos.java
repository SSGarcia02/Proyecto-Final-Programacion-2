package co.edu.uniquindio.poo.PF.model.patterns.facade;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.patterns.adapter.ProcesadorPago;
import java.util.ArrayList;
import java.util.List;

public class GestorPagos {
    public GestorPagos() {}

    private ProcesadorPago procesador;
    private List<Pago> transacciones = new ArrayList<>();

    public GestorPagos(ProcesadorPago procesador, List<Pago> transacciones) {
        this.procesador     = procesador;
        this.transacciones  = (transacciones != null) ? transacciones : new ArrayList<>();
    }

    public void procesarPago(Pago p) {
        p.procesar();
        transacciones.add(p);
    }

    public void registrarTransaccion(Pago p) { transacciones.add(p); }

    public ProcesadorPago getProcesador() { return procesador; }
    public void setProcesador(ProcesadorPago procesador) { this.procesador = procesador; }
    public List<Pago> getTransacciones() { return transacciones; }
    public void setTransacciones(List<Pago> transacciones) { this.transacciones = transacciones; }
}
