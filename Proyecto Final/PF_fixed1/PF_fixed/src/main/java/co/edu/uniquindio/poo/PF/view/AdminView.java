package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.Compra;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.stream.Collectors;

public class AdminView {
    public void start(Stage stage) {
        stage.setTitle("Panel Administrador");

        Button btnUsuarios    = new Button("Usuarios");
        Button btnEventos     = new Button("Eventos");
        Button btnRecintos    = new Button("Recintos");
        Button btnCompras     = new Button("Compras");
        Button btnAsientos    = new Button("Asientos");
        Button btnIncidencias = new Button("Incidencias");
        Button btnReportes    = new Button("Reportes");
        Button btnMetricas    = new Button("Metricas");
        Button btnSalir       = new Button("Salir");

        for (Button b : new Button[]{btnUsuarios, btnEventos, btnRecintos,
                btnCompras, btnAsientos, btnIncidencias, btnReportes, btnMetricas, btnSalir}) {
            b.setMaxWidth(Double.MAX_VALUE);
            b.setPrefHeight(40);
        }

        VBox menu = new VBox(8, new Label("Administracion"),
                btnUsuarios, btnEventos, btnRecintos,
                btnCompras, btnAsientos, btnIncidencias,
                btnReportes, btnMetricas, btnSalir);
        menu.setPadding(new Insets(16));
        menu.setStyle("-fx-background-color: #ecf0f1;");
        menu.setMinWidth(170);

        BorderPane contenido = new BorderPane();
        Label bienvenida = new Label("Panel de Administracion del Sistema");
        bienvenida.setStyle("-fx-font-size: 16px; -fx-padding: 20; -fx-text-fill: black;");
        contenido.setCenter(bienvenida);

        btnUsuarios.setOnAction(e    -> new GestionUsuariosView().show(contenido));
        btnEventos.setOnAction(e     -> new GestionEventosView().show(contenido));
        btnRecintos.setOnAction(e    -> new GestionRecintoView().show(contenido));
        btnCompras.setOnAction(e     -> mostrarCompras(contenido));
        btnAsientos.setOnAction(e    -> new AsientoAdminView().show(contenido));
        btnIncidencias.setOnAction(e -> new IncidenciaView().show(contenido));
        btnReportes.setOnAction(e    -> new ReporteView().show(contenido));
        btnMetricas.setOnAction(e    -> new MetricasView().show(contenido));
        btnSalir.setOnAction(e -> {
            MainApp.usuarioCtrl.cerrarSesion();
            new LoginView().start(stage);
        });

        BorderPane root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(contenido);
        
        Scene scene = new Scene(root, 1000, 660);
        MainApp.aplicarEstiloNegro(scene);
        
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarCompras(BorderPane area) {
        ListView<Compra> lista = new ListView<>();
        lista.getItems().setAll(MainApp.compraCtrl.getCompras());
        
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Compra c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) { setText(null); return; }
                setText(buildResumenCompra(c));
            }
        });

        Button btnRecibo = new Button("Ver Recibo Detallado");
        btnRecibo.setDisable(true);
        lista.getSelectionModel().selectedItemProperty().addListener((o, a, b) -> btnRecibo.setDisable(b == null));

        btnRecibo.setOnAction(e -> {
            Compra sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) mostrarReciboAdmin(sel);
        });

        VBox box = new VBox(10, new Label("Todas las compras del sistema:"), lista, btnRecibo);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private void mostrarReciboAdmin(Compra compra) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Recibo de Pago - Admin");

        TextArea taRecibo = new TextArea(MainApp.compraCtrl.generarComprobante(compra.getIdCompra()));
        taRecibo.setEditable(false);
        taRecibo.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> modal.close());

        VBox root = new VBox(10, new Label("Comprobante de Pago Cliente"), taRecibo, btnCerrar);
        root.setPadding(new Insets(20));
        Scene sc = new Scene(root, 450, 500);
        MainApp.aplicarEstiloNegro(sc);
        modal.setScene(sc);
        modal.show();
    }

    private String buildResumenCompra(Compra c) {
        String usuario = c.getUsuario() != null ? c.getUsuario().getNombre() : "N/A";
        String evento = c.getEvento() != null ? c.getEvento().getNombre() : "N/A";
        String total = String.format("$%,.0f", c.getTotal()).replace(",", ".");
        return "ID: " + c.getIdCompra().substring(0, 8) + " | User: " + usuario + " | Evento: " + evento + " | Total: " + total + " | " + c.getEstado();
    }
}
