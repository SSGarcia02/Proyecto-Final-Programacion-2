package co.edu.uniquindio.poo.PF.model.patterns.state;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

public class EstadoReembolsada implements EstadoCompraState {
    private void operacionNoPermitida(String op) {
        System.out.println("[State] Estado REEMBOLSADA — operación '" + op + "' no permitida.");
    }
    @Override public void pagar(Compra c)              { operacionNoPermitida("pagar"); }
    @Override public void confirmar(Compra c)          { operacionNoPermitida("confirmar"); }
    @Override public void cancelar(Compra c)           { operacionNoPermitida("cancelar"); }
    @Override public void reembolsar(Compra c)         { operacionNoPermitida("reembolsar"); }
    @Override public void reportarIncidencia(Compra c) { operacionNoPermitida("reportarIncidencia"); }
    @Override public String getDescripcion()           { return "Reembolso procesado — estado final"; }
    @Override public EstadoCompra getEstadoEnum()      { return EstadoCompra.REEMBOLSADA; }
}