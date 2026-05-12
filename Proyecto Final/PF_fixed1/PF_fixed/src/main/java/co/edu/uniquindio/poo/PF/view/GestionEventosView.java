package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.*;
import co.edu.uniquindio.poo.PF.model.enums.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class GestionEventosView {
    public void show(BorderPane area) {
        ListView<String> lista = new ListView<>();
        actualizarLista(lista);

        TextField tfNombre   = new TextField(); tfNombre.setPromptText("Nombre evento");
        TextField tfCiudad   = new TextField(); tfCiudad.setPromptText("Ciudad");
        TextField tfArtista  = new TextField(); tfArtista.setPromptText("Artista / Ponente");

        DatePicker dpInicio = new DatePicker(); dpInicio.setPromptText("Fecha inicio");
        DatePicker dpFin    = new DatePicker(); dpFin.setPromptText("Fecha fin");

        ComboBox<CategoriaEvento> cbCat = new ComboBox<>();
        cbCat.getItems().addAll(CategoriaEvento.values());
        cbCat.setPromptText("Tipo de evento");

        ComboBox<String> cbRecinto = new ComboBox<>();
        List<Recinto> recintos = MainApp.recintoCtrl.listar();
        recintos.forEach(r -> cbRecinto.getItems().add(r.getIdRecinto() + " | " + r.getNombre()));
        cbRecinto.setPromptText("Selecciona recinto existente");

        CheckBox chkVIP = new CheckBox("VIP");
        CheckBox chkPref = new CheckBox("Pref");
        CheckBox chkGen = new CheckBox("Gen");
        chkVIP.setSelected(true); chkPref.setSelected(true); chkGen.setSelected(true);

        Button btnCrear = new Button("Crear Evento");
        Label lblMsg = new Label();

        btnCrear.setOnAction(e -> {
            if (tfNombre.getText().isBlank() || dpInicio.getValue() == null || cbRecinto.getSelectionModel().isEmpty()) {
                lblMsg.setText("Complete nombre, fecha y recinto.");
                return;
            }
            Recinto r = recintos.get(cbRecinto.getSelectionModel().getSelectedIndex());
            LocalDateTime inicio = dpInicio.getValue().atTime(20, 0);
            LocalDateTime fin    = (dpFin.getValue() != null) ? dpFin.getValue().atTime(23, 0) : inicio.plusHours(4);

            Evento ev = MainApp.eventoCtrl.crearEvento(
                    tfNombre.getText(), "Evento Admin", tfCiudad.getText(), inicio, fin, r,
                    cbCat.getValue(), tfArtista.getText(), chkVIP.isSelected(), chkPref.isSelected(), chkGen.isSelected());

            lblMsg.setText("Evento " + ev.getIdEvento() + " creado.");
            actualizarLista(lista);
        });

        VBox box = new VBox(10, new Label("Gestión de Eventos"), 
                new HBox(5, tfNombre, tfCiudad, tfArtista, cbCat),
                new HBox(5, new Label("Inicio:"), dpInicio, new Label("Fin:"), dpFin),
                new HBox(5, cbRecinto, chkVIP, chkPref, chkGen, btnCrear),
                lista, lblMsg);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private void actualizarLista(ListView<String> lista) {
        lista.getItems().clear();
        MainApp.eventoCtrl.listar().forEach(ev ->
                lista.getItems().add(ev.getIdEvento() + " | " + ev.getNombre() + " | " + ev.getEstado()));
    }
}
