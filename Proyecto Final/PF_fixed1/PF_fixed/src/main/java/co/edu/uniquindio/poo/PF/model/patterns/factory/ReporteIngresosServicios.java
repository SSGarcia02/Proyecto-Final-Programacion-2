package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoServicio;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ReporteIngresosServicios extends Reporte {
    private Map<TipoServicio, Double> ingresosPorServicio;

    public ReporteIngresosServicios() {
        super();
    }

    public ReporteIngresosServicios(Map<TipoServicio, Double> ingresos, String generadoPor) {
        super(UUID.randomUUID().toString(), LocalDateTime.now(),
                TipoReporte.INGRESOS_SERVICIOS, FormatoReporte.CSV, generadoPor);
        this.ingresosPorServicio = ingresos;
    }

    @Override public byte[] generar() {
        return ("Ingresos servicios: " + ingresosPorServicio).getBytes();
    }
    @Override public byte[] exportar() { return generar(); }
    @Override public String getDescripcion() {
        return "Ingresos por servicios adicionales";
    }

    public Map<TipoServicio, Double> getIngresosPorServicio() { return ingresosPorServicio; }
    public void setIngresosPorServicio(Map<TipoServicio, Double> ingresosPorServicio) { this.ingresosPorServicio = ingresosPorServicio; }
}
