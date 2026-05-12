package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.domain.Recinto;
import co.edu.uniquindio.poo.PF.model.domain.Zona;
import java.util.*;

public class RecintoController {

    private final List<Recinto> recintos = new ArrayList<>();

    public RecintoController() {}

    public List<Recinto> listar() { return Collections.unmodifiableList(recintos); }

    public void registrar(Recinto r) { recintos.add(r); }

    public void agregar(Recinto r) { recintos.add(r); }

    public String generarId() {
        int max = 0;
        for (Recinto r : recintos) {
            String id = r.getIdRecinto();
            if (id != null && id.startsWith("R")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return "R" + (max + 1);
    }

    public boolean eliminar(String idRecinto) {
        return recintos.removeIf(r -> r.getIdRecinto().equals(idRecinto));
    }

    public Optional<Recinto> buscarPorId(String id) {
        return recintos.stream().filter(r -> r.getIdRecinto().equals(id)).findFirst();
    }

    public boolean agregarZona(String idRecinto, Zona zona) {
        Optional<Recinto> r = buscarPorId(idRecinto);
        r.ifPresent(recinto -> recinto.agregarZona(zona));
        return r.isPresent();
    }
}
