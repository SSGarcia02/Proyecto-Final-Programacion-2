package co.edu.uniquindio.poo.PF.model.patterns.decorator;

public interface IComponenteEntrada {
    double getPrecio();
    String getDescripcion();
    void activar();
    void marcarUsar();
    void anular();
    String generarCodigo();
}