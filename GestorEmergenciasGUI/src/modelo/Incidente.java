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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un incidente o emergencia en el sistema.
 * Demuestra **Encapsulamiento** con atributos privados y métodos públicos.
 * Implementa Serializable para permitir la **Persistencia de Datos** (Manejo de Archivos).
 * 
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class Incidente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Contador estático para generar IDs únicos
    private static int contadorId = 1;
    
    /**
     * Identificador único del incidente.
     */
    private int id;
    
    /**
     * Tipo de emergencia (Ej: "Incendio", "Accidente", "Emergencia Médica").
     */
    private String tipo;
    
    /**
     * Descripción detallada del incidente.
     */
    private String descripcion;
    
    /**
     * Ubicación aproximada del incidente.
     */
    private String ubicacion;
    
    /**
     * Nivel de prioridad ("Alta", "Media", "Baja").
     */
    private String prioridad;
    
    /**
     * Estado actual del incidente ("Abierto", "En Proceso", "Resuelto", "Cancelado").
     */
    private String estado;
    
    /**
     * Fecha y hora de registro del incidente.
     */
    private LocalDateTime fechaHora;
    
    /**
     * ID del brigadista asignado al incidente (null si no hay asignación).
     */
    private Integer brigadistaAsignadoId;
    
    /**
     * Nombre del brigadista asignado (para mostrar en la interfaz).
     */
    private String nombreBrigadista;
    
    /**
     * Ruta o nombre del archivo de foto asociada (opcional).
     */
    private String rutaFoto;
    
    /**
     * Observaciones o notas adicionales sobre el incidente.
     */
    private String observaciones;

    /**
     * Constructor completo para crear un nuevo incidente.
     * Demuestra **Creación de Objetos**.
     * 
     * @param tipo Tipo de emergencia
     * @param descripcion Descripción del incidente
     * @param ubicacion Ubicación del incidente
     * @param prioridad Nivel de prioridad
     */
    public Incidente(String tipo, String descripcion, String ubicacion, String prioridad) {
        this.id = contadorId++;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.prioridad = prioridad;
        this.estado = "Abierto"; // Estado inicial por defecto
        this.fechaHora = LocalDateTime.now();
        this.brigadistaAsignadoId = null;
        this.nombreBrigadista = "Sin asignar";
        this.rutaFoto = null;
        this.observaciones = "";
    }

    /**
     * Constructor sin parámetros para compatibilidad con algunas operaciones.
     */
    public Incidente() {
        this.id = contadorId++;
        this.fechaHora = LocalDateTime.now();
        this.estado = "Abierto";
        this.nombreBrigadista = "Sin asignar";
    }

    // =========================================================================
    // GETTERS (Demuestran Encapsulamiento)
    // =========================================================================

    /**
     * Obtiene el ID del incidente.
     * @return ID del incidente
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el tipo de emergencia.
     * @return Tipo de emergencia
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene la descripción del incidente.
     * @return Descripción del incidente
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la ubicación del incidente.
     * @return Ubicación del incidente
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Obtiene la prioridad del incidente.
     * @return Prioridad ("Alta", "Media", "Baja")
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * Obtiene el estado actual del incidente.
     * @return Estado del incidente
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Obtiene la fecha y hora de registro.
     * @return Fecha y hora del incidente
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Obtiene la fecha y hora formateada como String.
     * @return Fecha formateada (DD/MM/YYYY HH:MM)
     */
    public String getFechaHoraFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHora.format(formatter);
    }

    /**
     * Obtiene el ID del brigadista asignado.
     * @return ID del brigadista o null si no hay asignación
     */
    public Integer getBrigadistaAsignadoId() {
        return brigadistaAsignadoId;
    }

    /**
     * Obtiene el nombre del brigadista asignado.
     * @return Nombre del brigadista
     */
    public String getNombreBrigadista() {
        return nombreBrigadista;
    }

    /**
     * Obtiene la ruta de la foto asociada.
     * @return Ruta de la foto o null
     */
    public String getRutaFoto() {
        return rutaFoto;
    }

    /**
     * Obtiene las observaciones del incidente.
     * @return Observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    // =========================================================================
    // SETTERS (Demuestran Encapsulamiento)
    // =========================================================================

    /**
     * Establece el ID del incidente (usado al cargar desde archivo).
     * @param id Nuevo ID
     */
    public void setId(int id) {
        this.id = id;
        // Actualiza el contador si es necesario
        if (id >= contadorId) {
            contadorId = id + 1;
        }
    }

    /**
     * Establece el tipo de emergencia.
     * @param tipo Nuevo tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Establece la descripción del incidente.
     * @param descripcion Nueva descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Establece la ubicación del incidente.
     * @param ubicacion Nueva ubicación
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Establece la prioridad del incidente.
     * @param prioridad Nueva prioridad
     */
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Cambia el estado del incidente.
     * Demuestra **Lógica de Negocio** al validar transiciones de estado.
     * 
     * @param nuevoEstado Nuevo estado del incidente
     * @throws IllegalArgumentException Si el estado no es válido
     */
    public void cambiarEstado(String nuevoEstado) {
        // Validación de estados permitidos (Manejo de Errores)
        String[] estadosValidos = {"Abierto", "En Proceso", "Resuelto", "Cancelado"};
        boolean esValido = false;
        
        for (String estadoValido : estadosValidos) {
            if (estadoValido.equals(nuevoEstado)) {
                esValido = true;
                break;
            }
        }
        
        if (!esValido) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }
        
        this.estado = nuevoEstado;
    }

    /**
     * Establece la fecha y hora del incidente (usado al cargar desde archivo).
     * @param fechaHora Nueva fecha y hora
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Asigna un brigadista al incidente.
     * Demuestra **Lógica de Negocio** para asignación de recursos.
     * 
     * @param brigadistaId ID del brigadista a asignar
     * @param nombreBrigadista Nombre del brigadista
     */
    public void asignarBrigadista(int brigadistaId, String nombreBrigadista) {
        this.brigadistaAsignadoId = brigadistaId;
        this.nombreBrigadista = nombreBrigadista;
        
        // Si se asigna un brigadista, el incidente pasa a "En Proceso"
        if ("Abierto".equals(this.estado)) {
            this.estado = "En Proceso";
        }
    }

    /**
     * Remueve la asignación del brigadista.
     */
    public void desasignarBrigadista() {
        this.brigadistaAsignadoId = null;
        this.nombreBrigadista = "Sin asignar";
    }

    /**
     * Establece la ruta de la foto asociada.
     * @param rutaFoto Ruta del archivo de foto
     */
    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    /**
     * Establece las observaciones del incidente.
     * @param observaciones Nuevas observaciones
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // =========================================================================
    // MÉTODOS DE UTILIDAD
    // =========================================================================

    /**
     * Verifica si el incidente está activo (no resuelto ni cancelado).
     * @return true si está activo, false en caso contrario
     */
    public boolean estaActivo() {
        return !("Resuelto".equals(estado) || "Cancelado".equals(estado));
    }

    /**
     * Verifica si el incidente tiene brigadista asignado.
     * @return true si tiene asignación, false en caso contrario
     */
    public boolean tieneAsignacion() {
        return brigadistaAsignadoId != null;
    }

    /**
     * Calcula el tiempo transcurrido desde la creación del incidente.
     * @return String con el tiempo transcurrido
     */
    public String getTiempoTranscurrido() {
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = java.time.Duration.between(fechaHora, ahora).toMinutes();
        
        if (minutos < 60) {
            return minutos + " min";
        } else if (minutos < 1440) { // menos de 24 horas
            return (minutos / 60) + " hrs " + (minutos % 60) + " min";
        } else {
            return (minutos / 1440) + " días";
        }
    }

    /**
     * Reinicia el contador de IDs (usado al cargar datos desde archivo).
     * @param nuevoContador Nuevo valor del contador
     */
    public static void reiniciarContador(int nuevoContador) {
        contadorId = nuevoContador;
    }

    /**
     * Obtiene el valor actual del contador de IDs.
     * @return Valor actual del contador
     */
    public static int getContadorActual() {
        return contadorId;
    }

    // =========================================================================
    // MÉTODO TOSTRING (Para depuración y visualización)
    // =========================================================================

    /**
     * Representación en cadena del incidente.
     * @return String con los datos del incidente
     */
    @Override
    public String toString() {
        return "Incidente #" + id + 
               " [Tipo=" + tipo + 
               ", Prioridad=" + prioridad + 
               ", Estado=" + estado + 
               ", Ubicación=" + ubicacion + 
               ", Brigadista=" + nombreBrigadista + "]";
    }
}