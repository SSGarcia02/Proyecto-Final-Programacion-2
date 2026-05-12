package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GestionUsuariosView {
    public void show(BorderPane area) {
        ListView<String> lista = new ListView<>();
        actualizarLista(lista);

        Button btnEliminar = new Button("Eliminar seleccionado");
        Label lblMsg = new Label();

        btnEliminar.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                String id = MainApp.usuarioCtrl.listar().get(idx).getIdUsuario();
                MainApp.usuarioCtrl.eliminar(id);
                lblMsg.setText("Usuario eliminado.");
                actualizarLista(lista);
            } else {
                lblMsg.setText("Selecciona un usuario para eliminar.");
            }
        });

        VBox box = new VBox(10,
                new Label("Clientes registrados:"),
                lista,
                btnEliminar,
                lblMsg);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private void actualizarLista(ListView<String> lista) {
        lista.getItems().clear();
        MainApp.usuarioCtrl.listar().forEach(u ->
                lista.getItems().add(
                        u.getIdUsuario() + " | " + u.getNombre()
                        + " | " + u.getEmail() + " | " + u.getRol()));
    }
}
