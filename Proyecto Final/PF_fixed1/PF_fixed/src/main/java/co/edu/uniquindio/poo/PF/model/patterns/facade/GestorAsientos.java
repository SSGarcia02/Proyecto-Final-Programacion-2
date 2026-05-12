package co.edu.uniquindio.poo.PF.model.patterns.facade;

import co.edu.uniquindio.poo.PF.model.domain.Asiento;
import co.edu.uniquindio.poo.PF.model.domain.Zona;
import co.edu.uniquindio.poo.PF.model.enums.EstadoAsiento;
import java.util.*;

public class GestorAsientos {
    public GestorAsientos() {}

    public boolean reservar(String idAsiento) {
        for (List<Asiento> lista : asientosPorZona.values()) {
            for (Asiento a : lista) {
                if (a.getIdAsiento().equals(idAsiento)) {
                    if (a.isDisponible()) {
                        a.reservar();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean reservar(Asiento asiento) {
        if (asiento != null && asiento.isDisponible()) {
            asiento.reservar();
            return true;
        }
        return false;
    }

    public void liberar(String idAsiento) {
        for (List<Asiento> lista : asientosPorZona.values()) {
            for (Asiento a : lista) {
                if (a.getIdAsiento().equals(idAsiento)) {
                    a.liberar();
                    return;
                }
            }
        }
    }

    public int verificarDisponibilidad(String idZona) {
        return asientosPorZona.entrySet().stream()
                .filter(e -> e.getKey().getIdZona().equals(idZona))
                .mapToInt(e -> e.getKey().getDisponibilidad())
                .sum();
    }

    public void bloquear(String idAsiento) {
        for (List<Asiento> lista : asientosPorZona.values()) {
            for (Asiento a : lista) {
                if (a.getIdAsiento().equals(idAsiento)) {
                    a.bloquear();
                    return;
                }
            }
        }
    }

    private Map<Zona, List<Asiento>> asientosPorZona = new HashMap<>();

    public void registrarZona(Zona zona) {
        if (zona != null) {
            asientosPorZona.put(zona, zona.getAsientos());
        }
    }

    public Map<Zona, List<Asiento>> getAsientosPorZona() { return asientosPorZona; }
    public void setAsientosPorZona(Map<Zona, List<Asiento>> asientosPorZona) { this.asientosPorZona = asientosPorZona; }
}
