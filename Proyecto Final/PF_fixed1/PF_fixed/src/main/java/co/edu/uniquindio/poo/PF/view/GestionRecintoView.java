package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.Recinto;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class GestionRecintoView {
    public void show(BorderPane area) {
        ListView<String> lista = new ListView<>();
        actualizarLista(lista);

        TextField tfNombre  = new TextField(); tfNombre.setPromptText("Nombre");
        TextField tfDir     = new TextField(); tfDir.setPromptText("Dirección");
        TextField tfCiudad  = new TextField(); tfCiudad.setPromptText("Ciudad");
        TextField tfAforo   = new TextField(); tfAforo.setPromptText("Aforo");

        Label lblMsg   = new Label();
        Button btnAgregar  = new Button("Agregar");
        Button btnEliminar = new Button("Eliminar");

        btnAgregar.setOnAction(e -> {
            if (tfNombre.getText().isBlank()) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("El nombre es obligatorio.");
                return;
            }
            try {
                String nuevoId = MainApp.recintoCtrl.generarId();
                Recinto r = new Recinto(nuevoId,
                        tfNombre.getText(), tfDir.getText(),
                        tfCiudad.getText().isBlank() ? "Armenia" : tfCiudad.getText(),
                        Integer.parseInt(tfAforo.getText()), new ArrayList<>());
                MainApp.recintoCtrl.agregar(r);
                lblMsg.setStyle("-fx-text-fill: green;");
                lblMsg.setText("Recinto " + nuevoId + " creado correctamente.");
                tfNombre.clear(); tfDir.clear(); tfCiudad.clear(); tfAforo.clear();
                actualizarLista(lista);
            } catch (NumberFormatException ex) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Aforo debe ser un número entero.");
            }
        });

        btnEliminar.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                String id = MainApp.recintoCtrl.listar().get(idx).getIdRecinto();
                MainApp.recintoCtrl.eliminar(id);
                lblMsg.setStyle("-fx-text-fill: green;");
                lblMsg.setText("Recinto " + id + " eliminado.");
                actualizarLista(lista);
            } else {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Selecciona un recinto para eliminar.");
            }
        });

        HBox form = new HBox(6, tfNombre, tfDir, tfCiudad, tfAforo, btnAgregar);
        VBox box = new VBox(10,
                new Label("Gestión de Recintos"),
                form, lista, btnEliminar, lblMsg);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private void actualizarLista(ListView<String> lista) {
        lista.getItems().clear();
        MainApp.recintoCtrl.listar().forEach(r ->
                lista.getItems().add(r.getIdRecinto() + " | " + r.getNombre()
                        + " | " + r.getCiudad() + " | Aforo: " + r.getAforo()));
    }
}
