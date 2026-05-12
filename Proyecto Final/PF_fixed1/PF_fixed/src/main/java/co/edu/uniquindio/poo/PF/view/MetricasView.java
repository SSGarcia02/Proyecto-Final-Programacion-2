package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.controller.MetricasController;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MetricasView {

    public void show(BorderPane area) {
        MetricasController ctrl = MainApp.metricasCtrl;

        HBox tarjetas = new HBox(12,
            tarjeta("💰 Ingresos Totales",
                    "$" + formatPesos(ctrl.getTotalVentas()), "#27ae60"),
            tarjeta("🛒 Total Compras",
                    String.valueOf(ctrl.getTotalCompras()), "#2980b9"),
            tarjeta("✅ Confirmadas/Pagadas",
                    String.valueOf(ctrl.getTotalPagadasConfirmadas()), "#8e44ad"),
            tarjeta("❌ Cancelaciones",
                    ctrl.getTotalCanceladas() + "  ("
                    + String.format("%.1f%%", ctrl.getTasaCancelacion()) + ")", "#e74c3c")
        );
        tarjetas.setAlignment(Pos.CENTER_LEFT);

        Label lblTitulo = new Label("📊 Panel de Métricas y Analytics");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox content = new VBox(14,
                lblTitulo, tarjetas, new Separator(),
                new Label("Resumen por estado de compra"),
                buildTablaEstados(ctrl), new Separator(),
                new Label("Ventas por estado (BarChart)"),
                buildBarChartCantidad(ctrl), new Separator(),
                new Label("Ingresos por estado — monto ($)"),
                buildBarChartMonto(ctrl), new Separator(),
                new Label("Distribución de compras (PieChart)"),
                buildPieChart(ctrl), new Separator(),
                new Label("Evolución de ventas — últimos 30 días"),
                buildLineChart(ctrl), new Label(""),
                new Label("Ocupación por zona (BarChart)"),
                buildOcupacionBarChart(ctrl)
        );
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        area.setCenter(scroll);
    }

    private TableView<MetricasController.DatoEstado> buildTablaEstados(MetricasController ctrl) {
        TableView<MetricasController.DatoEstado> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(190);

        long totalComp = ctrl.getTotalCompras();

        TableColumn<MetricasController.DatoEstado, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().estado()));

        TableColumn<MetricasController.DatoEstado, String> colCant = new TableColumn<>("Cantidad");
        colCant.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().cantidad())));

        TableColumn<MetricasController.DatoEstado, String> colMonto = new TableColumn<>("Monto ($)");
        colMonto.setCellValueFactory(d -> new SimpleStringProperty("$" + formatPesos(d.getValue().monto())));

        TableColumn<MetricasController.DatoEstado, String> colPorc = new TableColumn<>("% del total");
        colPorc.setCellValueFactory(d -> new SimpleStringProperty(
                totalComp > 0
                    ? String.format("%.1f%%", d.getValue().cantidad() * 100.0 / totalComp)
                    : "0%"));

        table.getColumns().addAll(colEstado, colCant, colMonto, colPorc);
        table.getItems().addAll(ctrl.getDatosPorEstado());
        return table;
    }

    private BarChart<String, Number> buildBarChartCantidad(MetricasController ctrl) {
        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        chart.setTitle("Cantidad de compras por estado");
        chart.setPrefHeight(260); chart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ctrl.getDatosPorEstado().forEach(d ->
                series.getData().add(new XYChart.Data<>(d.estado(), d.cantidad())));
        chart.getData().add(series);
        return chart;
    }

    private BarChart<String, Number> buildBarChartMonto(MetricasController ctrl) {
        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        chart.setTitle("Ingresos por estado ($)");
        chart.setPrefHeight(260); chart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ctrl.getDatosPorEstado().forEach(d ->
                series.getData().add(new XYChart.Data<>(d.estado(), d.monto())));
        chart.getData().add(series);
        return chart;
    }

    private PieChart buildPieChart(MetricasController ctrl) {
        PieChart chart = new PieChart();
        chart.setTitle("Distribución de compras por estado");
        chart.setPrefHeight(300); chart.setLabelsVisible(true);
        ctrl.getDatosPorEstado().stream().filter(d -> d.cantidad() > 0).forEach(d ->
                chart.getData().add(new PieChart.Data(d.estado() + " (" + d.cantidad() + ")", d.cantidad())));
        if (chart.getData().isEmpty()) chart.getData().add(new PieChart.Data("Sin datos", 1));
        return chart;
    }

    private LineChart<String, Number> buildLineChart(MetricasController ctrl) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        LineChart<String, Number> chart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        chart.setTitle("Evolución de ingresos — últimos 30 días");
        chart.setPrefHeight(300); chart.setCreateSymbols(true);

        XYChart.Series<String, Number> serieIngresos  = new XYChart.Series<>();
        serieIngresos.setName("Ingresos ($)");
        XYChart.Series<String, Number> serieCantidad  = new XYChart.Series<>();
        serieCantidad.setName("Nº compras");

        List<MetricasController.DatoDia> datos = ctrl.getEvolucionUltimos30Dias();
        for (MetricasController.DatoDia d : datos) {
            String lbl = d.dia().format(fmt);
            serieIngresos.getData().add(new XYChart.Data<>(lbl, d.ingresos()));
            serieCantidad.getData().add(new XYChart.Data<>(lbl, d.cantidad()));
        }
        chart.getData().addAll(serieIngresos, serieCantidad);
        return chart;
    }

    private BarChart<String, Number> buildOcupacionBarChart(MetricasController ctrl) {
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        yAxis.setLabel("Ocupación (%)");
        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), yAxis);
        chart.setTitle("Ocupación por zona"); chart.setPrefHeight(280); chart.setLegendVisible(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<MetricasController.DatoOcupacion> datos = ctrl.getOcupacionPorZona();
        if (datos.isEmpty()) series.getData().add(new XYChart.Data<>("Sin recintos", 0));
        else datos.forEach(d -> series.getData().add(new XYChart.Data<>(d.etiqueta(), d.porcentaje())));
        chart.getData().add(series);
        return chart;
    }

    private VBox tarjeta(String titulo, String valor, String color) {
        Label lTitulo = new Label(titulo);
        lTitulo.setStyle("-fx-font-size: 11px; -fx-text-fill: #888;");
        Label lValor = new Label(valor);
        lValor.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        VBox box = new VBox(4, lTitulo, lValor);
        box.setPadding(new Insets(12, 16, 12, 16));
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0;"
                + "-fx-border-radius: 8; -fx-background-radius: 8;"
                + "-fx-effect: dropshadow(gaussian, #ccc, 4, 0, 1, 1);");
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private String formatPesos(double valor) {
        return String.format("%,.0f", valor).replace(",", ".");
    }
}
