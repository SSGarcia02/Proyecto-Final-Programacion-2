package co.edu.uniquindio.poo.PF.model.patterns.strategy;

import co.edu.uniquindio.poo.PF.model.domain.Evento;
import co.edu.uniquindio.poo.PF.model.domain.Zona;

public interface EstrategiaCalculo {
    double calcularPrecio(double precioBase, Zona zona, Evento evento);
    boolean esAplicable(Zona zona, Evento evento);
    String getDescripcion();
}
