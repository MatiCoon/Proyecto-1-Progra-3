package view.models;

import model.Funcionario;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class FuncionarioTableModel extends AbstractTableModel {
    private final String[] columnas = {"ID", "Nombre", "Teléfono"};
    private List<Funcionario> funcionarios;

    public FuncionarioTableModel(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
        fireTableDataChanged();
    }

    public Funcionario getFuncionarioAt(int rowIndex) {
        return funcionarios.get(rowIndex);
    }

    @Override
    public int getRowCount() { return funcionarios.size(); }

    @Override
    public int getColumnCount() { return columnas.length; }

    @Override
    public String getColumnName(int column) { return columnas[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Funcionario f = funcionarios.get(rowIndex);
        switch (columnIndex) {
            case 0: return f.getId();
            case 1: return f.getNombre();
            case 2: return f.getTelefono();
            default: return null;
        }
    }
}