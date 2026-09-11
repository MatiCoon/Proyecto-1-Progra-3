package view.auth;

import controller.*;
import model.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final AuthController authController;

    private final FuncionarioController funcionarioController;
    private final CategoriaController categoriaController;
    private final RecursoController recursoController;
    private final ReservaController reservaController;

    private JTextField txtId;
    private JPasswordField txtClave;

    public LoginView(AuthController auth, FuncionarioController fc, CategoriaController cc, RecursoController rc, ReservaController resC) {
        this.authController = auth;
        this.funcionarioController = fc;
        this.categoriaController = cc;
        this.recursoController = rc;
        this.reservaController = resC;

        setTitle("SISTEMA DE RESERVAS");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelCentral.add(new JLabel("ID:", SwingConstants.RIGHT));
        txtId = new JTextField();
        panelCentral.add(txtId);

        panelCentral.add(new JLabel("Clave:", SwingConstants.RIGHT));
        txtClave = new JPasswordField();
        panelCentral.add(txtClave);

        JPanel panelInferior = new JPanel();
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnCambiarClave = new JButton("Cambiar clave");

        btnIngresar.addActionListener(e -> procesarLogin());
        btnCambiarClave.addActionListener(e -> abrirDialogoCambioClave());

        panelInferior.add(btnIngresar);
        panelInferior.add(btnCambiarClave);

        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void procesarLogin() {
        String id = txtId.getText();
        String clave = new String(txtClave.getPassword());

        Usuario autenticado = authController.autenticar(id, clave);

        if (autenticado != null) {
            this.dispose(); // Close login window
            if (autenticado.getRol().equals("ADMIN")) {
                // TODO: new view.admin.AdminFrame(funcionarioController, categoriaController, recursoController).setVisible(true);
                System.out.println("Login ADMIN exitoso. (AdminFrame pendiente)");
            } else {
                // TODO: new view.funcionario.FuncionarioFrame(reservaController, autenticado.getId(), categoriaController).setVisible(true);
                System.out.println("Login FUNCIONARIO exitoso. (FuncionarioFrame pendiente)");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogoCambioClave() {
        CambiarClaveDialog dialog = new CambiarClaveDialog(this, authController);
        dialog.setVisible(true);
    }
}