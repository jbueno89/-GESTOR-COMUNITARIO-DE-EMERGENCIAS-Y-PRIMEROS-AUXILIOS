package controlador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Window;
import java.util.List;

import modelo.Brigadista;
import modelo.Incidente;
import servicio.BrigadistaService;
import servicio.IncidenteService;

public class IncidenteController {

    private final IncidenteService incidenteService;
    private final BrigadistaService brigadistaService;

    // Constructor
    public IncidenteController(IncidenteService incidenteService, BrigadistaService brigadistaService) {
        this.incidenteService = incidenteService;
        this.brigadistaService = brigadistaService;
    }

    // ------------------------------------------------------------
    // Actualizar tabla de incidentes
    // ------------------------------------------------------------
    public void actualizarTabla(JTable table, String busqueda, String filtroPrioridad) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Incidente i : incidenteService.getAll()) {
            boolean coincideBusqueda = busqueda == null || busqueda.isEmpty()
                    || i.getTipo().toLowerCase().contains(busqueda.toLowerCase())
                    || i.getUbicacion().toLowerCase().contains(busqueda.toLowerCase());

            boolean coincideFiltro = filtroPrioridad == null
                    || filtroPrioridad.equals("Prioridad")
                    || i.getPrioridad().equalsIgnoreCase(filtroPrioridad.replace("Prioridad ", ""));

            if (coincideBusqueda && coincideFiltro && !i.isResuelto()) {
                // Resolver nombre de brigadista dinámicamente
                String nombreBrig = "";
                if (i.getIdBrigadista() != 0) {
                    Brigadista b = brigadistaService.buscarPorId(i.getIdBrigadista());
                    if (b != null) nombreBrig = b.getNombre();
                }

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

    // ------------------------------------------------------------
    // Obtener incidente por ID
    // ------------------------------------------------------------
    public Incidente getIncidentePorId(int id) {
        return incidenteService.getAll()
                .stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // ------------------------------------------------------------
    // Mostrar detalles del incidente
    // ------------------------------------------------------------
    public void mostrarDetallesIncidente(Window parent, JTable table) {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(parent, "Seleccione un incidente", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(fila, 0);
        Incidente incidente = getIncidentePorId(id);

        if (incidente != null) {
            new vista.DetallesIncidenteDialog(parent, incidente).setVisible(true);
        }
    }

    // ------------------------------------------------------------
    // Marcar incidente como resuelto
    // ------------------------------------------------------------
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

        // Liberar brigadista si tiene asignado
        if (inc.getIdBrigadista() != 0) {
            Brigadista b = brigadistaService.buscarPorId(inc.getIdBrigadista());
            if (b != null) {
                b.setEstado("Libre");
                brigadistaService.guardarBrigadistas();
            }
        }

        // Marcar incidente como resuelto
        incidenteService.marcarComoResuelto(id);
        JOptionPane.showMessageDialog(parent, "Incidente resuelto.");

        // 🔹 REFRESCAR TABLA inmediatamente
        actualizarTabla(table, "", "Prioridad");
    }

    // ------------------------------------------------------------
    // Agregar incidente
    // ------------------------------------------------------------
    public void agregarIncidente(Incidente incidente) {
        incidenteService.addIncidente(incidente);
    }

    // ------------------------------------------------------------
    // Asignar brigadista a un incidente
    // ------------------------------------------------------------
    public void asignarBrigadista(Window parent, JTable table) {
        int fila = table.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(parent, "Seleccione un incidente primero.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idIncidente = (int) table.getValueAt(fila, 0);
        Incidente incidente = getIncidentePorId(idIncidente);
        if (incidente == null) return;

        List<Brigadista> libres = brigadistaService.getBrigadistasLibres();
        if (libres.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay brigadistas libres para asignar.", "Sin personal disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombres = libres.stream().map(Brigadista::getNombre).toArray(String[]::new);
        String seleccionado = (String) JOptionPane.showInputDialog(parent, "Seleccione un brigadista:", "Asignar Brigadista", JOptionPane.PLAIN_MESSAGE, null, nombres, nombres[0]);
        if (seleccionado == null) return;

        Brigadista b = libres.stream().filter(x -> x.getNombre().equals(seleccionado)).findFirst().orElse(null);
        if (b == null) return;

        // Asignar brigadista
        incidente.setIdBrigadista(b.getId());
        incidente.setNombreBrigadista(b.getNombre());
        b.setEstado("En Servicio");

        incidenteService.actualizarIncidente(incidente);
        brigadistaService.guardarBrigadistas();

        JOptionPane.showMessageDialog(parent, "Brigadista asignado correctamente.");

        // 🔹 REFRESCAR TABLA
        actualizarTabla(table, "", "Prioridad");
    }

    // ------------------------------------------------------------
    // Desasignar brigadista de un incidente
    // ------------------------------------------------------------
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

        Brigadista b = brigadistaService.buscarPorId(incidente.getIdBrigadista());
        if (b != null) {
            b.setEstado("Libre");
            brigadistaService.guardarBrigadistas();
        }

        incidente.setIdBrigadista(0);
        incidente.setNombreBrigadista("");

        incidenteService.actualizarIncidente(incidente);

        JOptionPane.showMessageDialog(parent, "Brigadista desasignado.");

        // 🔹 REFRESCAR TABLA
        actualizarTabla(table, "", "Prioridad");
    }
}



