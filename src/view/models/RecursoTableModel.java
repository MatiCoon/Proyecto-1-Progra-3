package view.models;

import model.Recurso;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RecursoTableModel extends AbstractTableModel {
    private final String[] columnas = {"ID", "Categoría", "Descripción"};
    private List<Recurso> recursos;

    public RecursoTableModel(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
        fireTableDataChanged();
    }

    public Recurso getRecursoAt(int rowIndex) {
        return recursos.get(rowIndex);
    }

    @Override
    public int getRowCount() { return recursos.size(); }

    @Override
    public int getColumnCount() { return columnas.length; }

    @Override
    public String getColumnName(int column) { return columnas[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Recurso r = recursos.get(rowIndex);
        switch (columnIndex) {
            case 0: return r.getId();
            case 1: return r.getCategoria().getDescripcion();
            case 2: return r.getDescripcion();
            default: return null;
        }
    }
}