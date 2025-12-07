package controlador;

import java.util.List;
import javax.swing.JOptionPane;

import modelo.Brigadista;
import modelo.Incidente;
import servicio.IncidenteService;
import servicio.BrigadistaService;

public class AsignacionController {

    private IncidenteService incidenteService;
    private BrigadistaService brigadistaService;

    public interface Callback {
        void actualizar();
    }

    public AsignacionController(IncidenteService incidenteService, BrigadistaService brigadistaService) {
        this.incidenteService = incidenteService;
        this.brigadistaService = brigadistaService;
    }

    // --------------------------------------------------------------------
    // MÉTODO PRINCIPAL: ASIGNAR / DESASIGNAR
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // ASIGNAR BRIGADISTA
    // --------------------------------------------------------------------
    private void asignar(Incidente incidente, Callback callback) {

        List<Brigadista> libres = brigadistaService.getBrigadistasLibres();

        if (libres == null || libres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay brigadistas libres disponibles");
            return;
        }

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

        Brigadista elegido = libres.stream()
                .filter(b -> b.getNombre().equals(seleccionado))
                .findFirst()
                .orElse(null);

        if (elegido == null) return;

        // Asignar correctamente
        incidente.setIdBrigadista(elegido.getId());
        incidente.setNombreBrigadista(elegido.getNombre());
        elegido.setEstado("Ocupado"); // unificar estado

        incidenteService.actualizarIncidente(incidente);
        brigadistaService.updateBrigadista(elegido);

        callback.actualizar();
        JOptionPane.showMessageDialog(null, "Brigadista asignado correctamente.");
    }

    // --------------------------------------------------------------------
    // DESASIGNAR BRIGADISTA
    // --------------------------------------------------------------------
    private void desasignar(Incidente incidente, Callback callback) {

        int idBrig = incidente.getIdBrigadista();

        if (idBrig != 0) {
            Brigadista brig = brigadistaService.buscarPorId(idBrig);

            if (brig != null) {
                brig.setEstado("Libre");
                brigadistaService.updateBrigadista(brig);
            }
        }

        // limpiar asignación
        incidente.setIdBrigadista(0);
        incidente.setNombreBrigadista("");

        incidenteService.actualizarIncidente(incidente);

        callback.actualizar();
        JOptionPane.showMessageDialog(null, "Brigadista desasignado.");
    }

}


