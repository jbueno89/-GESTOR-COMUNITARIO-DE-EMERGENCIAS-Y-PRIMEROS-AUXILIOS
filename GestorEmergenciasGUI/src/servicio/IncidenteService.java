/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package servicio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import modelo.Incidente;
import modelo.Brigadista;

/**
 * Clase de Servicio encargada de la lógica de negocio y la persistencia (manejo de archivos)
 * de los objetos {@link Incidente}.
 * Maneja operaciones CRUD, asignación/desasignación indirecta de brigadistas y la generación de IDs.
 * Demuestra el patrón de **Servicio**, **Inyección de Dependencias** del {@link BrigadistaService}
 * y **Persistencia por Serialización**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class IncidenteService {

    /**
     * Lista de incidentes cargada en memoria para manipulación rápida.
     */
    private List<Incidente> incidentes;
    
    /**
     * Ruta del archivo de persistencia para los incidentes.
     */
    private final String RUTA_ARCHIVO = "incidentes.dat";

    /**
     * Referencia al servicio de brigadistas, inyectado para poder manipular el estado del brigadista
     * cuando se asigna o se resuelve un incidente.
     */
    private final BrigadistaService brigadistaService;

    /**
     * Constructor del servicio.
     * Implementa **Inyección de Dependencias** para el servicio de brigadistas,
     * permitiendo la comunicación entre servicios. Carga la lista de incidentes desde el archivo.
     * @param brigadistaService El servicio de brigadistas necesario para las operaciones de estado.
     */
    public IncidenteService(BrigadistaService brigadistaService) {
        this.brigadistaService = brigadistaService;
        this.incidentes = cargarIncidentes();
    }

    // ====================================================================
    // CONSULTAS Y BÚSQUEDAS
    // ====================================================================
    
    /**
     * Obtiene la lista completa de incidentes cargada en memoria.
     * @return La lista de {@link Incidente}.
     */
    public List<Incidente> getAll() {
        return incidentes;
    }

    /**
     * Genera un nuevo ID incremental basado en el ID máximo actual de la lista.
     * @return El próximo ID disponible.
     */
    public int getNuevoId() {
        if (incidentes.isEmpty()) return 1;

        // Encuentra el ID máximo y le suma 1
        return incidentes.stream()
                .mapToInt(Incidente::getId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Busca un incidente por su identificador único.
     * @param id El ID del incidente a buscar.
     * @return El objeto {@link Incidente} si es encontrado, o {@code null} si no existe.
     */
    public Incidente buscarPorId(int id) {
        for (Incidente i : incidentes) {
            if (i.getId() == id) return i;
        }
        return null;
    }

    // ====================================================================
    // OPERACIONES CRUD
    // ====================================================================

    /**
     * Agrega un nuevo incidente a la lista en memoria.
     * Asegura que el incidente se guarde sin brigadista asignado inicialmente.
     * Persiste los cambios en el archivo.
     * @param incidente El objeto {@link Incidente} a añadir.
     */
    public void addIncidente(Incidente incidente) {
        // Garantizar que esté vacío antes de añadirlo al sistema
        incidente.setIdBrigadista(0);
        incidente.setNombreBrigadista("");
        incidentes.add(incidente);
        guardarIncidentes();
    }

    /**
     * Actualiza un incidente existente en la lista.
     * Se utiliza principalmente para persistir cambios en la asignación de brigadistas o campos modificados.
     * Persiste los cambios en el archivo.
     * @param incidente El objeto {@link Incidente} con los datos actualizados.
     */
    public void actualizarIncidente(Incidente incidente) {
        for (int i = 0; i < incidentes.size(); i++) {
            if (incidentes.get(i).getId() == incidente.getId()) {
                incidentes.set(i, incidente);
                break;
            }
        }
        guardarIncidentes();
    }

    /**
     * Marca un incidente como resuelto, actualizando su estado y liberando al brigadista asignado.
     * Persiste los cambios en ambos servicios (Incidente y Brigadista).
     * @param id El ID del incidente a marcar como resuelto.
     */
    public void marcarComoResuelto(int id) {
        for (Incidente i : incidentes) {
            if (i.getId() == id) {
                i.setResuelto(true);
                i.setTiempoActivo("Finalizado");

                // Liberar brigadista si estaba asignado a través de BrigadistaService
                if (i.getIdBrigadista() != 0) {
                    Brigadista b = brigadistaService.buscarPorId(i.getIdBrigadista());
                    if (b != null) {
                        b.setEstado("Libre");
                        // Actualiza el estado del brigadista a través de su propio servicio
                        brigadistaService.updateBrigadista(b);
                    }
                }

                // Limpiar asignación en el incidente
                i.setIdBrigadista(0);
                i.setNombreBrigadista("");
                break;
            }
        }
        guardarIncidentes();
    }

    /**
     * Elimina un incidente de la lista en memoria y persiste los cambios.
     * Nota: La liberación del brigadista debe manejarse en el controlador antes de la eliminación si el incidente no está resuelto,
     * o bien delegarse aquí.
     * @param id El ID del incidente a eliminar.
     */
    public void eliminarIncidente(int id) {
        incidentes.removeIf(i -> i.getId() == id);
        guardarIncidentes();
    }

    /**
     * Desasigna un brigadista de un incidente específico, liberando al brigadista
     * y actualizando el incidente. Persiste los cambios en ambos servicios.
     * @param idIncidente El ID del incidente a modificar.
     */
    public void desasignarBrigadistaDeIncidente(int idIncidente) {
        Incidente inc = buscarPorId(idIncidente);
        if (inc != null) {
            // Liberar brigadista si estaba asignado (a través de BrigadistaService)
            if (inc.getIdBrigadista() != 0) {
                Brigadista b = brigadistaService.buscarPorId(inc.getIdBrigadista());
                if (b != null) {
                    b.setEstado("Libre");
                    brigadistaService.updateBrigadista(b);
                }
            }

            // Limpiar asignación en el incidente
            inc.setIdBrigadista(0);
            inc.setNombreBrigadista("");
            guardarIncidentes();
        }
    }

    // ====================================================================
    // MÉTODOS DE PERSISTENCIA
    // ====================================================================

    /**
     * Guarda la lista completa de incidentes en el archivo de persistencia mediante serialización.
     */
    private void guardarIncidentes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA_ARCHIVO))) {
            oos.writeObject(incidentes);
        } catch (IOException e) {
            System.err.println("Error al guardar incidentes.");
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de incidentes desde el archivo de persistencia mediante deserialización.
     * @return Una lista de {@link Incidente} cargada, o una lista vacía si falla o no existe el archivo.
     */
    @SuppressWarnings("unchecked")
    private List<Incidente> cargarIncidentes() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Incidente>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al cargar incidentes desde el archivo.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}