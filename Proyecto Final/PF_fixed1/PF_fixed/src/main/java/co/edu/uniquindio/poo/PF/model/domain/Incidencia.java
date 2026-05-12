package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.TipoIncidencia;
import java.time.LocalDateTime;

public class Incidencia {
    public Incidencia() {}

    private String idIncidencia;
    private TipoIncidencia tipo;
    private String descripcion;
    private LocalDateTime fecha;
    private String entidadAfectada;
    private String idEntidadAfectada;
    private boolean resuelta = false;
    private String respuesta;
    private LocalDateTime fechaResolucion;
    private String reportadoPor;

    public Incidencia(String idIncidencia, TipoIncidencia tipo, String descripcion,
                      LocalDateTime fecha, String entidadAfectada, String idEntidadAfectada) {
        this.idIncidencia = idIncidencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.entidadAfectada = entidadAfectada;
        this.idEntidadAfectada = idEntidadAfectada;
    }

    public void resolver(String respuesta) {
        this.resuelta = true;
        this.respuesta = respuesta;
        this.fechaResolucion = LocalDateTime.now();
    }

    @Override public String toString() {
        return tipo + " - " + entidadAfectada + ": " + descripcion;
    }

    public String getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(String idIncidencia) { this.idIncidencia = idIncidencia; }
    public TipoIncidencia getTipo() { return tipo; }
    public void setTipo(TipoIncidencia tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getEntidadAfectada() { return entidadAfectada; }
    public void setEntidadAfectada(String entidadAfectada) { this.entidadAfectada = entidadAfectada; }
    public String getIdEntidadAfectada() { return idEntidadAfectada; }
    public void setIdEntidadAfectada(String idEntidadAfectada) { this.idEntidadAfectada = idEntidadAfectada; }
    public boolean isResuelta() { return resuelta; }
    public void setResuelta(boolean resuelta) { this.resuelta = resuelta; }
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    public String getRespuestaAdmin() { return respuesta; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }
    public String getReportadoPor() { return reportadoPor; }
    public void setReportadoPor(String reportadoPor) { this.reportadoPor = reportadoPor; }
}
