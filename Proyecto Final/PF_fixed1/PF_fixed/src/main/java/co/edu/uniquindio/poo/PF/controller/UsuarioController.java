package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.domain.Usuario;
import co.edu.uniquindio.poo.PF.model.enums.RolUsuario;
import co.edu.uniquindio.poo.PF.model.patterns.singleton.GestorSesion;
import java.time.LocalDateTime;
import java.util.*;

public class UsuarioController {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final GestorSesion gestorSesion = GestorSesion.getInstance();

    public UsuarioController() {
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public GestorSesion getGestorSesion() {
        return gestorSesion;
    }

    public boolean registrar(String id, String nombre, String email,
                             String password, String telefono) {
        if (buscarPorEmail(email) != null) {
            return false;
        }
        Usuario u = new Usuario(id, nombre, email, password,
                RolUsuario.USUARIO, telefono, LocalDateTime.now(), new ArrayList<>());
        usuarios.add(u);
        return true;
    }

    public boolean iniciarSesion(String email, String password) {
        Usuario u = buscarPorEmail(email);
        if (u != null && u.autenticar(password)) {
            gestorSesion.iniciarSesion(u, UUID.randomUUID().toString());
            return true;
        }
        return false;
    }

    public void cerrarSesion() { gestorSesion.cerrarSesion(); }

    public boolean actualizarPerfil(String id, String nombre, String email, String telefono) {
        Usuario u = buscarPorId(id);
        if (u == null) return false;
        u.actualizarPerfil(nombre, email, telefono);
        return true;
    }

    public boolean eliminar(String id) {
        return usuarios.removeIf(u -> u.getIdUsuario().equals(id));
    }

    public List<Usuario> listar() { return Collections.unmodifiableList(usuarios); }

    public Usuario buscarPorId(String id) {
        return usuarios.stream().filter(u -> u.getIdUsuario().equals(id)).findFirst().orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    public Usuario getUsuarioActual() { return gestorSesion.getUsuarioActual(); }
}
