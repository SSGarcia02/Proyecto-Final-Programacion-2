package co.edu.uniquindio.poo.PF.model.patterns.facade;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;
import co.edu.uniquindio.poo.PF.model.enums.TipoServicio;
import java.util.List;

public class CompraDTO {
    public CompraDTO() {}

    public CompraDTO(Usuario usuario, Evento evento, Zona zona, Asiento asiento, List<TipoServicio> servicios, TipoMetodoPago metodoPago) {
        this.usuario = usuario;
        this.evento = evento;
        this.zona = zona;
        this.asiento = asiento;
        this.servicios = servicios;
        this.metodoPago = metodoPago;
    }

    private Usuario usuario;
    private Evento evento;
    private Zona zona;
    private Asiento asiento;
    private List<TipoServicio> servicios;
    private TipoMetodoPago metodoPago;

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public Zona getZona() { return zona; }
    public void setZona(Zona zona) { this.zona = zona; }
    public Asiento getAsiento() { return asiento; }
    public void setAsiento(Asiento asiento) { this.asiento = asiento; }
    public List<TipoServicio> getServicios() { return servicios; }
    public void setServicios(List<TipoServicio> servicios) { this.servicios = servicios; }
    public TipoMetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(TipoMetodoPago metodoPago) { this.metodoPago = metodoPago; }
}
