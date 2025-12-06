package vista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import modelo.Suministro;
import controlador.SuministroController;

public class CrearSuministroDialog extends JDialog {

    private JTextField tfNombre, tfStock, tfUnidad, tfMinCritico, tfFechaCaducidad;
    private JComboBox<String> cbUbicacion;
    private JButton btnGuardar, btnCancelar;

    private final SuministroController suministroController;
    private final JTable tableSuministros;
    private final PanelPrincipal panelPrincipal; // Referencia al panel principal
    
    public CrearSuministroDialog(PanelPrincipal panelPrincipal, SuministroController controller, JTable table) {
        super(panelPrincipal, "Añadir Suministro", ModalityType.APPLICATION_MODAL);
        this.panelPrincipal = panelPrincipal;
        this.suministroController = controller;
        this.tableSuministros = table;

        setSize(400, 400);
        setLocationRelativeTo(panelPrincipal);
        setLayout(null);

        // Campos
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 30, 120, 25);
        add(lblNombre);

        tfNombre = new JTextField();
        tfNombre.setBounds(140, 30, 200, 25);
        add(tfNombre);

        JLabel lblStock = new JLabel("Stock Actual:");
        lblStock.setBounds(20, 70, 120, 25);
        add(lblStock);

        tfStock = new JTextField();
        tfStock.setBounds(140, 70, 200, 25);
        add(tfStock);

        JLabel lblUnidad = new JLabel("Unidad (Kg/Uni):");
        lblUnidad.setBounds(20, 110, 120, 25);
        add(lblUnidad);

        tfUnidad = new JTextField();
        tfUnidad.setBounds(140, 110, 200, 25);
        add(tfUnidad);

        JLabel lblMinCritico = new JLabel("Mínimo Crítico:");
        lblMinCritico.setBounds(20, 150, 120, 25);
        add(lblMinCritico);

        tfMinCritico = new JTextField();
        tfMinCritico.setBounds(140, 150, 200, 25);
        add(tfMinCritico);

        JLabel lblUbicacionLabel = new JLabel("Ubicación:");
        lblUbicacionLabel.setBounds(20, 190, 120, 25);
        add(lblUbicacionLabel);

        cbUbicacion = new JComboBox<>(new String[] {
            "Almacén Principal", "Almacén A", "Almacén B", "Almacén C"
        });
        cbUbicacion.setBounds(140, 190, 200, 25);
        add(cbUbicacion);

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

        // Acciones
        btnGuardar.addActionListener(e -> guardarSuministro());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarSuministro() {
        try {
            String nombre = tfNombre.getText().trim();
            int stock = Integer.parseInt(tfStock.getText().trim());
            String unidad = tfUnidad.getText().trim();
            int minCritico = Integer.parseInt(tfMinCritico.getText().trim());
            String ubicacion = (String) cbUbicacion.getSelectedItem();
            LocalDate fechaCaducidad = LocalDate.parse(tfFechaCaducidad.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);

            if (nombre.isEmpty() || unidad.isEmpty() || ubicacion == null) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear objeto suministro
            Suministro s = new Suministro(nombre, stock, unidad, minCritico, ubicacion, fechaCaducidad);

            // Agregar suministro y actualizar tabla
            suministroController.agregarSuministro(s, tableSuministros);


            dispose(); // cerrar dialog
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Stock y Mínimo Crítico deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}


