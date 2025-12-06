package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CrearIncidenteDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtTipo;
    private JComboBox<String> cmbPrioridad;
    private JTextField txtUbicacion;
    private JButton btnGuardar;

    private ActionListener guardarListener;

    // Constructor
    public CrearIncidenteDialog(Window parent) {
        super(parent, "Crear Incidente");
        setSize(350, 270);
        setLocationRelativeTo(parent);
        setLayout(null);

        // ---------- ID ----------
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(120, 20, 180, 25);
        add(txtId);

        // ---------- Tipo ----------
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 60, 100, 25);
        add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setBounds(120, 60, 180, 25);
        add(txtTipo);

        // ---------- Prioridad ----------
        JLabel lblPrioridad = new JLabel("Prioridad:");
        lblPrioridad.setBounds(20, 100, 100, 25);
        add(lblPrioridad);

        cmbPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        cmbPrioridad.setBounds(120, 100, 180, 25);
        add(cmbPrioridad);

        // ---------- Ubicación ----------
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(20, 140, 100, 25);
        add(lblUbicacion);

        txtUbicacion = new JTextField();
        txtUbicacion.setBounds(120, 140, 180, 25);
        add(txtUbicacion);

        // ---------- Botón Guardar ----------
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(120, 190, 100, 30);
        add(btnGuardar);

        // Listener interno del botón, llama al listener externo si existe
        btnGuardar.addActionListener(e -> {
            if (guardarListener != null) {
                guardarListener.actionPerformed(e);
            }
        });
    }

    // ---------- Métodos para obtener valores ----------
    public int getId() {
        try {
            return Integer.parseInt(txtId.getText().trim());
        } catch (NumberFormatException e) {
            return -1; // ID inválido
        }
    }

    public String getTipo() {
        return txtTipo.getText().trim();
    }

    public String getPrioridad() {
        return cmbPrioridad.getSelectedItem().toString();
    }

    public String getUbicacion() {
        return txtUbicacion.getText().trim();
    }

    // ---------- Permite asignar un listener externo para el botón Guardar ----------
    public void setGuardarListener(ActionListener listener) {
        this.guardarListener = listener;
    }
}
