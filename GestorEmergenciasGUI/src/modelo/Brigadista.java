/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package modelo;

import java.io.Serializable;

/**
 * Representa un Brigadista en el sistema. Es la clase fundamental del Modelo.
 * Implementa {@link Serializable} para permitir la **persistencia** (guardado y carga en archivos).
 * Demuestra **Encapsulamiento** y **Manejo de Estados**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 * @version 2.0
 * @since 2024-10-01
 */
public class Brigadista implements Serializable {

    /**
     * Identificador único para la serialización. Necesario porque la clase implementa Serializable.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del brigadista.
     */
    private int id;
    
    /**
     * Nombre completo del brigadista.
     */
    private String nombre;
    
    /**
     * Estado de disponibilidad del brigadista: "Libre", "En Servicio", "Descanso".
     * Clave para la lógica de asignación.
     */
    private String estado;       
    
    /**
     * Área de experticia del brigadista (ej. Primeros Auxilios, Rescate, Incendios).
     */
    private String especialidad;
    
    /**
     * Número de teléfono de contacto.
     */
    private String telefono;

    /**
     * Constructor vacío por defecto.
     */
    public Brigadista() {}

    /**
     * Constructor para inicializar un brigadista con todos sus atributos.
     * @param id Identificador único.
     * @param nombre Nombre completo.
     * @param telefono Teléfono de contacto.
     * @param estado Estado inicial de disponibilidad ("Libre", "En Servicio", "Descanso").
     * @param especialidad Especialidad principal.
     */
    public Brigadista(int id, String nombre, String telefono, String estado, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        setEstado(estado); // Usa el setter para aplicar la lógica de limpieza
        this.especialidad = especialidad;
    }

    // ====================================================================
    // GETTERS Y SETTERS (Encapsulamiento)
    // ====================================================================

    /**
     * Obtiene el ID del brigadista.
     * @return El ID.
     */
    public int getId() { return id; }
    
    /**
     * Establece el ID del brigadista.
     * @param id El nuevo ID.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Obtiene el nombre completo del brigadista.
     * @return El nombre.
     */
    public String getNombre() { return nombre; }
    
    /**
     * Establece el nombre completo del brigadista.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el estado de disponibilidad del brigadista.
     * @return El estado (Libre, En Servicio, Descanso).
     */
    public String getEstado() { return estado; }

    /**
     * Establece el estado de disponibilidad del brigadista, limpiando espacios en blanco
     * para mantener la consistencia del dato.
     * @param estado El nuevo estado.
     */
    public void setEstado(String estado) {
        if (estado != null)
            this.estado = estado.trim();
        else
            this.estado = "";
    }

    /**
     * Obtiene la especialidad del brigadista.
     * @return La especialidad.
     */
    public String getEspecialidad() { return especialidad; }
    
    /**
     * Establece la especialidad del brigadista.
     * @param especialidad La nueva especialidad.
     */
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    /**
     * Obtiene el número de teléfono del brigadista.
     * @return El teléfono.
     */
    public String getTelefono() { return telefono; }
    
    /**
     * Establece el número de teléfono del brigadista.
     * @param telefono El nuevo teléfono.
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // ====================================================================
    // MÉTODOS DE ESTADO Y UTILIDAD (Lógica de Negocio)
    // ====================================================================
    
    /**
     * Verifica si el brigadista está en estado "Libre" (disponible para asignación).
     * @return {@code true} si el estado es "Libre" (sin importar mayúsculas/minúsculas).
     */
    public boolean isLibre() {
        return "Libre".equalsIgnoreCase(estado);
    }

    /**
     * Verifica si el brigadista está en estado "En Servicio" (actualmente asignado).
     * @return {@code true} si el estado es "En Servicio" (sin importar mayúsculas/minúsculas).
     */
    public boolean isEnServicio() {
        return "En Servicio".equalsIgnoreCase(estado);
    }

    /**
     * Sobrescribe el método toString para proporcionar una representación legible del objeto.
     * Demuestra **Polimorfismo**.
     * @return Una cadena con el nombre y el estado del brigadista.
     */
    @Override
    public String toString() {
        return nombre + " (" + estado + ")";
    }
}