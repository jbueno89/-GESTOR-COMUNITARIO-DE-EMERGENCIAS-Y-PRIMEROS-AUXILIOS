package controlador;

import java.awt.Window;
import vista.CrearIncidenteDialog;
import servicio.IncidenteService;
import modelo.Incidente;

public class CrearIncidenteController {

    private final IncidenteService incidenteService;

    // Constructor
    public CrearIncidenteController(IncidenteService incidenteService) {
        this.incidenteService = incidenteService;
    }

    // Método para mostrar el diálogo de creación de incidente
    public void mostrarDialogoCrearIncidente(Window parent, Runnable callback) {

        CrearIncidenteDialog dialog = new CrearIncidenteDialog(parent);

        // Listener del botón Guardar
        dialog.setGuardarListener(e -> {

            int id = dialog.getId();
            String tipo = dialog.getTipo();
            String prioridad = dialog.getPrioridad();
            String ubicacion = dialog.getUbicacion();

            // Crear el incidente usando el constructor correcto (5 parámetros)
            Incidente nuevo = new Incidente(id, tipo, prioridad, ubicacion, "");

            // Agregar el incidente al servicio
            incidenteService.addIncidente(nuevo);

            // Ejecutar callback para refrescar la tabla en el PanelPrincipal
            if (callback != null) {
                callback.run();
            }

            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}

