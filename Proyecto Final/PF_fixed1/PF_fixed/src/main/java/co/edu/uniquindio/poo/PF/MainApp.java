package co.edu.uniquindio.poo.PF;

import co.edu.uniquindio.poo.PF.controller.*;
import co.edu.uniquindio.poo.PF.model.patterns.adapter.*;
import co.edu.uniquindio.poo.PF.model.patterns.builder.CompraBuilderImpl;
import co.edu.uniquindio.poo.PF.model.patterns.facade.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static UsuarioController usuarioCtrl = new UsuarioController();
    public static EventoController eventoCtrl = new EventoController();
    public static RecintoController recintoCtrl = new RecintoController();
    public static CompraController compraCtrl;
    public static IncidenciaController incidenciaCtrl = new IncidenciaController();
    public static ReporteController reporteCtrl = new ReporteController();
    public static MetricasController metricasCtrl;

    @Override
    public void start(Stage stage) throws Exception {
        PasarelaPSE pse = new PasarelaPSE("https://pse.com", "CLI-001");
        AdapterPSE adapterPSE = new AdapterPSE(pse);

        GestorAsientos gestorAsientos = new GestorAsientos();
        GestorEntradas gestorEntradas = new GestorEntradas();
        GestorPagos gestorPagos = new GestorPagos(adapterPSE, new java.util.ArrayList<>());
        NotificadorUsuario notificador = new NotificadorUsuario();

        ServicioCompra servicioCompra = new ServicioCompra(
                new CompraBuilderImpl(), notificador, gestorAsientos, gestorEntradas, gestorPagos);

        compraCtrl = new CompraController(servicioCompra);
        metricasCtrl = new MetricasController(compraCtrl, recintoCtrl);

        DataInitializer.inicializar(usuarioCtrl, recintoCtrl, eventoCtrl);

        co.edu.uniquindio.poo.PF.view.LoginView loginView = new co.edu.uniquindio.poo.PF.view.LoginView();
        loginView.start(stage);
    }

    public static void aplicarEstiloNegro(javafx.scene.Scene scene) {
        scene.getRoot().setStyle("-fx-text-fill: black; -fx-prompt-text-fill: #555;");
        String css = ".label { -fx-text-fill: black; } " +
                ".button { -fx-text-fill: black; } " +
                ".text-field { -fx-text-fill: black; } " +
                ".combo-box .list-cell { -fx-text-fill: black; } " +
                ".list-view .list-cell { -fx-text-fill: black; } " +
                ".text-area { -fx-text-fill: black; } " +
                ".check-box { -fx-text-fill: black; } " +
                ".date-picker { -fx-text-fill: black; } ";

        scene.getStylesheets().add("data:text/css," + css.replace(" ", "%20"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
