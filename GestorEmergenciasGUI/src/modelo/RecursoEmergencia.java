/**
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
 * Clase ABSTRACTA que representa un recurso genérico del sistema de emergencias.
 * Demuestra **ABSTRACCIÓN** en POO - define una estructura común para diferentes
 * tipos de recursos (brigadistas, suministros, vehículos, etc.).
 * 
 * Esta clase NO puede ser instanciada directamente, solo puede ser heredada.
 * 
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public abstract class RecursoEmergencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Identificador único del recurso.
     */
    protected int id;
    
    /**
     * Nombre o descripción del recurso.
     */
    protected String nombre;
    
    /**
     * Estado de disponibilidad del recurso.
     */
    protected boolean disponible;

    /**
     * Constructor protegido para ser usado por las clases hijas.
     * 
     * @param id Identificador del recurso
     * @param nombre Nombre del recurso
     */
    protected RecursoEmergencia(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.disponible = true; // Por defecto está disponible
    }

    // =========================================================================
    // MÉTODOS ABSTRACTOS (deben ser implementados por las clases hijas)
    // =========================================================================

    /**
     * Método ABSTRACTO que verifica si el recurso está disponible.
     * Cada tipo de recurso define su propia lógica de disponibilidad.
     * 
     * @return true si está disponible, false en caso contrario
     */
    public abstract boolean estaDisponible();

    /**
     * Método ABSTRACTO que asigna el recurso a una emergencia.
     * Cada tipo de recurso implementa su propia lógica de asignación.
     * 
     * @param idEmergencia ID de la emergencia a la que se asigna
     * @return true si la asignación fue exitosa
     */
    public abstract boolean asignarAEmergencia(int idEmergencia);

    /**
     * Método ABSTRACTO que libera el recurso (lo marca como disponible).
     */
    public abstract void liberar();

    /**
     * Método ABSTRACTO que obtiene información detallada del recurso.
     * @return String con los detalles del recurso
     */
    public abstract String obtenerDetalles();

    // =========================================================================
    // MÉTODOS CONCRETOS (compartidos por todas las clases hijas)
    // =========================================================================

    /**
     * Obtiene el ID del recurso.
     * @return ID del recurso
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del recurso.
     * @return Nombre del recurso
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del recurso.
     * @param nombre Nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la disponibilidad del recurso.
     * @param disponible true para marcar como disponible
     */
    protected void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Representación básica en cadena del recurso.
     * Las clases hijas pueden sobrescribirlo (POLIMORFISMO).
     * 
     * @return String con información básica
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ID=" + id + ", Nombre=" + nombre + 
               ", Disponible=" + disponible + "]";
    }
}