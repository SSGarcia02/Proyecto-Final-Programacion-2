package co.edu.uniquindio.poo.PF.model.patterns.singleton;

import co.edu.uniquindio.poo.PF.model.domain.Usuario;
import java.time.LocalDateTime;

public class GestorSesion {

    private static GestorSesion instancia;
    private Usuario usuarioActual;
    private String tokenActivo;
    private LocalDateTime fechaInicioSesion;

    private GestorSesion() {}

    public static GestorSesion getInstance() {
        if (instancia == null) {
            instancia = new GestorSesion();
        }
        return instancia;
    }

    public void iniciarSesion(Usuario u, String token) {
        this.usuarioActual = u;
        this.tokenActivo = token;
        this.fechaInicioSesion = LocalDateTime.now();
        System.out.println("Sesión iniciada para: " + u.getNombre());
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        this.tokenActivo = null;
        this.fechaInicioSesion = null;
        System.out.println("Sesión cerrada.");
    }

    public Usuario getUsuarioActual() { return usuarioActual; }

    public boolean estaAutenticado() {
        return usuarioActual != null && tokenActivo != null;
    }

    public String getTokenActivo() { return tokenActivo; }
    public LocalDateTime getFechaInicioSesion() { return fechaInicioSesion; }
}