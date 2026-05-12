package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import java.time.LocalDateTime;
import java.util.Collections;

public class ReporteFactoryImpl implements ReporteFactory {

    @Override
    public Reporte crearReporte(TipoReporte tipo, FormatoReporte formato) {
        return switch (tipo) {
            case VENTAS_PERIODO    -> new ReporteVentas(LocalDateTime.now().minusDays(30), LocalDateTime.now(), 0, "sistema");
            case OCUPACION_ZONA    -> new ReporteOcupacion("", Collections.emptyMap(), "sistema");
            case TASA_CANCELACION  -> new ReporteCancelacion(0, Collections.emptyMap(), "sistema");
            case TOP_EVENTOS       -> new ReporteTopEventos(5, Collections.emptyList(), "sistema");
            case INGRESOS_SERVICIOS-> new ReporteIngresosServicios(Collections.emptyMap(), "sistema");
        };
    }

    @Override
    public byte[] generarReporte(ReporteDTO dto) {
        Reporte r = crearReporte(dto.getTipo(), dto.getFormato());
        r.setGeneradoPor(dto.getGeneradoPor());
        r.setFechaGeneracion(LocalDateTime.now());
        return r.generar();
    }
}