package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.Usuario;
import co.edu.uniquindio.poo.PF.model.enums.TipoMetodoPago;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.*;

public class PerfilView {

    public void show(BorderPane area) {
        Usuario u = MainApp.usuarioCtrl.getUsuarioActual();
        if (u == null) {
            area.setCenter(new Label("No hay sesión activa."));
            return;
        }

        Label lblPerfil = new Label("👤 Datos del Perfil");
        lblPerfil.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField tfNombre = new TextField(u.getNombre());
        TextField tfEmail  = new TextField(u.getEmail());
        TextField tfTel    = new TextField(u.getTelefono());
        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Nueva contraseña (dejar vacío para no cambiar)");

        for (TextField tf : new TextField[]{tfNombre, tfEmail, tfTel}) {
            tf.setPrefWidth(280);
        }

        Label lblMsg = new Label();
        lblMsg.setWrapText(true);

        Button btnGuardar = new Button("💾  Guardar cambios de perfil");
        btnGuardar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;" +
                "-fx-padding: 8 18; -fx-background-radius: 5; -fx-cursor: hand;");

        btnGuardar.setOnAction(e -> {
            boolean ok = MainApp.usuarioCtrl.actualizarPerfil(
                    u.getIdUsuario(), tfNombre.getText().trim(),
                    tfEmail.getText().trim(), tfTel.getText().trim());
            if (ok && !tfPass.getText().isBlank()) {
                u.setPassword(tfPass.getText());
                tfPass.clear();
            }
            setMsg(lblMsg, ok ? "✅ Perfil actualizado correctamente." : "❌ Error al actualizar.", ok);
        });

        Label lblId   = new Label("ID: " + u.getIdUsuario());
        Label lblRol  = new Label("Rol: " + u.getRol());
        Label lblFech = new Label("Registro: " + (u.getFechaRegistro() != null
                ? u.getFechaRegistro().toLocalDate().toString() : "—"));
        for (Label l : new Label[]{lblId, lblRol, lblFech}) {
            l.setStyle("-fx-text-fill: #888; -fx-font-size: 11px;");
        }

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(8);
        grid.add(new Label("Nombre:"),    0, 0); grid.add(tfNombre, 1, 0);
        grid.add(new Label("Email:"),     0, 1); grid.add(tfEmail,  1, 1);
        grid.add(new Label("Teléfono:"),  0, 2); grid.add(tfTel,    1, 2);
        grid.add(new Label("Contraseña:"),0, 3); grid.add(tfPass,   1, 3);

        VBox seccionPerfil = new VBox(10, lblPerfil,
                new HBox(16, grid, new VBox(6, lblId, lblRol, lblFech)),
                btnGuardar, lblMsg);
        seccionPerfil.setPadding(new Insets(16));
        seccionPerfil.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0;" +
                "-fx-border-radius: 8; -fx-background-radius: 8;");

        Label lblMetodos = new Label("💳 Mis Métodos de Pago");
        lblMetodos.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox listaMetodos = new VBox(8);
        Label lblMsgPago = new Label();
        lblMsgPago.setWrapText(true);

        List<String> misMetodosStrings = u.getMetodosPayPago();

        Runnable refrescarLista = () -> {
            listaMetodos.getChildren().clear();
            if (misMetodosStrings.isEmpty()) {
                Label vacio = new Label("No tienes métodos de pago registrados.");
                vacio.setStyle("-fx-text-fill: #aaa; -fx-font-style: italic;");
                listaMetodos.getChildren().add(vacio);
            }
            for (int i = 0; i < misMetodosStrings.size(); i++) {
                final int idx = i;
                String data = misMetodosStrings.get(i);
                MetodoPago mp = deserializar(data);
                if (mp != null) {
                    listaMetodos.getChildren().add(buildTarjetaPago(mp, idx, area, lblMsgPago, misMetodosStrings));
                }
            }
        };

        ComboBox<TipoMetodoPago> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoMetodoPago.values());
        cbTipo.setPromptText("Tipo");
        
        TextField tfNumero = new TextField(); tfNumero.setPromptText("Número");
        TextField tfAlias  = new TextField(); tfAlias.setPromptText("Alias (ej: Mi Visa)");

        Button btnAgregar = new Button("Agregar");
        btnAgregar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        btnAgregar.setOnAction(e -> {
            if (cbTipo.getValue() == null || tfNumero.getText().isBlank()) {
                setMsg(lblMsgPago, "⚠️ Datos incompletos.", false); return;
            }
            TipoMetodoPago tipo = cbTipo.getValue();
            String num = tfNumero.getText().trim();
            if ((tipo == TipoMetodoPago.TARJETA_CREDITO || tipo == TipoMetodoPago.TARJETA_DEBITO) && num.length() >= 4) {
                num = "**** **** **** " + num.substring(num.length()-4);
            }
            String alias = tfAlias.getText().isBlank() ? tipo.name() : tfAlias.getText().trim();
            
            u.agregarMetodoPago(tipo.name() + "|" + num + "|" + alias);
            
            cbTipo.setValue(null); tfNumero.clear(); tfAlias.clear();
            setMsg(lblMsgPago, "✅ Agregado.", true);
            refrescarLista.run();
        });

        HBox formAgregar = new HBox(8, cbTipo, tfNumero, tfAlias, btnAgregar);
        VBox seccionPagos = new VBox(10, lblMetodos, listaMetodos, new Separator(), new Label("Añadir nuevo:"), formAgregar, lblMsgPago);
        seccionPagos.setPadding(new Insets(16));
        seccionPagos.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0; -fx-border-radius: 8; -fx-background-radius: 8;");

        refrescarLista.run();

        VBox root = new VBox(16, seccionPerfil, seccionPagos);
        root.setPadding(new Insets(20));
        area.setCenter(new ScrollPane(root));
    }

    private HBox buildTarjetaPago(MetodoPago mp, int idx, BorderPane area, Label lblMsg, List<String> metodos) {
        String icono = switch (mp.tipo) {
            case TARJETA_CREDITO -> "💳"; case TARJETA_DEBITO -> "🏦";
            case PSE -> "🔒"; case EFECTIVO -> "💵";
        };

        Label lblIcono = new Label(icono); lblIcono.setStyle("-fx-font-size: 22px;");
        Label lblAlias = new Label(mp.alias); lblAlias.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
        Label lblNumero = new Label(mp.numeroEnmascarado); lblNumero.setStyle("-fx-text-fill: #888; -fx-font-size: 11px;");

        Button btnEliminar = new Button("✕");
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> {
            metodos.remove(idx);
            show(area); 
        });

        VBox info = new VBox(3, lblAlias, lblNumero);
        HBox card = new HBox(12, lblIcono, info, new Region(), btnEliminar);
        HBox.setHgrow(info, Priority.ALWAYS);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8;");
        return card;
    }

    private void setMsg(Label lbl, String msg, boolean ok) {
        lbl.setStyle("-fx-text-fill: " + (ok ? "#27ae60" : "#e74c3c") + ";");
        lbl.setText(msg);
    }

    private MetodoPago deserializar(String data) {
        try {
            String[] p = data.split("\\|");
            if (p.length >= 3) {
                return new MetodoPago(TipoMetodoPago.valueOf(p[0]), p[1], p[2]);
            }
        } catch (Exception ignored) {}
        return null;
    }

    public static class MetodoPago {
        TipoMetodoPago tipo; String numeroEnmascarado; String alias;
        MetodoPago(TipoMetodoPago t, String n, String a) { tipo=t; numeroEnmascarado=n; alias=a; }
    }
}
