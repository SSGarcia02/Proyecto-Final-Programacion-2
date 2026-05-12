package co.edu.uniquindio.poo.PF.model.patterns.state;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

public class EstadoPagada implements EstadoCompraState {
    @Override public void pagar(Compra c)    { System.out.println("[State] Ya está pagada."); }
    @Override public void confirmar(Compra c) {
        System.out.println("[State] Confirmando compra...");
        c.setEstado(new EstadoConfirmada());
    }
    @Override public void cancelar(Compra c) {
        System.out.println("[State] Cancelando compra pagada...");
        c.setEstado(new EstadoCancelada());
    }
    @Override public void reembolsar(Compra c) { System.out.println("[State] Primero debe cancelar."); }
    @Override public void reportarIncidencia(Compra c) { c.setEstado(new EstadoIncidencia()); }
    @Override public String getDescripcion()  { return "Compra exitosa y pagada"; }
    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.PAGADA; }
}