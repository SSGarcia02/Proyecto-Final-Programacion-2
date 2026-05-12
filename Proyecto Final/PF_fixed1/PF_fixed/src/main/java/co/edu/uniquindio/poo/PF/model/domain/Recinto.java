package co.edu.uniquindio.poo.PF.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Recinto {
    public Recinto() {}

    private String idRecinto;
    private String nombre;
    private String ciudad;
    private String direccion;
    private int aforo;
    private List<Zona> zonas = new ArrayList<>();

    public Recinto(String idRecinto, String nombre, String ciudad,
                   String direccion, int aforo) {
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.aforo = aforo;
        this.zonas = new ArrayList<>();
    }

    public Recinto(String idRecinto, String nombre, String direccion, String ciudad,
                   int aforo, List<Zona> zonas) {
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.aforo = aforo;
        this.zonas = zonas != null ? zonas : new ArrayList<>();
    }

    public void agregarZona(Zona z) { zonas.add(z); }

    @Override public String toString() {
        return nombre + " - " + ciudad + " (aforo: " + aforo + ")";
    }

    public String getIdRecinto() { return idRecinto; }
    public void setIdRecinto(String idRecinto) { this.idRecinto = idRecinto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public int getAforo() { return aforo; }
    public void setAforo(int aforo) { this.aforo = aforo; }
    public List<Zona> getZonas() { return zonas; }
    public void setZonas(List<Zona> zonas) { this.zonas = zonas; }
}
