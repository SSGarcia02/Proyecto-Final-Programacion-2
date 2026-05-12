package co.edu.uniquindio.poo.PF.model.patterns.adapter;

import co.edu.uniquindio.poo.PF.model.domain.ResultadoPago;
import co.edu.uniquindio.poo.PF.model.enums.EstadoPago;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;

public interface ProcesadorPago {
    ResultadoPago procesar(double monto, TipoMetodoPago metodo);
    boolean reembolsar(String idTransaccion, double monto);
    EstadoPago verificarEstado(String idTransaccion);
}
