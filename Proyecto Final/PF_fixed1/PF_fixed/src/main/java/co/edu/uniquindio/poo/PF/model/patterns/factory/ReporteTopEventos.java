package co.edu.uniquindio.poo.PF.model.patterns.factory;

import co.edu.uniquindio.poo.PF.model.enums.FormatoReporte;
import co.edu.uniquindio.poo.PF.model.enums.TipoReporte;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReporteTopEventos extends Reporte {
    private int topN;
    private List<String> rankingEventos;

    public ReporteTopEventos() {
        super();
    }

    public ReporteTopEventos(int topN, List<String> ranking, String generadoPor) {
        super(UUID.randomUUID().toString(), LocalDateTime.now(),
                TipoReporte.TOP_EVENTOS, FormatoReporte.PDF, generadoPor);
        this.topN = topN;
        this.rankingEventos = ranking;
    }

    @Override public byte[] generar() {
        return ("Top " + topN + " eventos: " + rankingEventos).getBytes();
    }
    @Override public byte[] exportar() { return generar(); }
    @Override public String getDescripcion() {
        return "Top " + topN + " eventos más vendidos";
    }

    public int getTopN() { return topN; }
    public void setTopN(int topN) { this.topN = topN; }
    public List<String> getRankingEventos() { return rankingEventos; }
    public void setRankingEventos(List<String> rankingEventos) { this.rankingEventos = rankingEventos; }
}
