package co.edu.uniquindio.poo.PF.model.patterns.state;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.domain.Entrada;
import co.edu.uniquindio.poo.PF.model.enums.EstadoAsiento;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

public class EstadoCreada implements EstadoCompraState {
    @Override public void pagar(Compra c) {
        System.out.println("[State] Procesando pago...");
        
        // Actualizar el estado de los asientos a VENDIDO
        if (c.getEntradas() != null) {
            for (Entrada entrada : c.getEntradas()) {
                if (entrada.getAsiento() != null) {
                    entrada.getAsiento().setEstado(EstadoAsiento.VENDIDO);
                }
            }
        }
        
        c.setEstado(new EstadoPagada());
    }
    @Override public void confirmar(Compra c) { System.out.println("[State] Debe pagar primero."); }
    @Override public void cancelar(Compra c) {
        System.out.println("[State] Compra cancelada antes de pago.");
        c.setEstado(new EstadoCancelada());
    }
    @Override public void reembolsar(Compra c)           { System.out.println("[State] No se puede reembolsar sin pago."); }
    @Override public void reportarIncidencia(Compra c)   { c.setEstado(new EstadoIncidencia()); }
    @Override public String getDescripcion()             { return "Compra creada, pendiente de pago"; }
    @Override public EstadoCompra getEstadoEnum()        { return EstadoCompra.CREADA; }
}
