import controller.AuthController;
import model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {
    private AuthController authController;
    private List<Usuario> usuarios;
    private final String TEST_XML = "test_auth.xml";

    @BeforeEach
    void setUp() {
        usuarios = new ArrayList<>();
        usuarios.add(new Usuario("admin", "admin123", "ADMIN"));
        usuarios.add(new Usuario("func01", "pass01", "FUNCIONARIO"));
        authController = new AuthController(usuarios, TEST_XML);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_XML);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAutenticarExitoso() {
        Usuario usuario = authController.autenticar("admin", "admin123");
        assertNotNull(usuario);
        assertEquals("ADMIN", usuario.getRol());
    }

    @Test
    void testAutenticarFallido() {
        Usuario usuarioClaveErronea = authController.autenticar("admin", "wrongpass");
        assertNull(usuarioClaveErronea);

        Usuario usuarioInexistente = authController.autenticar("no_existe", "admin123");
        assertNull(usuarioInexistente);
    }

    @Test
    void testCambioDeClaveExitoso() {
        boolean exito = authController.cambiarClave("admin", "admin123", "nuevaClaveSegura");

        assertTrue(exito);
        assertNotNull(authController.autenticar("admin", "nuevaClaveSegura"));
        assertNull(authController.autenticar("admin", "admin123"));
    }

    @Test
    void testCambioDeClaveFallidoPorClaveActualErronea() {
        boolean exito = authController.cambiarClave("admin", "claveIncorrecta", "nuevaClave");

        assertFalse(exito);
        assertNotNull(authController.autenticar("admin", "admin123"));
    }
}