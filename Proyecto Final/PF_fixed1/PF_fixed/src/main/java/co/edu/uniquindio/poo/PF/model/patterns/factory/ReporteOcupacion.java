package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoZona;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ReporteOcupacion extends Reporte {
    private String idEvento;
    private Map<TipoZona, Double> ocupacionPorZona;

    public ReporteOcupacion() {
        super();
    }

    public ReporteOcupacion(String idEvento, Map<TipoZona, Double> ocupacion, String generadoPor) {
        super(UUID.randomUUID().toString(), LocalDateTime.now(),
                TipoReporte.OCUPACION_ZONA, FormatoReporte.CSV, generadoPor);
        this.idEvento = idEvento;
        this.ocupacionPorZona = ocupacion;
    }

    @Override public byte[] generar() {
        return ("Ocupacion evento " + idEvento + ": " + ocupacionPorZona).getBytes();
    }
    @Override public byte[] exportar() { return generar(); }
    @Override public String getDescripcion() {
        return "Ocupación por zona del evento " + idEvento;
    }

    public String getIdEvento() { return idEvento; }
    public void setIdEvento(String idEvento) { this.idEvento = idEvento; }
    public Map<TipoZona, Double> getOcupacionPorZona() { return ocupacionPorZona; }
    public void setOcupacionPorZona(Map<TipoZona, Double> ocupacionPorZona) { this.ocupacionPorZona = ocupacionPorZona; }
}
