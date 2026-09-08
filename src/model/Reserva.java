package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Reserva {
    private String id;
    private String funcionarioId;
    private String actividad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private List<Recurso> recursosAsignados;
    private String estado;

    public Reserva(String id, String funcionarioId, String actividad, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, List<Recurso> recursosAsignados) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.recursosAsignados = recursosAsignados;
        this.estado = "ACTIVA";
    }

    public String getId() {
        return id;
    }

    public String getFuncionarioId() {
        return funcionarioId;
    }

    public String getActividad() {
        return actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public List<Recurso> getRecursosAsignados() {
        return recursosAsignados;
    }

    public String getEstado() {
        return estado;
    }
}

