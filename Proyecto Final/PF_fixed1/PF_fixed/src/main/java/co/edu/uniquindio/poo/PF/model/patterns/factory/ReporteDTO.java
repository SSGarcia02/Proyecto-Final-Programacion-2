package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import java.time.LocalDateTime;

public class ReporteDTO {
    public ReporteDTO() {}

    public ReporteDTO(TipoReporte tipo, FormatoReporte formato, LocalDateTime inicio, LocalDateTime fin, String generadoPor) {
        this.tipo = tipo;
        this.formato = formato;
        this.inicio = inicio;
        this.fin = fin;
        this.generadoPor = generadoPor;
    }

    private TipoReporte tipo;
    private FormatoReporte formato;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private String generadoPor;

    public TipoReporte getTipo() { return tipo; }
    public void setTipo(TipoReporte tipo) { this.tipo = tipo; }
    public FormatoReporte getFormato() { return formato; }
    public void setFormato(FormatoReporte formato) { this.formato = formato; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public String getGeneradoPor() { return generadoPor; }
    public void setGeneradoPor(String generadoPor) { this.generadoPor = generadoPor; }
}
