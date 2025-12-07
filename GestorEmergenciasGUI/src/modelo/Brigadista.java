package modelo;

import java.io.Serializable;

public class Brigadista implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String estado;       // Libre, En Servicio, Descanso
    private String especialidad;
    private String telefono;

    // Constructor vacío
    public Brigadista() {}

    public Brigadista(int id, String nombre, String telefono, String estado, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        setEstado(estado);
        this.especialidad = especialidad;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) {
        if (estado != null)
            this.estado = estado.trim();
        else
            this.estado = "";
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // ---- MÉTODOS ÚTILES PARA ASIGNACIÓN ----
    public boolean isLibre() {
        return "Libre".equalsIgnoreCase(estado);
    }

    public boolean isEnServicio() {
        return "En Servicio".equalsIgnoreCase(estado);
    }

    @Override
    public String toString() {
        return nombre + " (" + estado + ")";
    }
}
