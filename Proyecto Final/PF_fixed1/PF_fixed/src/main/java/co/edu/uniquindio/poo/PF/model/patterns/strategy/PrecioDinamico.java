package co.edu.uniquindio.poo.PF.model.patterns.strategy;

import co.edu.uniquindio.poo.PF.model.domain.*;

public class PrecioDinamico implements EstrategiaCalculo {
    private double factorOcupacion;
    private double umbralAlta;

    public PrecioDinamico() {
    }

    public PrecioDinamico(double factorOcupacion, double umbralAlta) {
        this.factorOcupacion = factorOcupacion;
        this.umbralAlta = umbralAlta;
    }

    public double getFactorOcupacion() { return factorOcupacion; }
    public void setFactorOcupacion(double factorOcupacion) { this.factorOcupacion = factorOcupacion; }

    public double getUmbralAlta() { return umbralAlta; }
    public void setUmbralAlta(double umbralAlta) { this.umbralAlta = umbralAlta; }

    @Override
    public double calcularPrecio(double precioBase, Zona zona, Evento evento) {
        if (zona == null) return precioBase;
        double ocupacion = zona.consultarOcupacion() / 100.0;
        if (ocupacion >= umbralAlta) {
            return precioBase * (1 + factorOcupacion);
        }
        return precioBase;
    }

    @Override public boolean esAplicable(Zona zona, Evento evento) {
        return zona != null && zona.consultarOcupacion() >= umbralAlta * 100;
    }

    @Override public String getDescripcion() {
        return "Precio dinámico (factor:" + factorOcupacion + " umbral:" + umbralAlta + ")";
    }
}
