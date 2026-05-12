package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public class Parqueadero extends ServicioAdicional {
    public Parqueadero() {}

    private String placa;
    private String ubicacion;

    public Parqueadero(IComponenteEntrada componente, String nombre,
                       String descripcion, double precioExtra,
                       String placa, String ubicacion) {
        super(componente, nombre, descripcion, precioExtra);
        this.placa     = placa;
        this.ubicacion = ubicacion;
    }

    @Override public String getDescripcion() {
        return super.getDescripcion() + " [Placa:" + placa + " Ubic:" + ubicacion + "]";
    }
    @Override public ServicioAdicional clone() {
        return new Parqueadero(componente, nombre, descripcion, precioExtra, placa, ubicacion);
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
