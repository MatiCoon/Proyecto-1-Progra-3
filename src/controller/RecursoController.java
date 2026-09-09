package controller;

import data.XMLManager;
import model.Categoria;
import model.Recurso;

import java.util.List;
import java.util.stream.Collectors;

public class RecursoController {
    private final List<Recurso> recursos;
    private final String rutaXML;

    public RecursoController(List<Recurso> recursos, String rutaArchivoXML) {
        this.recursos = recursos;
        this.rutaXML = rutaArchivoXML;
    }
    public List<Recurso> obtenerTodos() {
        return recursos;
    }

    public List<Recurso> filtrarPorCategoria(String categoriaId) {
        return recursos.stream()
                .filter(r -> r.getCategoria().getId().equals(categoriaId))
                .collect(Collectors.toList());
    }

    public boolean agregar(String id, Categoria categoria, String descripcion) {
        if (buscarPorId(id) != null) {
            return false;
        }
        Recurso nuevo = new Recurso(id, categoria, descripcion);
        recursos.add(nuevo);
        guardarCambios();
        return true;
    }
    public boolean modificar(String id, Categoria nuevaCategoria, String nuevaDescripcion) {
        Recurso existente = buscarPorId(id);
        if (existente != null) {
            existente.setCategoria(nuevaCategoria);
            existente.setDescripcion(nuevaDescripcion);
            guardarCambios();
            return true;
        }
        return false;
    }

    public boolean eliminar(String id) {
        boolean removido = recursos.removeIf(r -> r.getId().equals(id));
        if (removido) {
            guardarCambios();
        }
        return removido;
    }

    public Recurso buscarPorId(String id) {
        return recursos.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void guardarCambios() {
        try {
            XMLManager.guardarRecursos(recursos, rutaXML);
        } catch (Exception e) {
            System.err.println("Error al guardar recursos: " + e.getMessage());
        }
    }
}
