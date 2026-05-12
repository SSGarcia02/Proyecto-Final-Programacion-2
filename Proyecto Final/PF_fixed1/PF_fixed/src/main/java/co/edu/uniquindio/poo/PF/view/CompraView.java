package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import co.edu.uniquindio.poo.PF.model.patterns.facade.CompraDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompraView {

    public void show(BorderPane area, String nombreEvento) {
        Evento evento = MainApp.eventoCtrl.activos().stream()
                .filter(e -> e.getNombre().equals(nombreEvento))
                .findFirst()
                .orElse(null);

        if (evento == null) {
            area.setCenter(new Label("Evento no disponible."));
            return;
        }

        MapaAsientosView mapa = new MapaAsientosView(evento);
        mapa.setOnConfirmar(asientos -> mostrarOpcionesConfirmacion(area, evento, asientos));
        area.setCenter(mapa.build());
    }

    private void mostrarOpcionesConfirmacion(BorderPane area, Evento evento, List<Asiento> asientos) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirmar Selección");

        Label lbl = new Label("Has seleccionado " + asientos.size() + " asientos.\n¿Qué deseas hacer?");
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

        Button btnReservar = new Button("Reservar Asientos");
        Button btnComprar  = new Button("Comprar Directamente");
        Button btnCancelar = new Button("Cancelar");

        btnReservar.setOnAction(e -> {
            try {
                CompraDTO dto = new CompraDTO(MainApp.usuarioCtrl.getUsuarioActual(), evento, null, null, new ArrayList<>(), null);
                Compra reserva = MainApp.compraCtrl.reservarMultiAsiento(dto, asientos);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Reserva exitosa: #" + reserva.getIdCompra().substring(0, 8));
                alert.showAndWait();
                dialog.close();
                show(area, evento.getNombre());
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al reservar: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        btnComprar.setOnAction(e -> {
            dialog.close();
            mostrarDialogoServicios(area, evento, asientos);
        });

        btnCancelar.setOnAction(e -> dialog.close());

        VBox root = new VBox(15, lbl, btnReservar, btnComprar, btnCancelar);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        
        Scene sc = new Scene(root, 300, 250);
        MainApp.aplicarEstiloNegro(sc);
        dialog.setScene(sc);
        dialog.show();
    }

    private void mostrarDialogoServicios(BorderPane area, Evento evento, List<Asiento> asientos) {
        if (asientos.isEmpty()) return;

        Label lblTitulo = new Label("Finalizar compra");
        lblTitulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

        VBox listaAsientos = new VBox(4);
        for (Asiento a : asientos) {
            double precio = a.getZona() != null ? a.getZona().getPrecioBase() : 0;
            listaAsientos.getChildren().add(new Label("  - Asiento " + a.getFila() + a.getNumero() + " → $" + formatPesos(precio)));
        }

        CheckBox chkVIP     = new CheckBox("Servicio VIP");
        CheckBox chkSeguro  = new CheckBox("Seguro Cancelación");
        CheckBox chkParking = new CheckBox("Parqueadero");
        CheckBox chkMerch   = new CheckBox("Merchandising");
        CheckBox chkPref    = new CheckBox("Acceso Preferencial");

        ComboBox<TipoMetodoPago> cbPago = new ComboBox<>();
        cbPago.getItems().addAll(TipoMetodoPago.values());
        cbPago.setValue(TipoMetodoPago.PSE);

        Label lblTotal = new Label("Total: $0");
        lblTotal.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");
        Label lblDesc  = new Label("");
        lblDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #333;");
        Label lblMsg   = new Label();

        Runnable recalcular = () -> {
            double baseTotal = asientos.stream()
                    .mapToDouble(a -> a.getZona() != null ? a.getZona().getPrecioBase() : 0)
                    .sum();
            List<TipoServicio> servicios = new ArrayList<>();
            if (chkVIP.isSelected())     servicios.add(TipoServicio.VIP);
            if (chkSeguro.isSelected())  servicios.add(TipoServicio.SEGURO_CANCELACION);
            if (chkParking.isSelected()) servicios.add(TipoServicio.PARQUEADERO);
            if (chkMerch.isSelected())   servicios.add(TipoServicio.MERCHANDISING);
            if (chkPref.isSelected())    servicios.add(TipoServicio.ACCESO_PREFERENCIAL);

            double totalDecorado = MainApp.compraCtrl.calcularPrecioDecorado(baseTotal, servicios);
            String descDecorada = MainApp.compraCtrl.obtenerDescripcionDecorada(baseTotal, servicios);

            lblTotal.setText("Total: $" + formatPesos(totalDecorado));
            lblDesc.setText(descDecorada);
        };

        chkVIP.setOnAction(e -> recalcular.run());
        chkSeguro.setOnAction(e -> recalcular.run());
        chkParking.setOnAction(e -> recalcular.run());
        chkMerch.setOnAction(e -> recalcular.run());
        chkPref.setOnAction(e -> recalcular.run());
        recalcular.run();

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> show(area, evento.getNombre()));
        Button btnPagar  = new Button("Realizar Compra");
        btnPagar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white;");

        btnPagar.setOnAction(e -> {
            Usuario user = MainApp.usuarioCtrl.getUsuarioActual();
            TipoMetodoPago metodo = cbPago.getValue();
            
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

            List<TipoServicio> servicios = new ArrayList<>();
            if (chkVIP.isSelected())     servicios.add(TipoServicio.VIP);
            if (chkSeguro.isSelected())  servicios.add(TipoServicio.SEGURO_CANCELACION);
            if (chkParking.isSelected()) servicios.add(TipoServicio.PARQUEADERO);
            if (chkMerch.isSelected())   servicios.add(TipoServicio.MERCHANDISING);
            if (chkPref.isSelected())    servicios.add(TipoServicio.ACCESO_PREFERENCIAL);

            CompraDTO dto = new CompraDTO(user, evento, null, null, servicios, metodo);
            try {
                Compra compra = MainApp.compraCtrl.crearCompraMultiAsiento(dto, asientos);
                lblMsg.setText("✅ Compra exitosa #" + compra.getIdCompra().substring(0, 8));
                btnPagar.setDisable(true);
            } catch (Exception ex) {
                lblMsg.setText("❌ Error: " + ex.getMessage());
            }
        });

        VBox box = new VBox(10, lblTitulo, listaAsientos, chkVIP, chkSeguro, chkParking, chkMerch, chkPref, new Label("Método de pago:"), cbPago, lblTotal, lblDesc, new HBox(10, btnVolver, btnPagar), lblMsg);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private String formatPesos(double valor) {
        return String.format("%,.0f", valor).replace(",", ".");
    }
}
