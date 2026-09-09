package controller;

import data.XMLManager;
import model.Categoria;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriaController {
    private final List<Categoria> categorias;
    private final String rutaXML;

    public CategoriaController(List<Categoria> categorias, String rutaXML) {
        this.categorias = categorias;
        this.rutaXML = rutaXML;
    }

    public List<Categoria> obtenerTodas() {
        return categorias;
    }

    public List<Categoria> buscarPorDescripcion(String descripcion) {
        String descLower = descripcion.toLowerCase();
        return categorias.stream()
                .filter(c -> c.getDescripcion().toLowerCase().contains(descLower))
                .collect(Collectors.toList());
    }

    public Categoria agregar(String descripcion) {
        String nuevoId = generarSiguienteId();
        Categoria nueva = new Categoria(nuevoId, descripcion);
        categorias.add(nueva);
        guardarCambios();
        return nueva;
    }

    public boolean modificar(String id, String nuevaDescripcion) {
        Categoria existente = buscarPorId(id);
        if (existente != null) {
            existente.setDescripcion(nuevaDescripcion);
            guardarCambios();
            return true;
        }
        return false;
    }

    public boolean eliminar(String id) {
        boolean removido = categorias.removeIf(c -> c.getId().equals(id));
        if (removido) {
            guardarCambios();
        }
        return removido;
    }

    public Categoria buscarPorId(String id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private String generarSiguienteId() {
        int max = 0;
        for (Categoria c : categorias) {
            try {
                int num = Integer.parseInt(c.getId().replace("CAT-", ""));
                if (num > max) {
                    max = num;
                }
            } catch (NumberFormatException ignored) {}
        }
        return String.format("CAT-%06d", max + 1);
    }

    private void guardarCambios() {
        try {
            XMLManager.guardarCategorias(categorias, rutaXML);
        } catch (Exception e) {
            System.err.println("Error al guardar categorías: " + e.getMessage());
        }
    }
}
