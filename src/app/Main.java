package app;

import controller.*;
import data.XMLManager;
import model.*;
import view.auth.LoginView;

import javax.swing.*;
import java.util.List;

public class Main {
    private static final String RUTA_USUARIOS = "datos/usuarios.xml";
    private static final String RUTA_CATEGORIAS = "datos/categorias.xml";
    private static final String RUTA_RECURSOS = "datos/recursos.xml";
    private static final String RUTA_RESERVAS = "datos/reservas.xml";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                List<Usuario> usuarios = XMLManager.cargarUsuarios(RUTA_USUARIOS);
                if (usuarios.isEmpty()) {
                    usuarios.add(new Usuario("admin", "admin", "ADMIN"));
                    XMLManager.guardarUsuarios(usuarios, RUTA_USUARIOS);
                }

                List<Categoria> categorias = XMLManager.cargarCategorias(RUTA_CATEGORIAS);
                List<Recurso> recursos = XMLManager.cargarRecursos(RUTA_RECURSOS, categorias);
                List<Reserva> reservas = XMLManager.cargarReservas(RUTA_RESERVAS, recursos);

                AuthController authController = new AuthController(usuarios, RUTA_USUARIOS);
                FuncionarioController funcionarioController = new FuncionarioController(usuarios, RUTA_USUARIOS);
                CategoriaController categoriaController = new CategoriaController(categorias, RUTA_CATEGORIAS);
                RecursoController recursoController = new RecursoController(recursos, RUTA_RECURSOS);
                ReservaController reservaController = new ReservaController(reservas, recursos, RUTA_RESERVAS);

                LoginView loginView = new LoginView(authController, funcionarioController, categoriaController, recursoController, reservaController);
                loginView.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error crítico al iniciar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}