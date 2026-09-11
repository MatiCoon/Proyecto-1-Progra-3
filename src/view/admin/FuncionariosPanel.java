package view.admin;

import controller.FuncionarioController;
import model.Funcionario;
import view.models.FuncionarioTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FuncionariosPanel extends JPanel {
    private final FuncionarioController controller;
    private FuncionarioTableModel tableModel;
    private JTable tabla;

    private JTextField txtBusquedaId, txtBusquedaNombre;
    private JTextField txtId, txtNombre, txtTelefono;

    public FuncionariosPanel(FuncionarioController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inicializarComponentes();
        actualizarTabla(controller.obtenerTodosLosFuncionarios());
    }

    private void inicializarComponentes() {
        // --- PANEL SUPERIOR: Búsqueda ---
        JPanel pnlBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        pnlBusqueda.add(new JLabel("ID:"));
        txtBusquedaId = new JTextField(10);
        pnlBusqueda.add(txtBusquedaId);

        pnlBusqueda.add(new JLabel("Nombre:"));
        txtBusquedaNombre = new JTextField(15);
        pnlBusqueda.add(txtBusquedaNombre);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnImprimir = new JButton("Imprimir"); // PDF Feature placeholder

        btnBuscar.addActionListener(e -> buscar());
        btnImprimir.addActionListener(e -> JOptionPane.showMessageDialog(this, "Generación PDF en Fase 4"));

        pnlBusqueda.add(btnBuscar);
        pnlBusqueda.add(btnImprimir);

        // --- PANEL MEDIO: Formulario ---
        JPanel pnlFormulario = new JPanel(new GridBagLayout());
        pnlFormulario.setBorder(BorderFactory.createTitledBorder("Funcionario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; pnlFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; txtId = new JTextField(15); pnlFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; txtNombre = new JTextField(15); pnlFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnlFormulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; txtTelefono = new JTextField(15); pnlFormulario.add(txtTelefono, gbc);

        JPanel pnlBotonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> guardar());
        btnBorrar.addActionListener(e -> borrar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        pnlBotonesForm.add(btnGuardar);
        pnlBotonesForm.add(btnBorrar);
        pnlBotonesForm.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        pnlFormulario.add(pnlBotonesForm, gbc);

        // Contenedor Norte (Busqueda + Formulario)
        JPanel pnlNorte = new JPanel(new BorderLayout());
        pnlNorte.add(pnlBusqueda, BorderLayout.NORTH);
        pnlNorte.add(pnlFormulario, BorderLayout.CENTER);
        add(pnlNorte, BorderLayout.NORTH);

        // --- PANEL INFERIOR: Tabla ---
        tableModel = new FuncionarioTableModel(controller.obtenerTodosLosFuncionarios());
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
        String criterio = txtBusquedaId.getText().trim();
        if (criterio.isEmpty()) {
            criterio = txtBusquedaNombre.getText().trim();
        }

        if (criterio.isEmpty()) {
            actualizarTabla(controller.obtenerTodosLosFuncionarios());
        } else {
            actualizarTabla(controller.buscar(criterio));
        }
    }

    private void guardar() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (id.isEmpty() || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID y el Nombre son requeridos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.buscarPorId(id) != null) {
            controller.modificar(id, nombre, telefono);
            JOptionPane.showMessageDialog(this, "Funcionario actualizado exitosamente.");
        } else {
            controller.agregar(id, nombre, telefono);
            JOptionPane.showMessageDialog(this, "Funcionario agregado exitosamente. Clave por defecto = ID.");
        }

        limpiarFormulario();
        actualizarTabla(controller.obtenerTodosLosFuncionarios());
    }

    private void borrar() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar al funcionario " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.eliminar(id)) {
                limpiarFormulario();
                actualizarTabla(controller.obtenerTodosLosFuncionarios());
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar al funcionario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarFormularioDesdeTabla() {
        int row = tabla.getSelectedRow();
        Funcionario f = tableModel.getFuncionarioAt(row);
        txtId.setText(f.getId());
        txtId.setEditable(false); // ID is primary key, cannot be changed
        txtNombre.setText(f.getNombre());
        txtTelefono.setText(f.getTelefono());
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtId.setEditable(true);
        txtNombre.setText("");
        txtTelefono.setText("");
        txtBusquedaId.setText("");
        txtBusquedaNombre.setText("");
        tabla.clearSelection();
    }

    private void actualizarTabla(List<Funcionario> lista) {
        tableModel.setFuncionarios(lista);
    }
}