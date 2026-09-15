package view.admin;

import controller.CategoriaController;
import controller.RecursoController;
import model.Categoria;
import model.Recurso;
import view.models.RecursoTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecursosPanel extends JPanel {
    private final RecursoController recursoController;
    private final CategoriaController categoriaController;
    private RecursoTableModel tableModel;
    private JTable tabla;

    private JComboBox<Categoria> cmbFiltroCategoria;
    private JComboBox<Categoria> cmbFormCategoria;
    private JTextField txtId, txtDescripcion;

    public RecursosPanel(RecursoController rc, CategoriaController cc) {
        this.recursoController = rc;
        this.categoriaController = cc;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        cargarCategoriasEnCombos();
        actualizarTabla(recursoController.obtenerTodos());
    }

    private void inicializarComponentes() {
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Filtro"));
        pnlBusqueda.add(new JLabel("Categoría:"));

        cmbFiltroCategoria = new JComboBox<>();
        configurarComboCategoria(cmbFiltroCategoria);
        pnlBusqueda.add(cmbFiltroCategoria);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnTodos = new JButton("Ver Todos");
        JButton btnImprimir = new JButton("Imprimir");

        btnBuscar.addActionListener(e -> filtrar());
        btnTodos.addActionListener(e -> { cmbFiltroCategoria.setSelectedIndex(-1); actualizarTabla(recursoController.obtenerTodos()); });
        btnImprimir.addActionListener(e -> service.PDFService.exportarConDialogo(this, tabla, "Reporte de Recursos"));

        pnlBusqueda.add(btnBuscar);
        pnlBusqueda.add(btnTodos);
        pnlBusqueda.add(btnImprimir);

        JPanel pnlFormulario = new JPanel(new GridBagLayout());
        pnlFormulario.setBorder(BorderFactory.createTitledBorder("Recurso"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlFormulario.add(new JLabel("ID (No. Activo):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtId = new JTextField(15); pnlFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlFormulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        cmbFormCategoria = new JComboBox<>();
        configurarComboCategoria(cmbFormCategoria);
        pnlFormulario.add(cmbFormCategoria, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnlFormulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtDescripcion = new JTextField(20); pnlFormulario.add(txtDescripcion, gbc);

        JPanel pnlBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardar());
        btnBorrar.addActionListener(e -> borrar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        pnlBotones.add(btnGuardar); pnlBotones.add(btnBorrar); pnlBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; pnlFormulario.add(pnlBotones, gbc);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlBusqueda, BorderLayout.NORTH);
        pnlNorte.add(pnlFormulario, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

        tableModel = new RecursoTableModel(recursoController.obtenerTodos());
        tabla = new JTable(tableModel);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) cargarFormularioDesdeTabla();
        });

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Listado"));
        pnlTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(pnlTabla, BorderLayout.CENTER);
    }

    private void configurarComboCategoria(JComboBox<Categoria> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Categoria) setText(((Categoria) value).getDescripcion());
                return this;
            }
        });
    }

    public void cargarCategoriasEnCombos() {
        DefaultComboBoxModel<Categoria> fModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<Categoria> frmModel = new DefaultComboBoxModel<>();
        for (Categoria c : categoriaController.obtenerTodas()) {
            fModel.addElement(c); frmModel.addElement(c);
        }
        cmbFiltroCategoria.setModel(fModel);
        cmbFormCategoria.setModel(frmModel);
        cmbFiltroCategoria.setSelectedIndex(-1);
    }

    private void filtrar() {
        Categoria cat = (Categoria) cmbFiltroCategoria.getSelectedItem();
        if (cat != null) actualizarTabla(recursoController.filtrarPorCategoria(cat.getId()));
    }

    private void guardar() {
        String id = txtId.getText().trim();
        Categoria cat = (Categoria) cmbFormCategoria.getSelectedItem();
        String desc = txtDescripcion.getText().trim();

        if (id.isEmpty() || cat == null || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son requeridos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (recursoController.buscarPorId(id) != null) {
            recursoController.modificar(id, cat, desc);
            JOptionPane.showMessageDialog(this, "Recurso actualizado.");
        } else {
            recursoController.agregar(id, cat, desc);
            JOptionPane.showMessageDialog(this, "Recurso agregado.");
        }
        limpiarFormulario();
        actualizarTabla(recursoController.obtenerTodos());
    }

    private void borrar() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar recurso " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            recursoController.eliminar(id);
            limpiarFormulario();
            actualizarTabla(recursoController.obtenerTodos());
        }
    }

    private void cargarFormularioDesdeTabla() {
        Recurso r = tableModel.getRecursoAt(tabla.getSelectedRow());
        txtId.setText(r.getId());
        txtId.setEditable(false);
        txtDescripcion.setText(r.getDescripcion());
        for (int i = 0; i < cmbFormCategoria.getItemCount(); i++) {
            if (cmbFormCategoria.getItemAt(i).getId().equals(r.getCategoria().getId())) {
                cmbFormCategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtId.setText(""); txtId.setEditable(true);
        txtDescripcion.setText(""); cmbFormCategoria.setSelectedIndex(-1); tabla.clearSelection();
    }

    private void actualizarTabla(List<Recurso> lista) { tableModel.setRecursos(lista); }
}