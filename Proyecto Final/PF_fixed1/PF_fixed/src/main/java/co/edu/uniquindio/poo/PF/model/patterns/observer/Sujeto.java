package co.edu.uniquindio.poo.PF.model.patterns.observer;

import java.util.Map;

public interface Sujeto {
    void agregarObservador(Observador o);
    void eliminarObservador(Observador o);
    void notificarObservadores(String evento, Map<String, Object> datos);
}
