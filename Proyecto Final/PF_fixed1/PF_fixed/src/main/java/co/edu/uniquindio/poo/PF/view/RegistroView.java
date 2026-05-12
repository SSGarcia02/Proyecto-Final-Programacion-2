package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegistroView {
    public void start(Stage stage) {
        stage.setTitle("Registro de Usuario");

        TextField tfNombre   = new TextField(); tfNombre.setPromptText("Nombre completo");
        TextField tfEmail    = new TextField(); tfEmail.setPromptText("Correo electrónico");
        TextField tfTel      = new TextField(); tfTel.setPromptText("Teléfono");
        PasswordField pfPass = new PasswordField(); pfPass.setPromptText("Contraseña");
        Label lblMsg = new Label();

        Button btnRegistrar = new Button("Registrarse");
        Button btnVolver    = new Button("Volver");

        btnRegistrar.setOnAction(e -> {
            String email = tfEmail.getText().trim();
            if (!email.contains("@")) {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("Correo inválido (falta '@').");
                return;
            }

            long siguiente = MainApp.usuarioCtrl.getUsuarios().stream()
                    .filter(u -> u.getIdUsuario().matches("U\\d+"))
                    .mapToLong(u -> Long.parseLong(u.getIdUsuario().substring(1)))
                    .max().orElse(0L) + 1;
            String nuevoId = "U" + siguiente;

            boolean ok = MainApp.usuarioCtrl.registrar(
                    nuevoId,
                    tfNombre.getText(), email,
                    pfPass.getText(), tfTel.getText());
            
            if (ok) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registro exitoso", ButtonType.OK);
                alert.setHeaderText(null);
                alert.showAndWait();
                
                new LoginView().start(stage);
            } else {
                lblMsg.setStyle("-fx-text-fill: red;");
                lblMsg.setText("El correo ya está registrado.");
            }
        });

        btnVolver.setOnAction(e -> new LoginView().start(stage));

        VBox root = new VBox(10, new Label("Registro"),
                tfNombre, tfEmail, tfTel, pfPass, btnRegistrar, btnVolver, lblMsg);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setMaxWidth(350);
        
        Scene scene = new Scene(new StackPane(root), 500, 420);
        MainApp.aplicarEstiloNegro(scene); 
        
        stage.setScene(scene);
        stage.show();
    }
}
