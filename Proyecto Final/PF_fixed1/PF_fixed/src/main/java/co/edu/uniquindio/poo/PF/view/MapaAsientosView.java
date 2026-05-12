package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.model.domain.Asiento;
import co.edu.uniquindio.poo.PF.model.domain.Evento;
import co.edu.uniquindio.poo.PF.model.domain.Zona;
import co.edu.uniquindio.poo.PF.model.enums.EstadoAsiento;
import co.edu.uniquindio.poo.PF.model.enums.TipoZona;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.function.Consumer;

public class MapaAsientosView {

    private static final Color COLOR_VIP         = Color.web("#E6B800");
    private static final Color COLOR_PREFERENCIAL = Color.web("#C2185B");
    private static final Color COLOR_GENERAL      = Color.web("#7C5CBF");
    private static final Color COLOR_SELECCIONADO = Color.web("#1A8FE3");
    private static final Color COLOR_OCUPADO      = Color.web("#9E9E9E");

    private static final double SEAT_SIZE = 26;
    private static final double SEAT_GAP  = 3;

    private static final String[] SECTORES       = {"ORI", "CEN", "OCC"};
    private static final String[] SECTOR_LABELS  = {"Oriental", "Central", "Occidental"};

    private final Evento evento;
    private final Set<Asiento> seleccionados = new LinkedHashSet<>();
    private Consumer<List<Asiento>> onConfirmar;
    private boolean modoLectura = false;

    private Label lblConteo;
    private Label lblTotal;
    private Button btnConfirmar;

    public MapaAsientosView(Evento evento) {
        this.evento = evento;
    }

    public void setOnConfirmar(Consumer<List<Asiento>> callback) {
        this.onConfirmar = callback;
    }

    public void setModoLectura(boolean modoLectura) {
        this.modoLectura = modoLectura;
    }

    public ScrollPane build() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));
        root.getChildren().add(buildHeader());
        root.getChildren().add(buildEscenario());
        root.getChildren().add(buildMapa());
        root.getChildren().add(buildLeyenda());
        if (!modoLectura) {
            root.getChildren().add(buildPanelConfirmacion());
        }
        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        return scroll;
    }

    private HBox buildHeader() {
        Label lblRecinto = new Label("Recinto: ");
        Label lblRecintoVal = new Label(
                evento.getRecinto() != null ? evento.getRecinto().getNombre() : "—");
        lblRecintoVal.setStyle("-fx-font-weight: bold;");

        Label lblEvento = new Label("Evento: ");
        Label lblEventoVal = new Label(evento.getNombre());
        lblEventoVal.setStyle("-fx-font-weight: bold;");

        HBox left  = new HBox(4, lblRecinto, lblRecintoVal);
        HBox right = new HBox(4, lblEvento, lblEventoVal);
        left.setAlignment(Pos.CENTER_LEFT);
        right.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(left, right);
        HBox.setHgrow(left,  Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        header.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");
        return header;
    }

    private StackPane buildEscenario() {
        Rectangle rect = new Rectangle(260, 38);
        rect.setArcWidth(10); rect.setArcHeight(10);
        rect.setFill(Color.web("#E0E0E0"));
        rect.setStroke(Color.web("#BDBDBD"));
        rect.setStrokeWidth(0.8);

        Label lbl = new Label("Escenario");
        lbl.setStyle("-fx-text-fill: #666; -fx-font-size: 13px;");

        StackPane stage = new StackPane(rect, lbl);
        stage.setMaxWidth(Double.MAX_VALUE);
        StackPane.setAlignment(lbl, Pos.CENTER);
        return stage;
    }

    private GridPane buildMapa() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(8, 0, 8, 0));

        ColumnConstraints labelCol = new ColumnConstraints(110);
        labelCol.setHalignment(javafx.geometry.HPos.RIGHT);
        grid.getColumnConstraints().add(labelCol);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setHgrow(Priority.ALWAYS);
            c.setFillWidth(true);
            grid.getColumnConstraints().add(c);
        }

        for (int s = 0; s < SECTORES.length; s++) {
            Label lbl = new Label(SECTOR_LABELS[s]);
            lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
            lbl.setMaxWidth(Double.MAX_VALUE);
            lbl.setAlignment(Pos.CENTER);
            grid.add(lbl, s + 1, 0);
        }

        List<Zona> zonas = getZonasOrdenadas();
        for (int zi = 0; zi < zonas.size(); zi++) {
            Zona zona = zonas.get(zi);
            int gridRow = zi + 1;

            Label lblZona = new Label(zonaNombreCorto(zona));
            lblZona.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: "
                    + colorHex(zona) + ";");
            lblZona.setAlignment(Pos.CENTER_RIGHT);
            lblZona.setMaxWidth(Double.MAX_VALUE);
            grid.add(lblZona, 0, gridRow);

            for (int si = 0; si < SECTORES.length; si++) {
                String sector = SECTORES[si];
                VBox sectorBox = buildSectorBox(zona, sector);
                sectorBox.setBorder(new Border(new BorderStroke(
                        Color.web("#DDDDDD"), BorderStrokeStyle.DASHED,
                        new CornerRadii(6), BorderWidths.DEFAULT)));
                sectorBox.setPadding(new Insets(6));
                grid.add(sectorBox, si + 1, gridRow);
            }
        }

        return grid;
    }

    private VBox buildSectorBox(Zona zona, String sector) {
        List<Asiento> asientosSector = zona.getAsientos().stream()
                .filter(a -> a.getIdAsiento().contains("-" + sector + "-"))
                .sorted(Comparator.comparing(Asiento::getFila)
                        .thenComparingInt(Asiento::getNumero))
                .toList();

        Map<String, List<Asiento>> porFila = new LinkedHashMap<>();
        for (Asiento a : asientosSector) {
            porFila.computeIfAbsent(a.getFila(), k -> new ArrayList<>()).add(a);
        }

        VBox vbox = new VBox(SEAT_GAP);
        vbox.setAlignment(Pos.CENTER);

        for (List<Asiento> fila : porFila.values()) {
            HBox rowBox = new HBox(SEAT_GAP);
            rowBox.setAlignment(Pos.CENTER);
            for (Asiento asiento : fila) {
                rowBox.getChildren().add(buildSeatButton(asiento, zona));
            }
            vbox.getChildren().add(rowBox);
        }

        return vbox;
    }

    private Button buildSeatButton(Asiento asiento, Zona zona) {
        boolean ocupado = asiento.getEstado() != EstadoAsiento.DISPONIBLE;
        boolean selec   = seleccionados.contains(asiento);

        String numStr = String.format("%02d", asiento.getNumero());
        Button btn = new Button(numStr);
        btn.setPrefSize(SEAT_SIZE, SEAT_SIZE);
        btn.setMinSize(SEAT_SIZE, SEAT_SIZE);
        btn.setMaxSize(SEAT_SIZE, SEAT_SIZE);
        btn.setStyle(buildSeatStyle(zona, ocupado, selec));

        if (!modoLectura && !ocupado) {
            btn.setOnAction(e -> {
                if (seleccionados.contains(asiento)) {
                    seleccionados.remove(asiento);
                } else {
                    seleccionados.add(asiento);
                }
                btn.setStyle(buildSeatStyle(zona, false, seleccionados.contains(asiento)));
                actualizarResumen();
            });
        } else {
            if (ocupado) {
                btn.setDisable(true);
                btn.setOpacity(0.45);
            }
        }

        return btn;
    }

    private String buildSeatStyle(Zona zona, boolean ocupado, boolean selec) {
        String bg = selec   ? "#1A8FE3"
                : ocupado   ? "#9E9E9E"
                : colorHex(zona);
        return "-fx-background-color: " + bg + ";"
             + "-fx-text-fill: white;"
             + "-fx-font-size: 8px;"
             + "-fx-font-weight: bold;"
             + "-fx-background-radius: 3;"
             + "-fx-border-radius: 3;"
             + "-fx-border-color: " + (selec ? "#0D47A1" : "transparent") + ";"
             + "-fx-border-width: " + (selec ? "2" : "0") + ";"
             + "-fx-cursor: hand;";
    }

    private HBox buildLeyenda() {
        HBox leyenda = new HBox(16);
        leyenda.setAlignment(Pos.CENTER_LEFT);
        leyenda.setPadding(new Insets(6, 0, 0, 0));

        leyenda.getChildren().addAll(
                itemLeyenda("#E6B800",  "VIP"),
                itemLeyenda("#C2185B",  "Preferencial"),
                itemLeyenda("#7C5CBF",  "General"),
                itemLeyenda("#1A8FE3",  "Seleccionado"),
                itemLeyendaOcupado()
        );
        return leyenda;
    }

    private HBox itemLeyenda(String hex, String texto) {
        Rectangle r = new Rectangle(14, 14);
        r.setArcWidth(3); r.setArcHeight(3);
        r.setFill(Color.web(hex));
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        HBox item = new HBox(5, r, lbl);
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private HBox itemLeyendaOcupado() {
        Rectangle r = new Rectangle(14, 14);
        r.setArcWidth(3); r.setArcHeight(3);
        r.setFill(Color.web("#9E9E9E"));
        r.setOpacity(0.45);
        Label lbl = new Label("Ocupado");
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        HBox item = new HBox(5, r, lbl);
        item.setAlignment(Pos.CENTER_LEFT);
        return item;
    }

    private VBox buildPanelConfirmacion() {
        lblConteo = new Label("0 asientos seleccionados");
        lblConteo.setStyle("-fx-font-size: 13px; -fx-text-fill: #444;");

        lblTotal = new Label("Total: $0");
        lblTotal.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        btnConfirmar = new Button("Confirmar seleccion");
        btnConfirmar.setDisable(true);
        btnConfirmar.setMaxWidth(Double.MAX_VALUE);
        btnConfirmar.setStyle(
                "-fx-background-color: #2E7D32; -fx-text-fill: white;"
              + "-fx-font-size: 14px; -fx-background-radius: 6; -fx-padding: 10 20;");
        btnConfirmar.setOnAction(e -> {
            if (onConfirmar != null) {
                onConfirmar.accept(new ArrayList<>(seleccionados));
            }
        });

        VBox panel = new VBox(8, new SeparatorLine(), lblConteo, lblTotal, btnConfirmar);
        panel.setPadding(new Insets(10, 0, 0, 0));
        return panel;
    }

    private void actualizarResumen() {
        int n = seleccionados.size();
        lblConteo.setText(n + (n == 1 ? " asiento seleccionado" : " asientos seleccionados"));

        double total = seleccionados.stream()
                .mapToDouble(a -> a.getZona() != null ? a.getZona().getPrecioBase() : 0)
                .sum();
        lblTotal.setText("Total: $" + String.format("%,.0f", total).replace(",", "."));

        btnConfirmar.setDisable(n == 0);
    }

    private List<Zona> getZonasOrdenadas() {
        List<Zona> zonas = new ArrayList<>(evento.getZonas());
        List<TipoZona> orden = List.of(TipoZona.VIP, TipoZona.PREFERENCIAL, TipoZona.GENERAL);
        zonas.sort(Comparator.comparingInt(z -> {
            int i = orden.indexOf(z.getTipoZona());
            return i < 0 ? 99 : i;
        }));
        return zonas;
    }

    private String colorHex(Zona zona) {
        if (zona.getTipoZona() == null) return "#888888";
        return switch (zona.getTipoZona()) {
            case VIP          -> "#E6B800";
            case PREFERENCIAL -> "#C2185B";
            case GENERAL      -> "#7C5CBF";
            default           -> "#888888";
        };
    }

    private String zonaNombreCorto(Zona zona) {
        if (zona.getTipoZona() == null) return zona.getNombre();
        return switch (zona.getTipoZona()) {
            case VIP          -> "VIP";
            case PREFERENCIAL -> "Preferencial";
            case GENERAL      -> "General";
            default           -> zona.getNombre();
        };
    }

    private static class SeparatorLine extends Region {
        SeparatorLine() {
            setStyle("-fx-background-color: #E0E0E0;");
            setPrefHeight(1);
            setMaxWidth(Double.MAX_VALUE);
        }
    }
}
