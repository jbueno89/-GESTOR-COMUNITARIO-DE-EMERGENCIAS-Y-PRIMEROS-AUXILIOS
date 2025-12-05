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
 * Clase gestora para la administración de brigadistas.
 * Implementa control de disponibilidad, asignaciones y prevención de conflictos.
 * Demuestra **Arquitectura por Capas**, **Manejo de Archivos** y **Reglas de Negocio**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class GestorBrigadistas implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Archivo de persistencia para brigadistas.
     */
    private static final String ARCHIVO_BRIGADISTAS = "brigadistas.dat";
    
    /**
     * Lista de brigadistas gestionados.
     * Demuestra **Manejo de Objetos** (colección de objetos Brigadista).
     */
    private List<Brigadista> listaBrigadistas;

    /**
     * Constructor del gestor.
     * Inicializa la lista cargando datos o creando brigadistas por defecto.
     */
    public GestorBrigadistas() {
        this.listaBrigadistas = cargarBrigadistas();
        
        // Inicializa brigadistas de ejemplo si la lista está vacía
        if (listaBrigadistas.isEmpty()) {
            inicializarBrigadistasDefault();
        }
        
        System.out.println("[GestorBrigadistas] Inicializado con " + listaBrigadistas.size() + " brigadistas.");
    }

    // =========================================================================
    // OPERACIONES CRUD
    // =========================================================================

    /**
     * Registra un nuevo brigadista en el sistema.
     * Demuestra **Creación de Objetos** y validaciones.
     * * @param nombreCompleto Nombre completo
     * @param telefono Teléfono de contacto
     * @param especialidad Especialidad
     * @return El brigadista creado
     * @throws IllegalArgumentException Si los datos son inválidos
     */
    public Brigadista registrarBrigadista(String nombreCompleto, String telefono, String especialidad) {
        // Validaciones (Manejo de Errores)
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío.");
        }
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad no puede estar vacía.");
        }
        
        // Validar que no exista brigadista con el mismo teléfono
        if (buscarPorTelefono(telefono) != null) {
            throw new IllegalArgumentException("Ya existe un brigadista con el teléfono: " + telefono);
        }
        
        // Crear brigadista
        Brigadista nuevoBrigadista = new Brigadista(nombreCompleto, telefono, especialidad);
        listaBrigadistas.add(nuevoBrigadista);
        
        guardarBrigadistas();
        System.out.println("[GestorBrigadistas] Brigadista #" + nuevoBrigadista.getId() + " registrado.");
        
        return nuevoBrigadista;
    }

    /**
     * Obtiene todos los brigadistas del sistema.
     * *** MÉTODO CORREGIDO: Renombrado de 'obtenerTodosBrigadistas' a 'obtenerTodos' para compatibilidad con el UI.
     * @return Lista de todos los brigadistas
     */
    public List<Brigadista> obtenerTodos() {
        return new ArrayList<>(listaBrigadistas);
    }
    
    // El método anterior 'obtenerTodosBrigadistas()' ha sido reemplazado por 'obtenerTodos()'
    // para corregir el error de compilación. Las búsquedas parciales ahora se basan en este.
    
    /**
     * Busca un brigadista por su ID.
     * @param id ID del brigadista
     * @return El brigadista encontrado o null
     */
    public Brigadista buscarPorId(int id) {
        return listaBrigadistas.stream()
            .filter(b -> b.getId() == id)
            .findFirst()
            .orElse(null);
    }

    /**
     * Busca un brigadista por teléfono.
     * @param telefono Teléfono a buscar
     * @return El brigadista encontrado o null
     */
    public Brigadista buscarPorTelefono(String telefono) {
        return listaBrigadistas.stream()
            .filter(b -> b.getTelefono().equals(telefono))
            .findFirst()
            .orElse(null);
    }

    /**
     * Busca brigadistas por nombre (búsqueda parcial).
     * @param nombre Nombre o parte del nombre
     * @return Lista de brigadistas que coinciden
     */
    public List<Brigadista> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return obtenerTodos();
        }
        
        String nombreBusqueda = nombre.toLowerCase();
        return listaBrigadistas.stream()
            .filter(b -> b.getNombreCompleto().toLowerCase().contains(nombreBusqueda))
            .collect(Collectors.toList());
    }

    /**
     * Busca brigadistas por especialidad.
     * @param especialidad Especialidad a buscar
     * @return Lista de brigadistas con esa especialidad
     */
    public List<Brigadista> buscarPorEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            return obtenerTodos();
        }
        
        return listaBrigadistas.stream()
            .filter(b -> b.getEspecialidad().equalsIgnoreCase(especialidad))
            .collect(Collectors.toList());
    }

    /**
     * Busca brigadistas por estado.
     * @param estado Estado a buscar ("Libre", "En Servicio", "Descanso")
     * @return Lista de brigadistas con ese estado
     */
    public List<Brigadista> buscarPorEstado(String estado) {
        return listaBrigadistas.stream()
            .filter(b -> b.getEstado().equalsIgnoreCase(estado))
            .collect(Collectors.toList());
    }

    // =========================================================================
    // GESTIÓN DE DISPONIBILIDAD (Reglas de Negocio Críticas)
    // =========================================================================

    /**
     * Obtiene todos los brigadistas disponibles (estado "Libre").
     * Implementa **Reglas de Negocio** para asignación de recursos.
     * * @return Lista de brigadistas disponibles
     */
    public List<Brigadista> obtenerDisponibles() {
        return listaBrigadistas.stream()
            .filter(Brigadista::estaDisponible)
            .collect(Collectors.toList());
    }

    /**
     * Valida si un brigadista está disponible para asignación.
     * Previene **asignaciones conflictivas**.
     * * @param idBrigadista ID del brigadista a validar
     * @return true si está disponible, false en caso contrario
     * @throws IllegalArgumentException Si el brigadista no existe
     */
    public boolean validarDisponibilidad(int idBrigadista) {
        Brigadista brigadista = buscarPorId(idBrigadista);
        
        if (brigadista == null) {
            throw new IllegalArgumentException("Brigadista #" + idBrigadista + " no encontrado.");
        }
        
        return brigadista.estaDisponible();
    }

    /**
     * Cambia el estado de un brigadista.
     * Implementa validación para prevenir cambios incorrectos.
     * * @param idBrigadista ID del brigadista
     * @param nuevoEstado Nuevo estado
     * @throws IllegalArgumentException Si el brigadista no existe o el estado es inválido
     */
    public void cambiarEstado(int idBrigadista, String nuevoEstado) {
        Brigadista brigadista = buscarPorId(idBrigadista);
        
        if (brigadista == null) {
            throw new IllegalArgumentException("Brigadista #" + idBrigadista + " no encontrado.");
        }
        
        brigadista.cambiarEstado(nuevoEstado);
        guardarBrigadistas();
        
        System.out.println("[GestorBrigadistas] Brigadista #" + idBrigadista + 
                         " cambió a estado: " + nuevoEstado);
    }

    /**
     * Marca un brigadista como "En Servicio".
     * @param idBrigadista ID del brigadista
     */
    public void marcarEnServicio(int idBrigadista) {
        cambiarEstado(idBrigadista, "En Servicio");
    }

    /**
     * Marca un brigadista como "Libre".
     * @param idBrigadista ID del brigadista
     */
    public void marcarLibre(int idBrigadista) {
        cambiarEstado(idBrigadista, "Libre");
    }

    /**
     * Marca un brigadista como "Descanso".
     * @param idBrigadista ID del brigadista
     */
    public void marcarDescanso(int idBrigadista) {
        cambiarEstado(idBrigadista, "Descanso");
    }

    // =========================================================================
    // GESTIÓN DE INTERVENCIONES
    // =========================================================================

    /**
     * Registra una intervención en el historial del brigadista.
     * @param idBrigadista ID del brigadista
     * @param descripcion Descripción de la intervención
     * @throws IllegalArgumentException Si el brigadista no existe
     */
    public void registrarIntervención(int idBrigadista, String descripcion) {
        Brigadista brigadista = buscarPorId(idBrigadista);
        
        if (brigadista == null) {
            throw new IllegalArgumentException("Brigadista #" + idBrigadista + " no encontrado.");
        }
        
        brigadista.agregarIntervención(descripcion);
        guardarBrigadistas();
    }

    /**
     * Agrega una certificación a un brigadista.
     * @param idBrigadista ID del brigadista
     * @param certificacion Nombre de la certificación
     * @throws IllegalArgumentException Si el brigadista no existe
     */
    public void agregarCertificacion(int idBrigadista, String certificacion) {
        Brigadista brigadista = buscarPorId(idBrigadista);
        
        if (brigadista == null) {
            throw new IllegalArgumentException("Brigadista #" + idBrigadista + " no encontrado.");
        }
        
        brigadista.agregarCertificacion(certificacion);
        guardarBrigadistas();
    }

    // =========================================================================
    // ACTUALIZACIÓN Y ELIMINACIÓN
    // =========================================================================

    /**
     * Actualiza los datos de un brigadista.
     * @param id ID del brigadista
     * @param nombreCompleto Nuevo nombre
     * @param telefono Nuevo teléfono
     * @param especialidad Nueva especialidad
     * @throws IllegalArgumentException Si el brigadista no existe
     */
    public void actualizarBrigadista(int id, String nombreCompleto, String telefono, String especialidad) {
        Brigadista brigadista = buscarPorId(id);
        
        if (brigadista == null) {
            throw new IllegalArgumentException("Brigadista #" + id + " no encontrado.");
        }
        
        brigadista.setNombreCompleto(nombreCompleto);
        brigadista.setTelefono(telefono);
        brigadista.setEspecialidad(especialidad);
        
        guardarBrigadistas();
        System.out.println("[GestorBrigadistas] Brigadista #" + id + " actualizado.");
    }

    /**
     * Elimina un brigadista del sistema.
     * @param id ID del brigadista a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminarBrigadista(int id) {
        boolean eliminado = listaBrigadistas.removeIf(b -> b.getId() == id);
        
        if (eliminado) {
            guardarBrigadistas();
            System.out.println("[GestorBrigadistas] Brigadista #" + id + " eliminado.");
        }
        
        return eliminado;
    }

    // =========================================================================
    // ESTADÍSTICAS
    // =========================================================================

    /**
     * Cuenta cuántos brigadistas están en cada estado.
     * @return Array con [libres, enServicio, descanso]
     */
    public int[] contarPorEstado() {
        int libres = (int) listaBrigadistas.stream().filter(b -> "Libre".equals(b.getEstado())).count();
        int enServicio = (int) listaBrigadistas.stream().filter(b -> "En Servicio".equals(b.getEstado())).count();
        int descanso = (int) listaBrigadistas.stream().filter(b -> "Descanso".equals(b.getEstado())).count();
        
        return new int[]{libres, enServicio, descanso};
    }

    /**
     * Obtiene el número total de brigadistas.
     * @return Total de brigadistas
     */
    public int getTotalBrigadistas() {
        return listaBrigadistas.size();
    }

    // =========================================================================
    // PERSISTENCIA (Manejo de Archivos)
    // =========================================================================

    /**
     * Guarda brigadistas en archivo.
     * Demuestra **Manejo de Archivos**.
     */
    private void guardarBrigadistas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_BRIGADISTAS))) {
            oos.writeObject(listaBrigadistas);
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo guardar brigadistas.");
            e.printStackTrace();
        }
    }

    /**
     * Carga brigadistas desde archivo.
     * Demuestra **Manejo de Archivos** y **Manejo de Errores**.
     * * @return Lista cargada o vacía
     */
    private List<Brigadista> cargarBrigadistas() {
        File archivo = new File(ARCHIVO_BRIGADISTAS);
        
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            @SuppressWarnings("unchecked")
            List<Brigadista> loadedList = (List<Brigadista>) ois.readObject();
            
            if (!loadedList.isEmpty()) {
                int maxId = loadedList.stream().mapToInt(Brigadista::getId).max().orElse(0);
                Brigadista.reiniciarContador(maxId + 1);
            }
            
            return loadedList;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ADVERTENCIA: Error al cargar brigadistas.");
            return new ArrayList<>();
        }
    }

    /**
     * Inicializa brigadistas de ejemplo.
     */
    private void inicializarBrigadistasDefault() {
        // Se asume la existencia de un constructor Brigadista(String, String, String)
        listaBrigadistas.add(new Brigadista("Carlos Rodríguez", "3001234567", "Primeros Auxilios"));
        listaBrigadistas.add(new Brigadista("Ana Martínez", "3109876543", "Incendios"));
        listaBrigadistas.add(new Brigadista("Luis Gómez", "3205551234", "Rescate"));
        listaBrigadistas.add(new Brigadista("María López", "3157778899", "Emergencias Médicas"));
        listaBrigadistas.add(new Brigadista("Pedro Sánchez", "3183334455", "Evacuación"));
        
        guardarBrigadistas();
        System.out.println("[GestorBrigadistas] Brigadistas por defecto creados.");
    }
}