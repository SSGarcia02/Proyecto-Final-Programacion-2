package co.edu.uniquindio.poo.PF.model.patterns.singleton;

import co.edu.uniquindio.poo.PF.model.domain.Evento;
import co.edu.uniquindio.poo.PF.model.enums.CategoriaEvento;

import java.util.*;
import java.util.stream.Collectors;

public class CatalogoEventos {

    private static CatalogoEventos instancia;
    private final List<Evento> eventos = new ArrayList<>();
    private final Map<CategoriaEvento, List<Evento>> indiceCategoria = new HashMap<>();
    private final Map<String, List<Evento>> indiceCiudad = new HashMap<>();

    private CatalogoEventos() {}

    public static CatalogoEventos getInstance() {
        if (instancia == null) {
            instancia = new CatalogoEventos();
        }
        return instancia;
    }

    public void registrarEvento(Evento e) {
        eventos.add(e);
        indiceCategoria.computeIfAbsent(e.getCategoria(), k -> new ArrayList<>()).add(e);
        indiceCiudad.computeIfAbsent(e.getCiudad(), k -> new ArrayList<>()).add(e);
    }

    public List<Evento> buscarPorCategoria(CategoriaEvento c) {
        return indiceCategoria.getOrDefault(c, Collections.emptyList());
    }

    public List<Evento> buscarPorCiudad(String ciudad) {
        return indiceCiudad.getOrDefault(ciudad, Collections.emptyList());
    }

    public List<Evento> getEventosActivos() {
        return eventos.stream()
                .filter(e -> e.getEstado() == co.edu.uniquindio.poo.PF.model.enums.EstadoEvento.PUBLICADO)
                .collect(Collectors.toList());
    }

    public void eliminarEvento(String idEvento) {
        eventos.removeIf(e -> e.getIdEvento().equals(idEvento));
        indiceCategoria.values().forEach(list -> list.removeIf(e -> e.getIdEvento().equals(idEvento)));
        indiceCiudad.values().forEach(list -> list.removeIf(e -> e.getIdEvento().equals(idEvento)));
    }

    public List<Evento> getEventos() { return eventos; }
}