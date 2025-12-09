/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package vista;

import javax.swing.*;
import java.awt.*;
import modelo.Incidente;

/**
 * Diálogo modal de la **Vista** que muestra los detalles completos de un
 * objeto {@link Incidente} específico.
 * Es una clase de presentación pura, sin lógica de negocio o manipulación de datos,
 * actuando como un componente de solo lectura en el **Patrón MVC**.
 * Muestra el ID, Tipo, Prioridad, Ubicación, Brigadista asignado y Tiempo Activo.
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class DetallesIncidenteDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor del diálogo.
     * Muestra la información del incidente recibido como argumento. Configura la interfaz de usuario (UI).
     * @param parent La ventana padre sobre la que se muestra este diálogo.
     * @param incidente El objeto {@link Incidente} cuyos detalles se van a mostrar.
     */
    public DetallesIncidenteDialog(Window parent, Incidente incidente) {
        super(parent, "Detalles del Incidente", ModalityType.APPLICATION_MODAL);
        setSize(350, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);

        // --- Etiquetas con los datos del incidente ---
        
        // ID
        JLabel lblId = new JLabel("ID: " + incidente.getId());
        lblId.setBounds(20, 20, 300, 25);
        add(lblId);

        // Tipo
        JLabel lblTipo = new JLabel("Tipo: " + incidente.getTipo());
        lblTipo.setBounds(20, 60, 300, 25);
        add(lblTipo);

        // Prioridad
        JLabel lblPrioridad = new JLabel("Prioridad: " + incidente.getPrioridad());
        lblPrioridad.setBounds(20, 100, 300, 25);
        add(lblPrioridad);

        // Ubicación
        JLabel lblUbicacion = new JLabel("Ubicación: " + incidente.getUbicacion());
        lblUbicacion.setBounds(20, 140, 300, 25);
        add(lblUbicacion);

        // Brigadista (muestra N/A si no hay nombre asignado)
        String nombreBrigadista = incidente.getNombreBrigadista();
        JLabel lblBrigadista = new JLabel("Brigadista: " + 
            (nombreBrigadista == null || nombreBrigadista.isEmpty() ? "N/A" : nombreBrigadista));
        lblBrigadista.setBounds(20, 180, 300, 25);
        add(lblBrigadista);

        // Tiempo Activo
        JLabel lblTiempo = new JLabel("Tiempo Activo: " + incidente.getTiempoActivo());
        lblTiempo.setBounds(20, 220, 300, 25);
        add(lblTiempo);
    }
}