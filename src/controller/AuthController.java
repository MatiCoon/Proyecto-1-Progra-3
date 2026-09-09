package controller;

import data.XMLManager;
import model.Usuario;

import java.util.List;

public class AuthController {
    private final List<Usuario> usuarios;
    private final String rutaXML;

    public AuthController(List<Usuario> usuarios, String rutaXML) {
        this.usuarios = usuarios;
        this.rutaXML = rutaXML;
    }

    public Usuario autenticar(String id, String clave) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id) && u.getClave().equals(clave))
                .findFirst()
                .orElse(null);
    }

    public boolean cambiarClave(String id, String claveActual, String nuevaClave) {
        Usuario usuario = autenticar(id, claveActual);
        if (usuario != null && nuevaClave != null && !nuevaClave.trim().isEmpty()) {
            usuario.setClave(nuevaClave.trim());
            guardarCambios();
            return true;
        }
        return false;
    }

    private void guardarCambios() {
        try {
            XMLManager.guardarUsuarios(usuarios, rutaXML);
        } catch (Exception e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }
}
