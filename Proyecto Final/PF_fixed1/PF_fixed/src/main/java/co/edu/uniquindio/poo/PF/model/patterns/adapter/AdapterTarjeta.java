package co.edu.uniquindio.poo.PF.model.patterns.adapter;

import co.edu.uniquindio.poo.PF.model.domain.ResultadoPago;
import co.edu.uniquindio.poo.PF.model.enums.EstadoPago;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;

public class AdapterTarjeta implements ProcesadorPago {
    private PasarelaTarjeta pasarelaTarjeta;

    public AdapterTarjeta() {
    }

    public AdapterTarjeta(PasarelaTarjeta pasarelaTarjeta) {
        this.pasarelaTarjeta = pasarelaTarjeta;
    }

    public PasarelaTarjeta getPasarelaTarjeta() { return pasarelaTarjeta; }
    public void setPasarelaTarjeta(PasarelaTarjeta pasarelaTarjeta) { this.pasarelaTarjeta = pasarelaTarjeta; }

    @Override
    public ResultadoPago procesar(double monto, TipoMetodoPago metodo) {
        String idTx = pasarelaTarjeta.cobrar(monto, "****1234");
        return new ResultadoPago(true, idTx, EstadoPago.APROBADO, "Pago tarjeta exitoso");
    }

    @Override
    public boolean reembolsar(String idTransaccion, double monto) {
        return pasarelaTarjeta.reversar(idTransaccion);
    }

    @Override
    public EstadoPago verificarEstado(String idTransaccion) {
        String estado = pasarelaTarjeta.consultarTrans(idTransaccion);
        return "APROBADO".equals(estado) ? EstadoPago.APROBADO : EstadoPago.RECHAZADO;
    }
}
