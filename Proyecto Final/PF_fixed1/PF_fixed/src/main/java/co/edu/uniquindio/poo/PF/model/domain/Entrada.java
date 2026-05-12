package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.EstadoEntrada;
import co.edu.uniquindio.poo.PF.model.patterns.decorator.IComponenteEntrada;
import co.edu.uniquindio.poo.PF.model.patterns.strategy.EstrategiaCalculo;
import java.time.LocalDateTime;

public class Entrada implements IComponenteEntrada {
    public Entrada() {}

    private int idEntrada;
    private Zona zona;
    private Asiento asiento;
    private double precioFinal;
    private EstadoEntrada estado;
    private LocalDateTime fechaEmision;
    private EstrategiaCalculo estrategiaCalculo;

    public Entrada(int idEntrada, Zona zona, Asiento asiento, double precioFinal,
                   EstadoEntrada estado, LocalDateTime fechaEmision,
                   EstrategiaCalculo estrategiaCalculo) {
        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
        this.estrategiaCalculo = estrategiaCalculo;
    }

    @Override
    public double getPrecio() {
        if (estrategiaCalculo != null && zona != null)
            return estrategiaCalculo.calcularPrecio(zona.getPrecioBase(), zona, null);
        return precioFinal;
    }

    @Override
    public String getDescripcion() {
        return "Entrada #" + idEntrada + " Zona:" + (zona != null ? zona.getNombre() : "N/A");
    }

    @Override public void activar()    { this.estado = EstadoEntrada.ACTIVA; }
    @Override public void marcarUsar() { this.estado = EstadoEntrada.USADA; }
    @Override public void anular()     { this.estado = EstadoEntrada.ANULADA; }

    @Override
    public String generarCodigo() {
        return "ENT-" + idEntrada + "-" + (zona != null ? zona.getIdZona() : "XX");
    }

    public double calcularPrecioFinal() {
        if (estrategiaCalculo != null && zona != null)
            this.precioFinal = estrategiaCalculo.calcularPrecio(zona.getPrecioBase(), zona, null);
        return precioFinal;
    }

    @Override public String toString() {
        return "Entrada{id=" + idEntrada + ", precio=" + precioFinal + ", estado=" + estado + '}';
    }

    public int getIdEntrada() { return idEntrada; }
    public void setIdEntrada(int idEntrada) { this.idEntrada = idEntrada; }
    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }
    public Asiento getAsiento() { return asiento; }
    public void setAsiento(Asiento asiento) { this.asiento = asiento; }
    public double getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(double precioFinal) { this.precioFinal = precioFinal; }
    public EstadoEntrada getEstado() { return estado; }
    public void setEstado(EstadoEntrada estado) { this.estado = estado; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }
    public EstrategiaCalculo getEstrategiaCalculo() { return estrategiaCalculo; }
    public void setEstrategiaCalculo(EstrategiaCalculo estrategiaCalculo) { this.estrategiaCalculo = estrategiaCalculo; }
}
