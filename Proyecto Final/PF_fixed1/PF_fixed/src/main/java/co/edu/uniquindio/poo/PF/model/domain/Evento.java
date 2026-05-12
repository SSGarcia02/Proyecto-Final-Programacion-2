package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.*;
import co.edu.uniquindio.poo.PF.model.patterns.observer.*;
import java.time.LocalDateTime;
import java.util.*;

public abstract class Evento implements Sujeto {
    public Evento() {}

    protected String idEvento;
    protected String nombre;
    protected CategoriaEvento categoria;
    protected String descripcion;
    protected String ciudad;
    protected LocalDateTime fechaHora;
    protected LocalDateTime fechaFinalizacion;
    protected EstadoEvento estado;
    protected Recinto recinto;
    protected List<Zona> zonas = new ArrayList<>();
    protected List<Observador> observadores = new ArrayList<>();

    public Evento(String idEvento, String nombre, CategoriaEvento categoria,
                  String descripcion, String ciudad, LocalDateTime fechaHora,
                  EstadoEvento estado, Recinto recinto, List<Zona> zonas) {
        this.idEvento = idEvento; this.nombre = nombre; this.categoria = categoria;
        this.descripcion = descripcion; this.ciudad = ciudad; this.fechaHora = fechaHora;
        this.estado = estado; this.recinto = recinto;
        this.zonas = zonas != null ? zonas : new ArrayList<>();
        this.fechaFinalizacion = fechaHora.plusHours(3);
    }

    @Override public void agregarObservador(Observador o)  { observadores.add(o); }
    @Override public void eliminarObservador(Observador o) { observadores.remove(o); }
    @Override public void notificarObservadores(String ev, Map<String, Object> datos) {
        for (Observador o : new ArrayList<>(observadores)) {
            if (o.getTiposEvento().contains(ev) || o.getTiposEvento().contains("*"))
                o.actualizar(ev, this, datos);
        }
    }

    public void publicar() {
        this.estado = EstadoEvento.PUBLICADO;
        notificarObservadores("PUBLICADO", Map.of("estado", estado));
    }

    public void pausar() {
        this.estado = EstadoEvento.PAUSADO;
        notificarObservadores("PAUSADO", Map.of("estado", estado));
    }

    public void cancelar() {
        this.estado = EstadoEvento.CANCELADO;
        notificarObservadores("CANCELADO", Map.of("estado", estado));
    }

    public Map<Zona, Integer> consultarDisponibilidad() {
        Map<Zona, Integer> mapa = new HashMap<>();
        for (Zona z : zonas) mapa.put(z, z.getDisponibilidad());
        return mapa;
    }

    public void cambiarEstado(EstadoEvento nuevo) {
        this.estado = nuevo;
        notificarObservadores("CAMBIO_ESTADO", Map.of("estado", nuevo));
    }

    @Override public String toString() {
        return nombre + " [" + (estado != null ? estado : "?") + "] - " + ciudad;
    }

    public String getIdEvento() { return idEvento; }
    public void setIdEvento(String idEvento) { this.idEvento = idEvento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public CategoriaEvento getCategoria() { return categoria; }
    public void setCategoria(CategoriaEvento categoria) { this.categoria = categoria; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }
    public EstadoEvento getEstado() { return estado; }
    public void setEstado(EstadoEvento estado) { this.estado = estado; }
    public Recinto getRecinto() { return recinto; }
    public void setRecinto(Recinto recinto) { this.recinto = recinto; }
    public List<Zona> getZonas() { return zonas; }
    public void setZonas(List<Zona> zonas) { this.zonas = zonas; }
    public List<Observador> getObservadores() { return observadores; }
    public void setObservadores(List<Observador> observadores) { this.observadores = observadores; }
}
