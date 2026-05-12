package co.edu.uniquindio.poo.PF.model.patterns.factory;

public interface ReporteFactory {
    Reporte crearReporte(co.edu.uniquindio.poo.PF.model.enums.TipoReporte tipo,
                         co.edu.uniquindio.poo.PF.model.enums.FormatoReporte formato);
    byte[] generarReporte(ReporteDTO dto);
}
