package co.edu.uniquindio.poo.PF.model.patterns.builder;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import co.edu.uniquindio.poo.PF.model.patterns.decorator.*;
import co.edu.uniquindio.poo.PF.model.patterns.state.EstadoCreada;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class CompraBuilderImpl implements CompraBuilder {

    private Compra compraEnConstruccion;
    private IComponenteEntrada componenteDecoradorActual;

    public CompraBuilderImpl() { reset(); }

    public void reset() {
        compraEnConstruccion = new Compra();
        compraEnConstruccion.setIdCompra(UUID.randomUUID().toString());
        compraEnConstruccion.setFechaCreacion(LocalDateTime.now());
        compraEnConstruccion.setEstado(new EstadoCreada());
        compraEnConstruccion.setEntradas(new ArrayList<>());
        compraEnConstruccion.setServiciosAdicionales(new ArrayList<>());
        componenteDecoradorActual = null;
    }

    @Override
    public CompraBuilder setUsuario(Usuario u) {
        compraEnConstruccion.setUsuario(u); return this;
    }

    @Override
    public CompraBuilder setEvento(Evento e) {
        compraEnConstruccion.setEvento(e); return this;
    }

    @Override
    public CompraBuilder agregarEntrada(Zona zona, Asiento asiento) {
        Entrada entrada = new Entrada(
                compraEnConstruccion.getEntradas().size() + 1,
                zona, asiento, zona.getPrecioBase(),
                EstadoEntrada.ACTIVA, LocalDateTime.now(), null);
        compraEnConstruccion.getEntradas().add(entrada);
        if (asiento != null) asiento.reservar();

        if (componenteDecoradorActual == null) {
            componenteDecoradorActual = new EntradaSimple(zona.getPrecioBase(), entrada.getDescripcion());
        }
        return this;
    }

    @Override
    public CompraBuilder agregarServicio(TipoServicio tipoServicio) {
        ServicioAdicional servicio = crearServicio(tipoServicio, componenteDecoradorActual);
        compraEnConstruccion.getServiciosAdicionales().add(servicio);
        componenteDecoradorActual = servicio; 
        return this;
    }

    private ServicioAdicional crearServicio(TipoServicio tipo, IComponenteEntrada base) {
        return switch (tipo) {
            case VIP                 -> new ServicioVIP(base, "VIP", "Acceso VIP", 50000, true, true);
            case SEGURO_CANCELACION  -> new SeguroCancelacion(base, "Seguro", "Seguro cancelación", 20000, 500000, "Sin penalidad");
            case MERCHANDISING       -> new Merchandising(base, "Merch", "Producto oficial", 30000, "Camiseta", "M");
            case PARQUEADERO         -> new Parqueadero(base, "Parking", "Parqueadero", 15000, "ABC123", "P1");
            case ACCESO_PREFERENCIAL -> new AccesoPreferencial(base, "Preferencial", "Entrada preferencial", 25000, 1, "Puerta A");
        };
    }

    @Override
    public CompraBuilder setPago(Pago p) {
        compraEnConstruccion.setPago(p); return this;
    }

    @Override
    public Compra build() {
        compraEnConstruccion.calcularTotal();
        Compra resultado = compraEnConstruccion;
        reset();
        return resultado;
    }
}
