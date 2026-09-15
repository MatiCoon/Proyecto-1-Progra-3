package service;

import java.util.List;

public class ReservaExtraidaDTO {
    private String actividad;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private List<String> categorias;

    public String getActividad() { return actividad; }
    public String getFecha() { return fecha; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public List<String> getCategorias() { return categorias; }
}
