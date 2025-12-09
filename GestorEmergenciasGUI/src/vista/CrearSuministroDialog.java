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
 * Diálogo modal para la creación de un nuevo Suministro.
 * Permite al usuario ingresar todos los datos requeridos para un suministro
 * (nombre, stock, unidad, mínimo crítico, ubicación y fecha de caducidad).
 * Actúa como la **Vista** en el **Patrón MVC**, manejando la interacción con el usuario
 * y delegando la lógica de negocio al {@link SuministroController}.
 * Demuestra manejo de tipos de datos complejos como {@link LocalDate}.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class CrearSuministroDialog extends JDialog {

    private final JTextField tfNombre, tfStock, tfUnidad, tfMinCritico, tfFechaCaducidad;
    private final JComboBox<String> cbUbicacion;
    private final JButton btnGuardar, btnCancelar;

    /**
     * Referencia al controlador para manejar la lógica de negocio.
     */
    private final SuministroController suministroController;
    
    /**
     * Referencia a la tabla principal que debe ser actualizada después de guardar.
     */
    private final JTable tableSuministros;
    
    /**
     * Referencia al componente padre para ubicar el diálogo.
     */
    private final Component panelPrincipal; 
    
    /**
     * Constructor del diálogo. Configura la interfaz de usuario (UI) y los oyentes de eventos.
     * @param panelPrincipal El componente padre, que es el panel principal de la aplicación.
     * @param controller El controlador de suministros, responsable de la persistencia.
     * @param table La tabla de suministros a refrescar.
     */
    public CrearSuministroDialog(Component panelPrincipal, SuministroController controller, JTable table) {
        // Usar JDialog con la referencia al Component padre
        super(SwingUtilities.getWindowAncestor(panelPrincipal), "Añadir Suministro", ModalityType.APPLICATION_MODAL);
        this.panelPrincipal = panelPrincipal;
        this.suministroController = controller;
        this.tableSuministros = table;

        // Configuración básica del diálogo
        setSize(400, 400);
        setLocationRelativeTo(panelPrincipal);
        setResizable(false);
        setLayout(null);

        // --- Inicialización de Componentes de la UI ---

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 30, 120, 25);
        add(lblNombre);

        tfNombre = new JTextField();
        tfNombre.setBounds(140, 30, 200, 25);
        add(tfNombre);

        // Stock Actual
        JLabel lblStock = new JLabel("Stock Actual:");
        lblStock.setBounds(20, 70, 120, 25);
        add(lblStock);

        tfStock = new JTextField();
        tfStock.setBounds(140, 70, 200, 25);
        add(tfStock);

        // Unidad
        JLabel lblUnidad = new JLabel("Unidad (Kg/Uni):");
        lblUnidad.setBounds(20, 110, 120, 25);
        add(lblUnidad);

        tfUnidad = new JTextField();
        tfUnidad.setBounds(140, 110, 200, 25);
        add(tfUnidad);

        // Mínimo Crítico
        JLabel lblMinCritico = new JLabel("Mínimo Crítico:");
        lblMinCritico.setBounds(20, 150, 120, 25);
        add(lblMinCritico);

        tfMinCritico = new JTextField();
        tfMinCritico.setBounds(140, 150, 200, 25);
        add(tfMinCritico);

        // Ubicación
        JLabel lblUbicacionLabel = new JLabel("Ubicación:");
        lblUbicacionLabel.setBounds(20, 190, 120, 25);
        add(lblUbicacionLabel);

        cbUbicacion = new JComboBox<>(new String[] {
            "Almacén Principal", "Almacén A", "Almacén B", "Almacén C"
        });
        cbUbicacion.setBounds(140, 190, 200, 25);
        add(cbUbicacion);

        // Fecha de Caducidad
        JLabel lblFechaCaducidad = new JLabel("Fecha Caducidad (yyyy-MM-dd):");
        lblFechaCaducidad.setBounds(20, 230, 180, 25);
        add(lblFechaCaducidad);

        tfFechaCaducidad = new JTextField();
        tfFechaCaducidad.setBounds(200, 230, 140, 25);
        add(tfFechaCaducidad);

        // Botones
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(70, 280, 100, 30);
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(200, 280, 100, 30);
        add(btnCancelar);

        // Acciones (Delegación)
        btnGuardar.addActionListener(e -> guardarSuministro());
        btnCancelar.addActionListener(e -> dispose());
    }

    /**
     * Maneja la lógica de guardado: valida la entrada, parsea los datos, crea el objeto {@link Suministro},
     * y utiliza el controlador para agregarlo al sistema y actualizar la tabla.
     */
    private void guardarSuministro() {
        try {
            // 1. Recolección de datos
            String nombre = tfNombre.getText().trim();
            String stockText = tfStock.getText().trim();
            String unidad = tfUnidad.getText().trim();
            String minCriticoText = tfMinCritico.getText().trim();
            String ubicacion = (String) cbUbicacion.getSelectedItem();
            String fechaText = tfFechaCaducidad.getText().trim();

            // 2. Validaciones básicas de campos obligatorios
            if (nombre.isEmpty() || stockText.isEmpty() || unidad.isEmpty() || minCriticoText.isEmpty() || ubicacion == null || fechaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Parseo y validación de tipos complejos (Números y Fecha)
            int stock = Integer.parseInt(stockText);
            int minCritico = Integer.parseInt(minCriticoText);
            
            // Usar el estándar ISO 8601 (yyyy-MM-dd)
            LocalDate fechaCaducidad = LocalDate.parse(fechaText, DateTimeFormatter.ISO_LOCAL_DATE);

            // 4. Crear objeto suministro (el ID se asignará automáticamente en el servicio)
            Suministro s = new Suministro(nombre, stock, unidad, minCritico, ubicacion, fechaCaducidad);

            // 5. Delegar la acción al Controlador (Persistencia y Actualización de Vista)
            suministroController.agregarSuministro(s, tableSuministros);

            JOptionPane.showMessageDialog(this, "Suministro agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // cerrar dialog
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock y Mínimo Crítico deben ser números enteros válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd (ej: 2025-12-31).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al guardar: " + ex.getMessage(), "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}