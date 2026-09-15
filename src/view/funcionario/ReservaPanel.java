package view.funcionario;

import controller.CategoriaController;
import controller.ReservaController;
import controller.ReservaResultado;
import model.Categoria;
import model.Reserva;
import view.models.ReservaTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReservaPanel extends JPanel {
    private final ReservaController reservaController;
    private final CategoriaController categoriaController;
    private final String funcionarioId;

    private ReservaTableModel tableModel;
    private JTable tablaReservas;

    private JTextField txtNlp, txtActividad, txtFecha, txtHoraInicio, txtHoraFin;
    private JList<Categoria> listCategorias;

    public ReservaPanel(ReservaController reservaController, CategoriaController categoriaController, String funcionarioId) {
        this.reservaController = reservaController;
        this.categoriaController = categoriaController;
        this.funcionarioId = funcionarioId;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        actualizarTabla();
    }

    private void inicializarComponentes() {
        //NLP
        JPanel pnlNlp = new JPanel(new BorderLayout(5, 5));
        pnlNlp.setBorder(BorderFactory.createTitledBorder("Asistente IA (Fase 4)"));
        txtNlp = new JTextField();
        JButton btnExtraer = new JButton("Extraer");
        btnExtraer.addActionListener(e -> {
            String frase = txtNlp.getText().trim();
            if (frase.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese una frase para extraer los datos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            btnExtraer.setEnabled(false);
            btnExtraer.setText("Extrayendo...");

            SwingWorker<service.ReservaExtraidaDTO, Void> worker = new SwingWorker<>() {
                @Override
                protected service.ReservaExtraidaDTO doInBackground() throws Exception {
                    return service.AIService.extraerDatosReserva(frase, categoriaController.obtenerTodas());
                }

                @Override
                protected void done() {
                    btnExtraer.setEnabled(true);
                    btnExtraer.setText("Extraer");
                    try {
                        service.ReservaExtraidaDTO resultado = get();
                        if (resultado != null) {
                            if (resultado.getActividad() != null) txtActividad.setText(resultado.getActividad());
                            if (resultado.getFecha() != null) txtFecha.setText(resultado.getFecha());
                            if (resultado.getHoraInicio() != null) txtHoraInicio.setText(resultado.getHoraInicio());
                            if (resultado.getHoraFin() != null) txtHoraFin.setText(resultado.getHoraFin());

                            if (resultado.getCategorias() != null) {
                                ListModel<Categoria> model = listCategorias.getModel();
                                java.util.List<Integer> indices = new java.util.ArrayList<>();
                                for (int i = 0; i < model.getSize(); i++) {
                                    String desc = model.getElementAt(i).getDescripcion().toLowerCase();
                                    for (String catSugerida : resultado.getCategorias()) {
                                        if (desc.contains(catSugerida.toLowerCase())) {
                                            indices.add(i);
                                        }
                                    }
                                }
                                int[] arrIndices = indices.stream().mapToInt(Integer::intValue).toArray();
                                listCategorias.setSelectedIndices(arrIndices);
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ReservaPanel.this,
                                "Error al contactar al asistente IA: " + ex.getCause().getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        });
        pnlNlp.add(new JLabel("Frase: "), BorderLayout.WEST);
        pnlNlp.add(txtNlp, BorderLayout.CENTER);
        pnlNlp.add(btnExtraer, BorderLayout.EAST);
        // --- Standard Form ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createTitledBorder("Nueva Reserva"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlForm.add(new JLabel("Actividad:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        txtActividad = new JTextField(20);
        pnlForm.add(txtActividad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlForm.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        txtFecha = new JTextField(LocalDate.now().toString());
        pnlForm.add(txtFecha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlForm.add(new JLabel("Hora Inicio (HH:MM):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        txtHoraInicio = new JTextField("08:00");
        pnlForm.add(txtHoraInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        pnlForm.add(new JLabel("Hora Fin (HH:MM):"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        txtHoraFin = new JTextField("10:00");
        pnlForm.add(txtHoraFin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        pnlForm.add(new JLabel("Categorías:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;

        DefaultListModel<Categoria> catModel = new DefaultListModel<>();
        categoriaController.obtenerTodas().forEach(catModel::addElement);
        listCategorias = new JList<>(catModel);
        listCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listCategorias.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Categoria) setText(((Categoria) value).getDescripcion());
                return this;
            }
        });
        JScrollPane scrollCategorias = new JScrollPane(listCategorias);
        scrollCategorias.setPreferredSize(new Dimension(200, 60));
        pnlForm.add(scrollCategorias, gbc);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnReservar = new JButton("Reservar");
        JButton btnLimpiar = new JButton("Limpiar");
        btnReservar.addActionListener(e -> reservar());
        btnLimpiar.addActionListener(e -> limpiar());
        pnlBotones.add(btnReservar);
        pnlBotones.add(btnLimpiar);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlNlp, BorderLayout.NORTH);
        pnlNorte.add(pnlForm, BorderLayout.CENTER);
        pnlNorte.add(pnlBotones, BorderLayout.SOUTH);
        add(pnlNorte, BorderLayout.NORTH);

        // --- Table Section ---
        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Mis Reservas"));
        tableModel = new ReservaTableModel(reservaController.getReservasPorFuncionario(funcionarioId));
        tablaReservas = new JTable(tableModel);
        pnlTabla.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        JPanel pnlAccionesTabla = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCancelar = new JButton("Cancelar reserva seleccionada");
        JButton btnImprimir = new JButton("Imprimir");
        btnCancelar.addActionListener(e -> cancelar());
        btnImprimir.addActionListener(e -> service.PDFService.exportarConDialogo(this, tablaReservas, "Mis Reservas"));
        pnlAccionesTabla.add(btnCancelar);
        pnlAccionesTabla.add(btnImprimir);
        pnlTabla.add(pnlAccionesTabla, BorderLayout.SOUTH);

        add(pnlTabla, BorderLayout.CENTER);
    }

    private void reservar() {
        try {
            String actividad = txtActividad.getText().trim();
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalTime inicio = LocalTime.parse(txtHoraInicio.getText().trim(), DateTimeFormatter.ISO_LOCAL_TIME);
            LocalTime fin = LocalTime.parse(txtHoraFin.getText().trim(), DateTimeFormatter.ISO_LOCAL_TIME);
            List<Categoria> seleccionadas = listCategorias.getSelectedValuesList();

            if (actividad.isEmpty() || seleccionadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una actividad y seleccionar al menos una categoría.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ReservaResultado resultado = reservaController.intentarReserva(funcionarioId, actividad, fecha, inicio, fin, seleccionadas);
            if (resultado.isExito()) {
                JOptionPane.showMessageDialog(this, "Reserva exitosa. ID: " + resultado.getReserva().getId());
                limpiar();
                actualizarTabla();
            } else {
                StringBuilder faltantes = new StringBuilder("No hay recursos disponibles para:\n");
                for (Categoria c : resultado.getCategoriasNoDisponibles())
                    faltantes.append("- ").append(c.getDescripcion()).append("\n");
                JOptionPane.showMessageDialog(this, faltantes.toString(), "Traslape/Falta de Recursos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha/hora incorrecto. Use YYYY-MM-DD y HH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        int row = tablaReservas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla.");
            return;
        }
        Reserva r = tableModel.getReservaAt(row);
        if (r.getEstado().equals("CANCELADA")) {
            JOptionPane.showMessageDialog(this, "La reserva ya está cancelada.");
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "¿Cancelar reserva " + r.getId() + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            reservaController.cancelarReserva(r.getId());
            actualizarTabla();
        }
    }

    private void limpiar() {
        txtNlp.setText("");
        txtActividad.setText("");
        txtFecha.setText(LocalDate.now().toString());
        txtHoraInicio.setText("08:00");
        txtHoraFin.setText("10:00");
        listCategorias.clearSelection();
    }

    private void actualizarTabla() {
        tableModel.setReservas(reservaController.getReservasPorFuncionario(funcionarioId));
    }
}