package controlador;

import java.awt.Window;
import vista.CrearIncidenteDialog;
import servicio.IncidenteService;
import modelo.Incidente;

public class CrearIncidenteController {

    private IncidenteService incidenteService;

    // Constructor correcto
    public CrearIncidenteController(IncidenteService incidenteService) {
        this.incidenteService = incidenteService;
    }

    // Método que pide tu PanelPrincipal
    public void mostrarDialogoCrearIncidente(Window parent, Runnable callback) {

        CrearIncidenteDialog dialog = new CrearIncidenteDialog(parent);

        // Listener del botón Guardar
        dialog.setGuardarListener(e -> {

            int id = dialog.getId();
            String tipo = dialog.getTipo();
            String prioridad = dialog.getPrioridad();
            String ubicacion = dialog.getUbicacion();

            Incidente nuevo = new Incidente(id, tipo, prioridad, ubicacion, "", "");
            incidenteService.addIncidente(nuevo);

            // Actualizar tabla en el PanelPrincipal
            if (callback != null) {
                callback.run();
            }

            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}
