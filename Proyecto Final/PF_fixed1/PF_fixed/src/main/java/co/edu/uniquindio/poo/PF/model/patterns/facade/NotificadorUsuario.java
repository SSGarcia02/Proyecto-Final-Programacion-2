package co.edu.uniquindio.poo.PF.model.patterns.facade;

import co.edu.uniquindio.poo.PF.model.domain.*;

public class NotificadorUsuario {
    public void notificarCompra(Usuario u, Compra c) {
        System.out.println("[Notificador] Compra confirmada para " + u.getNombre()
                + " — Total: $" + c.getTotal());
    }
    public void notificarCancelacion(Usuario u, Compra c) {
        System.out.println("[Notificador] Compra cancelada para " + u.getNombre());
    }
    public void notificarPago(Usuario u, Pago p) {
        System.out.println("[Notificador] Pago procesado: $" + p.getMonto()
                + " via " + p.getMetodoPago());
    }
}
