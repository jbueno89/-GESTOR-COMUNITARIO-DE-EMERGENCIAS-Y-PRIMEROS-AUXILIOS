package modelo;

import java.io.Serializable;

public class Incidente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String tipo;
    private String prioridad;
    private String ubicacion;
    private String brigadistaAsignado;
    private String tiempoActivo;
    private boolean resuelto; // atributo para marcar resuelto

    // Constructor principal
    public Incidente(int id, String tipo, String prioridad, String ubicacion,
                     String brigadistaAsignado, String tiempoActivo) {
        this.id = id;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.ubicacion = ubicacion;
        this.brigadistaAsignado = brigadistaAsignado;
        this.tiempoActivo = tiempoActivo;
        this.resuelto = false; // por defecto no está resuelto
    }

    // Constructor vacío (opcional, útil para serialización)
    public Incidente() {}

    // ---------- Getters ----------
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getPrioridad() { return prioridad; }
    public String getUbicacion() { return ubicacion; }
    public String getBrigadistaAsignado() { return brigadistaAsignado; }
    public String getTiempoActivo() { return tiempoActivo; }
    public boolean isResuelto() { return resuelto; }

    // ---------- Setters ----------
    public void setId(int id) { this.id = id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setBrigadistaAsignado(String brigadistaAsignado) { this.brigadistaAsignado = brigadistaAsignado; }
    public void setTiempoActivo(String tiempoActivo) { this.tiempoActivo = tiempoActivo; }
    public void setResuelto(boolean resuelto) { this.resuelto = resuelto; }
}
