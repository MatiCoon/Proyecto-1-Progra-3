package view.models;

import model.Reserva;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaTableModel extends AbstractTableModel {
    private final String[] columnas = {"Id", "Actividad", "Fecha", "Horario", "Recursos", "Estado"};
    private List<Reserva> reservas;

    public ReservaTableModel(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
        fireTableDataChanged();
    }

    public Reserva getReservaAt(int rowIndex) {
        return reservas.get(rowIndex);
    }

    @Override
    public int getRowCount() { return reservas.size(); }

    @Override
    public int getColumnCount() { return columnas.length; }

    @Override
    public String getColumnName(int column) { return columnas[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reserva r = reservas.get(rowIndex);
        switch (columnIndex) {
            case 0: return r.getId();
            case 1: return r.getActividad();
            case 2: return r.getFecha().toString();
            case 3: return r.getHoraInicio() + " - " + r.getHoraFin();
            case 4: return r.getRecursosAsignados().stream()
                    .map(recurso -> recurso.getId())
                    .collect(Collectors.joining(", "));
            case 5: return r.getEstado();
            default: return null;
        }
    }
}