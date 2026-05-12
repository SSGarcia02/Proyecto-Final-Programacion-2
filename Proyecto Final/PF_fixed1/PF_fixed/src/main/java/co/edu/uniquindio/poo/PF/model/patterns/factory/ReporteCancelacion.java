package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoIncidencia;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ReporteCancelacion extends Reporte {
    private double tasaCancelacion;
    private Map<TipoIncidencia, Integer> motivos;

    public ReporteCancelacion() {
        super();
    }

    public ReporteCancelacion(double tasa, Map<TipoIncidencia, Integer> motivos, String generadoPor) {
        super(UUID.randomUUID().toString(), LocalDateTime.now(),
                TipoReporte.TASA_CANCELACION, FormatoReporte.PDF, generadoPor);
        this.tasaCancelacion = tasa;
        this.motivos = motivos;
    }

    public double getTasaCancelacion() { return tasaCancelacion; }
    public void setTasaCancelacion(double tasaCancelacion) { this.tasaCancelacion = tasaCancelacion; }

    public Map<TipoIncidencia, Integer> getMotivos() { return motivos; }
    public void setMotivos(Map<TipoIncidencia, Integer> motivos) { this.motivos = motivos; }

    @Override public byte[] generar() {
        return ("Tasa cancelacion: " + tasaCancelacion + "% - " + motivos).getBytes();
    }
    @Override public byte[] exportar() { return generar(); }
    @Override public String getDescripcion() {
        return "Tasa de cancelación: " + tasaCancelacion + "%";
    }
}
