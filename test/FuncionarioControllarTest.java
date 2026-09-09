import controller.FuncionarioController;
import model.Funcionario;
import model.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioControllerTest {
    private FuncionarioController controller;
    private List<Usuario> usuarios;
    private final String TEST_XML = "test_funcionarios.xml";

    @BeforeEach
    void setUp() {
        usuarios = new ArrayList<>();
        controller = new FuncionarioController(usuarios, TEST_XML);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_XML).delete();
    }

    @Test
    void testAgregarFuncionarioAsignaClaveIgualAlId() {
        boolean exito = controller.agregar("11890", "Matías Serrano Palma", "8888-8888");

        assertTrue(exito);
        assertEquals(1, usuarios.size());

        Funcionario f = (Funcionario) usuarios.get(0);
        assertEquals("11890", f.getId());
        assertEquals("Matías Serrano Palma", f.getNombre());
        assertEquals("11890", f.getClave(), "La clave por defecto debe ser igual al ID al crearse");
    }

    @Test
    void testNoPermiteIdsDuplicados() {
        controller.agregar("111", "Funcionario 1", "1111");
        boolean exito = controller.agregar("111", "Funcionario 2", "2222");

        assertFalse(exito);
        assertEquals(1, usuarios.size());
    }
}