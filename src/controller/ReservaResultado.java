package controller;

import model.Categoria;
import model.Reserva;

import java.util.List;

public class ReservaResultado {
    private final boolean exito;
    private final Reserva reserva;
    private final List<Categoria> categoriasNoDisponibles;

    public ReservaResultado(boolean exito, Reserva reserva, List<Categoria> categoriasNoDisponibles) {
        this.exito = exito;
        this.reserva = reserva;
        this.categoriasNoDisponibles = categoriasNoDisponibles;
    }

    public boolean isExito() {return exito;}
    public Reserva getReserva() {return reserva;}
    public List<Categoria> getCategoriasNoDisponibles() {return categoriasNoDisponibles;}
}
