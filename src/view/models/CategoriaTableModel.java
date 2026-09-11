package view.models;

import model.Categoria;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CategoriaTableModel extends AbstractTableModel {
    private final String[] columnas = {"ID", "Descripción"};
    private List<Categoria> categorias;

    public CategoriaTableModel(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        fireTableDataChanged();
    }

    public Categoria getCategoriaAt(int rowIndex) {
        return categorias.get(rowIndex);
    }

    @Override
    public int getRowCount() { return categorias.size(); }

    @Override
    public int getColumnCount() { return columnas.length; }

    @Override
    public String getColumnName(int column) { return columnas[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Categoria c = categorias.get(rowIndex);
        switch (columnIndex) {
            case 0: return c.getId();
            case 1: return c.getDescripcion();
            default: return null;
        }
    }
}