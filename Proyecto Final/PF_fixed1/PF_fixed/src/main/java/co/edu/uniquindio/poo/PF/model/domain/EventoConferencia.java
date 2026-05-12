package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.*;
import java.time.LocalDateTime;
import java.util.List;

public class EventoConferencia extends Evento {
    public EventoConferencia() {}

    private String ponente;
    private String tema;

    public EventoConferencia(String idEvento, String nombre, CategoriaEvento categoria,
                             String descripcion, String ciudad, LocalDateTime fechaHora,
                             EstadoEvento estado, Recinto recinto, List<Zona> zonas,
                             String ponente, String tema) {
        super(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, estado, recinto, zonas);
        this.ponente = ponente;
        this.tema = tema;
    }

    public EventoConferencia(String idEvento, String nombre,
                             String descripcion, String ciudad, LocalDateTime fechaHora,
                             Recinto recinto, List<Zona> zonas,
                             String ponente, String tema) {
        super(idEvento, nombre, CategoriaEvento.CONFERENCIA, descripcion, ciudad,
              fechaHora, EstadoEvento.BORRADOR, recinto, zonas);
        this.ponente = ponente;
        this.tema = tema;
    }

    public String getPonente() { return ponente; }
    public void setPonente(String ponente) { this.ponente = ponente; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
}
