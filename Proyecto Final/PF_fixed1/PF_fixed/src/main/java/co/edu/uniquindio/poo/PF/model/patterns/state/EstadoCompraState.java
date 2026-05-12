package co.edu.uniquindio.poo.PF.model.patterns.state;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

public interface EstadoCompraState {
    void pagar(Compra c);
    void confirmar(Compra c);
    void cancelar(Compra c);
    void reembolsar(Compra c);
    void reportarIncidencia(Compra c);
    String getDescripcion();
    EstadoCompra getEstadoEnum();
}
