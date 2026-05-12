package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.*;
import java.time.LocalDateTime;
import java.util.List;

public class EventoConcierto extends Evento {
    public EventoConcierto() {}

    private String artista;
    private String generoMusical;

    public EventoConcierto(String idEvento, String nombre, CategoriaEvento categoria,
                           String descripcion, String ciudad, LocalDateTime fechaHora,
                           EstadoEvento estado, Recinto recinto, List<Zona> zonas,
                           String artista, String generoMusical) {
        super(idEvento, nombre, categoria, descripcion, ciudad, fechaHora, estado, recinto, zonas);
        this.artista = artista;
        this.generoMusical = generoMusical;
    }

    public EventoConcierto(String idEvento, String nombre,
                           String descripcion, String ciudad, LocalDateTime fechaHora,
                           Recinto recinto, List<Zona> zonas,
                           String artista, String generoMusical) {
        super(idEvento, nombre, CategoriaEvento.CONCIERTO, descripcion, ciudad,
              fechaHora, EstadoEvento.BORRADOR, recinto, zonas);
        this.artista = artista;
        this.generoMusical = generoMusical;
    }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }
    public String getGeneroMusical() { return generoMusical; }
    public void setGeneroMusical(String generoMusical) { this.generoMusical = generoMusical; }
}
