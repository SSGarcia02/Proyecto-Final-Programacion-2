package co.edu.uniquindio.poo.PF.model.patterns.observer;

import co.edu.uniquindio.poo.PF.model.domain.Incidencia;
import co.edu.uniquindio.poo.PF.model.enums.TipoIncidencia;
import java.time.LocalDateTime;
import java.util.*;

public class RegistradorIncidencia implements Observador {
    private List<Incidencia> incidencias = new ArrayList<>();

    public RegistradorIncidencia() {
    }

    public RegistradorIncidencia(List<Incidencia> incidencias) {
        this.incidencias = (incidencias != null) ? incidencias : new ArrayList<>();
    }

    public List<Incidencia> getIncidencias() { return incidencias; }
    public void setIncidencias(List<Incidencia> incidencias) { this.incidencias = incidencias; }

    @Override
    public void actualizar(String evento, Object fuente, Map<String, Object> datos) {
        System.out.println("[Incidencias] Procesando evento: " + evento);
        crearIncidencia(TipoIncidencia.OTRO, "Evento del sistema: " + evento);
    }

    @Override public List<String> getTiposEvento() {
        return List.of("CANCELACION", "ERROR_PAGO", "INCIDENCIA", "*");
    }

    public void crearIncidencia(TipoIncidencia tipo, String desc) {
        Incidencia inc = new Incidencia(
                UUID.randomUUID().toString(), tipo, desc,
                LocalDateTime.now(), "Sistema", "N/A");
        incidencias.add(inc);
        System.out.println("[Incidencias] Nueva incidencia registrada: " + desc);
    }
}