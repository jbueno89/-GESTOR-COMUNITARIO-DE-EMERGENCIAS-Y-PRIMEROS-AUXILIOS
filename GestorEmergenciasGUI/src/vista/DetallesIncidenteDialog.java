package vista;

import javax.swing.*;
import java.awt.*;
import modelo.Incidente;

public class DetallesIncidenteDialog extends JDialog {

    public DetallesIncidenteDialog(Window parent, Incidente incidente) {
        super(parent, "Detalles del Incidente", ModalityType.APPLICATION_MODAL);
        setSize(350, 300);
        setLocationRelativeTo(parent);
        setLayout(null);

        // Etiquetas con los datos del incidente
        JLabel lblId = new JLabel("ID: " + incidente.getId());
        lblId.setBounds(20, 20, 300, 25);
        add(lblId);

        JLabel lblTipo = new JLabel("Tipo: " + incidente.getTipo());
        lblTipo.setBounds(20, 60, 300, 25);
        add(lblTipo);

        JLabel lblPrioridad = new JLabel("Prioridad: " + incidente.getPrioridad());
        lblPrioridad.setBounds(20, 100, 300, 25);
        add(lblPrioridad);

        JLabel lblUbicacion = new JLabel("Ubicación: " + incidente.getUbicacion());
        lblUbicacion.setBounds(20, 140, 300, 25);
        add(lblUbicacion);

        JLabel lblBrigadista = new JLabel("Brigadista: " + incidente.getBrigadistaAsignado());
        lblBrigadista.setBounds(20, 180, 300, 25);
        add(lblBrigadista);

        JLabel lblTiempo = new JLabel("Tiempo Activo: " + incidente.getTiempoActivo());
        lblTiempo.setBounds(20, 220, 300, 25);
        add(lblTiempo);
    }
}
