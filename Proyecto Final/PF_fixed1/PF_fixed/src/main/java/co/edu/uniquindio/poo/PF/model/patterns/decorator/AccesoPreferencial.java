package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public class AccesoPreferencial extends ServicioAdicional {
    public AccesoPreferencial() {}

    private int nivelAcceso;
    private String puertaAsignada;

    public AccesoPreferencial(IComponenteEntrada componente, String nombre,
                              String descripcion, double precioExtra,
                              int nivelAcceso, String puertaAsignada) {
        super(componente, nombre, descripcion, precioExtra);
        this.nivelAcceso    = nivelAcceso;
        this.puertaAsignada = puertaAsignada;
    }

    @Override public String getDescripcion() {
        return super.getDescripcion() + " [Nivel:" + nivelAcceso + " Puerta:" + puertaAsignada + "]";
    }
    @Override public ServicioAdicional clone() {
        return new AccesoPreferencial(componente, nombre, descripcion, precioExtra, nivelAcceso, puertaAsignada);
    }

    public int getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(int nivelAcceso) { this.nivelAcceso = nivelAcceso; }
    public String getPuertaAsignada() { return puertaAsignada; }
    public void setPuertaAsignada(String puertaAsignada) { this.puertaAsignada = puertaAsignada; }
}
