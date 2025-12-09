/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package servicio;

import modelo.Brigadista;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase de Servicio encargada de la lógica de negocio y la persistencia (manejo de archivos)
 * de los objetos {@link Brigadista}.
 * Implementa el patrón de **Servicio** y el **Manejo de Archivos** (serialización/deserialización)
 * para la persistencia de datos.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class BrigadistaService {

    /**
     * Nombre del archivo donde se guardarán los objetos serializados de Brigadista.
     */
    private final String archivo = "brigadistas.dat";
    
    /**
     * Lista de brigadistas cargada en memoria para manipulación rápida.
     */
    private List<Brigadista> brigadistas;

    /**
     * Constructor del servicio.
     * Inicializa la lista de brigadistas cargando los datos desde el archivo
     * o creando una lista vacía si el archivo no existe o hay un error de lectura.
     */
    public BrigadistaService() {
        this.brigadistas = cargarBrigadistas();
    }

    // ====================================================================
    // MANEJO DE ARCHIVOS (PERSISTENCIA)
    // ====================================================================

    /**
     * Carga la lista de brigadistas desde el archivo de persistencia mediante deserialización.
     * Utiliza manejo de excepciones para robustez en caso de fallos de I/O o formato.
     * @return Una lista de {@link Brigadista} cargada, o una lista vacía si falla o no existe el archivo.
     */
    @SuppressWarnings("unchecked")
    private List<Brigadista> cargarBrigadistas() {
        File f = new File(archivo);
        // Si el archivo no existe, devuelve una lista vacía para inicializar.
        if (!f.exists()) return new ArrayList<>();
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            // Deserializa la lista de objetos
            return (List<Brigadista>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al cargar brigadistas desde el archivo.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Guarda la lista actual de brigadistas en el archivo de persistencia mediante serialización.
     * Este método sobrescribe el contenido anterior del archivo.
     */
    public void guardarBrigadistas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            // Serializa y escribe la lista completa
            oos.writeObject(brigadistas);
        } catch (Exception e) {
            System.err.println("Error al guardar brigadistas en el archivo.");
            e.printStackTrace();
        }
    }

    // ====================================================================
    // OPERACIONES CRUD
    // ====================================================================

    /**
     * Obtiene una copia de la lista completa de brigadistas en memoria.
     * @return Una nueva {@link List} que contiene todos los brigadistas.
     */
    public List<Brigadista> getAllBrigadistas() {
        return new ArrayList<>(brigadistas);
    }

    /**
     * Agrega un nuevo brigadista a la lista y guarda la lista actualizada en el archivo.
     * @param b El objeto {@link Brigadista} a agregar.
     */
    public void agregarBrigadista(Brigadista b) {
        brigadistas.add(b);
        guardarBrigadistas();
    }

    /**
     * Actualiza los datos de un brigadista existente buscándolo por su ID.
     * Si encuentra el brigadista, lo reemplaza y guarda la lista en el archivo.
     * @param actualizado El objeto {@link Brigadista} con los datos modificados.
     */
    public void updateBrigadista(Brigadista actualizado) {
        for (int i = 0; i < brigadistas.size(); i++) {
            if (brigadistas.get(i).getId() == actualizado.getId()) {
                brigadistas.set(i, actualizado);
                guardarBrigadistas();
                return;
            }
        }
    }


    /**
     * Elimina un brigadista de la lista por su ID y guarda la lista actualizada.
     * @param id El ID del brigadista a eliminar.
     */
    public void eliminarBrigadista(int id) {
        brigadistas.removeIf(b -> b.getId() == id);
        guardarBrigadistas();
    }

    /**
     * Cambia el estado de un brigadista específico por su ID y persiste el cambio.
     * @param id El ID del brigadista.
     * @param nuevoEstado El nuevo estado (ej. "Libre", "En Servicio").
     */
    public void cambiarEstado(int id, String nuevoEstado) {
        Brigadista b = buscarPorId(id);
        if (b != null) {
            b.setEstado(nuevoEstado);
            guardarBrigadistas();
        }
    }

    // ====================================================================
    // MÉTODOS DE BÚSQUEDA Y FILTRADO
    // ====================================================================

    /**
     * Busca un brigadista específico por su ID único.
     * @param id El ID numérico del brigadista a buscar.
     * @return El objeto {@link Brigadista} encontrado, o {@code null} si no existe.
     */
    public Brigadista buscarPorId(int id) {
        return brigadistas.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    /**
     * Busca un brigadista por su nombre (coincidencia exacta sin distinguir mayúsculas/minúsculas).
     * @param nombre El nombre del brigadista a buscar.
     * @return El objeto {@link Brigadista} encontrado, o {@code null} si no existe.
     */
    public Brigadista buscarPorNombre(String nombre) {
        return brigadistas.stream().filter(b -> b.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null);
    }

    /**
     * Obtiene una lista de todos los brigadistas cuyo estado es "Libre".
     * Demuestra el uso de la API Stream para filtrado.
     * @return Una {@link List} de brigadistas disponibles.
     */
    public List<Brigadista> getBrigadistasLibres() {
        return brigadistas.stream().filter(b -> "Libre".equalsIgnoreCase(b.getEstado())).collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los brigadistas cuyo estado es "En Servicio".
     * @return Una {@link List} de brigadistas actualmente asignados a incidentes.
     */
    public List<Brigadista> getBrigadistasEnServicio() {
        return brigadistas.stream().filter(b -> "En Servicio".equalsIgnoreCase(b.getEstado())).collect(Collectors.toList());
    }

    /**
     * Obtiene el primer brigadista encontrado que esté en estado "Libre".
     * Útil para asignaciones rápidas.
     * @return El primer {@link Brigadista} libre, o {@code null} si no hay ninguno.
     */
    public Brigadista getPrimerBrigadistaLibre() {
        return brigadistas.stream().filter(b -> "Libre".equalsIgnoreCase(b.getEstado())).findFirst().orElse(null);
    }

    /**
     * Reinicia el sistema de brigadistas, limpiando la lista en memoria y el archivo de persistencia.
     */
    public void resetBrigadistas() {
        brigadistas.clear();
        guardarBrigadistas();
    }
}