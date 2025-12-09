/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package controlador;

import java.awt.Window;
import vista.CrearIncidenteDialog;
import servicio.IncidenteService;
import modelo.Incidente;

/**
 * Controlador para manejar la lógica de la ventana de creación de un nuevo incidente.
 * Este controlador es responsable de coordinar la **Vista** (CrearIncidenteDialog)
 * con el **Modelo/Servicio** (IncidenteService) y aplicar la lógica de guardado.
 */
public class CrearIncidenteController {

    /**
     * Servicio que proporciona acceso a la lógica de negocio y persistencia de incidentes.
     * Demuestra el patrón de **Inyección de Dependencias** y **Arquitectura por Capas**.
     */
    private final IncidenteService incidenteService;

    /**
     * Constructor del controlador.
     * Recibe el servicio necesario para las operaciones de negocio.
     * @param incidenteService La instancia del {@link IncidenteService} para gestionar los datos.
     */
    public CrearIncidenteController(IncidenteService incidenteService) {
        this.incidenteService = incidenteService;
    }

    /**
     * Muestra la ventana de diálogo para que el usuario ingrese los datos del nuevo incidente.
     * Este método configura el listener para el botón de guardar y maneja la creación
     * del objeto {@link Incidente} y su posterior persistencia.
     *
     * @param parent La ventana padre que bloquea este diálogo (Window).
     * @param callback Una interfaz {@link Runnable} que se ejecuta después de guardar
     * un incidente exitosamente, típicamente para refrescar una tabla.
     */
    public void mostrarDialogoCrearIncidente(Window parent, Runnable callback) {

        CrearIncidenteDialog dialog = new CrearIncidenteDialog(parent);

        // Listener del botón Guardar (Lógica de control y coordinación)
        dialog.setGuardarListener(e -> {

            int id = dialog.getId();
            String tipo = dialog.getTipo();
            String prioridad = dialog.getPrioridad();
            String ubicacion = dialog.getUbicacion();

            // Crear el incidente usando el constructor de la clase modelo
            Incidente nuevo = new Incidente(id, tipo, prioridad, ubicacion, "");

            // Agregar el incidente al servicio (Persistencia)
            incidenteService.addIncidente(nuevo);

            // Ejecutar callback para refrescar la tabla en el PanelPrincipal (Notificación a la Vista)
            if (callback != null) {
                callback.run();
            }

            // Cerrar el diálogo
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
}