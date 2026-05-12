package co.edu.uniquindio.poo.PF.model.patterns.builder;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.TipoServicio;

public interface CompraBuilder {
    CompraBuilder setUsuario(Usuario u);
    CompraBuilder setEvento(Evento e);
    CompraBuilder agregarEntrada(Zona zona, Asiento asiento);
    CompraBuilder agregarServicio(TipoServicio tipo);
    CompraBuilder setPago(Pago p);
    Compra build();
}
