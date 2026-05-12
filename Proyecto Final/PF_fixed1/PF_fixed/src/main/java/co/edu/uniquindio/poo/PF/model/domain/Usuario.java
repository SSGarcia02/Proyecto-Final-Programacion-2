package co.edu.uniquindio.poo.PF.model.domain;

import co.edu.uniquindio.poo.PF.model.enums.RolUsuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    public Usuario() {
        this.metodosPayPago = new ArrayList<>();
    }

    private String idUsuario;
    private String nombre;
    private String email;
    private String password;
    private RolUsuario rol;
    private String telefono;
    private LocalDateTime fechaRegistro;
    
    private List<String> metodosPayPago = new ArrayList<>();

    public Usuario(String idUsuario, String nombre, String email, String password,
                   RolUsuario rol, String telefono, LocalDateTime fechaRegistro,
                   List<String> metodosPago) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.metodosPayPago = (metodosPago != null) ? new ArrayList<>(metodosPago) : new ArrayList<>();
    }

    public boolean autenticar(String pw) { return this.password != null && this.password.equals(pw); }

    public void actualizarPerfil(String nombre, String email, String telefono) {
        if (nombre != null && !nombre.isBlank()) this.nombre = nombre;
        if (email != null && !email.isBlank()) this.email = email;
        if (telefono != null && !telefono.isBlank()) this.telefono = telefono;
    }

    public boolean isAdmin() { return rol == RolUsuario.ADMINISTRADOR; }

    public void agregarMetodoPago(String metodo) {
        if (this.metodosPayPago == null) this.metodosPayPago = new ArrayList<>();
        this.metodosPayPago.add(metodo);
    }

    @Override public String toString() { return nombre + " <" + email + "> [" + rol + "]"; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public RolUsuario getRol() { return rol; }
    public void setRol(RolUsuario rol) { this.rol = rol; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public List<String> getMetodosPayPago() { return metodosPayPago; }
    public void setMetodosPayPago(List<String> metodosPayPago) { this.metodosPayPago = metodosPayPago; }
}
