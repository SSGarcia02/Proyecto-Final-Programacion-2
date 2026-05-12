package co.edu.uniquindio.poo.PF.model.patterns.facade;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.EstadoPago;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;
import co.edu.uniquindio.poo.PF.model.patterns.builder.*;
import co.edu.uniquindio.poo.PF.model.patterns.singleton.*;
import java.time.LocalDateTime;
import java.util.*;

public class ServicioCompra {
    public ServicioCompra() {}

    private GestorSesion gestorSesion  = GestorSesion.getInstance();
    private CatalogoEventos catalogo   = CatalogoEventos.getInstance();
    private CompraBuilder builder;
    private CompraDirector director;
    private NotificadorUsuario notificador;
    private GestorAsientos gestorAsientos;
    private GestorEntradas gestorEntradas;
    private GestorPagos gestorPagos;

    public ServicioCompra(CompraBuilder builder, NotificadorUsuario notificador,
                          GestorAsientos gestorAsientos, GestorEntradas gestorEntradas,
                          GestorPagos gestorPagos) {
        this.builder       = builder;
        this.director      = new CompraDirector(builder);
        this.notificador   = notificador;
        this.gestorAsientos = gestorAsientos;
        this.gestorEntradas = gestorEntradas;
        this.gestorPagos    = gestorPagos;
    }

    private void sincronizarZonas(Evento e) {
        if (e != null && e.getZonas() != null) {
            e.getZonas().forEach(gestorAsientos::registrarZona);
        }
    }

    public Compra reservarMultiAsiento(CompraDTO dto, List<Asiento> asientos) {
        verificarAutenticacion();
        sincronizarZonas(dto.getEvento());
        
        for (Asiento a : asientos) {
            gestorAsientos.reservar(a);
        }

        Compra compra = director.construirCompraMultiAsiento(
                dto.getUsuario(), dto.getEvento(), asientos, Collections.emptyList());
        
        notificador.notificarCompra(dto.getUsuario(), compra);
        return compra;
    }

    public Compra realizarCompra(CompraDTO dto) {
        verificarAutenticacion();
        sincronizarZonas(dto.getEvento());

        if (dto.getAsiento() != null) {
            gestorAsientos.reservar(dto.getAsiento());
        }

        List<co.edu.uniquindio.poo.PF.model.enums.TipoServicio> servicios =
                dto.getServicios() != null ? dto.getServicios() : Collections.emptyList();

        Compra compra = director.construirCompraConAsiento(
                dto.getUsuario(), dto.getEvento(), dto.getZona(), dto.getAsiento(), servicios);

        procesarPagoYFinalizar(compra, dto.getUsuario(), dto.getMetodoPago());
        return compra;
    }

    public Compra realizarCompraMultiAsiento(CompraDTO dto, List<Asiento> asientos) {
        verificarAutenticacion();
        sincronizarZonas(dto.getEvento());

        for (Asiento a : asientos) {
            gestorAsientos.reservar(a);
        }

        List<co.edu.uniquindio.poo.PF.model.enums.TipoServicio> servicios =
                dto.getServicios() != null ? dto.getServicios() : Collections.emptyList();

        Compra compra = director.construirCompraMultiAsiento(
                dto.getUsuario(), dto.getEvento(), asientos, servicios);

        procesarPagoYFinalizar(compra, dto.getUsuario(), dto.getMetodoPago());
        return compra;
    }

    public void pagarCompraPendiente(Compra compra, TipoMetodoPago metodo) {
        if (compra == null) return;
        procesarPagoYFinalizar(compra, compra.getUsuario(), metodo);
    }

    private void verificarAutenticacion() {
        if (!gestorSesion.estaAutenticado())
            throw new IllegalStateException("Usuario no autenticado");
    }

    private void procesarPagoYFinalizar(Compra compra, Usuario usuario, TipoMetodoPago metodo) {
        Pago pago = new Pago(java.util.UUID.randomUUID().toString(), compra.getTotal(),
                metodo, EstadoPago.PENDIENTE, null, LocalDateTime.now(), null);
        
        gestorPagos.procesarPago(pago);
        compra.setPago(pago);
        compra.pagar();
        
        notificador.notificarCompra(usuario, compra);
    }

    public Map<Zona, Integer> consultarDisponibilidad(String idEvento) {
        Map<Zona, Integer> mapa = new HashMap<>();
        catalogo.getEventos().stream()
                .filter(e -> e.getIdEvento().equals(idEvento))
                .findFirst()
                .ifPresent(e -> {
                    sincronizarZonas(e);
                    e.getZonas().forEach(z ->
                        mapa.put(z, gestorAsientos.verificarDisponibilidad(z.getIdZona())));
                });
        return mapa;
    }

    public GestorSesion getGestorSesion() { return gestorSesion; }
    public void setGestorSesion(GestorSesion gestorSesion) { this.gestorSesion = gestorSesion; }
    public CatalogoEventos getCatalogo() { return catalogo; }
    public void setCatalogo(CatalogoEventos catalogo) { this.catalogo = catalogo; }
    public CompraBuilder getBuilder() { return builder; }
    public void setBuilder(CompraBuilder builder) { this.builder = builder; }
    public CompraDirector getDirector() { return director; }
    public void setDirector(CompraDirector director) { this.director = director; }
    public NotificadorUsuario getNotificador() { return notificador; }
    public void setNotificador(NotificadorUsuario notificador) { this.notificador = notificador; }
    public GestorAsientos getGestorAsientos() { return gestorAsientos; }
    public void setGestorAsientos(GestorAsientos gestorAsientos) { this.gestorAsientos = gestorAsientos; }
    public GestorEntradas getGestorEntradas() { return gestorEntradas; }
    public void setGestorEntradas(GestorEntradas gestorEntradas) { this.gestorEntradas = gestorEntradas; }
    public GestorPagos getGestorPagos() { return gestorPagos; }
    public void setGestorPagos(GestorPagos gestorPagos) { this.gestorPagos = gestorPagos; }
}
