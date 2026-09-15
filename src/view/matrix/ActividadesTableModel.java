package view.matrix;

import model.Reserva;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActividadesTableModel extends AbstractTableModel {
    private final String[] bloquesHorarios = {
            "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00",
            "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"
    };
    private LocalDate fechaInicio;
    private List<Reserva> reservasFuncionario;

    public void actualizarDatos(LocalDate fechaInicio, List<Reserva> reservasFuncionario) {
        this.fechaInicio = fechaInicio;
        this.reservasFuncionario = reservasFuncionario;
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount() {
        return bloquesHorarios.length;
    }

    @Override
    public int getColumnCount() {
        return 8;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) return "Hora";
        if (fechaInicio == null) return "Día " + column;

        LocalDate fechaColumna = fechaInicio.plusDays(column - 1);
        return fechaColumna.format(DateTimeFormatter.ofPattern("dd/MM"));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return bloquesHorarios[rowIndex] + " - " + String.format("%02d:00", Integer.parseInt(bloquesHorarios[rowIndex].substring(0, 2)) + 1);
        }

        if (fechaInicio == null) return "";

        LocalDate fechaColumna = fechaInicio.plusDays(columnIndex - 1);
        LocalTime horaFila = LocalTime.parse(bloquesHorarios[rowIndex]);

        for (Reserva r : reservasFuncionario) {
            if (!r.getEstado().equals("ACTIVA")) continue;

            if (r.getFecha().equals(fechaColumna)) {
                if ((horaFila.equals(r.getHoraInicio()) || horaFila.isAfter(r.getHoraInicio())) && horaFila.isBefore(r.getHoraFin())) {
                    return r.getActividad();
                }
            }
        }
        return "";
    }
}
