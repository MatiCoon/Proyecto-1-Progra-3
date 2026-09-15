package view.estadisticas;

import controller.ReservaController;
import model.Recurso;
import model.Reserva;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasPanel extends JPanel {
    private final ReservaController reservaController;
    private JPanel panelGrafico;

    public EstadisticasPanel(ReservaController reservaController) {
        this.reservaController = reservaController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- TOP: Refresh Button ---
        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnActualizar = new JButton("Actualizar Gráfico");
        btnActualizar.addActionListener(e -> actualizarGrafico());
        pnlNorte.add(btnActualizar);
        add(pnlNorte, BorderLayout.NORTH);

        // --- CENTER: Chart Container ---
        panelGrafico = new JPanel(new BorderLayout());
        add(panelGrafico, BorderLayout.CENTER);

        actualizarGrafico();
    }

    private void actualizarGrafico() {
        panelGrafico.removeAll();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> conteoPorCategoria = new HashMap<>();

        List<Reserva> todasLasReservas = reservaController.getTodasLasReservas();
        for (Reserva r : todasLasReservas) {
            if ("ACTIVA".equals(r.getEstado())) {
                for (Recurso recurso : r.getRecursosAsignados()) {
                    String nombreCat = recurso.getCategoria().getDescripcion();
                    conteoPorCategoria.put(nombreCat, conteoPorCategoria.getOrDefault(nombreCat, 0) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : conteoPorCategoria.entrySet()) {
            dataset.addValue(entry.getValue(), "Recursos Asignados", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Uso de Recursos por Categoría", // Chart Title
                "Categoría",                     // X-Axis Label
                "Frecuencia de Uso",             // Y-Axis Label
                dataset,                         // Dataset
                PlotOrientation.VERTICAL,        // Orientation
                true,                            // Show Legend
                true,                            // Use Tooltips
                false                            // Generate URLs
        );


        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 500));
        panelGrafico.add(chartPanel, BorderLayout.CENTER);

        panelGrafico.revalidate();
        panelGrafico.repaint();
    }
}