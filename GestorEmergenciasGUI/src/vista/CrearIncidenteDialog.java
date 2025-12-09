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
import java.awt.event.ActionListener;

/**
 * Diálogo modal para la creación de un nuevo incidente.
 * Este componente de la **Vista** recopila los datos necesarios (ID, Tipo, Prioridad, Ubicación)
 * y expone un método para adjuntar un {@link ActionListener} externo al botón "Guardar",
 * lo que permite que el Controlador maneje la lógica de persistencia.
 * Demuestra la separación de la **Vista** de la **Lógica de Control** en el Patrón MVC.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class CrearIncidenteDialog extends JDialog {

    private final JTextField txtId;
    private final JTextField txtTipo;
    private final JComboBox<String> cmbPrioridad;
    private final JTextField txtUbicacion;
    private final JButton btnGuardar;

    /**
     * Listener externo que será ejecutado cuando se presione el botón Guardar.
     * Típicamente es implementado por el Controller para manejar la acción de negocio.
     */
    private ActionListener guardarListener;

    /**
     * Constructor del diálogo. Configura la interfaz de usuario (UI).
     * @param parent La ventana padre sobre la que se muestra este diálogo.
     */
    public CrearIncidenteDialog(Window parent) {
        super(parent, "Crear Incidente", ModalityType.APPLICATION_MODAL);
        setSize(350, 270);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);

        // ---------- Componentes de la UI ----------

        // ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(120, 20, 180, 25);
        add(txtId);

        // Tipo
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setBounds(20, 60, 100, 25);
        add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setBounds(120, 60, 180, 25);
        add(txtTipo);

        // Prioridad
        JLabel lblPrioridad = new JLabel("Prioridad:");
        lblPrioridad.setBounds(20, 100, 100, 25);
        add(lblPrioridad);

        cmbPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        cmbPrioridad.setBounds(120, 100, 180, 25);
        add(cmbPrioridad);

        // Ubicación
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(20, 140, 100, 25);
        add(lblUbicacion);

        txtUbicacion = new JTextField();
        txtUbicacion.setBounds(120, 140, 180, 25);
        add(txtUbicacion);

        // Botón Guardar
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(120, 190, 100, 30);
        add(btnGuardar);

        // Listener interno del botón: su única responsabilidad es ejecutar el listener externo (Controlador)
        btnGuardar.addActionListener(e -> {
            if (guardarListener != null) {
                // Notifica al Controlador cuando se presiona el botón
                guardarListener.actionPerformed(e);
            }
        });
    }

    // ====================================================================
    // MÉTODOS DE ACCESO A DATOS (GETTERS)
    // Estos métodos son utilizados por el Controlador para extraer la información
    // de la UI y crear el objeto del Modelo.
    // ====================================================================

    /**
     * Obtiene el valor del campo ID como un entero.
     * @return El ID del incidente, o -1 si el formato es inválido (NumberFormatException).
     */
    public int getId() {
        try {
            return Integer.parseInt(txtId.getText().trim());
        } catch (NumberFormatException e) {
            return -1; // ID inválido
        }
    }

    /**
     * Obtiene el tipo de incidente ingresado.
     * @return El tipo.
     */
    public String getTipo() {
        return txtTipo.getText().trim();
    }

    /**
     * Obtiene el valor seleccionado del JComboBox de Prioridad.
     * @return La prioridad seleccionada.
     */
    public String getPrioridad() {
        // Garantiza que la prioridad no sea nula si el JComboBox está inicializado correctamente
        if (cmbPrioridad.getSelectedItem() != null) {
            return cmbPrioridad.getSelectedItem().toString();
        }
        return "";
    }

    /**
     * Obtiene la ubicación del incidente ingresada.
     * @return La ubicación.
     */
    public String getUbicacion() {
        return txtUbicacion.getText().trim();
    }

    // ====================================================================
    // EXPOSICIÓN DE EVENTOS
    // ====================================================================

    /**
     * Permite asignar un {@link ActionListener} externo al botón Guardar,
     * desacoplando la lógica de negocio de la vista. Esto es clave en el patrón MVC.
     * @param listener El {@link ActionListener} a ejecutar al hacer clic en Guardar.
     */
    public void setGuardarListener(ActionListener listener) {
        this.guardarListener = listener;
    }
}