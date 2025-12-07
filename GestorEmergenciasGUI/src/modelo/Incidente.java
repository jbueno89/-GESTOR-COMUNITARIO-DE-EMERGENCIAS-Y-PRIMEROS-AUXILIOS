package modelo;

import java.io.Serializable;

public class Incidente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String tipo;
    private String prioridad;
    private String ubicacion;

    // ---- Campo correcto para asignación ----
    private int idBrigadista; // 0 significa "sin brigadista asignado"
    private String nombreBrigadista; // solo para mostrar en tabla

    private String tiempoActivo;
    private boolean resuelto;

    // ---------- CONSTRUCTORES ----------
    public Incidente(int id, String tipo, String prioridad, String ubicacion, String tiempoActivo) {
        this.id = id;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.ubicacion = ubicacion;
        this.tiempoActivo = tiempoActivo;

        this.idBrigadista = 0;        // NO asigna por defecto
        this.nombreBrigadista = "";   // Muestra vacío
        this.resuelto = false;
    }

    public Incidente() {}

    // ---------- GETTERS ----------
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getPrioridad() { return prioridad; }
    public String getUbicacion() { return ubicacion; }
    public int getIdBrigadista() { return idBrigadista; }
    public String getNombreBrigadista() { return nombreBrigadista; }
    public String getTiempoActivo() { return tiempoActivo; }
    public boolean isResuelto() { return resuelto; }

    // ---------- SETTERS ----------
    public void setId(int id) { this.id = id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setTiempoActivo(String tiempoActivo) { this.tiempoActivo = tiempoActivo; }
    public void setResuelto(boolean resuelto) { this.resuelto = resuelto; }

    public void setIdBrigadista(int idBrigadista) {
        this.idBrigadista = idBrigadista;
    }

    public void setNombreBrigadista(String nombreBrigadista) {
        this.nombreBrigadista = nombreBrigadista;
    }

    // ---------- UTIL ----------
    public void marcarResuelto() {
        this.resuelto = true;
        this.tiempoActivo = "Finalizado";
    }

    @Override
    public String toString() {
        return "Incidente{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", idBrigadista=" + idBrigadista +
                ", nombreBrigadista='" + nombreBrigadista + '\'' +
                ", tiempoActivo='" + tiempoActivo + '\'' +
                ", resuelto=" + resuelto +
                '}';
    }
}

