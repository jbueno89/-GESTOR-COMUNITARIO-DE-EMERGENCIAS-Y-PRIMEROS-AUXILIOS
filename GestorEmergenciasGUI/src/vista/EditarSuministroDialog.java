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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import modelo.Suministro;
import controlador.SuministroController;

/**
 * Diálogo modal polivalente para la gestión de suministros.
 * Permite **crear** un nuevo suministro o **editar** uno existente, incluyendo la opción de **eliminación**.
 * Actúa como la **Vista/Formulario** en el **Patrón MVC**, manejando la captura de datos y delegando
 * las operaciones CRUD al {@link SuministroController}.
 * <p>
 * El modo de operación (Agregar o Editar) se define mediante el flag {@code esNuevo}.
 * </p>
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class EditarSuministroDialog extends JDialog {

    private final JTextField tfNombre, tfStock, tfUnidad, tfMinCritico, tfFechaCaducidad;
    private final JComboBox<String> cbUbicacion;
    private final JButton btnGuardar, btnCancelar, btnEliminar;

    /**
     * Referencia al controlador para manejar la lógica de negocio y persistencia.
     */
    private final SuministroController suministroController;
    /**
     * Referencia a la tabla principal que debe ser actualizada tras una operación CRUD.
     */
    private final JTable tableSuministros;
    /**
     * El objeto {@link Suministro} que se está editando o creando.
     */
    private final Suministro suministro;
    /**
     * Indica si la operación actual es para crear un nuevo suministro ({@code true}) o editar uno existente ({@code false}).
     */
    private final boolean esNuevo; 

    /**
     * Constructor del diálogo. Configura la interfaz de usuario y precarga los datos si es modo edición.
     * @param parent La ventana padre.
     * @param controller El controlador de suministros.
     * @param table La tabla de suministros a refrescar.
     * @param suministro El objeto suministro a editar (o uno nuevo vacío si es {@code esNuevo}).
     * @param esNuevo Bandera que indica si se está creando un nuevo suministro.
     */
    public EditarSuministroDialog(Window parent, SuministroController controller,
                                  JTable table, Suministro suministro, boolean esNuevo) {
        super(parent, esNuevo ? "Agregar Suministro" : "Editar Suministro", ModalityType.APPLICATION_MODAL);
        this.suministroController = controller;
        this.tableSuministros = table;
        this.suministro = suministro;
        this.esNuevo = esNuevo;

        // Configuración básica
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        setLayout(null);

        // --- Inicialización de Campos de Entrada ---
        
        // El constructor del Suministro debe asegurar que los valores por defecto no son nulos
        tfNombre = crearCampo("Nombre:", 30, suministro.getNombre());
        tfStock = crearCampo("Stock Actual:", 70, String.valueOf(suministro.getStockActual()));
        tfUnidad = crearCampo("Unidad (Kg/Uni):", 110, suministro.getUnidad());
        tfMinCritico = crearCampo("Mínimo Crítico:", 150, String.valueOf(suministro.getMinimoCritico()));

        // Ubicación (JComboBox)
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(20, 190, 120, 25);
        add(lblUbicacion);

        cbUbicacion = new JComboBox<>(new String[]{
                "Almacén Principal", "Almacén A", "Almacén B", "Almacén C"
        });
        cbUbicacion.setBounds(180, 190, 160, 25);
        cbUbicacion.setSelectedItem(suministro.getUbicacion());
        add(cbUbicacion);

        // Fecha de Caducidad. Formato por defecto para evitar NullPointerException.
        LocalDate fechaDefecto = suministro.getFechaCaducidad() != null ? suministro.getFechaCaducidad() : LocalDate.now();
        tfFechaCaducidad = crearCampo("Fecha Caducidad (yyyy-MM-dd):", 230, fechaDefecto.toString());

        // --- Botones ---
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(40, 280, 100, 30);
        add(btnGuardar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(150, 280, 100, 30);
        add(btnEliminar);
        // Mostrar/Ocultar el botón Eliminar según el modo
        btnEliminar.setVisible(!esNuevo); 

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(260, 280, 100, 30);
        add(btnCancelar);

        // --- Acciones ---
        btnGuardar.addActionListener(e -> guardarCambios());
        btnEliminar.addActionListener(e -> eliminarSuministro());
        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Método auxiliar para crear una etiqueta y su campo de texto asociado.
     * @param label Texto de la etiqueta.
     * @param y Coordenada Y.
     * @param valor Valor inicial del campo de texto.
     * @return El {@link JTextField} creado.
     */
    private JTextField crearCampo(String label, int y, String valor) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(20, y, 150, 25);
        add(lbl);
        JTextField tf = new JTextField(valor);
        tf.setBounds(180, y, 160, 25);
        add(tf);
        return tf;
    }

    /**
     * Maneja la recopilación, validación y persistencia de los datos del suministro.
     * Llama al método apropiado del controlador (agregar o actualizar) según el modo.
     */
    private void guardarCambios() {
        try {
            // 1. Recolección de datos
            String nombre = tfNombre.getText().trim();
            String stockText = tfStock.getText().trim();
            String unidad = tfUnidad.getText().trim();
            String minCriticoText = tfMinCritico.getText().trim();
            String fechaText = tfFechaCaducidad.getText().trim();
            
            // 2. Validación de campos obligatorios
            if (nombre.isEmpty() || stockText.isEmpty() || unidad.isEmpty() || minCriticoText.isEmpty() || fechaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 3. Parseo y asignación
            int stock = Integer.parseInt(stockText);
            int minCritico = Integer.parseInt(minCriticoText);
            LocalDate fechaCaducidad = LocalDate.parse(fechaText, DateTimeFormatter.ISO_LOCAL_DATE);

            // 4. Actualizar el objeto Suministro (Modelo)
            suministro.setNombre(nombre);
            suministro.setStockActual(stock);
            suministro.setUnidad(unidad);
            suministro.setMinimoCritico(minCritico);
            suministro.setUbicacion((String) cbUbicacion.getSelectedItem());
            suministro.setFechaCaducidad(fechaCaducidad);

            // 5. Ajustar crítico automáticamente y delegar al Controlador
            suministro.setCritico(suministro.getStockActual() <= suministro.getMinimoCritico());

            if (esNuevo) {
                // Delegar al controlador para asignar ID y persistir
                suministroController.agregarSuministro(suministro, tableSuministros);
                JOptionPane.showMessageDialog(this, "Suministro agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Delegar al controlador para actualizar y persistir
                suministroController.actualizarSuministro(suministro, tableSuministros);
                JOptionPane.showMessageDialog(this, "Suministro actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock y Mínimo Crítico deben ser números enteros válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd (ej: 2025-12-31).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar los cambios: " + ex.getMessage(), "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra una confirmación y delega la eliminación del suministro al controlador.
     */
    private void eliminarSuministro() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar este suministro permanentemente? Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            // Delegar la eliminación al controlador
            suministroController.eliminarSuministro(suministro.getId(), tableSuministros);
            JOptionPane.showMessageDialog(this, "Suministro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}