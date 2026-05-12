package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.Incidencia;
import co.edu.uniquindio.poo.PF.model.enums.TipoIncidencia;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class IncidenciaView {

    public void show(BorderPane area) {
        ComboBox<TipoIncidencia> cbTipo = new ComboBox<>();
        cbTipo.getItems().add(null);
        cbTipo.getItems().addAll(TipoIncidencia.values());
        cbTipo.setPromptText("Filtrar por tipo");

        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("Todas", "Pendientes", "Resueltas");
        cbEstado.setValue("Todas");

        DatePicker dpDesde = new DatePicker(); dpDesde.setPromptText("Desde");
        DatePicker dpHasta = new DatePicker(); dpHasta.setPromptText("Hasta");

        ListView<Incidencia> lista = new ListView<>();
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Incidencia inc, boolean empty) {
                super.updateItem(inc, empty);
                if (empty || inc == null) { setText(null); return; }
                String estado = inc.isResuelta() ? "✅" : "⚠";
                setText(estado + " " + inc.getIdIncidencia() + " | " + inc.getTipo() + " | " + inc.getEntidadAfectada() + ": " + inc.getIdEntidadAfectada());
            }
        });

        TextArea taDetalle = new TextArea(); taDetalle.setEditable(false);
        TextArea taRespuesta = new TextArea(); taRespuesta.setPromptText("Respuesta del administrador");
        Button btnResolver = new Button("Marcar como resuelta");
        Label lblMsg = new Label();

        lista.getSelectionModel().selectedItemProperty().addListener((obs, old, inc) -> {
            if (inc == null) { taDetalle.clear(); btnResolver.setDisable(true); return; }
            taDetalle.setText("ID: " + inc.getIdIncidencia() + "\nDescripción: " + inc.getDescripcion() + (inc.isResuelta() ? "\n\nRespuesta: " + inc.getRespuestaAdmin() : ""));
            btnResolver.setDisable(inc.isResuelta());
        });

        btnResolver.setOnAction(e -> {
            Incidencia sel = lista.getSelectionModel().getSelectedItem();
            if (sel != null && !taRespuesta.getText().isBlank()) {
                MainApp.incidenciaCtrl.resolver(sel.getIdIncidencia(), taRespuesta.getText());
                cargar(lista, cbTipo, cbEstado, dpDesde, dpHasta);
                taRespuesta.clear();
            }
        });

        Runnable recargar = () -> cargar(lista, cbTipo, cbEstado, dpDesde, dpHasta);
        cbTipo.valueProperty().addListener((o, a, b) -> recargar.run());
        cbEstado.valueProperty().addListener((o, a, b) -> recargar.run());
        recargar.run();

        VBox box = new VBox(10, new Label("Gestión de Incidencias (Admin)"), new HBox(5, cbTipo, cbEstado, dpDesde, dpHasta), lista, new Label("Detalle:"), taDetalle, new Label("Resolver:"), taRespuesta, btnResolver, lblMsg);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private void cargar(ListView<Incidencia> lista, ComboBox<TipoIncidencia> cbTipo, ComboBox<String> cbEstado, DatePicker d1, DatePicker d2) {
        List<Incidencia> filtradas = MainApp.incidenciaCtrl.listar().stream()
                .filter(i -> cbTipo.getValue() == null || i.getTipo().equals(cbTipo.getValue()))
                .filter(i -> {
                    if ("Pendientes".equals(cbEstado.getValue())) return !i.isResuelta();
                    if ("Resueltas".equals(cbEstado.getValue()))  return  i.isResuelta();
                    return true;
                }).collect(Collectors.toList());
        lista.getItems().setAll(filtradas);
    }
}
