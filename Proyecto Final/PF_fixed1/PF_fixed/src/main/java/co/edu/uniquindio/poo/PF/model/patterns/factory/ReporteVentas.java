package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import java.time.LocalDateTime;
import java.util.UUID;

public class ReporteVentas extends Reporte {
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFin;
    private double totalVentas;

    public ReporteVentas() {
        super();
    }

    public ReporteVentas(LocalDateTime inicio, LocalDateTime fin, double total, String generadoPor) {
        super(UUID.randomUUID().toString(), LocalDateTime.now(),
                TipoReporte.VENTAS_PERIODO, FormatoReporte.PDF, generadoPor);
        this.periodoInicio = inicio;
        this.periodoFin = fin;
        this.totalVentas = total;
    }

    public LocalDateTime getPeriodoInicio() { return periodoInicio; }
    public void setPeriodoInicio(LocalDateTime periodoInicio) { this.periodoInicio = periodoInicio; }

    public LocalDateTime getPeriodoFin() { return periodoFin; }
    public void setPeriodoFin(LocalDateTime periodoFin) { this.periodoFin = periodoFin; }

    public double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(double totalVentas) { this.totalVentas = totalVentas; }

    @Override public byte[] generar() {
        String contenido = "Reporte Ventas: " + totalVentas;
        return contenido.getBytes();
    }
    @Override public byte[] exportar() { return generar(); }
    @Override public String getDescripcion() {
        return "Ventas del " + periodoInicio + " al " + periodoFin + " = $" + totalVentas;
    }
}
