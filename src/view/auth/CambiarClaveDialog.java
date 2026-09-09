package view.auth;

import controller.AuthController;
import javax.swing.*;
import java.awt.*;

public class CambiarClaveDialog extends JDialog {
    private final AuthController authController;

    private JTextField txtId;
    private JPasswordField txtClaveActual;
    private JPasswordField txtNuevaClave;
    private JPasswordField txtConfirmarClave;

    public CambiarClaveDialog(JFrame parent, AuthController authController) {
        super(parent, "Cambiar Clave", true);
        this.authController = authController;

        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelFormulario.add(new JLabel("ID Usuario:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Clave Actual:"));
        txtClaveActual = new JPasswordField();
        panelFormulario.add(txtClaveActual);

        panelFormulario.add(new JLabel("Clave Nueva:"));
        txtNuevaClave = new JPasswordField();
        panelFormulario.add(txtNuevaClave);

        panelFormulario.add(new JLabel("Confirmar Nueva:"));
        txtConfirmarClave = new JPasswordField();
        panelFormulario.add(txtConfirmarClave);

        JPanel panelBotones = new JPanel();
        JButton btnCambiar = new JButton("Cambiar");
        JButton btnCancelar = new JButton("Cancelar");

        btnCambiar.addActionListener(e -> procesarCambio());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnCambiar);
        panelBotones.add(btnCancelar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void procesarCambio() {
        String id = txtId.getText();
        String actual = new String(txtClaveActual.getPassword());
        String nueva = new String(txtNuevaClave.getPassword());
        String confirmar = new String(txtConfirmarClave.getPassword());

        if (id.isEmpty() || actual.isEmpty() || nueva.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe llenar todos los campos.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!nueva.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las claves nuevas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean exito = authController.cambiarClave(id, actual, nueva);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Clave cambiada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "ID o clave actual incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}