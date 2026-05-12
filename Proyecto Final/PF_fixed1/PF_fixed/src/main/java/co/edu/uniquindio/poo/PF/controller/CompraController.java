package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import co.edu.uniquindio.poo.PF.model.patterns.decorator.*;
import co.edu.uniquindio.poo.PF.model.patterns.facade.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CompraController {

    private final List<Compra> compras = new ArrayList<>();
    private ServicioCompra servicioCompra;

    public CompraController() {}
    public CompraController(ServicioCompra servicioCompra) {
        this.servicioCompra = servicioCompra;
    }

    public List<Compra> getCompras()             { return compras; }
    public ServicioCompra getServicioCompra()    { return servicioCompra; }
    public void setServicioCompra(ServicioCompra s) { this.servicioCompra = s; }

    public Compra crearCompra(CompraDTO dto) {
        Compra c = servicioCompra.realizarCompra(dto);
        compras.add(c);
        return c;
    }

    public Compra crearCompraMultiAsiento(CompraDTO dto, List<Asiento> asientos) {
        Compra c = servicioCompra.realizarCompraMultiAsiento(dto, asientos);
        compras.add(c);
        return c;
    }

    public Compra reservarMultiAsiento(CompraDTO dto, List<Asiento> asientos) {
        Compra c = servicioCompra.reservarMultiAsiento(dto, asientos);
        compras.add(c);
        return c;
    }

    public boolean pagarReserva(String idCompra, TipoMetodoPago metodo) {
        Compra c = buscarPorId(idCompra);
        if (c == null || c.getEstado() != EstadoCompra.CREADA) return false;
        
        servicioCompra.pagarCompraPendiente(c, metodo);
        return true;
    }

    public boolean cancelar(String idCompra) {
        Compra c = buscarPorId(idCompra);
        if (c == null) return false;
        c.cancelar();
        return true;
    }

    public boolean pagar(String idCompra) {
        Compra c = buscarPorId(idCompra);
        if (c == null) return false;
        c.pagar();
        return true;
    }

    public List<Compra> listarPorUsuario(String idUsuario) {
        return compras.stream()
                .filter(c -> c.getUsuario() != null
                        && c.getUsuario().getIdUsuario().equals(idUsuario))
                .toList();
    }

    public List<Compra> filtrarComprasUsuario(String idUsuario, LocalDate desde, LocalDate hasta, EstadoCompra estado, String nombreEvento) {
        String evLower = (nombreEvento == null) ? "" : nombreEvento.trim().toLowerCase();
        return listarPorUsuario(idUsuario).stream()
                .filter(c -> estado == null || estado.equals(c.getEstado()))
                .filter(c -> desde == null || !c.getFechaCreacion().toLocalDate().isBefore(desde))
                .filter(c -> hasta == null || !c.getFechaCreacion().toLocalDate().isAfter(hasta))
                .filter(c -> evLower.isEmpty() || (c.getEvento() != null && c.getEvento().getNombre().toLowerCase().contains(evLower)))
                .collect(Collectors.toList());
    }

    public List<Compra> listarPorEstado(EstadoCompra estado) {
        return compras.stream()
                .filter(c -> estado.equals(c.getEstado()))
                .toList();
    }

    public Compra buscarPorId(String id) {
        return compras.stream()
                .filter(c -> c.getIdCompra().equals(id))
                .findFirst().orElse(null);
    }

    public String generarComprobante(String idCompra) {
        Compra c = buscarPorId(idCompra);
        return c != null ? c.generarComprobante() : "Compra no encontrada";
    }

    public double calcularPrecioDecorado(double precioBase, List<TipoServicio> servicios) {
        IComponenteEntrada componente = new co.edu.uniquindio.poo.PF.model.patterns.decorator.EntradaSimple(precioBase);
        for (TipoServicio tipo : servicios) {
            componente = aplicarDecorador(componente, tipo);
        }
        return componente.getPrecio();
    }

    public String obtenerDescripcionDecorada(double precioBase, List<TipoServicio> servicios) {
        IComponenteEntrada componente = new co.edu.uniquindio.poo.PF.model.patterns.decorator.EntradaSimple(precioBase);
        for (TipoServicio tipo : servicios) {
            componente = aplicarDecorador(componente, tipo);
        }
        return componente.getDescripcion();
    }

    private IComponenteEntrada aplicarDecorador(IComponenteEntrada base, TipoServicio tipo) {
        return switch (tipo) {
            case VIP                 -> new ServicioVIP(base, "VIP", "Acceso VIP", 50000, true, true);
            case SEGURO_CANCELACION  -> new SeguroCancelacion(base, "Seguro", "Seguro cancelación", 20000, 500000, "Sin penalidad");
            case MERCHANDISING       -> new Merchandising(base, "Merch", "Producto oficial", 30000, "Camiseta", "M");
            case PARQUEADERO         -> new Parqueadero(base, "Parking", "Parqueadero", 15000, "ABC123", "P1");
            case ACCESO_PREFERENCIAL -> new AccesoPreferencial(base, "Preferencial", "Entrada preferencial", 25000, 1, "Puerta A");
        };
    }
}
