package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioView {
    public void start(Stage stage) {
        stage.setTitle("Portal Usuario - " + MainApp.usuarioCtrl.getUsuarioActual().getNombre());

        Button btnEventos = new Button("Ver Eventos");
        Button btnCompras = new Button("Mis Compras");
        Button btnQuejas   = new Button("Mis Quejas");
        Button btnPerfil   = new Button("Mi Perfil");
        Button btnSalir    = new Button("Cerrar Sesión");

        for (Button b : new Button[]{btnEventos, btnCompras, btnQuejas, btnPerfil, btnSalir}) {
            b.setMaxWidth(Double.MAX_VALUE);
            b.setPrefHeight(40);
        }

        VBox menu = new VBox(10, btnEventos, btnCompras, btnQuejas, btnPerfil, btnSalir);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #ecf0f1;");

        BorderPane contenido = new BorderPane();
        Label lblBienvenido = new Label("¡Bienvenido, " + MainApp.usuarioCtrl.getUsuarioActual().getNombre() + "!");
        lblBienvenido.setStyle("-fx-font-size: 16px; -fx-padding: 20; -fx-text-fill: black;");
        contenido.setCenter(lblBienvenido);

        btnEventos.setOnAction(e -> mostrarEventos(contenido));
        btnCompras.setOnAction(e -> mostrarMisCompras(contenido));
        btnQuejas.setOnAction(e  -> new QuejasView().show(contenido));
        btnPerfil.setOnAction(e  -> new PerfilView().show(contenido));
        btnSalir.setOnAction(e -> new LoginView().start(stage));

        BorderPane root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(contenido);
        Scene scene = new Scene(root, 950, 640);
        MainApp.aplicarEstiloNegro(scene);
        stage.setScene(scene);
        stage.show();
    }

    private void mostrarEventos(BorderPane area) {
        TextField tfCiudad = new TextField();
        tfCiudad.setPromptText("Filtrar ciudad");
        ComboBox<CategoriaEvento> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().add(null);
        cbCategoria.getItems().addAll(CategoriaEvento.values());
        
        ListView<Evento> lista = new ListView<>();
        Label lblDetalle = new Label();
        lblDetalle.setWrapText(true);

        Runnable aplicarFiltros = () -> {
            List<Evento> filtrados = MainApp.eventoCtrl.filtrarActivos(tfCiudad.getText(), cbCategoria.getValue(), null, null);
            lista.getItems().setAll(filtrados);
        };

        tfCiudad.textProperty().addListener((o, a, b) -> aplicarFiltros.run());
        cbCategoria.valueProperty().addListener((o, a, b) -> aplicarFiltros.run());
        aplicarFiltros.run();

        Button btnComprar = new Button("Comprar");
        btnComprar.setOnAction(e -> {
            Evento sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) new CompraView().show(area, sel.getNombre());
        });

        VBox box = new VBox(10, new Label("Eventos Disponibles:"), new HBox(5, tfCiudad, cbCategoria), lista, lblDetalle, btnComprar);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private void mostrarMisCompras(BorderPane area) {
        ListView<Compra> lista = new ListView<>();
        lista.getItems().setAll(MainApp.compraCtrl.listarPorUsuario(MainApp.usuarioCtrl.getUsuarioActual().getIdUsuario()));
        
        Button btnDetalle = new Button("Ver Detalle");
        Button btnPDF = new Button("Descargar Factura PDF");
        Button btnPagar = new Button("Pagar Reserva");
        btnPagar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnPagar.setDisable(true);
        Button btnCancelar = new Button("Cancelar Reserva");
        btnCancelar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: white;");
        btnCancelar.setDisable(true);

        lista.getSelectionModel().selectedItemProperty().addListener((o, a, sel) -> {
            boolean esCreada = sel != null && sel.getEstado() == EstadoCompra.CREADA;
            btnPagar.setDisable(!esCreada);
            btnCancelar.setDisable(!esCreada);
        });
        
        btnDetalle.setOnAction(e -> {
            Compra sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) mostrarDetalleCompra(sel);
        });

        btnPDF.setOnAction(e -> {
            Compra sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) descargarFactura(sel, area);
        });

        btnPagar.setOnAction(e -> {
            Compra sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) {
                ChoiceDialog<TipoMetodoPago> dialog = new ChoiceDialog<>(TipoMetodoPago.PSE, TipoMetodoPago.values());
                dialog.setTitle("Pagar Reserva");
                dialog.setHeaderText("Seleccione el método de pago");
                dialog.setContentText("Método:");
                
                Optional<TipoMetodoPago> result = dialog.showAndWait();
                if (result.isPresent()) {
                    TipoMetodoPago metodo = result.get();
                    Usuario user = MainApp.usuarioCtrl.getUsuarioActual();

                    if (metodo != TipoMetodoPago.EFECTIVO) {
                        List<String> rawMetodos = user.getMetodosPayPago().stream()
                                .filter(s -> s.startsWith(metodo.name() + "|"))
                                .collect(Collectors.toList());

                        if (rawMetodos.isEmpty()) {
                            new Alert(Alert.AlertType.ERROR, "No tienes registrado ningún método de tipo " + metodo + " en tu perfil.").show();
                            return;
                        }
                        
                        List<String> visualMetodos = rawMetodos.stream().map(s -> {
                            String[] p = s.split("\\|");
                            return (p.length >= 3) ? p[2] + " (" + p[1] + ")" : s;
                        }).collect(Collectors.toList());

                        ChoiceDialog<String> selectionDialog = new ChoiceDialog<>(visualMetodos.get(0), visualMetodos);
                        selectionDialog.setTitle("Seleccionar " + metodo);
                        selectionDialog.setHeaderText("Elige uno de tus registros para " + metodo);
                        selectionDialog.setContentText("Opción:");
                        if (selectionDialog.showAndWait().isEmpty()) return;
                    }

                    boolean exito = MainApp.compraCtrl.pagarReserva(sel.getIdCompra(), metodo);
                    if (exito) {
                        new Alert(Alert.AlertType.INFORMATION, "Pago realizado con éxito.").show();
                        mostrarMisCompras(area);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "No se pudo realizar el pago.").show();
                    }
                }
            }
        });

        btnCancelar.setOnAction(e -> {
            Compra sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null) {
                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacion.setTitle("Cancelar Reserva");
                confirmacion.setHeaderText("¿Estás seguro de cancelar la reserva #" + sel.getIdCompra().substring(0, 8) + "?");
                confirmacion.setContentText("Esta acción no se puede deshacer.");

                Optional<ButtonType> resultado = confirmacion.showAndWait();
                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    boolean exito = MainApp.compraCtrl.cancelarReserva(sel.getIdCompra());
                    if (exito) {
                        new Alert(Alert.AlertType.INFORMATION, "Reserva cancelada exitosamente.").show();
                        mostrarMisCompras(area); // Refrescar lista
                    } else {
                        new Alert(Alert.AlertType.ERROR, "No se pudo cancelar la reserva.").show();
                    }
                }
            }
        });

        VBox box = new VBox(10, new Label("Mis Compras:"), lista, new HBox(10, btnDetalle, btnPDF, btnPagar, btnCancelar));
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private void descargarFactura(Compra c, BorderPane area) {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("Factura_" + c.getIdCompra().substring(0,8) + ".pdf");
        File dest = fc.showSaveDialog(area.getScene().getWindow());
        if (dest == null) return;

        try {
            List<String> lineas = new ArrayList<>();
            lineas.add("FACTURA DE VENTA - " + (c.getEvento() != null ? c.getEvento().getNombre() : "Evento"));
            lineas.add("ID: " + c.getIdCompra());
            lineas.add("Cliente: " + c.getUsuario().getNombre());
            lineas.add("Email: " + c.getUsuario().getEmail());
            lineas.add("------------------------------------------");
            lineas.add("DETALLE:");
            for (var ent : c.getEntradas()) {
                lineas.add("- " + ent.getZona().getNombre() + " | Asiento: " + (ent.getAsiento() != null ? ent.getAsiento().getFila() + ent.getAsiento().getNumero() : "N/A"));
            }
            lineas.add("------------------------------------------");
            lineas.add("TOTAL PAGADO: $" + String.format("%.0f", c.getTotal()));
            lineas.add("Estado: " + c.getEstado());
            lineas.add("------------------------------------------");
            lineas.add("Gracias por su compra.");

            byte[] pdf = construirPDFRobusto(lineas);
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(pdf);
            }
            new Alert(Alert.AlertType.INFORMATION, "Factura descargada con éxito.").show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] construirPDFRobusto(List<String> lineas) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        List<Long> offsets = new ArrayList<>();
        buf.write("%PDF-1.4\n".getBytes());
        buf.write("%\u00d3\u00d4\u00c3\u00cf\n".getBytes());
        offsets.add((long) buf.size());
        buf.write("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n".getBytes());
        offsets.add((long) buf.size());
        buf.write("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n".getBytes());
        offsets.add((long) buf.size());
        buf.write("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n".getBytes());
        StringBuilder sb = new StringBuilder();
        sb.append("BT\n/F1 11 Tf\n13 TL\n50 780 Td\n");
        for (String l : lineas) {
            sb.append("(").append(l.replace("(","\\(").replace(")","\\)")).append(") Tj T*\n");
        }
        sb.append("ET\n");
        byte[] stream = sb.toString().getBytes("ISO-8859-1");
        offsets.add((long) buf.size());
        buf.write(("4 0 obj\n<< /Length " + stream.length + " >>\nstream\n").getBytes());
        buf.write(stream);
        buf.write("\nendstream\nendobj\n".getBytes());
        offsets.add((long) buf.size());
        buf.write("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>\nendobj\n".getBytes());
        long xref = (long) buf.size();
        buf.write(("xref\n0 " + (offsets.size() + 1) + "\n0000000000 65535 f \n").getBytes());
        for (long off : offsets) {
            buf.write(String.format(Locale.US, "%010d 00000 n \n", off).getBytes());
        }
        buf.write(("trailer\n<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\n").getBytes());
        buf.write(("startxref\n" + xref + "\n%%EOF\n").getBytes());
        return buf.toByteArray();
    }

    private void mostrarDetalleCompra(Compra compra) {
        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setTitle("Detalle de Compra");
        VBox root = new VBox(10, new Label("ID: " + compra.getIdCompra()), new Label("Evento: " + (compra.getEvento() != null ? compra.getEvento().getNombre() : "N/A")), new Label("Total: $" + String.format("%.0f", compra.getTotal())), new Label("Estado: " + compra.getEstado()));
        root.setPadding(new Insets(20));
        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> modal.close());
        root.getChildren().add(btnCerrar);
        Scene sc = new Scene(root);
        MainApp.aplicarEstiloNegro(sc);
        modal.setScene(sc);
        modal.show();
    }
}
