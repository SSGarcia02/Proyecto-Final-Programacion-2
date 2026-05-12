package co.edu.uniquindio.poo.PF.model.patterns.state;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

public class EstadoConfirmada implements EstadoCompraState {
    @Override public void pagar(Compra c)     { System.out.println("[State] Ya confirmada."); }
    @Override public void confirmar(Compra c) { System.out.println("[State] Ya confirmada."); }
    @Override public void cancelar(Compra c) {
        System.out.println("[State] Cancelando compra confirmada...");
        c.setEstado(new EstadoCancelada());
    }
    @Override public void reembolsar(Compra c) { System.out.println("[State] Debe cancelar primero."); }
    @Override public void reportarIncidencia(Compra c) { c.setEstado(new EstadoIncidencia()); }
    @Override public String getDescripcion()  { return "Compra confirmada y lista"; }
    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.CONFIRMADA; }
}
