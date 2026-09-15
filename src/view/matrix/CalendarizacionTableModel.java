package view.matrix;

import model.Recurso;
import model.Reserva;

import javax.swing.table.AbstractTableModel;
import java.time.LocalTime;
import java.util.List;

public class CalendarizacionTableModel extends AbstractTableModel {
    private final String[] bloquesHorarios = {
            "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"
    };
    private List<Recurso> recursos;
    private List<Reserva> reservasDelDia;

    public void actualizarDatos(List<Recurso> recursos, List<Reserva> reservasDelDia) {
        this.recursos = recursos;
        this.reservasDelDia = reservasDelDia;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return bloquesHorarios.length;
    }

    @Override
    public int getColumnCount() {
        return (recursos == null ? 1 : recursos.size() + 1);
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) return "Hora";
        return recursos.get(column - 1).getId();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return bloquesHorarios[rowIndex] + " - " + String.format("%02d:00", Integer.parseInt(bloquesHorarios[rowIndex].substring(0, 2)) + 1);
        }

        Recurso recursoActual = recursos.get(columnIndex - 1);
        LocalTime horaFila = LocalTime.parse(bloquesHorarios[rowIndex]);

        for (Reserva r : reservasDelDia) {
            if (!r.getEstado().equals("ACTIVA")) continue;

            boolean usaRecurso = r.getRecursosAsignados().stream().anyMatch(rec -> rec.getId().equals(recursoActual.getId()));
            if (usaRecurso) {
                if ((horaFila.equals(r.getHoraInicio()) || horaFila.isAfter(r.getHoraInicio())) && horaFila.isBefore(r.getHoraFin())) {
                    return r.getActividad() + " (" + r.getFuncionarioId() + ")";
                }
            }
        }
        return "";
    }
}
