/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package controlador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Window;
import java.util.List;

import modelo.Brigadista;
import modelo.Incidente;
import servicio.BrigadistaService;
import servicio.IncidenteService;

/**
 * Controlador principal para la gestión y visualización de incidentes.
 * Es responsable de la coordinación de la vista de la tabla de incidentes
 * con las operaciones de negocio (Servicios) como la búsqueda, filtrado,
 * y las acciones de marcar como resuelto o asignar personal.
 * Demuestra el patrón **MVC (Controlador)**, **Inyección de Dependencias** y **Arquitectura por Capas**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class IncidenteController {

    /**
     * Servicio para la lógica de negocio y persistencia de incidentes.
     */
    private final IncidenteService incidenteService;
    
    /**
     * Servicio para la lógica de negocio y persistencia de brigadistas, necesario para asignaciones.
     */
    private final BrigadistaService brigadistaService;

    /**
     * Constructor del controlador.
     * Recibe y establece los servicios necesarios.
     * @param incidenteService La instancia de {@link IncidenteService}.
     * @param brigadistaService La instancia de {@link BrigadistaService}.
     */
    public IncidenteController(IncidenteService incidenteService, BrigadistaService brigadistaService) {
        this.incidenteService = incidenteService;
        this.brigadistaService = brigadistaService;
    }

    // ====================================================================
    // MANEJO DE VISTA (JTABLE)
    // ====================================================================

    /**
     * Actualiza el modelo de la tabla de incidentes aplicando filtros de búsqueda y prioridad.
     * Solo muestra incidentes que **NO** están resueltos.
     * @param table El componente {@link JTable} a actualizar.
     * @param busqueda Cadena de texto para buscar en tipo o ubicación. Puede ser {@code null} o vacío.
     * @param filtroPrioridad Filtro para prioridad (ej. "Alta", "Media").
     */
    public void actualizarTabla(JTable table, String busqueda, String filtroPrioridad) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpia la tabla

        for (Incidente i : incidenteService.getAll()) {
            
            // 1. Lógica de Filtrado:
            boolean coincideBusqueda = busqueda == null || busqueda.isEmpty()
                    || i.getTipo().toLowerCase().contains(busqueda.toLowerCase())
                    || i.getUbicacion().toLowerCase().contains(busqueda.toLowerCase());

            boolean coincideFiltro = filtroPrioridad == null
                    || filtroPrioridad.equals("Prioridad")
                    || i.getPrioridad().equalsIgnoreCase(filtroPrioridad.replace("Prioridad ", "")); // Maneja el prefijo en el JComboBox

            // Solo agrega si coincide con los filtros y no está resuelto
            if (coincideBusqueda && coincideFiltro && !i.isResuelto()) {
                
                // 2. Obtener nombre del Brigadista (Enriquecimiento de datos para la Vista)
                String nombreBrig = "";
                if (i.getIdBrigadista() != 0) {
                    Brigadista b = brigadistaService.buscarPorId(i.getIdBrigadista());
                    if (b != null) nombreBrig = b.getNombre();
                }

                // 3. Rellenar la fila
                model.addRow(new Object[]{
                        i.getId(),
                        i.getTipo(),
                        i.getPrioridad(),
                        i.getUbicacion(),
                        i.getTiempoActivo(),
                        nombreBrig
                });
            }
        }
    }

    // ====================================================================
    // CONSULTAS
    // ====================================================================

    /**
     * Obtiene un incidente específico de la lista de todos los incidentes por su ID.
     * @param id El ID del incidente a buscar.
     * @return El objeto {@link Incidente} encontrado, o {@code null} si no existe.
     */
    public Incidente getIncidentePorId(int id) {
        return incidenteService.getAll()
                .stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ====================================================================
    // ACCIONES DE LA UI
    // ====================================================================

    /**
     * Muestra una ventana de diálogo con los detalles completos del incidente seleccionado en la tabla.
     * @param parent La ventana padre para el diálogo.
     * @param table La tabla {@link JTable} de incidentes.
     */
    public void mostrarDetallesIncidente(Window parent, JTable table) {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(parent, "Seleccione un incidente", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(fila, 0);
        Incidente incidente = getIncidentePorId(id);

        if (incidente != null) {
            // Se asume que vista.DetallesIncidenteDialog existe y es modal
            new vista.DetallesIncidenteDialog(parent, incidente).setVisible(true);
        }
    }

    /**
     * Marca el incidente seleccionado en la tabla como resuelto.
     * Si el incidente tenía un brigadista asignado, lo libera (cambia su estado a "Libre").
     * @param parent La ventana padre para los diálogos de confirmación y mensaje.
     * @param table La tabla {@link JTable} de incidentes.
     */
    public void marcarIncidenteResuelto(Window parent, JTable table) {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(parent, "Seleccione un incidente", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(fila, 0);
        Incidente inc = getIncidentePorId(id);

        if (inc == null) return;

        int confirm = JOptionPane.showConfirmDialog(parent, "¿Desea marcar este incidente como resuelto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Liberar brigadista si tiene asignado (Actualización de recurso)
        if (inc.getIdBrigadista() != 0) {
            Brigadista b = brigadistaService.buscarPorId(inc.getIdBrigadista());
            if (b != null) {
                b.setEstado("Libre");
                // Es importante guardar la persistencia del brigadista
                brigadistaService.guardarBrigadistas();
            }
        }

        // Marcar incidente como resuelto (Persistencia del incidente)
        incidenteService.marcarComoResuelto(id);
        JOptionPane.showMessageDialog(parent, "Incidente resuelto.");

        // Refrescar tabla para reflejar el cambio
        actualizarTabla(table, "", "Prioridad");
    }

    /**
     * Agrega un nuevo incidente al sistema a través del servicio.
     * Este método es llamado por el {@link CrearIncidenteController}.
     * @param incidente El objeto {@link Incidente} a agregar.
     */
    public void agregarIncidente(Incidente incidente) {
        incidenteService.addIncidente(incidente);
    }

    /**
     * Muestra una ventana de selección para asignar un brigadista libre al incidente seleccionado.
     * Si la asignación es exitosa, el estado del brigadista cambia a "En Servicio".
     * @param parent La ventana padre para los diálogos.
     * @param table La tabla {@link JTable} de incidentes.
     */
    public void asignarBrigadista(Window parent, JTable table) {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(parent, "Seleccione un incidente primero.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idIncidente = (int) table.getValueAt(fila, 0);
        Incidente incidente = getIncidentePorId(idIncidente);
        if (incidente == null) return;

        // Búsqueda de recursos disponibles
        List<Brigadista> libres = brigadistaService.getBrigadistasLibres();
        if (libres.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay brigadistas libres para asignar.", "Sin personal disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Interacción con el usuario para seleccionar brigadista
        String[] nombres = libres.stream().map(Brigadista::getNombre).toArray(String[]::new);
        String seleccionado = (String) JOptionPane.showInputDialog(parent, "Seleccione un brigadista:", "Asignar Brigadista", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (seleccionado == null) return;

        Brigadista b = libres.stream().filter(x -> x.getNombre().equals(seleccionado)).findFirst().orElse(null);
        if (b == null) return;

        // Asignar brigadista y cambiar su estado
        incidente.setIdBrigadista(b.getId());
        incidente.setNombreBrigadista(b.getNombre());
        b.setEstado("En Servicio"); // El estado debe unificarse con el modelo de Brigadista

        // Persistencia de los cambios en ambos objetos
        incidenteService.actualizarIncidente(incidente);
        brigadistaService.guardarBrigadistas();

        JOptionPane.showMessageDialog(parent, "Brigadista asignado correctamente.");

        // Refrescar tabla para mostrar el nombre del brigadista
        actualizarTabla(table, "", "Prioridad");
    }

    /**
     * Desasigna al brigadista actualmente asignado al incidente seleccionado.
     * Si la desasignación es exitosa, el estado del brigadista cambia a "Libre".
     * @param parent La ventana padre para los diálogos.
     * @param table La tabla {@link JTable} de incidentes.
     */
    public void desasignarBrigadista(Window parent, JTable table) {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(parent, "Seleccione un incidente.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idIncidente = (int) table.getValueAt(fila, 0);
        Incidente incidente = getIncidentePorId(idIncidente);
        if (incidente == null || incidente.getIdBrigadista() == 0) {
            JOptionPane.showMessageDialog(parent, "Este incidente no tiene brigadista asignado.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(parent, "¿Desea quitar al brigadista asignado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Liberar al brigadista y actualizar su persistencia
        Brigadista b = brigadistaService.buscarPorId(incidente.getIdBrigadista());
        if (b != null) {
            b.setEstado("Libre");
            brigadistaService.guardarBrigadistas();
        }

        // Limpiar asignación del incidente
        incidente.setIdBrigadista(0);
        incidente.setNombreBrigadista("");

        // Persistencia de los cambios en el incidente
        incidenteService.actualizarIncidente(incidente);

        JOptionPane.showMessageDialog(parent, "Brigadista desasignado.");

        // Refrescar tabla para reflejar la desasignación
        actualizarTabla(table, "", "Prioridad");
    }
}