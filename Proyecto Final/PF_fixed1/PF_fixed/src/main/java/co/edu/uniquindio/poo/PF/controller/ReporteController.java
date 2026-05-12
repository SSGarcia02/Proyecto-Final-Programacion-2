package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.patterns.factory.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import java.time.LocalDateTime;

public class ReporteController {

    private final ReporteFactory factory = new ReporteFactoryImpl();

    public ReporteController() {}

    public ReporteFactory getFactory() { return factory; }

    public byte[] generarReporte(TipoReporte tipo, FormatoReporte formato,
                                 LocalDateTime inicio, LocalDateTime fin,
                                 String generadoPor) {
        ReporteDTO dto = new ReporteDTO(tipo, formato, inicio, fin, generadoPor);
        byte[] data = factory.generarReporte(dto);
        System.out.println("[Reporte] Generado: " + tipo + " formato: " + formato);
        return data;
    }

    public Reporte crearReporte(TipoReporte tipo, FormatoReporte formato) {
        return factory.crearReporte(tipo, formato);
    }
}
