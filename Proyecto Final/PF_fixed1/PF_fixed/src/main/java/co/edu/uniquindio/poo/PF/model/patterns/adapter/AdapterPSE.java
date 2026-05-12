package co.edu.uniquindio.poo.PF.model.patterns.adapter;

import co.edu.uniquindio.poo.PF.model.domain.ResultadoPago;
import co.edu.uniquindio.poo.PF.model.enums.EstadoPago;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;

public class AdapterPSE implements ProcesadorPago {
    private PasarelaPSE pasarelaPSE;

    public AdapterPSE() {
    }

    public AdapterPSE(PasarelaPSE pasarelaPSE) {
        this.pasarelaPSE = pasarelaPSE;
    }

    public PasarelaPSE getPasarelaPSE() { return pasarelaPSE; }
    public void setPasarelaPSE(PasarelaPSE pasarelaPSE) { this.pasarelaPSE = pasarelaPSE; }

    @Override
    public ResultadoPago procesar(double monto, TipoMetodoPago metodo) {
        String idTx = pasarelaPSE.iniciarTransaccion(monto);
        return new ResultadoPago(true, idTx, EstadoPago.APROBADO, "Pago PSE exitoso");
    }

    @Override
    public boolean reembolsar(String idTransaccion, double monto) {
        return pasarelaPSE.solicitarDevolucion(idTransaccion);
    }

    @Override
    public EstadoPago verificarEstado(String idTransaccion) {
        String estado = pasarelaPSE.consultarEstado(idTransaccion);
        return "APROBADO".equals(estado) ? EstadoPago.APROBADO : EstadoPago.RECHAZADO;
    }
}