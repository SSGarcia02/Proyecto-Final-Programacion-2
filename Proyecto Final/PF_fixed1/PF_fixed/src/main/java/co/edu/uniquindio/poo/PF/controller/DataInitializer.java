package co.edu.uniquindio.poo.PF.controller;

import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataInitializer {

    public static void inicializar(UsuarioController usuarioCtrl,
                                   RecintoController recintoCtrl,
                                   EventoController eventoCtrl) {

        usuarioCtrl.registrar("U1", "Carlos Perez",   "carlos@test.com", "1234",  "3001000001");
        usuarioCtrl.registrar("U2", "Laura Gomez",    "laura@test.com",  "5678",  "3001000002");
        usuarioCtrl.registrar("A1", "Admin Sistema",  "admin@test.com",  "admin", "3001000003");
        
        var carlos = usuarioCtrl.buscarPorId("U1");
        if (carlos != null) {
            carlos.agregarMetodoPago("TARJETA_CREDITO|**** **** **** 1234|Mi Visa Principal");
            carlos.agregarMetodoPago("TARJETA_DEBITO|**** **** **** 5678|Mastercard Debito");
        }

        var laura = usuarioCtrl.buscarPorId("U2");
        if (laura != null) {
            laura.agregarMetodoPago("TARJETA_CREDITO|**** **** **** 9012|American Express");
        }

        var admin = usuarioCtrl.buscarPorId("A1");
        if (admin != null) admin.setRol(RolUsuario.ADMINISTRADOR);

        Recinto estadio = new Recinto("R1", "Estadio Centenario", "Cra 5 # 30-00",
                "Armenia", 144, new ArrayList<>());
        recintoCtrl.agregar(estadio);

        List<Zona> zonasE1 = eventoCtrl.construirZonas("E1", true, true, true);
        EventoConcierto concierto = new EventoConcierto(
                "E1", "Concierto Juanes", CategoriaEvento.CONCIERTO, "Gira mundial En Paz.", "Armenia",
                LocalDateTime.now().plusDays(30), EstadoEvento.PUBLICADO, estadio,
                zonasE1, "Juanes", "Pop/Rock");
        eventoCtrl.registrarEvento(concierto);

        List<Zona> zonasE2 = eventoCtrl.construirZonas("E2", false, true, true);
        EventoConferencia conferencia = new EventoConferencia(
                "E2", "Cumbre IA Colombia 2026", CategoriaEvento.CONFERENCIA, "Lideres de tecnologia.", "Armenia",
                LocalDateTime.now().plusDays(15), EstadoEvento.PUBLICADO, estadio,
                zonasE2, "Dr. Juan Rios", "Inteligencia Artificial");
        eventoCtrl.registrarEvento(conferencia);

        List<Zona> zonasE3 = eventoCtrl.construirZonas("E3", true, true, true);
        EventoTeatro teatro = new EventoTeatro(
                "E3", "La Casa de Bernarda Alba", CategoriaEvento.TEATRO, "Clasico de Garcia Lorca.", "Armenia",
                LocalDateTime.now().plusDays(45), EstadoEvento.PUBLICADO, estadio,
                zonasE3, "Teatro Nacional", "Garcia Lorca");
        eventoCtrl.registrarEvento(teatro);

        System.out.println("Datos inicializados correctamente.");
    }
}
