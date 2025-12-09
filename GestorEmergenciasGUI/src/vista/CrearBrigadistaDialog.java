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
import modelo.Brigadista;
import controlador.BrigadistaController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Diálogo modal para la creación de un nuevo Brigadista.
 * Permite al usuario ingresar todos los datos del brigadista, incluyendo el ID.
 * Actúa como la **Vista** en el **Patrón MVC**, manejando la interacción con el usuario
 * y delegando la lógica de negocio al {@link BrigadistaController}.
 * <p>
 * Este componente es modal, bloqueando la interacción con el marco principal hasta que se cierra.
 * </p>
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class CrearBrigadistaDialog extends JDialog {

    private final JTextField tfId, tfNombre, tfTelefono, tfEspecialidad;
    private final JComboBox<String> cbEstado;
    private final JButton btnGuardar, btnCancelar;

    /**
     * Referencia al controlador para manejar la lógica de negocio.
     */
    private final BrigadistaController brigadistaController;
    
    /**
     * Referencia a la tabla principal que debe ser actualizada después de guardar
     * para reflejar los cambios en la UI.
     */
    private final JTable tableBrigadistas;
    
    /**
     * Referencia al JFrame padre (utilizado en el constructor de JDialog).
     */
    private final JFrame parent;

    /**
     * Constructor del diálogo. Configura la interfaz de usuario (UI) y los oyentes de eventos.
     * @param parent El marco padre.
     * @param controller El controlador de brigadistas, responsable de la persistencia.
     * @param table La tabla de brigadistas a refrescar.
     */
    public CrearBrigadistaDialog(JFrame parent, BrigadistaController controller, JTable table) {
        super(parent, "Añadir Brigadista", ModalityType.APPLICATION_MODAL);
        this.parent = parent;
        this.brigadistaController = controller;
        this.tableBrigadistas = table;

        // Configuración básica del diálogo
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);

        // --- Inicialización de Componentes de la UI ---
        
        // Campo ID
        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);
        tfId = new JTextField();
        tfId.setBounds(140, 20, 200, 25);
        // Sugerencia: En sistemas reales, este campo se deshabilitaría
        // y el ID se generaría automáticamente en el controlador/servicio.
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

        // --- Acciones (Delegación) ---
        // El botón Guardar delega la tarea al método interno guardarBrigadista()
        btnGuardar.addActionListener(e -> guardarBrigadista());
        // El botón Cancelar simplemente cierra el diálogo
        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Maneja la lógica de guardado:
     * 1. Valida la entrada del usuario.
     * 2. Crea el objeto {@link Brigadista} (Modelo).
     * 3. Utiliza el controlador ({@link BrigadistaController}) para persistir el objeto
     * y actualizar la tabla principal.
     */
    private void guardarBrigadista() {
        try {
            // 1. Recolección y validación de datos
            String idText = tfId.getText().trim();
            if (idText.isEmpty()) {
                 // Si el ID está vacío, se podría generar en el servicio, pero se asume que debe ser ingresado.
                 JOptionPane.showMessageDialog(this, "El ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            
            int id = Integer.parseInt(idText);
            String nombre = tfNombre.getText().trim();
            String telefono = tfTelefono.getText().trim();
            String estado = (String) cbEstado.getSelectedItem();
            String especialidad = tfEspecialidad.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty() || especialidad.isEmpty() || estado == null) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Opcional: Validar si el ID ya existe antes de crear
            if (brigadistaController.buscarPorId(id) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un brigadista con el ID: " + id, "Error de ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Crear el objeto Brigadista (Modelo)
            Brigadista b = new Brigadista(id, nombre, telefono, estado, especialidad);

            // 3. Delegar la acción al Controlador
            brigadistaController.agregarBrigadista(b, tableBrigadistas);
            
            JOptionPane.showMessageDialog(this, "Brigadista agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Cerrar el diálogo
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}