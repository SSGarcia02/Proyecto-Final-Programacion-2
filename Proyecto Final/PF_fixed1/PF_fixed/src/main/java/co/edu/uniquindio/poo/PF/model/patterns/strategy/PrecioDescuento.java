package co.edu.uniquindio.poo.PF.model.patterns.strategy;

import co.edu.uniquindio.poo.PF.model.domain.*;

public class PrecioDescuento implements EstrategiaCalculo {
    private Tarifa tarifa;
    private double porcentajeDescuento;

    public PrecioDescuento() {
    }

    public PrecioDescuento(Tarifa tarifa, double porcentajeDescuento) {
        this.tarifa = tarifa;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Tarifa getTarifa() { return tarifa; }
    public void setTarifa(Tarifa tarifa) { this.tarifa = tarifa; }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    @Override
    public double calcularPrecio(double precioBase, Zona zona, Evento evento) {
        return precioBase * (1 - porcentajeDescuento / 100);
    }

    @Override public boolean esAplicable(Zona zona, Evento evento) { return tarifa != null; }

    @Override public String getDescripcion() {
        return "Descuento del " + porcentajeDescuento + "%" + (tarifa != null ? " [" + tarifa.getNombre() + "]" : "");
    }
}