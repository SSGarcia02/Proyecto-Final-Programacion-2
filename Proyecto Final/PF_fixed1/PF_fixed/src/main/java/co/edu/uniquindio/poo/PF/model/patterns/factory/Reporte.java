package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import java.time.LocalDateTime;
import java.util.Map;

public abstract class Reporte {
    public Reporte() {}

    protected String idReporte;
    protected LocalDateTime fechaGeneracion;
    protected TipoReporte tipo;
    protected FormatoReporte formato;
    protected String generadoPor;

    public Reporte(String idReporte, LocalDateTime fechaGeneracion,
                   TipoReporte tipo, FormatoReporte formato, String generadoPor) {
        this.idReporte = idReporte;
        this.fechaGeneracion = fechaGeneracion;
        this.tipo = tipo;
        this.formato = formato;
        this.generadoPor = generadoPor;
    }

    public abstract byte[] generar();
    public abstract byte[] exportar();
    public abstract String getDescripcion();

    public Map<String, String> getMetadatos() {
        return Map.of(
                "id",          idReporte != null ? idReporte : "",
                "tipo",        tipo != null ? tipo.name() : "",
                "formato",     formato != null ? formato.name() : "",
                "generadoPor", generadoPor != null ? generadoPor : "",
                "fecha",       fechaGeneracion != null ? fechaGeneracion.toString() : "null"
        );
    }

    public String getIdReporte() { return idReporte; }
    public void setIdReporte(String idReporte) { this.idReporte = idReporte; }
    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
    public TipoReporte getTipo() { return tipo; }
    public void setTipo(TipoReporte tipo) { this.tipo = tipo; }
    public FormatoReporte getFormato() { return formato; }
    public void setFormato(FormatoReporte formato) { this.formato = formato; }
    public String getGeneradoPor() { return generadoPor; }
    public void setGeneradoPor(String generadoPor) { this.generadoPor = generadoPor; }
}
