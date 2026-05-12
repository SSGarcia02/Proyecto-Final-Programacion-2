package co.edu.uniquindio.poo.PF.model.patterns.state;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

public class EstadoCancelada implements EstadoCompraState {
    @Override public void pagar(Compra c)     { System.out.println("[State] Compra cancelada, no se puede pagar."); }
    @Override public void confirmar(Compra c) { System.out.println("[State] Cancelada."); }
    @Override public void cancelar(Compra c)  { System.out.println("[State] Ya cancelada."); }
    @Override public void reembolsar(Compra c) {
        System.out.println("[State] Procesando reembolso...");
        c.setEstado(new EstadoReembolsada());
    }
    @Override public void reportarIncidencia(Compra c) { c.setEstado(new EstadoIncidencia()); }
    @Override public String getDescripcion()  { return "Compra cancelada"; }
    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.CANCELADA; }
}
