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
 * Representa un incidente o emergencia en el sistema.
 * Contiene todos los datos relevantes para su seguimiento y la asignación de personal.
 * Implementa {@link Serializable} para permitir la **persistencia** (guardado en archivos).
 * Demuestra **Encapsulamiento** y **Manejo de Estados**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 * @version 2.0
 * @since 2024-10-01
 */
public class Incidente implements Serializable {
    
    /**
     * Identificador único para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del incidente.
     */
    private int id;
    
    /**
     * Tipo de incidente (ej. "Incendio", "Accidente", "Inundación").
     */
    private String tipo;
    
    /**
     * Nivel de prioridad (ej. "Alta", "Media", "Baja").
     */
    private String prioridad;
    
    /**
     * Ubicación geográfica o descripción del lugar del incidente.
     */
    private String ubicacion;

    /**
     * ID del brigadista asignado. 0 indica que no hay asignación.
     * Es el campo clave para la relación con la clase {@link Brigadista}.
     */
    private int idBrigadista; 
    
    /**
     * Nombre del brigadista asignado. Se usa para mostrar rápidamente en la UI,
     * sin necesidad de buscar en el servicio de brigadistas.
     */
    private String nombreBrigadista; 

    /**
     * Indicador del tiempo que lleva activo el incidente.
     */
    private String tiempoActivo;
    
    /**
     * Bandera que indica si el incidente ha sido resuelto y cerrado.
     */
    private boolean resuelto;

    // ====================================================================
    // CONSTRUCTORES
    // ====================================================================
    
    /**
     * Constructor principal para la creación de un nuevo incidente.
     * Inicializa la asignación del brigadista a 0 y el estado a no resuelto.
     * @param id Identificador único.
     * @param tipo Tipo de emergencia.
     * @param prioridad Nivel de prioridad.
     * @param ubicacion Lugar del incidente.
     * @param tiempoActivo Tiempo que lleva activo.
     */
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

    /**
     * Constructor vacío por defecto, necesario para la serialización o frameworks.
     */
    public Incidente() {}

    // ====================================================================
    // GETTERS (Encapsulamiento)
    // ====================================================================
    
    /**
     * Obtiene el ID del incidente.
     * @return El ID.
     */
    public int getId() { return id; }
    
    /**
     * Obtiene el tipo de incidente.
     * @return El tipo.
     */
    public String getTipo() { return tipo; }
    
    /**
     * Obtiene la prioridad del incidente.
     * @return La prioridad.
     */
    public String getPrioridad() { return prioridad; }
    
    /**
     * Obtiene la ubicación del incidente.
     * @return La ubicación.
     */
    public String getUbicacion() { return ubicacion; }
    
    /**
     * Obtiene el ID del brigadista asignado.
     * @return El ID del brigadista (0 si no está asignado).
     */
    public int getIdBrigadista() { return idBrigadista; }
    
    /**
     * Obtiene el nombre del brigadista asignado.
     * @return El nombre del brigadista.
     */
    public String getNombreBrigadista() { return nombreBrigadista; }
    
    /**
     * Obtiene el tiempo que lleva activo el incidente.
     * @return El tiempo activo.
     */
    public String getTiempoActivo() { return tiempoActivo; }
    
    /**
     * Verifica si el incidente ha sido marcado como resuelto.
     * @return {@code true} si está resuelto.
     */
    public boolean isResuelto() { return resuelto; }

    // ====================================================================
    // SETTERS (Encapsulamiento)
    // ====================================================================
    
    /**
     * Establece el ID del incidente.
     * @param id El nuevo ID.
     */
    public void setId(int id) { this.id = id; }
    
    /**
     * Establece el tipo de incidente.
     * @param tipo El nuevo tipo.
     */
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    /**
     * Establece la prioridad del incidente.
     * @param prioridad La nueva prioridad.
     */
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    
    /**
     * Establece la ubicación del incidente.
     * @param ubicacion La nueva ubicación.
     */
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    /**
     * Establece el tiempo activo del incidente.
     * @param tiempoActivo El nuevo tiempo activo.
     */
    public void setTiempoActivo(String tiempoActivo) { this.tiempoActivo = tiempoActivo; }
    
    /**
     * Establece el estado de resolución del incidente.
     * @param resuelto {@code true} si está resuelto, {@code false} en caso contrario.
     */
    public void setResuelto(boolean resuelto) { this.resuelto = resuelto; }

    /**
     * Establece el ID del brigadista asignado.
     * @param idBrigadista El ID del brigadista. Use 0 para desasignar.
     */
    public void setIdBrigadista(int idBrigadista) {
        this.idBrigadista = idBrigadista;
    }

    /**
     * Establece el nombre del brigadista asignado.
     * @param nombreBrigadista El nombre del brigadista.
     */
    public void setNombreBrigadista(String nombreBrigadista) {
        this.nombreBrigadista = nombreBrigadista;
    }

    // ====================================================================
    // MÉTODOS DE UTILIDAD
    // ====================================================================
    
    /**
     * Marca el incidente como resuelto, actualizando su estado a {@code true} y
     * modificando el campo de tiempo activo.
     */
    public void marcarResuelto() {
        this.resuelto = true;
        this.tiempoActivo = "Finalizado";
    }

    /**
     * Sobrescribe el método toString para proporcionar una representación legible del objeto.
     * Demuestra **Polimorfismo**.
     * @return Una cadena que representa los detalles del incidente.
     */
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