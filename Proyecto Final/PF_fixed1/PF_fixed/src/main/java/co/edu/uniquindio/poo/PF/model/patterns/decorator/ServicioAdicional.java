package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public abstract class ServicioAdicional implements IComponenteEntrada {
    protected IComponenteEntrada componente;
    protected String nombre;
    protected String descripcion;
    protected double precioExtra;

    public ServicioAdicional() {}

    public ServicioAdicional(IComponenteEntrada componente, String nombre,
                             String descripcion, double precioExtra) {
        this.componente  = componente;
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.precioExtra = precioExtra;
    }

    @Override
    public double getPrecio() {
        return (componente != null ? componente.getPrecio() : 0) + precioExtra;
    }

    @Override
    public String getDescripcion() {
        return (componente != null ? componente.getDescripcion() + " + " : "") + nombre;
    }

    @Override public void activar()    { if (componente != null) componente.activar(); }
    @Override public void marcarUsar() { if (componente != null) componente.marcarUsar(); }
    @Override public void anular()     { if (componente != null) componente.anular(); }

    @Override
    public String generarCodigo() {
        String sufijo = (nombre != null && nombre.length() >= 3)
                ? nombre.toUpperCase().substring(0, 3) : "SRV";
        return (componente != null ? componente.generarCodigo() : "") + "-" + sufijo;
    }

    public abstract ServicioAdicional clone();

    public IComponenteEntrada getComponente() { return componente; }
    public void setComponente(IComponenteEntrada componente) { this.componente = componente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecioExtra() { return precioExtra; }
    public void setPrecioExtra(double precioExtra) { this.precioExtra = precioExtra; }
}
