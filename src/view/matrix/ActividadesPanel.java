package view.matrix;

import controller.ReservaController;
import model.Reserva;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class ActividadesPanel extends JPanel {
    private final ReservaController reservaController;
    private final String funcionarioId;
    private ActividadesTableModel tableModel;

    public ActividadesPanel(ReservaController reservaController, String funcionarioId) {
        this.reservaController = reservaController;
        this.funcionarioId = funcionarioId;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNorte.add(new JLabel("Mi Calendario de Actividades (Semana Actual)"));

        JButton btnActualizar = new JButton("Actualizar");
        JButton btnImprimir = new JButton("Imprimir");

        btnActualizar.addActionListener(e -> cargarMatriz());
        btnImprimir.addActionListener(e -> service.PDFService.exportarConDialogo(this, new JTable(tableModel), "Mis_Actividades_" + funcionarioId));

        pnlNorte.add(btnActualizar);
        pnlNorte.add(btnImprimir);
        add(pnlNorte, BorderLayout.NORTH);

        tableModel = new ActividadesTableModel();
        JTable tabla = new JTable(tableModel);
        tabla.setRowHeight(30);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarMatriz();
    }

    private void cargarMatriz() {
        LocalDate inicioSemana = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<Reserva> misReservas = reservaController.getReservasPorFuncionario(funcionarioId);
        tableModel.actualizarDatos(inicioSemana, misReservas);
    }
}
