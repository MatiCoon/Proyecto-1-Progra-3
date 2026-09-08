package controller;

import data.XMLManager;
import model.Categoria;
import model.Recurso;
import model.Reserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ReservaController {
    private List<Reserva> Reservas;
    private List<Recurso> Recursos;
    private String pathFile;

    public ReservaController(List<Reserva> Reservas, List<Recurso> Recursos, String pathFile) {
        this.Reservas = Reservas;
        this.Recursos = Recursos;
        this.pathFile = pathFile;
    }

    public ReservaResultado intentarReserva(String funcionarioId, String actividad,
                                            LocalDate fecha, LocalTime inicio, LocalTime fin, List<Categoria> categoriasRequeridas) {
        List<Recurso> recursosAsignados = new ArrayList<>();
        List<Categoria> categoriasFaltantes = new ArrayList<>();

        for (Categoria cat : categoriasRequeridas) {
            Recurso recursoDisponible = primerRecursoDisponible(cat, fecha, inicio, fin);

            if (recursoDisponible != null) {
                recursosAsignados.add(recursoDisponible);
            } else {
                categoriasFaltantes.add(cat);
            }
        }

        if (!categoriasFaltantes.isEmpty()) {
            return new ReservaResultado(false, null, categoriasFaltantes);
        }

        String nuevoId = "RES-" + UUID.randomUUID().toString().substring(0,6).toUpperCase();
        Reserva nuevaReserva = new Reserva(nuevoId, funcionarioId, actividad, fecha, inicio, fin, recursosAsignados);
        Reservas.add(nuevaReserva);

        try {
            XMLManager.guardarReservas(Reservas, pathFile);
        } catch (Exception e) {
            System.err.println("Error al guardar en XML: " + e.getMessage());
        }

        return new ReservaResultado(true, nuevaReserva, null);
    }

    private Recurso primerRecursoDisponible(Categoria categoria, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        List<Recurso> recursosDeCategoria = Recursos.stream().filter(
                r->r.getCategoria().getId().equals((categoria.getId()))).toList();
        for (Recurso recurso : recursosDeCategoria) {
            if (isLibre(recurso, fecha, inicio, fin)) {
                return recurso;
            }
        }
        return null;
    }

    private boolean isLibre(Recurso recurso, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        for (Reserva reserva : Reservas) {
            if (reserva.getEstado().equals("CANCELADA") || !reserva.getFecha().equals(fecha)) {
                continue;
            }

            boolean usaEsteRecurso = reserva.getRecursosAsignados().stream().anyMatch(
                    r->r.getId().equals(recurso.getId()));

            if (usaEsteRecurso) {
                boolean timeOverlap = inicio.isBefore(reserva.getHoraFin()) && fin.isAfter(reserva.getHoraInicio());
                if (timeOverlap) {
                    return false;
                }
            }
        }
        return true;
    }
}
