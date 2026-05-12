package co.edu.uniquindio.poo.PF.model.patterns.builder;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.TipoServicio;
import co.edu.uniquindio.poo.PF.model.enums.TipoZona;
import java.util.List;

public class CompraDirector {

    private CompraBuilder builder;

    public CompraDirector(CompraBuilder builder) { this.builder = builder; }

    public Compra construirCompraBasica(Usuario u, Evento e, Zona zona) {
        return builder.setUsuario(u).setEvento(e).agregarEntrada(zona, null).build();
    }

    public Compra construirCompraVIP(Usuario u, Evento e) {
        Zona zonaVIP = e.getZonas().stream()
                .filter(z -> z.getTipoZona() == TipoZona.VIP)
                .findFirst().orElse(e.getZonas().get(0));
        return builder.setUsuario(u).setEvento(e)
                .agregarEntrada(zonaVIP, null)
                .agregarServicio(TipoServicio.VIP)
                .agregarServicio(TipoServicio.ACCESO_PREFERENCIAL)
                .build();
    }

    public Compra construirCompraConServicios(Usuario u, Evento e, List<TipoServicio> servicios) {
        Zona zona = e.getZonas().get(0);
        builder.setUsuario(u).setEvento(e).agregarEntrada(zona, null);
        servicios.forEach(builder::agregarServicio);
        return builder.build();
    }

    public Compra construirCompraConAsiento(Usuario u, Evento e, Zona zona,
                                            Asiento asiento, List<TipoServicio> servicios) {
        builder.setUsuario(u).setEvento(e).agregarEntrada(zona, asiento);
        servicios.forEach(builder::agregarServicio);
        return builder.build();
    }

    public Compra construirCompraMultiAsiento(Usuario u, Evento e,
                                              List<Asiento> asientos,
                                              List<TipoServicio> servicios) {
        builder.setUsuario(u).setEvento(e);
        for (Asiento asiento : asientos) {
            Zona zona = asiento.getZona() != null ? asiento.getZona() : e.getZonas().get(0);
            builder.agregarEntrada(zona, asiento);
        }
        servicios.forEach(builder::agregarServicio);
        return builder.build();
    }

    public void setBuilder(CompraBuilder builder) { this.builder = builder; }
}
