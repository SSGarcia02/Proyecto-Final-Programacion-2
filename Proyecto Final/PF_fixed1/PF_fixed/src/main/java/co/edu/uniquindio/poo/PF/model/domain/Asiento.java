package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.EstadoAsiento;

public class Asiento {
    public Asiento() {}

    private String idAsiento;
    private String fila;
    private int numero;
    private EstadoAsiento estado = EstadoAsiento.DISPONIBLE;
    private Zona zona;

    public Asiento(String idAsiento, String fila, int numero, Zona zona) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.zona = zona;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public Asiento(String idAsiento, String fila, int numero, EstadoAsiento estado, Zona zona) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado != null ? estado : EstadoAsiento.DISPONIBLE;
        this.zona = zona;
    }

    public void reservar()  { this.estado = EstadoAsiento.RESERVADO; }
    public void liberar()   { this.estado = EstadoAsiento.DISPONIBLE; }
    public void bloquear()  { this.estado = EstadoAsiento.BLOQUEADO; }
    public void cambiarEstado(EstadoAsiento nuevoEstado) { this.estado = nuevoEstado; }
    public boolean isDisponible() { return estado == EstadoAsiento.DISPONIBLE; }

    @Override public String toString() {
        return fila + String.format("%02d", numero) + " [" + estado + "]";
    }

    public String getIdAsiento() { return idAsiento; }
    public void setIdAsiento(String idAsiento) { this.idAsiento = idAsiento; }
    public String getFila() { return fila; }
    public void setFila(String fila) { this.fila = fila; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public EstadoAsiento getEstado() { return estado; }
    public void setEstado(EstadoAsiento estado) { this.estado = estado; }
    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }
}
