package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.Incidencia;
import co.edu.uniquindio.poo.PF.model.enums.TipoIncidencia;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class QuejasView {

    public void show(BorderPane area) {
        String idUsuario = MainApp.usuarioCtrl.getUsuarioActual().getIdUsuario();

        Label lblMisQuejas = new Label("Mis quejas e incidencias:");
        lblMisQuejas.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        ListView<Incidencia> listaQuejas = new ListView<>();
        listaQuejas.setPrefHeight(220);
        listaQuejas.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Incidencia inc, boolean empty) {
                super.updateItem(inc, empty);
                if (empty || inc == null) { setText(null); setStyle(""); return; }
                String estado = inc.isResuelta() ? "✅ RESUELTA" : "⏳ EN REVISIÓN";
                setText("[" + estado + "] " + inc.getFecha().toLocalDate()
                        + " | " + inc.getTipo()
                        + " | " + inc.getDescripcion().substring(0, Math.min(50, inc.getDescripcion().length()))
                        + (inc.getDescripcion().length() > 50 ? "..." : ""));
                setStyle(inc.isResuelta()
                        ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
                        : "-fx-text-fill: #e67e22;");
            }
        });

        Label lblDetalleTit = new Label("Detalle de la queja seleccionada:");
        lblDetalleTit.setStyle("-fx-font-weight: bold;");

        TextArea taDetalle = new TextArea();
        taDetalle.setEditable(false);
        taDetalle.setPrefRowCount(5);
        taDetalle.setWrapText(true);
        taDetalle.setPromptText("Selecciona una queja para ver su detalle y la respuesta del administrador.");

        listaQuejas.getSelectionModel().selectedItemProperty().addListener((obs, old, inc) -> {
            if (inc == null) { taDetalle.clear(); return; }
            StringBuilder sb = new StringBuilder();
            sb.append("Tipo: ").append(inc.getTipo()).append("\n");
            sb.append("Fecha: ").append(inc.getFecha()).append("\n");
            sb.append("Descripción: ").append(inc.getDescripcion()).append("\n");
            sb.append("\n");
            if (inc.isResuelta()) {
                sb.append("──────────────────────────────────────\n");
                sb.append("✅ RESUELTA el ").append(inc.getFechaResolucion()).append("\n");
                sb.append("──────────────────────────────────────\n");
                sb.append("Respuesta del administrador:\n");
                sb.append(inc.getRespuestaAdmin() != null ? inc.getRespuestaAdmin() : "Sin comentarios adicionales.");
            } else {
                sb.append("──────────────────────────────────────\n");
                sb.append("⏳ Tu queja está siendo revisada por el administrador.\n");
                sb.append("   Te notificaremos cuando sea resuelta.\n");
            }
            taDetalle.setText(sb.toString());
        });

        Button btnRecargar = new Button("↻ Actualizar estado");
        btnRecargar.setOnAction(e -> cargarQuejas(listaQuejas, idUsuario));

        Label lblNuevaQueja = new Label("Reportar nueva queja:");
        lblNuevaQueja.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 0 4 0;");

        ComboBox<TipoIncidencia> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoIncidencia.values());
        cbTipo.setPromptText("Tipo de problema");

        TextField tfEntidad = new TextField();
        tfEntidad.setPromptText("¿A qué se refiere? (ej: Compra, Evento, Asiento)");

        TextField tfIdEntidad = new TextField();
        tfIdEntidad.setPromptText("ID del elemento afectado (opcional)");

        TextArea taDesc = new TextArea();
        taDesc.setPromptText("Describe detalladamente tu queja o problema...");
        taDesc.setPrefRowCount(4);
        taDesc.setWrapText(true);

        Label lblMsg = new Label();

        Button btnEnviar = new Button("📩 Enviar queja");
        btnEnviar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; "
                + "-fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 6;");

        btnEnviar.setOnAction(e -> {
            if (cbTipo.getValue() == null) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Selecciona el tipo de problema.");
                return;
            }
            if (taDesc.getText().isBlank()) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("La descripción no puede estar vacía.");
                return;
            }
            MainApp.incidenciaCtrl.reportarPorUsuario(
                    cbTipo.getValue(),
                    taDesc.getText(),
                    tfEntidad.getText().isBlank() ? "General" : tfEntidad.getText(),
                    tfIdEntidad.getText(),
                    idUsuario);
            lblMsg.setStyle("-fx-text-fill: #27ae60;");
            lblMsg.setText("✅ Queja enviada correctamente. El administrador la revisará pronto.");
            cbTipo.setValue(null);
            tfEntidad.clear();
            tfIdEntidad.clear();
            taDesc.clear();
            cargarQuejas(listaQuejas, idUsuario);
        });

        cargarQuejas(listaQuejas, idUsuario);

        VBox box = new VBox(8,
                lblMisQuejas,
                listaQuejas,
                btnRecargar,
                lblDetalleTit,
                taDetalle,
                new Separator(),
                lblNuevaQueja,
                cbTipo,
                tfEntidad,
                tfIdEntidad,
                taDesc,
                btnEnviar,
                lblMsg
        );
        box.setPadding(new Insets(20));
        area.setCenter(new ScrollPane(box));
    }

    private void cargarQuejas(ListView<Incidencia> lista, String idUsuario) {
        List<Incidencia> mis = MainApp.incidenciaCtrl.listarPorUsuario(idUsuario);
        lista.getItems().setAll(mis);
        if (mis.isEmpty()) {
            lista.setPlaceholder(new Label("No tienes quejas registradas."));
        }
    }
}
