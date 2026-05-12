package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public class Merchandising extends ServicioAdicional {
    public Merchandising() {}

    private String producto;
    private String talla;

    public Merchandising(IComponenteEntrada componente, String nombre,
                         String descripcion, double precioExtra,
                         String producto, String talla) {
        super(componente, nombre, descripcion, precioExtra);
        this.producto = producto;
        this.talla    = talla;
    }

    @Override public String getDescripcion() {
        return super.getDescripcion() + " [" + producto + " talla:" + talla + "]";
    }
    @Override public ServicioAdicional clone() {
        return new Merchandising(componente, nombre, descripcion, precioExtra, producto, talla);
    }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
}
