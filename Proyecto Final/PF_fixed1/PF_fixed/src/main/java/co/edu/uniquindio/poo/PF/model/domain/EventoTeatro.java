package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.*;
import java.time.LocalDateTime;
import java.util.List;

public class EventoTeatro extends Evento {
    public EventoTeatro() {}

    private String obra;
    private String compania;

    public EventoTeatro(String idEvento, String nombre, CategoriaEvento categoria,
                        String descripcion, String ciudad, LocalDateTime fechaHora,
                        EstadoEvento estado, Recinto recinto, List<Zona> zonas,
                        String obra, String compania) {
        super(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, estado, recinto, zonas);
        this.obra = obra;
        this.compania = compania;
    }

    public EventoTeatro(String idEvento, String nombre,
                        String descripcion, String ciudad, LocalDateTime fechaHora,
                        Recinto recinto, List<Zona> zonas,
                        String obra, String compania) {
        super(idEvento, nombre, CategoriaEvento.TEATRO, descripcion, ciudad,
              fechaHora, EstadoEvento.BORRADOR, recinto, zonas);
        this.obra = obra;
        this.compania = compania;
    }

    public String getObra() { return obra; }
    public void setObra(String obra) { this.obra = obra; }
    public String getCompania() { return compania; }
    public void setCompania(String compania) { this.compania = compania; }
}
