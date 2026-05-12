package co.edu.uniquindio.poo.PF.model.domain;

public class Tarifa {
    public Tarifa() {}

    public Tarifa(String idTarifa, String nombre, double descuento, String condiciones) {
        this.idTarifa = idTarifa;
        this.nombre = nombre;
        this.descuento = descuento;
        this.condiciones = condiciones;
    }

    private String idTarifa;
    private String nombre;
    private double descuento;
    private String condiciones;

    @Override public String toString() { return nombre + " (" + descuento + "%)"; }

    public String getIdTarifa() { return idTarifa; }
    public void setIdTarifa(String idTarifa) { this.idTarifa = idTarifa; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
}
