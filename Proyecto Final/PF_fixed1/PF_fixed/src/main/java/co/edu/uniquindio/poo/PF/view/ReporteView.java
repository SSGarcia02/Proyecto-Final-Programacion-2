package co.edu.uniquindio.poo.PF.view;

import co.edu.uniquindio.poo.PF.MainApp;
import co.edu.uniquindio.poo.PF.model.enums.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReporteView {

    public void show(BorderPane area) {
        ComboBox<TipoReporte> cbTipo = new ComboBox<>();
        ComboBox<FormatoReporte> cbFormato = new ComboBox<>();
        cbTipo.getItems().addAll(TipoReporte.values());
        cbFormato.getItems().addAll(FormatoReporte.values());
        cbTipo.setValue(TipoReporte.VENTAS_PERIODO);
        cbFormato.setValue(FormatoReporte.PDF);

        DatePicker dpDesde = new DatePicker();
        DatePicker dpHasta = new DatePicker();
        Label lblMsg = new Label();

        Button btnGenerar = new Button("⬇ Generar y guardar reporte");
        btnGenerar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 10;");

        btnGenerar.setOnAction(e -> {
            LocalDateTime inicio = (dpDesde.getValue() != null) ? dpDesde.getValue().atStartOfDay() : LocalDateTime.now().minusDays(30);
            LocalDateTime fin = (dpHasta.getValue() != null) ? dpHasta.getValue().atTime(23, 59) : LocalDateTime.now();
            
            FileChooser fc = new FileChooser();
            fc.setInitialFileName(cbTipo.getValue().name().toLowerCase() + ".pdf");
            File dest = fc.showSaveDialog(area.getScene().getWindow());
            if (dest == null) return;

            try {
                byte[] pdfBytes = generarPDF(cbTipo.getValue(), inicio, fin, MainApp.usuarioCtrl.getUsuarioActual().getNombre());
                try (FileOutputStream fos = new FileOutputStream(dest)) {
                    fos.write(pdfBytes);
                }
                lblMsg.setText("✅ Reporte guardado: " + dest.getName());
            } catch (Exception ex) {
                lblMsg.setText("❌ Error: " + ex.getMessage());
            }
        });

        VBox box = new VBox(15, new Label("📊 Reportes Administrativos"), cbTipo, new HBox(10, dpDesde, dpHasta), btnGenerar, lblMsg);
        box.setPadding(new Insets(25));
        area.setCenter(box);
    }

    private byte[] generarPDF(TipoReporte tipo, LocalDateTime inicio, LocalDateTime fin, String por) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("REPORTE DE SISTEMA - " + tipo.name());
        lineas.add("Generado por: " + por);
        lineas.add("Periodo: " + inicio.toLocalDate() + " a " + fin.toLocalDate());
        lineas.add("----------------------------------------------------------");
        
        if (tipo == TipoReporte.VENTAS_PERIODO) {
            var compras = MainApp.compraCtrl.getCompras();
            lineas.add("Detalle de Ventas:");
            for (var c : compras) {
                lineas.add("- " + c.getIdCompra().substring(0,8) + " | " + c.getUsuario().getNombre() + " | $" + String.format("%.0f", c.getTotal()) + " | " + c.getEstado());
            }
        } else {
            lineas.add("Datos generales del sistema cargados correctamente.");
        }
        
        return construirPDFEstructurado(lineas);
    }

    private byte[] construirPDFEstructurado(List<String> lineas) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        List<Long> offsets = new ArrayList<>();

        buf.write("%PDF-1.4\n".getBytes());
        buf.write("%\u00d3\u00d4\u00c3\u00cf\n".getBytes());

        offsets.add((long) buf.size());
        buf.write("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n".getBytes());

        offsets.add((long) buf.size());
        buf.write("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n".getBytes());

        offsets.add((long) buf.size());
        buf.write("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n".getBytes());

        StringBuilder sb = new StringBuilder();
        sb.append("BT\n/F1 10 Tf\n12 TL\n45 800 Td\n");
        for (String linea : lineas) {
            String clean = linea.replace("(", "\\(").replace(")", "\\)");
            sb.append("(").append(clean).append(") Tj T*\n");
        }
        sb.append("ET\n");
        byte[] content = sb.toString().getBytes("ISO-8859-1");

        offsets.add((long) buf.size());
        buf.write(("4 0 obj\n<< /Length " + content.length + " >>\nstream\n").getBytes());
        buf.write(content);
        buf.write("\nendstream\nendobj\n".getBytes());

        offsets.add((long) buf.size());
        buf.write("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>\nendobj\n".getBytes());

        long xrefPos = (long) buf.size();
        buf.write(("xref\n0 " + (offsets.size() + 1) + "\n0000000000 65535 f \n").getBytes());
        for (long off : offsets) {
            buf.write(String.format(Locale.US, "%010d 00000 n \n", off).getBytes());
        }

        buf.write(("trailer\n<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\n").getBytes());
        buf.write(("startxref\n" + xrefPos + "\n%%EOF\n").getBytes());

        return buf.toByteArray();
    }
}
