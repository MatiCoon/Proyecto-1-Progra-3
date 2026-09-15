package view;

import controller.CategoriaController;
import controller.RecursoController;
import controller.ReservaController;
import view.estadisticas.EstadisticasPanel;
import view.funcionario.ReservaPanel;
import view.matrix.ActividadesPanel;
import view.matrix.CalendarizacionPanel;

import javax.swing.*;

public class FuncionarioFrame extends JFrame {
    public FuncionarioFrame(ReservaController reservaController,
                            CategoriaController categoriaController,
                            RecursoController recursoController,
                            String funcionarioId) {
        setTitle("SISTEMA DE RESERVAS - MÓDULO FUNCIONARIO (" + funcionarioId + ")");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Reservas", new ReservaPanel(reservaController, categoriaController, funcionarioId));
        tabs.addTab("Calendarización", new CalendarizacionPanel(reservaController, recursoController));
        tabs.addTab("Actividades", new ActividadesPanel(reservaController, funcionarioId));
        tabs.addTab("Estadísticas", new EstadisticasPanel(reservaController));

        add(tabs);
    }
}