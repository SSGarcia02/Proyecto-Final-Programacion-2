package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.domain.Incidencia;
import co.edu.uniquindio.poo.PF.model.enums.TipoIncidencia;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class IncidenciaController {

    private final List<Incidencia> incidencias = new ArrayList<>();
    private int contador = 1000;

    public IncidenciaController() {}

    private String generarId() {
        return "INC-" + (contador++);
    }

    public Incidencia registrar(TipoIncidencia tipo, String descripcion,
                                String entidad, String idEntidad) {
        Incidencia inc = new Incidencia(generarId(), tipo,
                descripcion, LocalDateTime.now(), entidad, idEntidad);
        incidencias.add(inc);
        return inc;
    }

    public Incidencia reportarPorUsuario(TipoIncidencia tipo, String descripcion,
                                          String entidad, String idEntidad,
                                          String idUsuario) {
        Incidencia inc = new Incidencia(generarId(), tipo,
                descripcion, LocalDateTime.now(), entidad, idEntidad);
        inc.setReportadoPor(idUsuario);
        incidencias.add(inc);
        return inc;
    }

    public boolean resolver(String idIncidencia, String respuesta) {
        Optional<Incidencia> opt = incidencias.stream()
                .filter(i -> i.getIdIncidencia().equals(idIncidencia))
                .findFirst();
        opt.ifPresent(i -> i.resolver(respuesta));
        return opt.isPresent();
    }

    public List<Incidencia> listar() { return Collections.unmodifiableList(incidencias); }

    public List<Incidencia> listarPorUsuario(String idUsuario) {
        return incidencias.stream()
                .filter(i -> idUsuario.equals(i.getReportadoPor()))
                .collect(Collectors.toList());
    }
}
