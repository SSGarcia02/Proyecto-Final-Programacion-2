package co.edu.uniquindio.poo.PF.model.patterns.strategy;

import co.edu.uniquindio.poo.PF.model.domain.*;

public class PrecioFijo implements EstrategiaCalculo {
    private double precioFijo;

    public PrecioFijo() {
    }

    public PrecioFijo(double precioFijo) {
        this.precioFijo = precioFijo;
    }

    public double getPrecioFijo() { return precioFijo; }
    public void setPrecioFijo(double precioFijo) { this.precioFijo = precioFijo; }

    @Override public double calcularPrecio(double precioBase, Zona zona, Evento evento) {
        return precioFijo;
    }
    @Override public boolean esAplicable(Zona zona, Evento evento) { return true; }
    @Override public String getDescripcion() { return "Precio fijo: $" + precioFijo; }
}
