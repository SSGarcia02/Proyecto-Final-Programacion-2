package co.edu.uniquindio.poo.PF.model.patterns.observer;

import java.util.List;
import java.util.Map;

public interface Observador {
    void actualizar(String evento, Object fuente, Map<String, Object> datos);
    List<String> getTiposEvento();
}
