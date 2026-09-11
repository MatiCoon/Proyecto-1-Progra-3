package view;

import controller.*;
import view.admin.*;

import javax.swing.*;

public class AdminFrame extends JFrame {
    public AdminFrame(FuncionarioController fc, CategoriaController cc, RecursoController rc) {
        setTitle("SISTEMA DE RESERVAS - MÓDULO ADMINISTRADOR");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Funcionarios", new FuncionariosPanel(fc));
        tabs.addTab("Categorías", new CategoriasPanel(cc));
        tabs.addTab("Recursos", new RecursosPanel(rc, cc));
        tabs.addTab("Calendarización", new JPanel());
        tabs.addTab("Estadísticas", new JPanel());

        add(tabs);
    }
}