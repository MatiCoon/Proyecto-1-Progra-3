package view.matrix;

import controller.RecursoController;
import controller.ReservaController;
import model.Reserva;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarizacionPanel extends JPanel {
    private final ReservaController reservaController;
    private final RecursoController recursoController;
    private CalendarizacionTableModel tableModel;
    private JTextField txtFecha;

    public CalendarizacionPanel(ReservaController reservaController, RecursoController recursoController) {
        this.reservaController = reservaController;
        this.recursoController = recursoController;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel pnlNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlNorte.add(new JLabel("Ver disponibilidad para (YYYY-MM-DD):"));
        txtFecha = new JTextField(LocalDate.now().toString(), 10);
        pnlNorte.add(txtFecha);

        JButton btnBuscar = new JButton("Visualizar Matriz");
        JButton btnImprimir = new JButton("Imprimir");

        btnBuscar.addActionListener(e -> cargarMatriz());
        btnImprimir.addActionListener(e -> service.PDFService.exportarConDialogo(this, new JTable(tableModel), "Calendarizacion_" + txtFecha.getText()));

        pnlNorte.add(btnBuscar);
        pnlNorte.add(btnImprimir);
        add(pnlNorte, BorderLayout.NORTH);

        tableModel = new CalendarizacionTableModel();
        JTable tabla = new JTable(tableModel);
        tabla.setRowHeight(30);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarMatriz();
    }

    private void cargarMatriz() {
        try {
            LocalDate fechaConsultada = LocalDate.parse(txtFecha.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            List<Reserva> reservasDia = reservaController.getTodasLasReservas().stream()
                    .filter(r -> r.getFecha().equals(fechaConsultada))
                    .collect(Collectors.toList());

            tableModel.actualizarDatos(recursoController.obtenerTodos(), reservasDia);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
