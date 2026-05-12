package co.edu.uniquindio.poo.PF.model.patterns.adapter;

import co.edu.uniquindio.poo.PF.model.domain.ResultadoPago;
import co.edu.uniquindio.poo.PF.model.enums.EstadoPago;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;

public class AdapterEfectivo implements ProcesadorPago {
    private CajaEfectivo cajaEfectivo;

    public AdapterEfectivo() {
    }

    public AdapterEfectivo(CajaEfectivo cajaEfectivo) {
        this.cajaEfectivo = cajaEfectivo;
    }

    public CajaEfectivo getCajaEfectivo() { return cajaEfectivo; }
    public void setCajaEfectivo(CajaEfectivo cajaEfectivo) { this.cajaEfectivo = cajaEfectivo; }

    @Override
    public ResultadoPago procesar(double monto, TipoMetodoPago metodo) {
        String idTx = cajaEfectivo.registrarPago(monto);
        return new ResultadoPago(true, idTx, EstadoPago.APROBADO, "Pago efectivo registrado");
    }

    @Override
    public boolean reembolsar(String idTransaccion, double monto) {
        return cajaEfectivo.anularPago(idTransaccion);
    }

    @Override
    public EstadoPago verificarEstado(String idTransaccion) {
        return EstadoPago.APROBADO;
    }
}