package view.admin;

import controller.CategoriaController;
import model.Categoria;
import view.models.CategoriaTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CategoriasPanel extends JPanel {
    private final CategoriaController controller;
    private CategoriaTableModel tableModel;
    private JTable tabla;

    private JTextField txtBusquedaDesc;
    private JTextField txtId, txtDescripcion;

    public CategoriasPanel(CategoriaController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        actualizarTabla(controller.obtenerTodas());
    }

    private void inicializarComponentes() {
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda"));
        pnlBusqueda.add(new JLabel("Descripción:"));
        txtBusquedaDesc = new JTextField(20);
        pnlBusqueda.add(txtBusquedaDesc);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnImprimir = new JButton("Imprimir");
        btnBuscar.addActionListener(e -> buscar());
        btnImprimir.addActionListener(e -> service.PDFService.exportarConDialogo(this, tabla, "Reporte de Categorias"));
        pnlBusqueda.add(btnBuscar);
        pnlBusqueda.add(btnImprimir);

        JPanel pnlFormulario = new JPanel(new GridBagLayout());
        pnlFormulario.setBorder(BorderFactory.createTitledBorder("Categoría"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        txtId.setText("Auto-generado");
        pnlFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlFormulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtDescripcion = new JTextField(20); pnlFormulario.add(txtDescripcion, gbc);

        JPanel pnlBotones = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("Guardar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardar());
        btnBorrar.addActionListener(e -> borrar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnBorrar);
        pnlBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        pnlFormulario.add(pnlBotones, gbc);

        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlBusqueda, BorderLayout.NORTH);
        pnlNorte.add(pnlFormulario, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

        tableModel = new CategoriaTableModel(controller.obtenerTodas());
        tabla = new JTable(tableModel);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                cargarFormularioDesdeTabla();
            }
        });

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Listado"));
        pnlTabla.add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(pnlTabla, BorderLayout.CENTER);
    }

    private void buscar() {
        String desc = txtBusquedaDesc.getText().trim();
        if (desc.isEmpty()) {
            actualizarTabla(controller.obtenerTodas());
        } else {
            actualizarTabla(controller.buscarPorDescripcion(desc));
        }
    }

    private void guardar() {
        String descripcion = txtDescripcion.getText().trim();
        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción es requerida.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = txtId.getText();
        if (id.equals("Auto-generado") || id.isEmpty()) {
            controller.agregar(descripcion);
            JOptionPane.showMessageDialog(this, "Categoría agregada exitosamente.");
        } else {
            controller.modificar(id, descripcion);
            JOptionPane.showMessageDialog(this, "Categoría actualizada exitosamente.");
        }

        limpiarFormulario();
        actualizarTabla(controller.obtenerTodas());
    }

    private void borrar() {
        String id = txtId.getText();
        if (id.equals("Auto-generado")) return;

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar la categoría " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.eliminar(id);
            limpiarFormulario();
            actualizarTabla(controller.obtenerTodas());
        }
    }

    private void cargarFormularioDesdeTabla() {
        int row = tabla.getSelectedRow();
        Categoria c = tableModel.getCategoriaAt(row);
        txtId.setText(c.getId());
        txtDescripcion.setText(c.getDescripcion());
    }

    private void limpiarFormulario() {
        txtId.setText("Auto-generado");
        txtDescripcion.setText("");
        txtBusquedaDesc.setText("");
        tabla.clearSelection();
    }

    private void actualizarTabla(List<Categoria> lista) {
        tableModel.setCategorias(lista);
    }
}