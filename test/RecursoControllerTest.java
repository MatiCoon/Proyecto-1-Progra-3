import controller.RecursoController;
import model.Categoria;
import model.Recurso;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecursoControllerTest {
    private RecursoController recursoController;
    private List<Recurso> recursos;
    private Categoria catMonitores;
    private Categoria catSillas;
    private final String TEST_XML = "test_recursos.xml";

    @BeforeEach
    void setUp() {
        catMonitores = new Categoria("CAT-001", "Monitores");
        catSillas = new Categoria("CAT-002", "Sillas");

        recursos = new ArrayList<>();
        recursos.add(new Recurso("R-001", catMonitores, "Monitor 24 pulgadas"));
        recursos.add(new Recurso("R-002", catMonitores, "Monitor 27 pulgadas"));
        recursos.add(new Recurso("R-003", catSillas, "Silla ergonómica"));

        recursoController = new RecursoController(recursos, TEST_XML);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_XML);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAgregarRecurso() {
        boolean exito = recursoController.agregar("R-004", catSillas, "Silla de juntas");

        assertTrue(exito);
        assertEquals(4, recursoController.obtenerTodos().size());
        assertNotNull(recursoController.buscarPorId("R-004"));
    }

    @Test
    void testNoPermitirIdsDuplicados() {
        boolean exito = recursoController.agregar("R-001", catSillas, "Otro monitor");

        assertFalse(exito);
        assertEquals(3, recursoController.obtenerTodos().size());
    }

    @Test
    void testFiltrarPorCategoria() {
        List<Recurso> monitores = recursoController.filtrarPorCategoria("CAT-001");
        assertEquals(2, monitores.size());

        List<Recurso> sillas = recursoController.filtrarPorCategoria("CAT-002");
        assertEquals(1, sillas.size());
    }

    @Test
    void testModificarRecurso() {
        boolean modificado = recursoController.modificar("R-001", catMonitores, "Monitor 4K");

        assertTrue(modificado);
        Recurso r = recursoController.buscarPorId("R-001");
        assertNotNull(r);
        assertEquals("Monitor 4K", r.getDescripcion());
    }

    @Test
    void testEliminarRecurso() {
        boolean eliminado = recursoController.eliminar("R-003");

        assertTrue(eliminado);
        assertEquals(2, recursoController.obtenerTodos().size());
        assertNull(recursoController.buscarPorId("R-003"));
    }
}