package co.edu.uniquindio.poo.PF.model.patterns.facade;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.EstadoEntrada;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorEntradas {
    public GestorEntradas() {}

    private List<Entrada> entradasEmitidas = new ArrayList<>();

    public GestorEntradas(List<Entrada> entradasEmitidas) {
        this.entradasEmitidas = (entradasEmitidas != null) ? entradasEmitidas : new ArrayList<>();
    }

    public Entrada emitirEntrada(Zona zona, Asiento asiento) {
        Entrada e = new Entrada(entradasEmitidas.size() + 1, zona, asiento,
                zona.getPrecioBase(), EstadoEntrada.ACTIVA, LocalDateTime.now(), null);
        entradasEmitidas.add(e);
        return e;
    }

    public void anularEntrada(int idEntrada) {
        entradasEmitidas.stream().filter(e -> e.getIdEntrada() == idEntrada)
                .findFirst().ifPresent(Entrada::anular);
    }

    public boolean validarEntrada(String codigo) {
        return entradasEmitidas.stream()
                .anyMatch(e -> e.generarCodigo().equals(codigo)
                        && EstadoEntrada.ACTIVA.equals(e.getEstado()));
    }

    public List<Entrada> getEntradasEmitidas() { return entradasEmitidas; }
    public void setEntradasEmitidas(List<Entrada> entradasEmitidas) { this.entradasEmitidas = entradasEmitidas; }
}
