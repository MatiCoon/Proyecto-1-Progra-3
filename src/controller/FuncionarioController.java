package controller;

import data.XMLManager;
import model.Funcionario;
import model.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public class FuncionarioController {
    private final List<Usuario> usuarios;
    private final String rutaXML;

    public FuncionarioController(List<Usuario> usuarios, String rutaXML) {
        this.usuarios = usuarios;
        this.rutaXML = rutaXML;
    }

    public List<Funcionario> obtenerTodosLosFuncionarios() {
        return usuarios.stream()
                .filter(u -> u instanceof Funcionario)
                .map(u -> (Funcionario) u)
                .collect(Collectors.toList());
    }

    public List<Funcionario> buscar(String criterio) {
        String criterioLower = criterio.toLowerCase();
        return obtenerTodosLosFuncionarios().stream()
                .filter(f -> f.getId().toLowerCase().contains(criterioLower) ||
                        f.getNombre().toLowerCase().contains(criterioLower))
                .collect(Collectors.toList());
    }

    public boolean agregar(String id, String nombre, String telefono) {
        if (usuarios.stream().anyMatch(u -> u.getId().equals(id))) {
            return false; // User ID already exists
        }
        Funcionario nuevo = new Funcionario(id, id, nombre, telefono);
        usuarios.add(nuevo);
        guardarCambios();
        return true;
    }

    public boolean modificar(String id, String nuevoNombre, String nuevoTelefono) {
        Funcionario existente = buscarPorId(id);
        if (existente != null) {
            existente.setNombre(nuevoNombre);
            existente.setTelefono(nuevoTelefono);
            guardarCambios();
            return true;
        }
        return false;
    }

    public boolean eliminar(String id) {
        boolean removido = usuarios.removeIf(u -> u.getId().equals(id) && u instanceof Funcionario);
        if (removido) {
            guardarCambios();
        }
        return removido;
    }

    public Funcionario buscarPorId(String id) {
        return obtenerTodosLosFuncionarios().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void guardarCambios() {
        try {
            XMLManager.guardarUsuarios(usuarios, rutaXML);
        } catch (Exception e) {
            System.err.println("Error al guardar funcionarios: " + e.getMessage());
        }
    }
}
