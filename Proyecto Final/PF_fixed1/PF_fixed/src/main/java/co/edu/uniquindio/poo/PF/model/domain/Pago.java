package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.EstadoPago;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;
import java.time.LocalDateTime;

public class Pago {
    public Pago() {}

    private String idPago;
    private double monto;
    private TipoMetodoPago metodoPago;
    private EstadoPago estado;
    private String codigoTransaccion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaProcesamiento;

    public Pago(String idPago, double monto, TipoMetodoPago metodoPago,
                EstadoPago estado, String codigoTransaccion,
                LocalDateTime fechaCreacion, LocalDateTime fechaProcesamiento) {
        this.idPago = idPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.codigoTransaccion = codigoTransaccion;
        this.fechaCreacion = fechaCreacion;
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public void procesar() {
        this.estado = EstadoPago.APROBADO;
        this.fechaProcesamiento = LocalDateTime.now();
    }

    public void rechazar() { this.estado = EstadoPago.RECHAZADO; }

    @Override public String toString() {
        return "Pago{" + metodoPago + " $" + monto + " [" + estado + "]}";
    }

    public String getIdPago() { return idPago; }
    public void setIdPago(String idPago) { this.idPago = idPago; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public TipoMetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(TipoMetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }
    public String getCodigoTransaccion() { return codigoTransaccion; }
    public void setCodigoTransaccion(String codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) { this.fechaProcesamiento = fechaProcesamiento; }
}
