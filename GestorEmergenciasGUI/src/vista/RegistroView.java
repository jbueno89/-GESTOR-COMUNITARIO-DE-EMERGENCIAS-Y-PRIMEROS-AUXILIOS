package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.SwingConstants;

// IMPORTACIONES NECESARIAS PARA LA CONEXIÓN
import modelo.GestorUsuarios;
import modelo.Usuario;

public class RegistroView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfNombre;
    private JTextField tfUsuario;
    private JPasswordField pfContraseña;
    private JPasswordField pfConfirmarContraseña;
    private JComboBox<String> cbRol;
    
    // Campo para la CONEXIÓN: Almacena la referencia al gestor de datos.
    private GestorUsuarios gestorUsuarios; 
    
    // Campo para la CONEXIÓN: Referencia al Login para mostrarlo de nuevo.
    private LoginView loginFrame; 

    /**
     * CONSTRUCTOR MODIFICADO para recibir el GestorUsuarios.
     * @param loginFrame Referencia al LoginView.
     * @param gestorUsuarios Instancia del GestorUsuarios para guardar datos.
     */
    public RegistroView(LoginView loginFrame, GestorUsuarios gestorUsuarios) {
        this.loginFrame = loginFrame; // Guardamos la referencia
        this.gestorUsuarios = gestorUsuarios; // Guardamos el gestor

        setTitle("Registro de Nuevo Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 480, 600);
        setLocationRelativeTo(null); 
        setResizable(false); 

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // --- Componentes (omito comentarios repetitivos para brevedad) ---
        JLabel lblTitulo = new JLabel("CREAR NUEVA CUENTA");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBounds(10, 30, 444, 30);
        contentPane.add(lblTitulo);
        
        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(new Font("SansSansSerif", Font.BOLD, 12));
        lblNombre.setBounds(50, 100, 150, 20);
        contentPane.add(lblNombre);

        tfNombre = new JTextField();
        tfNombre.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tfNombre.setBounds(190, 95, 220, 30);
        contentPane.add(tfNombre);
        tfNombre.setColumns(10);
        
        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUsuario.setBounds(50, 160, 150, 20);
        contentPane.add(lblUsuario);

        tfUsuario = new JTextField();
        tfUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tfUsuario.setBounds(190, 155, 220, 30);
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);
        
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblContraseña.setBounds(50, 220, 150, 20);
        contentPane.add(lblContraseña);

        pfContraseña = new JPasswordField();
        pfContraseña.setBounds(190, 215, 220, 30);
        contentPane.add(pfContraseña);
        
        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        lblConfirmarContraseña.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblConfirmarContraseña.setBounds(50, 280, 150, 20);
        contentPane.add(lblConfirmarContraseña);

        pfConfirmarContraseña = new JPasswordField();
        pfConfirmarContraseña.setBounds(190, 275, 220, 30);
        contentPane.add(pfConfirmarContraseña);
        
        JLabel lblRol = new JLabel("Rol (Tipo de Usuario):");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRol.setBounds(50, 340, 150, 20);
        contentPane.add(lblRol);

        cbRol = new JComboBox<>();
        cbRol.setModel(new DefaultComboBoxModel<>(new String[] {"Residente", "Brigadista"}));
        cbRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cbRol.setBounds(190, 335, 220, 30);
        contentPane.add(cbRol);

        // --- BOTÓN DE REGISTRAR ---
        JButton btnRegistrar = new JButton("REGISTRAR"); // Cambiado el texto para no sugerir ACCEDER directo
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setBackground(new Color(34, 139, 34)); 
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Llama al método para procesar el registro
                procesarRegistro(); // Ya no necesita pasar loginFrame como argumento, lo tiene guardado
            }
        });
        btnRegistrar.setBounds(50, 430, 360, 45);
        contentPane.add(btnRegistrar);
        
        // --- BOTÓN VOLVER AL LOGIN ---
        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnVolver.setForeground(new Color(0, 102, 204));
        btnVolver.setContentAreaFilled(false); 
        btnVolver.setBorderPainted(false); 
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.setVisible(true); // Muestra la ventana de login
                dispose(); // Cierra esta ventana de registro
            }
        });
        btnVolver.setBounds(120, 490, 220, 20); 
        contentPane.add(btnVolver);
    }
    
    /**
     * Valida los campos y REGISTRA el usuario utilizando el GestorUsuarios.
     */
    private void procesarRegistro() {
        String nombre = tfNombre.getText().trim();
        String usuario = tfUsuario.getText().trim();
        String contraseña = new String(pfContraseña.getPassword());
        String confirmar = new String(pfConfirmarContraseña.getPassword());
        String rol = (String) cbRol.getSelectedItem();
        
        // 1. VALIDACIONES BÁSICAS
        if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || confirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser completados.", "Error de Registro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!contraseña.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE);
            pfContraseña.setText("");
            pfConfirmarContraseña.setText("");
            return;
        }
        
        if (contraseña.length() < 5) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 5 caracteres.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. CREACIÓN y REGISTRO DEL USUARIO
        try {
             Usuario nuevoUsuario = new Usuario(nombre, usuario, contraseña, rol);
             gestorUsuarios.registrarUsuario(nuevoUsuario); // ¡Aquí se guarda la información!

             // 3. REGISTRO EXITOSO Y VUELTA AL LOGIN
             JOptionPane.showMessageDialog(this, 
                 "✅ ¡Registro Exitoso!\nUsuario: " + usuario + "\nAhora puede iniciar sesión.", 
                 "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            
             // Vuelve al Login y cierra el Registro
             loginFrame.setVisible(true); 
             this.dispose(); 
             
        } catch (Exception e) {
            // Manejo de errores específicos del gestor, como que el usuario ya existe.
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Este método ya no es relevante si siempre volvemos al Login. Se mantiene por si lo usas después.
    private void abrirPanelPrincipal(String rol) {
        // Lógica de apertura del PanelPrincipal...
    }
}
// Las clases LoginView, Usuario y GestorUsuarios deben ser creadas.}