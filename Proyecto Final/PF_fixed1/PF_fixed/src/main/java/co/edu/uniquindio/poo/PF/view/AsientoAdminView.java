package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.domain.Asiento;
import co.edu.uniquindio.poo.PF.model.domain.Evento;
import co.edu.uniquindio.poo.PF.model.domain.Zona;
import co.edu.uniquindio.poo.PF.model.enums.EstadoAsiento;
import co.edu.uniquindio.poo.PF.model.enums.TipoZona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.stream.Collectors;

public class AsientoAdminView {

    private static final String COLOR_VIP          = "#E6B800";
    private static final String COLOR_PREFERENCIAL = "#C2185B";
    private static final String COLOR_GENERAL      = "#7C5CBF";
    private static final String COLOR_BLOQUEADO    = "#E53935";
    private static final String COLOR_VENDIDO      = "#9E9E9E";
    private static final String COLOR_SELECCIONADO = "#1A8FE3";

    private static final double SEAT_SIZE = 26;
    private static final double SEAT_GAP  = 3;
    private static final String[] SECTORES = {"ORI", "CEN", "OCC"};

    private final Set<Asiento> seleccionados = new LinkedHashSet<>();
    private Label lblInfo;
    private VBox mapaContainer;
    private List<Evento> eventos;
    private Evento eventoActual;
    private Zona   zonaActual;

    public void show(BorderPane area) {
        eventos = new ArrayList<>(MainApp.eventoCtrl.listar());

        ComboBox<String> cbEvento = new ComboBox<>();
        eventos.forEach(ev -> cbEvento.getItems().add(ev.getIdEvento() + " | " + ev.getNombre()));
        cbEvento.setPromptText("Selecciona evento");
        cbEvento.setStyle("-fx-pref-width: 260px;");

        ComboBox<String> cbZona = new ComboBox<>();
        cbZona.setPromptText("Selecciona zona");
        cbZona.setStyle("-fx-pref-width: 200px;");

        lblInfo = new Label();
        lblInfo.setWrapText(true);
        lblInfo.setStyle("-fx-font-size: 12px;");

        mapaContainer = new VBox(8);
        mapaContainer.setAlignment(Pos.TOP_CENTER);

        Label lblSeleccion = new Label("Seleccionados: 0 asiento(s)");
        lblSeleccion.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        Button btnHabilitar = new Button("✅  Habilitar (Disponible)");
        Button btnBloquear  = new Button("🚫  Bloquear");
        Button btnLiberar   = new Button("🔓  Liberar Reservado");
        Button btnOcupacion = new Button("📊  Ver ocupación zona");

        btnHabilitar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;" +
                "-fx-background-radius: 5; -fx-padding: 7 14; -fx-font-size: 12px; -fx-cursor: hand;");
        btnBloquear.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;" +
                "-fx-background-radius: 5; -fx-padding: 7 14; -fx-font-size: 12px; -fx-cursor: hand;");
        for (Button b : new Button[]{btnLiberar, btnOcupacion}) {
            b.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;" +
                    "-fx-background-radius: 5; -fx-padding: 7 14; -fx-font-size: 12px; -fx-cursor: hand;");
        }

        HBox botonesAccion = new HBox(8, btnHabilitar, btnBloquear, btnLiberar, btnOcupacion);
        botonesAccion.setAlignment(Pos.CENTER_LEFT);

        cbEvento.setOnAction(e -> {
            int idx = cbEvento.getSelectionModel().getSelectedIndex();
            cbZona.getItems().clear();
            mapaContainer.getChildren().clear();
            seleccionados.clear();
            lblSeleccion.setText("Seleccionados: 0 asiento(s)");
            zonaActual = null;
            if (idx >= 0) {
                eventoActual = eventos.get(idx);
                eventoActual.getZonas().forEach(z ->
                        cbZona.getItems().add(z.getNombre() + " (" + z.getTipoZona() + ")"));
            }
        });

        cbZona.setOnAction(e -> {
            int evIdx   = cbEvento.getSelectionModel().getSelectedIndex();
            int zonaIdx = cbZona.getSelectionModel().getSelectedIndex();
            seleccionados.clear();
            lblSeleccion.setText("Seleccionados: 0 asiento(s)");
            mapaContainer.getChildren().clear();
            if (evIdx >= 0 && zonaIdx >= 0) {
                zonaActual = eventoActual.getZonas().get(zonaIdx);
                mapaContainer.getChildren().add(buildMapaZona(zonaActual, lblSeleccion));
            }
        });

        btnHabilitar.setOnAction(e -> {
            if (seleccionados.isEmpty()) { setInfo("⚠️ Selecciona al menos un asiento.", false); return; }
            long cnt = seleccionados.stream().filter(a -> a.getEstado() != EstadoAsiento.VENDIDO)
                    .peek(a -> a.cambiarEstado(EstadoAsiento.DISPONIBLE)).count();
            setInfo("✅ " + cnt + " asiento(s) habilitados.", true);
            clearAndRefresh(lblSeleccion);
        });

        btnBloquear.setOnAction(e -> {
            if (seleccionados.isEmpty()) { setInfo("⚠️ Selecciona al menos un asiento.", false); return; }
            long cnt = seleccionados.stream().filter(a -> a.getEstado() != EstadoAsiento.VENDIDO)
                    .peek(Asiento::bloquear).count();
            setInfo("🚫 " + cnt + " asiento(s) bloqueados.", true);
            clearAndRefresh(lblSeleccion);
        });

        btnLiberar.setOnAction(e -> {
            if (seleccionados.isEmpty()) { setInfo("⚠️ Selecciona al menos un asiento.", false); return; }
            long cnt = seleccionados.stream().filter(a -> a.getEstado() == EstadoAsiento.RESERVADO)
                    .peek(a -> a.cambiarEstado(EstadoAsiento.DISPONIBLE)).count();
            setInfo("🔓 " + cnt + " asiento(s) liberados (Reservado → Disponible).", true);
            clearAndRefresh(lblSeleccion);
        });

        btnOcupacion.setOnAction(e -> {
            if (zonaActual == null) { setInfo("⚠️ Selecciona una zona primero.", false); return; }
            long disp = zonaActual.getAsientos().stream().filter(a -> a.getEstado() == EstadoAsiento.DISPONIBLE).count();
            long bloq = zonaActual.getAsientos().stream().filter(a -> a.getEstado() == EstadoAsiento.BLOQUEADO).count();
            long vend = zonaActual.getAsientos().stream().filter(a -> a.getEstado() == EstadoAsiento.VENDIDO).count();
            long res  = zonaActual.getAsientos().stream().filter(a -> a.getEstado() == EstadoAsiento.RESERVADO).count();
            setInfo(String.format(
                "📊 %s  |  ✅ Disponibles: %d  |  🚫 Bloqueados: %d  |  🔒 Vendidos: %d  |  🕐 Reservados: %d  |  Ocupación: %d%%",
                zonaActual.getNombre(), disp, bloq, vend, res, zonaActual.consultarOcupacion()), true);
        });

        Label titulo = new Label("🎭 Gestión Visual de Asientos");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label instruccion = new Label("Haz clic en los asientos para seleccionarlos, luego aplica la acción.");
        instruccion.setStyle("-fx-font-size: 12px; -fx-text-fill: #888; -fx-font-style: italic;");

        HBox selectores = new HBox(10, new Label("Evento:"), cbEvento, new Label("Zona:"), cbZona);
        selectores.setAlignment(Pos.CENTER_LEFT);

        ScrollPane scrollMapa = new ScrollPane(mapaContainer);
        scrollMapa.setFitToWidth(true);
        scrollMapa.setPrefHeight(400);
        scrollMapa.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox box = new VBox(10,
                titulo, selectores, instruccion,
                buildLeyenda(),
                scrollMapa,
                botonesAccion, lblSeleccion, lblInfo);
        box.setPadding(new Insets(20));
        area.setCenter(box);
    }

    private VBox buildMapaZona(Zona zona, Label lblSeleccion) {
        StackPane escenario = new StackPane(new Label("🎭 Escenario"));
        escenario.setStyle("-fx-background-color: #E0E0E0; -fx-border-color: #BDBDBD;" +
                "-fx-border-width: 1; -fx-padding: 8 40; -fx-background-radius: 6; -fx-border-radius: 6;");
        escenario.setMaxWidth(280);

        Label lblZona = new Label("Zona: " + zona.getNombre() +
                "   |   Precio base: $" + String.format("%,.0f", zona.getPrecioBase()).replace(",", "."));
        lblZona.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;" +
                "-fx-padding: 6 12; -fx-background-radius: 5; -fx-background-color: " + colorHex(zona) + "22;");

        GridPane grid = new GridPane();
        grid.setHgap(8); grid.setVgap(8);

        String[] sectorLabels = {"◀ Oriental", "▣ Central", "Occidental ▶"};
        for (int si = 0; si < SECTORES.length; si++) {
            Label lh = new Label(sectorLabels[si]);
            lh.setStyle("-fx-font-size: 10px; -fx-text-fill: #888; -fx-font-weight: bold;");
            grid.add(lh, si, 0);
        }
        for (int si = 0; si < SECTORES.length; si++) {
            VBox sectorBox = buildSectorBox(zona, SECTORES[si], lblSeleccion);
            sectorBox.setBorder(new Border(new BorderStroke(
                    Color.web("#DDDDDD"), BorderStrokeStyle.DASHED,
                    new CornerRadii(6), BorderWidths.DEFAULT)));
            sectorBox.setPadding(new Insets(6));
            grid.add(sectorBox, si, 1);
        }

        VBox root = new VBox(10, escenario, lblZona, grid);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private VBox buildSectorBox(Zona zona, String sector, Label lblSeleccion) {
        List<Asiento> asientosSector = zona.getAsientos().stream()
                .filter(a -> a.getIdAsiento().contains("-" + sector + "-"))
                .sorted(Comparator.comparing(Asiento::getFila).thenComparingInt(Asiento::getNumero))
                .collect(Collectors.toList());

        Map<String, List<Asiento>> porFila = new LinkedHashMap<>();
        for (Asiento a : asientosSector) {
            porFila.computeIfAbsent(a.getFila(), k -> new ArrayList<>()).add(a);
        }

        VBox vbox = new VBox(SEAT_GAP);
        vbox.setAlignment(Pos.CENTER);
        for (Map.Entry<String, List<Asiento>> entry : porFila.entrySet()) {
            HBox rowBox = new HBox(SEAT_GAP);
            rowBox.setAlignment(Pos.CENTER);
            Label lblFila = new Label(entry.getKey());
            lblFila.setStyle("-fx-font-size: 9px; -fx-text-fill: #aaa; -fx-min-width: 10px;");
            rowBox.getChildren().add(lblFila);
            for (Asiento asiento : entry.getValue()) {
                rowBox.getChildren().add(buildSeatButton(asiento, zona, lblSeleccion));
            }
            vbox.getChildren().add(rowBox);
        }
        return vbox;
    }

    private Button buildSeatButton(Asiento asiento, Zona zona, Label lblSeleccion) {
        String numStr = String.format("%02d", asiento.getNumero());
        Button btn = new Button(numStr);
        btn.setPrefSize(SEAT_SIZE, SEAT_SIZE);
        btn.setMinSize(SEAT_SIZE, SEAT_SIZE);
        btn.setMaxSize(SEAT_SIZE, SEAT_SIZE);
        btn.setStyle(buildSeatStyle(asiento, zona, false));

        Tooltip tip = new Tooltip("Asiento " + asiento.getFila() + String.format("%02d", asiento.getNumero())
                + "\nEstado: " + asiento.getEstado());
        Tooltip.install(btn, tip);

        if (asiento.getEstado() == EstadoAsiento.VENDIDO) {
            btn.setDisable(true);
            btn.setOpacity(0.5);
        } else {
            btn.setOnAction(e -> {
                if (seleccionados.contains(asiento)) {
                    seleccionados.remove(asiento);
                } else {
                    seleccionados.add(asiento);
                }
                btn.setStyle(buildSeatStyle(asiento, zona, seleccionados.contains(asiento)));
                lblSeleccion.setText("Seleccionados: " + seleccionados.size() + " asiento(s)");
            });
        }
        return btn;
    }

    private String buildSeatStyle(Asiento asiento, Zona zona, boolean seleccionado) {
        String bg = seleccionado ? COLOR_SELECCIONADO : switch (asiento.getEstado()) {
            case DISPONIBLE -> colorHex(zona);
            case BLOQUEADO  -> COLOR_BLOQUEADO;
            case VENDIDO    -> COLOR_VENDIDO;
            case RESERVADO  -> "#FF9800";
            default         -> "#888888";
        };
        return "-fx-background-color: " + bg + "; -fx-text-fill: white; -fx-font-size: 8px;" +
               "-fx-font-weight: bold; -fx-background-radius: 3; -fx-border-radius: 3;" +
               "-fx-border-color: " + (seleccionado ? "#0D47A1" : "transparent") + ";" +
               "-fx-border-width: " + (seleccionado ? "2" : "0") + "; -fx-cursor: hand;";
    }

    private HBox buildLeyenda() {
        HBox leyenda = new HBox(10);
        leyenda.setAlignment(Pos.CENTER_LEFT);
        leyenda.setPadding(new Insets(4, 0, 4, 0));
        leyenda.getChildren().addAll(
            itemLeyenda(COLOR_VIP,          "VIP"),
            itemLeyenda(COLOR_PREFERENCIAL, "Preferencial"),
            itemLeyenda(COLOR_GENERAL,      "General"),
            itemLeyenda(COLOR_SELECCIONADO, "Seleccionado"),
            itemLeyenda(COLOR_BLOQUEADO,    "Bloqueado"),
            itemLeyenda("#FF9800",          "Reservado"),
            itemLeyenda(COLOR_VENDIDO,      "Vendido")
        );
        return leyenda;
    }

    private HBox itemLeyenda(String hex, String texto) {
        Rectangle r = new Rectangle(13, 13);
        r.setArcWidth(3); r.setArcHeight(3);
        r.setFill(Color.web(hex));
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #666;");
        HBox item = new HBox(4, r, lbl);
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private void clearAndRefresh(Label lblSeleccion) {
        seleccionados.clear();
        lblSeleccion.setText("Seleccionados: 0 asiento(s)");
        if (zonaActual != null) {
            mapaContainer.getChildren().clear();
            mapaContainer.getChildren().add(buildMapaZona(zonaActual, lblSeleccion));
        }
    }

    private void setInfo(String msg, boolean ok) {
        lblInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: " + (ok ? "#27ae60" : "#e74c3c") + ";");
        lblInfo.setText(msg);
    }

    private String colorHex(Zona zona) {
        if (zona.getTipoZona() == null) return "#888888";
        return switch (zona.getTipoZona()) {
            case VIP          -> COLOR_VIP;
            case PREFERENCIAL -> COLOR_PREFERENCIAL;
            case GENERAL      -> COLOR_GENERAL;
            default           -> "#888888";
        };
    }
}
