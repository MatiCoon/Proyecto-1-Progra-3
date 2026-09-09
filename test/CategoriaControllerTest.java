import controller.CategoriaController;
import model.Categoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaControllerTest {
    private CategoriaController controller;
    private List<Categoria> categorias;
    private final String TEST_XML = "test_categorias.xml";

    @BeforeEach
    void setUp() {
        categorias = new ArrayList<>();
        controller = new CategoriaController(categorias, TEST_XML);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_XML).delete();
    }

    @Test
    void testAgregarGeneraIdsSecuenciales() {
        Categoria cat1 = controller.agregar("Sala Juntas");
        Categoria cat2 = controller.agregar("Laptop Windows");

        assertEquals("CAT-000001", cat1.getId());
        assertEquals("CAT-000002", cat2.getId());
    }
}