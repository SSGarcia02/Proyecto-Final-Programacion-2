package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public class EntradaSimple implements IComponenteEntrada {
    private final double precioBase;
    private String descripcion;

    public EntradaSimple(double precioBase) {
        this.precioBase  = precioBase;
        this.descripcion = "Entrada base";
    }

    public EntradaSimple(double precioBase, String descripcion) {
        this.precioBase  = precioBase;
        this.descripcion = descripcion;
    }

    @Override public double getPrecio()      { return precioBase; }
    @Override public String getDescripcion() { return descripcion; }
    @Override public void activar()          { }
    @Override public void marcarUsar()       { }
    @Override public void anular()           { }
    @Override public String generarCodigo()  { return "BASE-" + (int) precioBase; }
}
