package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.EstadoPago;

public class ResultadoPago {
    public ResultadoPago() {}

    public ResultadoPago(boolean exitoso, String codigoTransaccion, EstadoPago estado, String mensaje) {
        this.exitoso = exitoso;
        this.codigoTransaccion = codigoTransaccion;
        this.estado = estado;
        this.mensaje = mensaje;
    }

    private boolean exitoso;
    private String codigoTransaccion;
    private EstadoPago estado;
    private String mensaje;

    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
    public String getCodigoTransaccion() { return codigoTransaccion; }
    public void setCodigoTransaccion(String codigoTransaccion) { this.codigoTransaccion = codigoTransaccion; }
    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
