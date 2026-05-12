package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public class ServicioVIP extends ServicioAdicional {
    public ServicioVIP() {}

    private boolean loungeAccess;
    private boolean cateringIncluido;

    public ServicioVIP(IComponenteEntrada componente, String nombre,
                       String descripcion, double precioExtra,
                       boolean loungeAccess, boolean cateringIncluido) {
        super(componente, nombre, descripcion, precioExtra);
        this.loungeAccess      = loungeAccess;
        this.cateringIncluido  = cateringIncluido;
    }

    @Override public String getDescripcion() {
        return super.getDescripcion()
                + (cateringIncluido ? " [Catering]" : "")
                + (loungeAccess     ? " [Lounge]"   : "");
    }
    @Override public ServicioAdicional clone() {
        return new ServicioVIP(componente, nombre, descripcion, precioExtra, loungeAccess, cateringIncluido);
    }

    public boolean isLoungeAccess() { return loungeAccess; }
    public void setLoungeAccess(boolean loungeAccess) { this.loungeAccess = loungeAccess; }
    public boolean isCateringIncluido() { return cateringIncluido; }
    public void setCateringIncluido(boolean cateringIncluido) { this.cateringIncluido = cateringIncluido; }
}
