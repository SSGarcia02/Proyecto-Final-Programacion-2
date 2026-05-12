package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import co.edu.uniquindio.poo.PF.model.patterns.singleton.CatalogoEventos;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EventoController {

    private final CatalogoEventos catalogo = CatalogoEventos.getInstance();

    public EventoController() {}

    public CatalogoEventos getCatalogo() { return catalogo; }

    public String generarId() {
        int max = 0;
        for (Evento e : catalogo.getEventos()) {
            String id = e.getIdEvento();
            if (id != null && id.startsWith("E")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return "E" + (max + 1);
    }

    public void registrarEvento(Evento e) { catalogo.registrarEvento(e); }
    public void publicar(String idEvento) { buscarPorId(idEvento).ifPresent(Evento::publicar); }
    public void pausar(String idEvento)   { buscarPorId(idEvento).ifPresent(Evento::pausar); }
    public void cancelar(String idEvento) { buscarPorId(idEvento).ifPresent(Evento::cancelar); }
    public void eliminar(String idEvento) { catalogo.eliminarEvento(idEvento); }
    public List<Evento> listar()  { return catalogo.getEventos(); }
    public List<Evento> activos() { return catalogo.getEventosActivos(); }

    public List<Evento> filtrarActivos(String ciudad, CategoriaEvento cat, LocalDate desde, LocalDate hasta) {
        String ciudadLower = (ciudad == null) ? "" : ciudad.trim().toLowerCase();
        return activos().stream()
                .filter(ev -> ciudadLower.isEmpty() || (ev.getCiudad() != null && ev.getCiudad().toLowerCase().contains(ciudadLower)))
                .filter(ev -> cat == null || ev.getCategoria() == cat)
                .filter(ev -> desde == null || !ev.getFechaHora().toLocalDate().isBefore(desde))
                .filter(ev -> hasta == null || !ev.getFechaHora().toLocalDate().isAfter(hasta))
                .collect(Collectors.toList());
    }

    public Optional<Evento> buscarPorId(String id) {
        return catalogo.getEventos().stream().filter(e -> e.getIdEvento().equals(id)).findFirst();
    }

    public Evento crearEvento(String nombre, String descripcion, String ciudad,
                               LocalDateTime inicio, LocalDateTime fin, Recinto recinto,
                               CategoriaEvento categoria, String protagonista,
                               boolean conVIP, boolean conPref, boolean conGen) {

        String id = generarId();
        List<Zona> zonas = construirZonas(id, conVIP, conPref, conGen);

        Evento ev = switch (categoria) {
            case CONCIERTO   -> new EventoConcierto(id, nombre, categoria, descripcion, ciudad, inicio, EstadoEvento.BORRADOR, recinto, zonas, protagonista, "General");
            case TEATRO      -> new EventoTeatro(id, nombre, categoria, descripcion, ciudad, inicio, EstadoEvento.BORRADOR, recinto, zonas, protagonista, "Cia");
            default          -> new EventoConferencia(id, nombre, categoria, descripcion, ciudad, inicio, EstadoEvento.BORRADOR, recinto, zonas, protagonista, "Tema");
        };
        
        ev.setFechaFinalizacion(fin);
        registrarEvento(ev);
        return ev;
    }

    public List<Zona> construirZonas(String prefijo, boolean conVIP, boolean conPref, boolean conGen) {
        List<Zona> zonas = new ArrayList<>();
        if (conVIP) {
            Zona z = new Zona(prefijo + "-Z-VIP", "VIP", TipoZona.VIP, 350_000.0, 36);
            crearAsientos("VIP", 2, 6).forEach(a -> { a.setZona(z); z.agregarAsiento(a); });
            zonas.add(z);
        }
        if (conPref) {
            Zona z = new Zona(prefijo + "-Z-PREF", "Preferencial", TipoZona.PREFERENCIAL, 200_000.0, 36);
            crearAsientos("PREF", 2, 6).forEach(a -> { a.setZona(z); z.agregarAsiento(a); });
            zonas.add(z);
        }
        if (conGen) {
            Zona z = new Zona(prefijo + "-Z-GEN", "General", TipoZona.GENERAL, 80_000.0, 72);
            crearAsientos("GEN", 4, 6).forEach(a -> { a.setZona(z); z.agregarAsiento(a); });
            zonas.add(z);
        }
        return zonas;
    }

    private List<Asiento> crearAsientos(String prefijo, int filas, int cols) {
        List<Asiento> lista = new ArrayList<>();
        for (String sector : new String[]{"ORI", "CEN", "OCC"}) {
            for (int f = 0; f < filas; f++) {
                String fila = String.valueOf((char) ('A' + f));
                for (int c = 1; c <= cols; c++) {
                    lista.add(new Asiento(prefijo + "-" + sector + "-" + fila + c, fila, c, EstadoAsiento.DISPONIBLE, null));
                }
            }
        }
        return lista;
    }
}
