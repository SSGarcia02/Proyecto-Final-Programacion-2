package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public class SeguroCancelacion extends ServicioAdicional {
    public SeguroCancelacion() {}

    private double montoCobertura;
    private String condiciones;

    public SeguroCancelacion(IComponenteEntrada componente, String nombre,
                             String descripcion, double precioExtra,
                             double montoCobertura, String condiciones) {
        super(componente, nombre, descripcion, precioExtra);
        this.montoCobertura = montoCobertura;
        this.condiciones    = condiciones;
    }

    @Override public String getDescripcion() {
        return super.getDescripcion() + " [Cobertura $" + montoCobertura + "]";
    }
    @Override public ServicioAdicional clone() {
        return new SeguroCancelacion(componente, nombre, descripcion, precioExtra, montoCobertura, condiciones);
    }

    public double getMontoCobertura() { return montoCobertura; }
    public void setMontoCobertura(double montoCobertura) { this.montoCobertura = montoCobertura; }
    public String getCondiciones() { return condiciones; }
    public void setCondiciones(String condiciones) { this.condiciones = condiciones; }
}
