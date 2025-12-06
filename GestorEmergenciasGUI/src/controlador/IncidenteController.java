package controlador;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Window;
import java.util.List;
import modelo.Incidente;
import servicio.IncidenteService;
import vista.DetallesIncidenteDialog;

import java.awt.*;

public class IncidenteController {

    private final IncidenteService incidenteService;

    public IncidenteController(IncidenteService incidenteService) {
        this.incidenteService = incidenteService;
    }

    // ---------------- Actualizar tabla ----------------
    public void actualizarTabla(JTable table, String busqueda, String filtroPrioridad) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // limpiar tabla

        for (Incidente i : incidenteService.getAll()) {
            boolean coincideBusqueda = busqueda == null || busqueda.isEmpty()
                    || i.getTipo().toLowerCase().contains(busqueda.toLowerCase())
                    || i.getUbicacion().toLowerCase().contains(busqueda.toLowerCase());

            boolean coincideFiltro = filtroPrioridad == null
                    || filtroPrioridad.equals("Prioridad") // "Prioridad" = mostrar todas
                    || i.getPrioridad().equalsIgnoreCase(filtroPrioridad.replace("Prioridad ", ""));

            if (coincideBusqueda && coincideFiltro && !i.isResuelto()) {
                model.addRow(new Object[]{
                        i.getId(),
                        i.getTipo(),
                        i.getPrioridad(),
                        i.getUbicacion(),
                        i.getTiempoActivo(),
                        i.getBrigadistaAsignado()
                });
            }
        }
    }

    // ---------------- Obtener incidente por ID ----------------
    public Incidente getIncidentePorId(int id) {
        for (Incidente i : incidenteService.getAll()) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    // ---------------- Mostrar detalles ----------------
    public void mostrarDetallesIncidente(Window parent, JTable table) {
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) table.getValueAt(filaSeleccionada, 0); // columna 0 = ID
            Incidente incidente = getIncidentePorId(id);
            if (incidente != null) {
                new vista.DetallesIncidenteDialog(parent, incidente).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Seleccione un incidente para ver detalles",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // ---------------- Marcar como resuelto ----------------
    public void marcarIncidenteResuelto(Window parent, JTable table) {
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) table.getValueAt(filaSeleccionada, 0); // columna 0 = ID
            int confirm = JOptionPane.showConfirmDialog(parent,
                    "¿Desea marcar este incidente como resuelto?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                incidenteService.marcarComoResuelto(id); // <<--- Aquí se marca y guarda automáticamente
                JOptionPane.showMessageDialog(parent, "Incidente marcado como resuelto.");
            }
        } else {
            JOptionPane.showMessageDialog(parent,
                    "Seleccione un incidente para marcar como resuelto",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // ---------------- Añadir un nuevo incidente ----------------
    public void agregarIncidente(Incidente incidente) {
        incidenteService.addIncidente(incidente); // ya guarda automáticamente
    }

}
