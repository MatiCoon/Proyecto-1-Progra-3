import controller.ReservaController;
import controller.ReservaResultado;
import model.Categoria;
import model.Recurso;
import model.Reserva;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaTests {
    private ReservaController controller;
    private Categoria cat;
    private Recurso sala1;
    private final String XML_PATH = "test_reservas.xml";

    @BeforeEach
    void setUp() {
        cat = new Categoria("CAT-001", "Sala de conferencias");
        sala1 = new Recurso("REC-001", cat, "Sala 1");

        List<Recurso> recursos = List.of(sala1);
        List<Reserva> reservas = new ArrayList<>();
        controller = new ReservaController(reservas, recursos, XML_PATH);
    }

    @AfterEach
    void deleteXML() {
        File testFile = new File(XML_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }
    @Test
    void testReservaExitosa() {
        LocalDate fecha = LocalDate.of(2026, 8, 14);
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(10, 0);

        ReservaResultado resultado = controller.intentarReserva("111", "Reunión", fecha, inicio, fin, List.of(cat));

        assertTrue(resultado.isExito(), "La reserva debería ser exitosa");
        assertNotNull(resultado.getReserva());
        assertEquals(1, resultado.getReserva().getRecursosAsignados().size());

        File testFile = new File(XML_PATH);
        assertTrue(testFile.exists(), "El archivo XML debería haberse creado");
    }

    @Test
    void testFallaPorFaltaDeRecursos() {
        Reserva existente = new Reserva("RES-OLD", "222", "Junta",
                LocalDate.of(2026, 8, 14), LocalTime.of(8, 0), LocalTime.of(10, 0), List.of(sala1));

        List<Reserva> reservasExistentes = new ArrayList<>(List.of(existente));

        controller = new ReservaController(reservasExistentes, List.of(sala1), XML_PATH);

        ReservaResultado resultado = controller.intentarReserva("111", "Reunión",
                LocalDate.of(2026, 8, 14), LocalTime.of(9, 0), LocalTime.of(11, 0), List.of(cat));

        assertFalse(resultado.isExito(), "La reserva debería fallar por traslape");
        assertFalse(resultado.getCategoriasNoDisponibles().isEmpty());
        assertEquals("CAT-001", resultado.getCategoriasNoDisponibles().get(0).getId());
    }
}
