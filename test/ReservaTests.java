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

class ReservaControllerTest {
    private ReservaController controller;
    private Categoria cat;
    private final String TEST_XML = "test_reservas.xml";

    @BeforeEach
    void setUp() {
        cat = new Categoria("CAT-001", "Proyector");
        Recurso proyector = new Recurso("P-01", cat, "Proyector EPSON");

        controller = new ReservaController(new ArrayList<>(), List.of(proyector), TEST_XML);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_XML).delete();
    }

    @Test
    void testCancelarReservaLiberaRecursos() {
        LocalDate fecha = LocalDate.of(2026, 10, 5);
        LocalTime inicio = LocalTime.of(14, 0);
        LocalTime fin = LocalTime.of(16, 0);

        ReservaResultado r1 = controller.intentarReserva("111", "Clase A", fecha, inicio, fin, List.of(cat));
        assertTrue(r1.isExito());

        boolean cancelada = controller.cancelarReserva(r1.getReserva().getId());
        assertTrue(cancelada);
        assertEquals("CANCELADA", r1.getReserva().getEstado());

        ReservaResultado r2 = controller.intentarReserva("222", "Clase B", fecha, inicio, fin, List.of(cat));
        assertTrue(r2.isExito(), "El recurso debió ser liberado al cancelar la reserva anterior");
    }
}