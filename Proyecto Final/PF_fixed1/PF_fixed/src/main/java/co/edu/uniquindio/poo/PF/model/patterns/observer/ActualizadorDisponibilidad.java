package co.edu.uniquindio.poo.PF.model.patterns.observer;

import co.edu.uniquindio.poo.PF.model.domain.Evento;
import co.edu.uniquindio.poo.PF.model.patterns.singleton.CatalogoEventos;
import java.util.List;
import java.util.Map;

public class ActualizadorDisponibilidad implements Observador {
    private CatalogoEventos catalogo = CatalogoEventos.getInstance();

    public ActualizadorDisponibilidad() {
    }

    public ActualizadorDisponibilidad(CatalogoEventos catalogo) {
        this.catalogo = catalogo;
    }

    public CatalogoEventos getCatalogo() { return catalogo; }
    public void setCatalogo(CatalogoEventos catalogo) { this.catalogo = catalogo; }

    @Override
    public void actualizar(String evento, Object fuente, Map<String, Object> datos) {
        if (fuente instanceof Evento e) {
            sincronizarDisponibilidad(e);
        }
    }

    @Override public List<String> getTiposEvento() {
        return List.of("CAMBIO_ESTADO", "DISPONIBILIDAD_ACTUALIZADA");
    }

    public void sincronizarDisponibilidad(Evento e) {
        System.out.println("[Disponibilidad] Sincronizando evento: " + e.getNombre());
    }
}
