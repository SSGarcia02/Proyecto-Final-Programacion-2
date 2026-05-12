package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.domain.Compra;
import co.edu.uniquindio.poo.PF.model.domain.Recinto;
import co.edu.uniquindio.poo.PF.model.domain.Zona;
import co.edu.uniquindio.poo.PF.model.enums.EstadoCompra;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MetricasController {

    private final CompraController compraCtrl;
    private final RecintoController recintoCtrl;

    public MetricasController(CompraController compraCtrl, RecintoController recintoCtrl) {
        this.compraCtrl  = compraCtrl;
        this.recintoCtrl = recintoCtrl;
    }

    public double getTotalVentas() {
        return compraCtrl.getCompras().stream()
                .filter(c -> c.getEstado() == EstadoCompra.PAGADA || c.getEstado() == EstadoCompra.CONFIRMADA)
                .mapToDouble(Compra::getTotal).sum();
    }

    public long getTotalCompras() {
        return compraCtrl.getCompras().size();
    }

    public long getTotalPagadasConfirmadas() {
        return compraCtrl.getCompras().stream()
                .filter(c -> c.getEstado() == EstadoCompra.PAGADA || c.getEstado() == EstadoCompra.CONFIRMADA)
                .count();
    }

    public long getTotalCanceladas() {
        return compraCtrl.listarPorEstado(EstadoCompra.CANCELADA).size();
    }

    public double getTasaCancelacion() {
        long total = getTotalCompras();
        if (total == 0) return 0.0;
        return (double) getTotalCanceladas() / total * 100.0;
    }

    public List<DatoEstado> getDatosPorEstado() {
        List<DatoEstado> resultado = new ArrayList<>();
        for (EstadoCompra estado : EstadoCompra.values()) {
            List<Compra> lista = compraCtrl.listarPorEstado(estado);
            double monto = lista.stream().mapToDouble(Compra::getTotal).sum();
            resultado.add(new DatoEstado(estado.name(), lista.size(), monto));
        }
        return resultado;
    }

    public record DatoEstado(String estado, int cantidad, double monto) {}

    public List<DatoDia> getEvolucionUltimos30Dias() {
        LocalDate hoy = LocalDate.now();
        Map<LocalDate, DatoDia> mapa = new TreeMap<>();
        
        for (int i = 29; i >= 0; i--) {
            LocalDate d = hoy.minusDays(i);
            mapa.put(d, new DatoDia(d, 0, 0.0));
        }

        for (Compra c : compraCtrl.getCompras()) {
            if (c.getFechaCreacion() == null) continue;
            LocalDate fecha = c.getFechaCreacion().toLocalDate();
            if (mapa.containsKey(fecha)) {
                DatoDia actual = mapa.get(fecha);
                double nuevoIngreso = actual.ingresos();
                if (c.getEstado() == EstadoCompra.PAGADA || c.getEstado() == EstadoCompra.CONFIRMADA) {
                    nuevoIngreso += c.getTotal();
                }
                mapa.put(fecha, new DatoDia(fecha, actual.cantidad() + 1, nuevoIngreso));
            }
        }
        return new ArrayList<>(mapa.values());
    }

    public record DatoDia(LocalDate dia, int cantidad, double ingresos) {}

    public List<DatoOcupacion> getOcupacionPorZona() {
        List<DatoOcupacion> resultado = new ArrayList<>();
        if (recintoCtrl != null) {
            for (Recinto r : recintoCtrl.listar()) {
                for (Zona z : r.getZonas()) {
                    resultado.add(new DatoOcupacion(
                            r.getNombre() + " - " + z.getNombre(),
                            z.consultarOcupacion()));
                }
            }
        }
        return resultado;
    }

    public record DatoOcupacion(String etiqueta, int porcentaje) {}
}
