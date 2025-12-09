/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package controlador;

import modelo.Brigadista;
import servicio.BrigadistaService;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la gestión de Brigadistas.
 * Actúa como la capa de control entre la Interfaz de Usuario (Vista) y la Lógica de Negocio (Servicio).
 * Maneja la lógica de manipulación de la colección de brigadistas (CRUD) y la actualización de tablas en la UI.
 * Demuestra el patrón **MVC (Controlador)** y la **Arquitectura por Capas**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class BrigadistaController {

    /**
     * Instancia del servicio que maneja la persistencia y las reglas de negocio de los Brigadistas.
     * Demuestra el patrón de **Arquitectura por Capas** (Controlador llamando al Servicio).
     */
    private final BrigadistaService brigadistaService;
    
    /**
     * Colección local de brigadistas cargada en memoria.
     * Es la fuente de datos para todas las operaciones de la UI.
     * Demuestra **Manejo de Colecciones**.
     */
    private List<Brigadista> brigadistas;

    /**
     * Constructor del controlador.
     * Inicializa el servicio y carga la lista completa de brigadistas desde el origen de datos.
     */
    public BrigadistaController() {
        brigadistaService = new BrigadistaService();
        brigadistas = new ArrayList<>(brigadistaService.getAllBrigadistas());
    }

    // ====================================================================
    // OPERACIONES BÁSICAS Y PERSISTENCIA
    // ====================================================================

    /**
     * Obtiene una copia de la lista completa de brigadistas en memoria.
     * @return Una nueva {@link List} que contiene todos los brigadistas.
     */
    public List<Brigadista> getAllBrigadistas() {
        return new ArrayList<>(brigadistas);
    }

    /**
     * Guarda el estado actual de la lista de brigadistas en el archivo de persistencia
     * sobrescribiendo el contenido anterior.
     * Este método asegura que la lista en memoria sea la fuente de verdad.
     */
    private void guardarCambios() {
        // Lógica de guardado que limpia y rescribe.
        brigadistaService.resetBrigadistas();
        for (Brigadista b : brigadistas) {
            brigadistaService.agregarBrigadista(b);
        }
    }

    /**
     * Agrega un nuevo brigadista a la lista, guarda los cambios y refresca la tabla.
     * @param b El objeto {@link Brigadista} a agregar.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void agregarBrigadista(Brigadista b, JTable table) {
        brigadistas.add(b);
        guardarCambios();
        actualizarTabla(table);
    }

    /**
     * Actualiza un brigadista existente en la lista, guarda los cambios y refresca la tabla.
     * El brigadista es identificado por su ID.
     * @param b El objeto {@link Brigadista} con los datos actualizados.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void actualizarBrigadista(Brigadista b, JTable table) {
        for (int i = 0; i < brigadistas.size(); i++) {
            if (brigadistas.get(i).getId() == b.getId()) {
                brigadistas.set(i, b);
                break;
            }
        }
        guardarCambios();
        actualizarTabla(table);
    }

    /**
     * Elimina un brigadista de la lista basándose en su ID, guarda los cambios y refresca la tabla.
     * @param id El ID del brigadista a eliminar.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void eliminarBrigadista(int id, JTable table) {
        brigadistas.removeIf(b -> b.getId() == id);
        guardarCambios();
        actualizarTabla(table);
    }

    // ====================================================================
    // MANEJO DE VISTA (JTABLE)
    // ====================================================================

    /**
     * Limpia y rellena la tabla de la UI con la lista completa de brigadistas en memoria.
     * @param table El componente {@link JTable} a rellenar.
     */
    public void actualizarTabla(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpia la tabla

        for (Brigadista b : brigadistas) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getNombre(),
                    b.getEstado(),
                    b.getEspecialidad(),
                    b.getTelefono()
            });
        }
    }

    /**
     * Limpia y rellena la tabla de la UI con una lista de brigadistas previamente filtrada.
     * @param table El componente {@link JTable} a rellenar.
     * @param lista La {@link List} de brigadistas ya filtrada.
     */
    public void actualizarTablaFiltrada(JTable table, List<Brigadista> lista) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpia la tabla

        for (Brigadista b : lista) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getNombre(),
                    b.getEstado(),
                    b.getEspecialidad(),
                    b.getTelefono()
            });
        }
    }

    // ====================================================================
    // FILTROS Y BÚSQUEDAS
    // ====================================================================

    /**
     * Filtra la lista de brigadistas por su estado de disponibilidad.
     * Si el estado es nulo o "Todos", devuelve la lista completa.
     * @param estado El estado por el cual filtrar (ej. "Libre", "En Servicio").
     * @return Una {@link List} de brigadistas que coinciden con el estado.
     */
    public List<Brigadista> filtrarPorEstado(String estado) {
        if (estado == null || estado.isEmpty() || estado.equalsIgnoreCase("Estado") || estado.equalsIgnoreCase("Todos")) {
            return getAllBrigadistas();
        }
        return brigadistas.stream()
                .filter(b -> b.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    /**
     * Busca brigadistas cuyo nombre contenga la cadena de búsqueda (búsqueda parcial).
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     * Demuestra el uso de la API Stream para filtros.
     * @param nombre La cadena de texto a buscar dentro del nombre completo.
     * @return Una {@link List} de brigadistas que coinciden con la búsqueda.
     */
    public List<Brigadista> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) return getAllBrigadistas();

        return brigadistas.stream()
                .filter(b -> b.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Busca un brigadista específico por su ID único.
     * @param id El ID numérico del brigadista a buscar.
     * @return El objeto {@link Brigadista} encontrado, o {@code null} si no existe.
     */
    public Brigadista buscarPorId(int id) {
        return brigadistas.stream().filter(b -> b.getId() == id).findFirst().orElse(null);
    }

    // ====================================================================
    // MANTENIMIENTO
    // ====================================================================

    /**
     * Ejecuta un reset completo: limpia la lista en memoria, limpia el archivo de persistencia
     * y refresca la tabla de la UI.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void resetBrigadistas(JTable table) {
        brigadistas.clear();
        guardarCambios(); // Esto borra el archivo
        actualizarTabla(table);
    }
}