package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {

    public void start(Stage stage) {
        stage.setTitle("Plataforma de Eventos - Login");

        Label lblTitulo = new Label("Sistema de Gestión de Eventos");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Correo electrónico");

        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Contraseña");

        Label lblMsg = new Label();
        lblMsg.setStyle("-fx-text-fill: red;");

        Button btnLogin = new Button("Iniciar Sesión");
        Button btnRegistrar = new Button("Registrarse");

        btnLogin.setOnAction(e -> {
            boolean ok = MainApp.usuarioCtrl.iniciarSesion(
                    tfEmail.getText().trim(), pfPass.getText().trim());
            if (ok) {
                var usuario = MainApp.usuarioCtrl.getUsuarioActual();
                if (usuario.isAdmin()) {
                    new AdminView().start(stage);
                } else {
                    new UsuarioView().start(stage);
                }
            } else {
                lblMsg.setText("Credenciales incorrectas.");
            }
        });

        btnRegistrar.setOnAction(e -> new RegistroView().start(stage));

        VBox root = new VBox(12, lblTitulo, tfEmail, pfPass, btnLogin, btnRegistrar, lblMsg);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setMaxWidth(350);

        StackPane container = new StackPane(root);
        container.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(container, 500, 400);
        MainApp.aplicarEstiloNegro(scene);
        
        stage.setScene(scene);
        stage.show();
    }
}
