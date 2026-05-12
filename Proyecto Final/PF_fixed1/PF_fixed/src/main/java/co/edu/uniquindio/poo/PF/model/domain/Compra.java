package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;
import co.edu.uniquindio.poo.PF.model.patterns.decorator.IComponenteEntrada;
import co.edu.uniquindio.poo.PF.model.patterns.decorator.ServicioAdicional;
import co.edu.uniquindio.poo.PF.model.patterns.observer.Observador;
import co.edu.uniquindio.poo.PF.model.patterns.observer.Sujeto;
import co.edu.uniquindio.poo.PF.model.patterns.state.EstadoCompraState;
import co.edu.uniquindio.poo.PF.model.patterns.state.EstadoCreada;
import java.time.LocalDateTime;
import java.util.*;

public class Compra implements Sujeto {
    public Compra() {}

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private LocalDateTime fechaCreacion;
    private double total;
    private EstadoCompra estado;
    private List<Entrada> entradas = new ArrayList<>();
    private List<ServicioAdicional> serviciosAdicionales = new ArrayList<>();
    private Pago pago;

    private EstadoCompraState estadoActual = new EstadoCreada();
    private List<Observador> observadores = new ArrayList<>();

    public Compra(String idCompra, Usuario usuario, Evento evento,
                  LocalDateTime fechaCreacion, double total, EstadoCompra estado,
                  List<Entrada> entradas, List<ServicioAdicional> servicios, Pago pago) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.fechaCreacion = fechaCreacion;
        this.total = total;
        this.estado = estado;
        this.entradas = entradas != null ? entradas : new ArrayList<>();
        this.serviciosAdicionales = servicios != null ? servicios : new ArrayList<>();
        this.pago = pago;
        this.estadoActual = new EstadoCreada();
    }

    public void pagar()              { estadoActual.pagar(this); }
    public void confirmar()          { estadoActual.confirmar(this); }
    public void cancelar()           { estadoActual.cancelar(this); }
    public void reembolsar()         { estadoActual.reembolsar(this); }
    public void reportarIncidencia() { estadoActual.reportarIncidencia(this); }
    public String getDescripcionEstado() { return estadoActual.getDescripcion(); }

    public void setEstado(EstadoCompraState nuevo) {
        this.estadoActual = nuevo;
        this.estado = nuevo.getEstadoEnum();
        notificarObservadores("CAMBIO_ESTADO", Map.of("estado", nuevo.getEstadoEnum()));
    }

    public double calcularTotal() {
        double baseEntradas = entradas == null ? 0 :
                entradas.stream().mapToDouble(IComponenteEntrada::getPrecio).sum();
        double extraServicios = serviciosAdicionales == null ? 0 :
                serviciosAdicionales.stream().mapToDouble(ServicioAdicional::getPrecioExtra).sum();
        this.total = baseEntradas + extraServicios;
        return this.total;
    }

    @Override public void agregarObservador(Observador o)  { observadores.add(o); }
    @Override public void eliminarObservador(Observador o) { observadores.remove(o); }
    @Override public void notificarObservadores(String ev, Map<String, Object> datos) {
        for (Observador o : new ArrayList<>(observadores)) {
            if (o.getTiposEvento().contains(ev) || o.getTiposEvento().contains("*"))
                o.actualizar(ev, this, datos);
        }
    }

    public void confirmarPago() {
        if (pago != null) pago.procesar();
        this.estado = EstadoCompra.PAGADA;
        notificarObservadores("PAGO_CONFIRMADO", Map.of("compra", idCompra));
    }

    public void agregarServicio(ServicioAdicional s) {
        if (serviciosAdicionales == null) serviciosAdicionales = new ArrayList<>();
        serviciosAdicionales.add(s);
    }

    public String generarComprobante() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════ COMPROBANTE ═══════════════\n");
        sb.append("# ").append(idCompra).append("\n");
        sb.append("Usuario : ").append(usuario != null ? usuario.getNombre() : "N/A").append("\n");
        sb.append("Evento  : ").append(evento != null ? evento.getNombre() : "N/A").append("\n");
        sb.append("Fecha   : ").append(fechaCreacion).append("\n");
        sb.append("─────────────────────────────────────────\n");
        if (entradas != null) entradas.forEach(e -> sb.append("  · ").append(e.getDescripcion())
                .append(" $").append(e.getPrecio()).append("\n"));
        if (serviciosAdicionales != null) serviciosAdicionales.forEach(s ->
                sb.append("  + ").append(s.getNombre()).append(" $").append(s.getPrecioExtra()).append("\n"));
        sb.append("─────────────────────────────────────────\n");
        sb.append("TOTAL   : $").append(total).append("\n");
        sb.append("Estado  : ").append(estado).append("\n");
        sb.append("═════════════════════════════════════════\n");
        return sb.toString();
    }

    @Override public String toString() {
        return "Compra{id='" + idCompra + "', usuario=" +
                (usuario != null ? usuario.getNombre() : "null") +
                ", total=" + total + ", estado=" + estado + '}';
    }

    public String getIdCompra() { return idCompra; }
    public void setIdCompra(String idCompra) { this.idCompra = idCompra; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public EstadoCompra getEstado() { return estado; }
    public void setEstado(EstadoCompra estado) { this.estado = estado; }
    public List<Entrada> getEntradas() { return entradas; }
    public void setEntradas(List<Entrada> entradas) { this.entradas = entradas; }
    public List<ServicioAdicional> getServiciosAdicionales() { return serviciosAdicionales; }
    public void setServiciosAdicionales(List<ServicioAdicional> serviciosAdicionales) { this.serviciosAdicionales = serviciosAdicionales; }
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }
    public EstadoCompraState getEstadoActual() { return estadoActual; }
    public void setEstadoActual(EstadoCompraState estadoActual) { this.estadoActual = estadoActual; }
    public List<Observador> getObservadores() { return observadores; }
    public void setObservadores(List<Observador> observadores) { this.observadores = observadores; }
}
