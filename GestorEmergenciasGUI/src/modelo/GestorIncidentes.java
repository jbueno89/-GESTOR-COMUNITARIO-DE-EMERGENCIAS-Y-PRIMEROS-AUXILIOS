/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase gestora para la administración de incidentes/emergencias.
 * Implementa CRUD completo y demuestra **Arquitectura por Capas** (Capa de Lógica),
 * **Manejo de Archivos** (persistencia), **Manejo de Errores** y **Manejo de Objetos**.
 * 
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class GestorIncidentes implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Nombre del archivo donde se persisten los incidentes.
     */
    private static final String ARCHIVO_INCIDENTES = "incidentes.dat";
    
    /**
     * Lista de incidentes gestionados por el sistema.
     * Demuestra **Manejo de Objetos** (colección de objetos Incidente).
     */
    private List<Incidente> listaIncidentes;

    /**
     * Constructor del gestor.
     * Inicializa la lista cargando datos si existen, o crea una lista vacía.
     * Demuestra **Manejo de Archivos**.
     */
    public GestorIncidentes() {
        this.listaIncidentes = cargarIncidentes();
        System.out.println("[GestorIncidentes] Inicializado con " + listaIncidentes.size() + " incidentes.");
    }

    // =========================================================================
    // OPERACIONES CRUD (Create, Read, Update, Delete)
    // =========================================================================

    /**
     * Crea y registra un nuevo incidente en el sistema.
     * Demuestra **Creación de Objetos** y **Manejo de Errores**.
     * 
     * @param tipo Tipo de emergencia
     * @param descripcion Descripción del incidente
     * @param ubicacion Ubicación del incidente
     * @param prioridad Nivel de prioridad
     * @return El incidente creado
     * @throws IllegalArgumentException Si los datos son inválidos
     */
    public Incidente crearIncidente(String tipo, String descripcion, String ubicacion, String prioridad) {
        // Validación de datos (Manejo de Errores)
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de incidente no puede estar vacío.");
        }
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación no puede estar vacía.");
        }
        if (!esPrioridadValida(prioridad)) {
            throw new IllegalArgumentException("Prioridad inválida. Debe ser: Alta, Media o Baja.");
        }

        // Creación del objeto Incidente
        Incidente nuevoIncidente = new Incidente(tipo, descripcion, ubicacion, prioridad);
        listaIncidentes.add(nuevoIncidente);
        
        // Persistencia automática
        guardarIncidentes();
        
        System.out.println("[GestorIncidentes] Incidente #" + nuevoIncidente.getId() + " creado exitosamente.");
        return nuevoIncidente;
    }

    /**
     * Obtiene todos los incidentes del sistema.
     * @return Lista de todos los incidentes
     */
    public List<Incidente> obtenerTodosIncidentes() {
        return new ArrayList<>(listaIncidentes); // Retorna una copia para evitar modificaciones externas
    }

    /**
     * Busca un incidente por su ID.
     * @param id ID del incidente a buscar
     * @return El incidente encontrado o null si no existe
     */
    public Incidente buscarPorId(int id) {
        return listaIncidentes.stream()
            .filter(i -> i.getId() == id)
            .findFirst()
            .orElse(null);
    }

    /**
     * Obtiene todos los incidentes activos (no resueltos ni cancelados).
     * Demuestra uso de **Streams** y programación funcional.
     * 
     * @return Lista de incidentes activos
     */
    public List<Incidente> obtenerIncidentesActivos() {
        return listaIncidentes.stream()
            .filter(Incidente::estaActivo)
            .collect(Collectors.toList());
    }

    /**
     * Busca incidentes por tipo de emergencia.
     * @param tipo Tipo de emergencia a filtrar
     * @return Lista de incidentes del tipo especificado
     */
    public List<Incidente> buscarPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return obtenerTodosIncidentes();
        }
        
        String tipoBusqueda = tipo.toLowerCase();
        return listaIncidentes.stream()
            .filter(i -> i.getTipo().toLowerCase().contains(tipoBusqueda))
            .collect(Collectors.toList());
    }

    /**
     * Busca incidentes por prioridad.
     * @param prioridad Nivel de prioridad ("Alta", "Media", "Baja")
     * @return Lista de incidentes con la prioridad especificada
     */
    public List<Incidente> buscarPorPrioridad(String prioridad) {
        return listaIncidentes.stream()
            .filter(i -> i.getPrioridad().equalsIgnoreCase(prioridad))
            .collect(Collectors.toList());
    }

    /**
     * Busca incidentes por estado.
     * @param estado Estado del incidente ("Abierto", "En Proceso", "Resuelto", "Cancelado")
     * @return Lista de incidentes con el estado especificado
     */
    public List<Incidente> buscarPorEstado(String estado) {
        return listaIncidentes.stream()
            .filter(i -> i.getEstado().equalsIgnoreCase(estado))
            .collect(Collectors.toList());
    }

    /**
     * Busca incidentes por ubicación.
     * @param ubicacion Ubicación a buscar (búsqueda parcial)
     * @return Lista de incidentes en la ubicación
     */
    public List<Incidente> buscarPorUbicacion(String ubicacion) {
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            return obtenerTodosIncidentes();
        }
        
        String ubicacionBusqueda = ubicacion.toLowerCase();
        return listaIncidentes.stream()
            .filter(i -> i.getUbicacion().toLowerCase().contains(ubicacionBusqueda))
            .collect(Collectors.toList());
    }

    /**
     * Actualiza el estado de un incidente.
     * Demuestra **Reglas de Negocio** y **Manejo de Errores**.
     * 
     * @param id ID del incidente
     * @param nuevoEstado Nuevo estado
     * @throws IllegalArgumentException Si el incidente no existe o el estado es inválido
     */
    public void cambiarEstado(int id, String nuevoEstado) {
        Incidente incidente = buscarPorId(id);
        
        if (incidente == null) {
            throw new IllegalArgumentException("Incidente #" + id + " no encontrado.");
        }
        
        incidente.cambiarEstado(nuevoEstado);
        guardarIncidentes();
        
        System.out.println("[GestorIncidentes] Incidente #" + id + " cambió a estado: " + nuevoEstado);
    }

    /**
     * Asigna un brigadista a un incidente.
     * Implementa **Reglas de Negocio** para evitar asignaciones conflictivas.
     * 
     * @param idIncidente ID del incidente
     * @param idBrigadista ID del brigadista a asignar
     * @param nombreBrigadista Nombre del brigadista
     * @throws IllegalArgumentException Si el incidente no existe o ya tiene asignación
     */
    public void asignarBrigadista(int idIncidente, int idBrigadista, String nombreBrigadista) {
        Incidente incidente = buscarPorId(idIncidente);
        
        if (incidente == null) {
            throw new IllegalArgumentException("Incidente #" + idIncidente + " no encontrado.");
        }
        
        if (incidente.tieneAsignacion()) {
            throw new IllegalArgumentException("El incidente ya tiene un brigadista asignado. Primero debe desasignar.");
        }
        
        incidente.asignarBrigadista(idBrigadista, nombreBrigadista);
        guardarIncidentes();
        
        System.out.println("[GestorIncidentes] Brigadista " + nombreBrigadista + 
                         " asignado al incidente #" + idIncidente);
    }

    /**
     * Desasigna un brigadista de un incidente.
     * @param idIncidente ID del incidente
     * @throws IllegalArgumentException Si el incidente no existe
     */
    public void desasignarBrigadista(int idIncidente) {
        Incidente incidente = buscarPorId(idIncidente);
        
        if (incidente == null) {
            throw new IllegalArgumentException("Incidente #" + idIncidente + " no encontrado.");
        }
        
        incidente.desasignarBrigadista();
        guardarIncidentes();
        
        System.out.println("[GestorIncidentes] Brigadista desasignado del incidente #" + idIncidente);
    }

    /**
     * Actualiza los datos de un incidente existente.
     * @param id ID del incidente a actualizar
     * @param tipo Nuevo tipo
     * @param descripcion Nueva descripción
     * @param ubicacion Nueva ubicación
     * @param prioridad Nueva prioridad
     * @throws IllegalArgumentException Si el incidente no existe o los datos son inválidos
     */
    public void actualizarIncidente(int id, String tipo, String descripcion, 
                                   String ubicacion, String prioridad) {
        Incidente incidente = buscarPorId(id);
        
        if (incidente == null) {
            throw new IllegalArgumentException("Incidente #" + id + " no encontrado.");
        }
        
        if (!esPrioridadValida(prioridad)) {
            throw new IllegalArgumentException("Prioridad inválida.");
        }
        
        incidente.setTipo(tipo);
        incidente.setDescripcion(descripcion);
        incidente.setUbicacion(ubicacion);
        incidente.setPrioridad(prioridad);
        
        guardarIncidentes();
        System.out.println("[GestorIncidentes] Incidente #" + id + " actualizado.");
    }

    /**
     * Elimina un incidente del sistema.
     * @param id ID del incidente a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminarIncidente(int id) {
        boolean eliminado = listaIncidentes.removeIf(i -> i.getId() == id);
        
        if (eliminado) {
            guardarIncidentes();
            System.out.println("[GestorIncidentes] Incidente #" + id + " eliminado.");
        }
        
        return eliminado;
    }

    // =========================================================================
    // MÉTODOS DE ESTADÍSTICAS Y REPORTES
    // =========================================================================

    /**
     * Cuenta cuántos incidentes hay por tipo.
     * @return String con el resumen
     */
    public String obtenerResumenPorTipo() {
        StringBuilder resumen = new StringBuilder("Resumen por Tipo:\n");
        
        listaIncidentes.stream()
            .collect(Collectors.groupingBy(Incidente::getTipo, Collectors.counting()))
            .forEach((tipo, count) -> resumen.append("  ").append(tipo).append(": ").append(count).append("\n"));
        
        return resumen.toString();
    }

    /**
     * Obtiene el conteo de incidentes activos.
     * @return Número de incidentes activos
     */
    public int contarIncidentesActivos() {
        return (int) listaIncidentes.stream()
            .filter(Incidente::estaActivo)
            .count();
    }

    /**
     * Obtiene el conteo de incidentes por prioridad alta.
     * @return Número de incidentes de prioridad alta
     */
    public int contarIncidentesPrioridadAlta() {
        return (int) listaIncidentes.stream()
            .filter(i -> "Alta".equalsIgnoreCase(i.getPrioridad()))
            .count();
    }

    // =========================================================================
    // PERSISTENCIA DE DATOS (Manejo de Archivos)
    // =========================================================================

    /**
     * Guarda la lista de incidentes en un archivo mediante serialización.
     * Demuestra **Manejo de Archivos** y **Manejo de Errores**.
     */
    private void guardarIncidentes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_INCIDENTES))) {
            oos.writeObject(listaIncidentes);
            System.out.println("[GestorIncidentes] Datos guardados en " + ARCHIVO_INCIDENTES);
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo guardar el archivo de incidentes.");
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de incidentes desde un archivo.
     * Demuestra **Manejo de Archivos** y **Manejo de Errores**.
     * 
     * @return Lista de incidentes cargada o lista vacía si no existe el archivo
     */
    private List<Incidente> cargarIncidentes() {
        File archivo = new File(ARCHIVO_INCIDENTES);
        
        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("[GestorIncidentes] No se encontró archivo previo. Iniciando con lista vacía.");
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = ois.readObject();
            
            if (obj instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Incidente> loadedList = (List<Incidente>) obj;
                
                // Ajustar el contador de IDs
                if (!loadedList.isEmpty()) {
                    int maxId = loadedList.stream()
                        .mapToInt(Incidente::getId)
                        .max()
                        .orElse(0);
                    Incidente.reiniciarContador(maxId + 1);
                }
                
                System.out.println("[GestorIncidentes] Cargados " + loadedList.size() + " incidentes desde archivo.");
                return loadedList;
            }
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ADVERTENCIA: Error al cargar incidentes. Iniciando con lista vacía.");
            e.printStackTrace();
        }
        
        return new ArrayList<>();
    }

    // =========================================================================
    // MÉTODOS DE UTILIDAD
    // =========================================================================

    /**
     * Valida si una prioridad es correcta.
     * @param prioridad Prioridad a validar
     * @return true si es válida, false en caso contrario
     */
    private boolean esPrioridadValida(String prioridad) {
        return "Alta".equalsIgnoreCase(prioridad) || 
               "Media".equalsIgnoreCase(prioridad) || 
               "Baja".equalsIgnoreCase(prioridad);
    }

    /**
     * Obtiene el número total de incidentes.
     * @return Total de incidentes
     */
    public int getTotalIncidentes() {
        return listaIncidentes.size();
    }
}