package view;

import controller.*;
import view.admin.*;

import javax.swing.*;

public class AdminFrame extends JFrame {
    public AdminFrame(FuncionarioController fc, CategoriaController cc, RecursoController rc, ReservaController resController) {
        setTitle("SISTEMA DE RESERVAS - MÓDULO ADMINISTRADOR");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        FuncionariosPanel pnlFuncionarios = new FuncionariosPanel(fc);
        CategoriasPanel pnlCategorias = new CategoriasPanel(cc);
        RecursosPanel pnlRecursos = new RecursosPanel(rc, cc);

        tabs.addTab("Funcionarios", pnlFuncionarios);
        tabs.addTab("Categorías", pnlCategorias);
        tabs.addTab("Recursos", pnlRecursos);
        tabs.addTab("Calendarización", new JPanel());
        tabs.addTab("Estadísticas", new view.estadisticas.EstadisticasPanel(resController));

        tabs.addChangeListener(e -> {
            if (tabs.getSelectedComponent() == pnlRecursos) {
                pnlRecursos.cargarCategoriasEnCombos();
            }
        });

        add(tabs);
    }
}