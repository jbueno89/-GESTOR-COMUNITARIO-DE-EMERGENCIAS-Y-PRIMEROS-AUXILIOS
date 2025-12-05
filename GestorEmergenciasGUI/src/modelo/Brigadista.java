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
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un Brigadista, que es un tipo especializado de Usuario.
 * Demuestra **HERENCIA** al extender de Usuario y agregar atributos/métodos específicos.
 * También demuestra **POLIMORFISMO** al sobrescribir el método toString().
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class Brigadista extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Contador estático para IDs únicos de brigadistas
    private static int contadorId = 1;
    
    /**
     * ID único del brigadista.
     */
    private int id;
    
    /**
     * Número de teléfono de contacto del brigadista.
     */
    private String telefono;
    
    /**
     * Especialidad del brigadista (Ej: "Primeros Auxilios", "Incendios", "Rescate").
     */
    private String especialidad;
    
    /**
     * Estado actual del brigadista ("Libre", "En Servicio", "Descanso").
     */
    private String estado;
    
    /**
     * Historial de intervenciones realizadas por el brigadista.
     * Demuestra **Manejo de Objetos** (colección de objetos Brigadista).
     */
    private List<String> historialIntervenciones;
    
    /**
     * Certificaciones o cursos completados por el brigadista.
     */
    private List<String> certificaciones;

    /**
     * Constructor completo para crear un nuevo brigadista.
     * Demuestra **HERENCIA** al usar super() para llamar al constructor de Usuario.
     * * @param nombreCompleto Nombre completo del brigadista
     * @param nombreUsuario Nombre de usuario para login
     * @param contrasena Contraseña
     * @param telefono Teléfono de contacto
     * @param especialidad Especialidad del brigadista
     */
    public Brigadista(String nombreCompleto, String nombreUsuario, String contrasena, 
                     String telefono, String especialidad) {
        // Llamada al constructor de la clase padre (Usuario)
        super(nombreCompleto, nombreUsuario, contrasena, "Brigadista");
        
        this.id = contadorId++;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.estado = "Libre"; // Estado inicial por defecto
        this.historialIntervenciones = new ArrayList<>();
        this.certificaciones = new ArrayList<>();
    }

    /**
     * Constructor simplificado (sin usuario/contraseña).
     * Útil cuando solo se necesita gestionar datos del brigadista.
     * * @param nombreCompleto Nombre completo
     * @param telefono Teléfono
     * @param especialidad Especialidad
     */
    public Brigadista(String nombreCompleto, String telefono, String especialidad) {
        // Llama al constructor padre con datos temporales o por defecto
        super(nombreCompleto, "brigadista" + contadorId, "temp123", "Brigadista");
        
        this.id = contadorId++;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.estado = "Libre";
        this.historialIntervenciones = new ArrayList<>();
        this.certificaciones = new ArrayList<>();
    }

    // =========================================================================
    // GETTERS (Encapsulamiento)
    // =========================================================================
    
    /**
     * *** MÉTODO AÑADIDO PARA CORREGIR EL ERROR DE COMPILACIÓN ***
     * Obtiene el nombre completo del brigadista.
     * Llama al método getNombreCompleto() de la clase padre (Usuario).
     * * @return Nombre completo del brigadista
     */
    public String getNombre() {
        return getNombreCompleto(); // Heredado de Usuario
    }

    /**
     * Obtiene el ID del brigadista.
     * @return ID del brigadista
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el teléfono del brigadista.
     * @return Teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Obtiene la especialidad del brigadista.
     * @return Especialidad
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Obtiene el estado actual del brigadista.
     * @return Estado ("Libre", "En Servicio", "Descanso")
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Obtiene el historial completo de intervenciones.
     * @return Lista de intervenciones
     */
    public List<String> getHistorialIntervenciones() {
        return new ArrayList<>(historialIntervenciones); // Retorna copia
    }

    /**
     * Obtiene las certificaciones del brigadista.
     * @return Lista de certificaciones
     */
    public List<String> getCertificaciones() {
        return new ArrayList<>(certificaciones); // Retorna copia
    }

    /**
     * Obtiene el número de intervenciones realizadas.
     * @return Cantidad de intervenciones
     */
    public int getNumeroIntervenciones() {
        return historialIntervenciones.size();
    }

    // =========================================================================
    // SETTERS (Encapsulamiento)
    // =========================================================================

    /**
     * Establece el ID del brigadista (usado al cargar desde archivo).
     * @param id Nuevo ID
     */
    public void setId(int id) {
        this.id = id;
        if (id >= contadorId) {
            contadorId = id + 1;
        }
    }

    /**
     * Establece el teléfono del brigadista.
     * @param telefono Nuevo teléfono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Establece la especialidad del brigadista.
     * @param especialidad Nueva especialidad
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Cambia el estado del brigadista.
     * Demuestra **Lógica de Negocio** con validación de estados.
     * * @param nuevoEstado Nuevo estado
     * @throws IllegalArgumentException Si el estado no es válido
     */
    public void cambiarEstado(String nuevoEstado) {
        // Validación de estados permitidos
        String[] estadosValidos = {"Libre", "En Servicio", "Descanso"};
        boolean esValido = false;
        
        for (String estadoValido : estadosValidos) {
            if (estadoValido.equals(nuevoEstado)) {
                esValido = true;
                break;
            }
        }
        
        if (!esValido) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado + 
                ". Debe ser: Libre, En Servicio o Descanso.");
        }
        
        this.estado = nuevoEstado;
    }

    // =========================================================================
    // MÉTODOS DE LÓGICA DE NEGOCIO
    // =========================================================================

    /**
     * Verifica si el brigadista está disponible para ser asignado.
     * Implementa **Reglas de Negocio** para asignación de recursos.
     * * @return true si está en estado "Libre", false en caso contrario
     */
    public boolean estaDisponible() {
        return "Libre".equals(this.estado);
    }

    /**
     * Registra una nueva intervención en el historial del brigadista.
     * Demuestra **Manejo de Objetos** y actualización de colecciones.
     * * @param descripcionIntervención Descripción de la intervención realizada
     */
    public void agregarIntervención(String descripcionIntervención) {
        if (descripcionIntervención != null && !descripcionIntervención.trim().isEmpty()) {
            String registro = java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            ) + " - " + descripcionIntervención;
            
            historialIntervenciones.add(registro);
            System.out.println("[Brigadista #" + id + "] Intervención registrada: " + descripcionIntervención);
        }
    }

    /**
     * Agrega una certificación al brigadista.
     * @param certificacion Nombre de la certificación
     */
    public void agregarCertificacion(String certificacion) {
        if (certificacion != null && !certificacion.trim().isEmpty()) {
            certificaciones.add(certificacion);
        }
    }

    /**
     * Obtiene las últimas N intervenciones del historial.
     * @param cantidad Número de intervenciones a obtener
     * @return Lista con las últimas intervenciones
     */
    public List<String> obtenerUltimasIntervenciones(int cantidad) {
        int size = historialIntervenciones.size();
        if (size == 0) {
            return new ArrayList<>();
        }
        
        int inicio = Math.max(0, size - cantidad);
        return new ArrayList<>(historialIntervenciones.subList(inicio, size));
    }

    /**
     * Limpia el historial de intervenciones (útil para mantenimiento).
     */
    public void limpiarHistorial() {
        historialIntervenciones.clear();
        System.out.println("[Brigadista #" + id + "] Historial limpiado.");
    }

    /**
     * Reinicia el contador de IDs (usado al cargar datos).
     * @param nuevoContador Nuevo valor del contador
     */
    public static void reiniciarContador(int nuevoContador) {
        contadorId = nuevoContador;
    }

    /**
     * Obtiene el valor actual del contador.
     * @return Contador actual
     */
    public static int getContadorActual() {
        return contadorId;
    }

    // =========================================================================
    // POLIMORFISMO - Sobrescritura de métodos de la clase padre
    // =========================================================================

    /**
     * Sobrescribe el método toString() de la clase Usuario.
     * Demuestra **POLIMORFISMO** al redefinir el comportamiento heredado.
     * * @return Representación en cadena del brigadista
     */
    @Override
    public String toString() {
        return "Brigadista #" + id + 
               " [Nombre=" + getNombreCompleto() + 
               ", Especialidad=" + especialidad + 
               ", Estado=" + estado + 
               ", Teléfono=" + telefono + 
               ", Intervenciones=" + historialIntervenciones.size() + "]";
    }

    /**
     * Compara dos brigadistas por su ID.
     * Sobrescribe equals() de Object (POLIMORFISMO).
     * * @param obj Objeto a comparar
     * @return true si son el mismo brigadista, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Brigadista other = (Brigadista) obj;
        return this.id == other.id;
    }

    /**
     * Genera un código hash basado en el ID.
     * Sobrescribe hashCode() de Object (POLIMORFISMO).
     * * @return Código hash
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}