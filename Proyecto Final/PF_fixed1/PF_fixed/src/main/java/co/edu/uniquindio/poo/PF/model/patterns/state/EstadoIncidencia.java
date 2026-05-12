package co.edu.uniquindio.poo.PF.model.patterns.state;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

public class EstadoIncidencia implements EstadoCompraState {
    @Override public void pagar(Compra c)     { System.out.println("[State] En incidencia, sin acciones."); }
    @Override public void confirmar(Compra c) { System.out.println("[State] En incidencia."); }
    @Override public void cancelar(Compra c)  { System.out.println("[State] En incidencia."); }
    @Override public void reembolsar(Compra c) {
        System.out.println("[State] Reembolsando por incidencia...");
        c.setEstado(new EstadoReembolsada());
    }
    @Override public void reportarIncidencia(Compra c) { System.out.println("[State] Ya en incidencia."); }
    @Override public String getDescripcion()  { return "Compra en estado de incidencia"; }
    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.INCIDENCIA; }
}