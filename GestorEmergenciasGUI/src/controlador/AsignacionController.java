/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package controlador;

import java.util.List;
import javax.swing.JOptionPane;

import modelo.Brigadista;
import modelo.Incidente;
import servicio.IncidenteService;
import servicio.BrigadistaService;

/**
 * Controlador encargado de la lógica de asignación y desasignación de brigadistas a incidentes.
 * Actúa como la capa intermedia entre la Interfaz de Usuario (Vista) y la Lógica de Negocio (Servicios),
 * implementando la lógica del flujo de trabajo de recursos humanos.
 * Demuestra el patrón **MVC (Controlador)** y **Inyección de Dependencias** (a través del constructor).
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class AsignacionController {

    /**
     * Servicio para acceder y manipular datos de Incidentes.
     */
    private IncidenteService incidenteService;
    
    /**
     * Servicio para acceder y manipular datos de Brigadistas.
     */
    private BrigadistaService brigadistaService;

    /**
     * Interfaz de callback utilizada para notificar a la Vista (UI) que debe actualizar su contenido
     * después de una operación exitosa de asignación o desasignación.
     * Demuestra el patrón **Callback / Observer** para comunicación entre capas.
     */
    public interface Callback {
        
        /**
         * Método llamado para solicitar a la Vista que refresque la información.
         */
        void actualizar();
    }

    /**
     * Constructor del controlador de asignaciones.
     * Implementa Inyección de Dependencias para desacoplar el controlador de los servicios.
     * * @param incidenteService Servicio de incidentes.
     * @param brigadistaService Servicio de brigadistas.
     */
    public AsignacionController(IncidenteService incidenteService, BrigadistaService brigadistaService) {
        this.incidenteService = incidenteService;
        this.brigadistaService = brigadistaService;
    }

    // ====================================================================
    // MÉTODO PRINCIPAL: ASIGNAR / DESASIGNAR
    // ====================================================================
    
    /**
     * Procesa la solicitud de asignación o desasignación para un incidente específico.
     * Si el incidente ya tiene un brigadista asignado, pregunta al usuario si desea desasignarlo.
     * Si no tiene brigadista, inicia el proceso de asignación.
     * * @param idIncidente El ID del incidente a procesar.
     * @param callback La interfaz de callback para notificar a la vista sobre cambios.
     */
    public void procesarAsignacion(int idIncidente, Callback callback) {

        Incidente incidente = incidenteService.buscarPorId(idIncidente);

        if (incidente == null) {
            JOptionPane.showMessageDialog(null, "Error: incidente no encontrado");
            return;
        }

        // SI YA TIENE BRIGADISTA → DESASIGNAR
        if (incidente.getIdBrigadista() != 0) {

            int option = JOptionPane.showConfirmDialog(
                    null,
                    "Este incidente tiene el brigadista: " + incidente.getNombreBrigadista() +
                    "\n¿Desea desasignarlo?",
                    "Desasignar brigadista",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                desasignar(incidente, callback);
            }

            return;
        }

        // NO TIENE BRIGADISTA → ASIGNAR UNO
        asignar(incidente, callback);
    }

    // ====================================================================
    // ASIGNAR BRIGADISTA (Método Privado)
    // ====================================================================
    
    /**
     * Muestra una lista de brigadistas libres y asigna el seleccionado al incidente.
     * El estado del brigadista elegido cambia a "Ocupado".
     * * @param incidente El objeto Incidente al que se asignará el brigadista.
     * @param callback La interfaz de callback para notificar a la vista.
     */
    private void asignar(Incidente incidente, Callback callback) {

        List<Brigadista> libres = brigadistaService.getBrigadistasLibres();

        if (libres == null || libres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay brigadistas libres disponibles");
            return;
        }

        // Convertir la lista de objetos a un array de nombres para el JDialog
        String[] nombresLibres = libres.stream()
                .map(Brigadista::getNombre)
                .toArray(String[]::new);

        String seleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione un brigadista disponible:",
                "Asignar Brigadista",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nombresLibres,
                nombresLibres[0]
        );

        if (seleccionado == null) return;

        // Buscar el objeto Brigadista elegido
        Brigadista elegido = libres.stream()
                .filter(b -> b.getNombre().equals(seleccionado))
                .findFirst()
                .orElse(null);

        if (elegido == null) return;

        // Aplicar la asignación en ambos objetos y persistir
        incidente.setIdBrigadista(elegido.getId());
        incidente.setNombreBrigadista(elegido.getNombre());
        elegido.setEstado("En Servicio"); // El estado debe unificarse ("En Servicio" en Brigadista, "Ocupado" aquí)

        incidenteService.actualizarIncidente(incidente);
        brigadistaService.updateBrigadista(elegido);

        callback.actualizar();
        JOptionPane.showMessageDialog(null, "Brigadista asignado correctamente: " + elegido.getNombre());
    }

    // ====================================================================
    // DESASIGNAR BRIGADISTA (Método Privado)
    // ====================================================================
    
    /**
     * Elimina la asignación de brigadista del incidente y lo marca como "Libre" en su objeto.
     * * @param incidente El objeto Incidente del cual se desasignará el brigadista.
     * @param callback La interfaz de callback para notificar a la vista.
     */
    private void desasignar(Incidente incidente, Callback callback) {

        int idBrig = incidente.getIdBrigadista();

        if (idBrig != 0) {
            Brigadista brig = brigadistaService.buscarPorId(idBrig);

            if (brig != null) {
                // Actualizar el estado del brigadista
                brig.setEstado("Libre");
                brigadistaService.updateBrigadista(brig);
            }
        }

        // Limpiar la asignación en el incidente
        incidente.setIdBrigadista(0);
        incidente.setNombreBrigadista("");

        incidenteService.actualizarIncidente(incidente);

        callback.actualizar();
        JOptionPane.showMessageDialog(null, "Brigadista desasignado y marcado como Libre.");
    }

}
