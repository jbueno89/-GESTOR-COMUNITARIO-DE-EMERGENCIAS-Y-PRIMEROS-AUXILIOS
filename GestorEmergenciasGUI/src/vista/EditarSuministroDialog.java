package vista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import modelo.Suministro;
import controlador.SuministroController;

public class EditarSuministroDialog extends JDialog {

    private JTextField tfNombre, tfStock, tfUnidad, tfMinCritico, tfFechaCaducidad;
    private JComboBox<String> cbUbicacion;
    private JButton btnGuardar, btnCancelar, btnEliminar;

    private final SuministroController suministroController;
    private final JTable tableSuministros;
    private final Suministro suministro;

    public EditarSuministroDialog(Window parent, SuministroController controller,
                                  JTable table, Suministro suministro) {
        super(parent, "Editar Suministro", ModalityType.APPLICATION_MODAL);
        this.suministroController = controller;
        this.tableSuministros = table;
        this.suministro = suministro;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(null);

        // Campos
        tfNombre = crearCampo("Nombre:", 30, suministro.getNombre());
        tfStock = crearCampo("Stock Actual:", 70, String.valueOf(suministro.getStockActual()));
        tfUnidad = crearCampo("Unidad (Kg/Uni):", 110, suministro.getUnidad());
        tfMinCritico = crearCampo("Mínimo Crítico:", 150, String.valueOf(suministro.getMinimoCritico()));

        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(20, 190, 120, 25);
        add(lblUbicacion);

        cbUbicacion = new JComboBox<>(new String[] {
                "Almacén Principal", "Almacén A", "Almacén B", "Almacén C"
        });
        cbUbicacion.setBounds(140, 190, 200, 25);
        cbUbicacion.setSelectedItem(suministro.getUbicacion());
        add(cbUbicacion);

        tfFechaCaducidad = crearCampo("Fecha Caducidad (yyyy-MM-dd):", 230, suministro.getFechaCaducidad().toString());

        // Botones
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(40, 280, 100, 30);
        add(btnGuardar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(150, 280, 100, 30);
        add(btnEliminar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(260, 280, 100, 30);
        add(btnCancelar);

        // Acciones
        btnGuardar.addActionListener(e -> guardarCambios());
        btnEliminar.addActionListener(e -> eliminarSuministro());
        btnCancelar.addActionListener(e -> dispose());
    }

    private JTextField crearCampo(String label, int y, String valor) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(20, y, 150, 25);
        add(lbl);
        JTextField tf = new JTextField(valor);
        tf.setBounds(180, y, 160, 25);
        add(tf);
        return tf;
    }

    private void guardarCambios() {
        try {
            suministro.setNombre(tfNombre.getText().trim());
            suministro.setStockActual(Integer.parseInt(tfStock.getText().trim()));
            suministro.setUnidad(tfUnidad.getText().trim());
            suministro.setMinimoCritico(Integer.parseInt(tfMinCritico.getText().trim()));
            suministro.setUbicacion((String) cbUbicacion.getSelectedItem());
            suministro.setFechaCaducidad(LocalDate.parse(tfFechaCaducidad.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE));

            suministroController.actualizarSuministro(suministro);
            suministroController.actualizarTabla(tableSuministros);
            
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar los cambios. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarSuministro() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar este suministro permanentemente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            suministroController.eliminarSuministro(suministro.getId());
            suministroController.actualizarTabla(tableSuministros);
            dispose();
        }
    }
}
