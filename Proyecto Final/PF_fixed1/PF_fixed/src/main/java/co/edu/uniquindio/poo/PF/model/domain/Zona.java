package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.EstadoAsiento;
import co.edu.uniquindio.poo.PF.model.enums.TipoZona;
import java.util.ArrayList;
import java.util.List;

public class Zona {
    public Zona() {}

    private String idZona;
    private String nombre;
    private TipoZona tipoZona;
    private double precioBase;
    private int capacidad;
    private List<Asiento> asientos = new ArrayList<>();

    public Zona(String idZona, String nombre, TipoZona tipoZona,
                double precioBase, int capacidad) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.tipoZona = tipoZona;
        this.precioBase = precioBase;
        this.capacidad = capacidad;
        this.asientos = new ArrayList<>();
    }

    public Zona(String idZona, String nombre, int capacidad,
                double precioBase, TipoZona tipoZona, List<Asiento> asientos) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.tipoZona = tipoZona;
        this.asientos = asientos != null ? asientos : new ArrayList<>();
    }

    public void agregarAsiento(Asiento a) { asientos.add(a); }

    public int getDisponibilidad() {
        return (int) asientos.stream()
                .filter(a -> a.getEstado() == EstadoAsiento.DISPONIBLE)
                .count();
    }

    public int consultarOcupacion() {
        if (asientos.isEmpty()) return 0;
        long ocupados = asientos.stream()
                .filter(a -> a.getEstado() != EstadoAsiento.DISPONIBLE).count();
        return (int) (ocupados * 100 / asientos.size());
    }

    @Override public String toString() {
        return nombre + " (" + tipoZona + ") $" + precioBase;
    }

    public String getIdZona() { return idZona; }
    public void setIdZona(String idZona) { this.idZona = idZona; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoZona getTipoZona() { return tipoZona; }
    public void setTipoZona(TipoZona tipoZona) { this.tipoZona = tipoZona; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public List<Asiento> getAsientos() { return asientos; }
    public void setAsientos(List<Asiento> asientos) { this.asientos = asientos; }
}
