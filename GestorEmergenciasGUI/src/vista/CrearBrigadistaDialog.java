package vista;

import javax.swing.*;

import modelo.Brigadista;
import controlador.BrigadistaController;

public class CrearBrigadistaDialog extends JDialog {

    private JTextField tfId, tfNombre, tfTelefono, tfEspecialidad;
    private JComboBox<String> cbEstado;
    private JButton btnGuardar, btnCancelar;

    private final BrigadistaController brigadistaController;
    private final JTable tableBrigadistas;
    private final JFrame parent;

    public CrearBrigadistaDialog(JFrame parent, BrigadistaController controller, JTable table) {
        super(parent, "Añadir Brigadista", ModalityType.APPLICATION_MODAL);
        this.parent = parent;
        this.brigadistaController = controller;
        this.tableBrigadistas = table;

        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(null);

        // Campo ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);
        tfId = new JTextField();
        tfId.setBounds(140, 20, 200, 25);
        add(tfId);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 60, 100, 25);
        add(lblNombre);
        tfNombre = new JTextField();
        tfNombre.setBounds(140, 60, 200, 25);
        add(tfNombre);

        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setBounds(20, 100, 100, 25);
        add(lblTelefono);
        tfTelefono = new JTextField();
        tfTelefono.setBounds(140, 100, 200, 25);
        add(tfTelefono);

        // Estado
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(20, 140, 100, 25);
        add(lblEstado);
        cbEstado = new JComboBox<>(new String[] {"Libre", "En Servicio", "Descanso"});
        cbEstado.setBounds(140, 140, 200, 25);
        add(cbEstado);

        // Especialidad
        JLabel lblEspecialidad = new JLabel("Especialidad:");
        lblEspecialidad.setBounds(20, 180, 100, 25);
        add(lblEspecialidad);
        tfEspecialidad = new JTextField();
        tfEspecialidad.setBounds(140, 180, 200, 25);
        add(tfEspecialidad);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(60, 230, 100, 30);
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 230, 100, 30);
        add(btnCancelar);

        // Acciones
        btnGuardar.addActionListener(e -> guardarBrigadista());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarBrigadista() {
        try {
            int id = Integer.parseInt(tfId.getText().trim());
            String nombre = tfNombre.getText().trim();
            String telefono = tfTelefono.getText().trim();
            String estado = (String) cbEstado.getSelectedItem();
            String especialidad = tfEspecialidad.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || especialidad.isEmpty() || estado == null) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Brigadista b = new Brigadista(id, nombre, telefono, estado, especialidad); // 👈 usamos el ID ingresado
            brigadistaController.agregarBrigadista(b, tableBrigadistas);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


