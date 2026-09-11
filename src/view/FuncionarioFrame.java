package view;

import controller.*;
import view.funcionario.ReservaPanel;

import javax.swing.*;

public class FuncionarioFrame extends JFrame {
    public FuncionarioFrame(ReservaController rc, CategoriaController cc, String funcionarioId) {
        setTitle("SISTEMA DE RESERVAS - MÓDULO FUNCIONARIO (" + funcionarioId + ")");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Reservas", new ReservaPanel(rc, cc, funcionarioId));
        tabs.addTab("Calendarización", new JPanel());
        tabs.addTab("Actividades", new JPanel());
        tabs.addTab("Estadísticas", new JPanel());
        add(tabs);
    }
}