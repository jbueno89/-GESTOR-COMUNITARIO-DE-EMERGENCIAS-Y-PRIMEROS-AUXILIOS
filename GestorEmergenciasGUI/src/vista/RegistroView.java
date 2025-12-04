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
import javax.swing.BorderFactory;

// IMPORTACIONES NECESARIAS PARA LA CONEXIÓN
import modelo.GestorUsuarios;
import modelo.Usuario;

public class RegistroView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Campos de formulario (pueden ser private)
    private JTextField tfNombre;
    private JTextField tfUsuario;
    private JPasswordField pfContraseña;
    private JPasswordField pfConfirmarContraseña;
    private JComboBox<String> cbRol;
    
    // --- COLORES DE ESTILO CONSISTENTE (static final) ---
    private static final Color COLOR_PRIMARIO = new Color(26, 75, 140);   
    private static final Color COLOR_ACCENTO_SUCCESS = new Color(34, 139, 34); 
    private static final Color COLOR_ACCENTO_LINK = new Color(0, 102, 204); 
    private static final Color COLOR_FONDO_CLARO = new Color(245, 245, 245); 
    private static final Color COLOR_PANEL_BLANCO = Color.WHITE; 
    
    // Dependencias
    private GestorUsuarios gestorUsuarios; 
    private LoginView loginFrame; 

    /**
     * Launch the application (Método main solo como ejemplo de ejecución).
     * En una aplicación real, esta vista debe ser llamada desde el Login.
     */
    public static void main(String[] args) {
        // En una aplicación real, se iniciaría el Login, no el Registro directamente.
        // Aquí se mantiene la estructura estándar del main.
    }
    
    /**
     * CONSTRUCTOR PRINCIPAL: Inicializa la vista y recibe las dependencias.
     * @param loginFrame Referencia al LoginView (para volver).
     * @param gestorUsuarios Instancia del GestorUsuarios (para guardar datos).
     */
    public RegistroView(LoginView loginFrame, GestorUsuarios gestorUsuarios) {
        this.loginFrame = loginFrame; // Referencia al Login
        this.gestorUsuarios = gestorUsuarios; // Referencia al Gestor

        inicializarComponentes();
    }
    
    /**
     * Inicializa y configura todos los componentes de la interfaz gráfica.
     */
    private void inicializarComponentes() {
        setTitle("Sistema de Gestión de Emergencias - Registro de Nuevo Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 480, 600);
        setLocationRelativeTo(null); 
        setResizable(false); 

        contentPane = new JPanel();
        contentPane.setBackground(COLOR_FONDO_CLARO);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // -------------------------------------------------------------
        // --- 1. TÍTULO Y MENSAJE ---
        // -------------------------------------------------------------
        JLabel lblTitulo = new JLabel("CREAR NUEVA CUENTA DE USUARIO");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_PRIMARIO); 
        lblTitulo.setBounds(10, 30, 444, 30);
        contentPane.add(lblTitulo);
        
        JLabel lblSubtitulo = new JLabel("Completa el formulario para unirte al sistema.");
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
        lblSubtitulo.setBounds(10, 60, 444, 20);
        contentPane.add(lblSubtitulo);
        
        // -------------------------------------------------------------
        // --- 2. CAMPOS DEL FORMULARIO ---
        // -------------------------------------------------------------
        int yStart = 120;
        int yOffset = 55;
        int fieldWidth = 220;
        int xField = 190;
        int xLabel = 50;
        
        // --- Nombre Completo ---
        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblNombre.setForeground(COLOR_PRIMARIO);
        lblNombre.setBounds(xLabel, yStart, 150, 20);
        contentPane.add(lblNombre);

        tfNombre = new JTextField();
        tfNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tfNombre.setBounds(xField, yStart - 5, fieldWidth, 35);
        tfNombre.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(tfNombre);
        tfNombre.setColumns(10);
        
        // --- Nombre de Usuario ---
        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUsuario.setForeground(COLOR_PRIMARIO);
        lblUsuario.setBounds(xLabel, yStart + yOffset, 150, 20);
        contentPane.add(lblUsuario);

        tfUsuario = new JTextField();
        tfUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tfUsuario.setBounds(xField, yStart + yOffset - 5, fieldWidth, 35);
        tfUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);
        
        // --- Contraseña ---
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblContraseña.setForeground(COLOR_PRIMARIO);
        lblContraseña.setBounds(xLabel, yStart + 2 * yOffset, 150, 20);
        contentPane.add(lblContraseña);

        pfContraseña = new JPasswordField();
        pfContraseña.setBounds(xField, yStart + 2 * yOffset - 5, fieldWidth, 35);
        pfContraseña.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(pfContraseña);
        
        // --- Confirmar Contraseña ---
        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        lblConfirmarContraseña.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblConfirmarContraseña.setForeground(COLOR_PRIMARIO);
        lblConfirmarContraseña.setBounds(xLabel, yStart + 3 * yOffset, 150, 20);
        contentPane.add(lblConfirmarContraseña);

        pfConfirmarContraseña = new JPasswordField();
        pfConfirmarContraseña.setBounds(xField, yStart + 3 * yOffset - 5, fieldWidth, 35);
        pfConfirmarContraseña.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(pfConfirmarContraseña);
        
        // --- Rol (Tipo de Usuario) ---
        JLabel lblRol = new JLabel("Rol (Tipo de Usuario):");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRol.setForeground(COLOR_PRIMARIO);
        lblRol.setBounds(xLabel, yStart + 4 * yOffset, 150, 20);
        contentPane.add(lblRol);

        cbRol = new JComboBox<>();
        // Nota: Asegúrate de que los roles aquí sean los mismos que usa GestorUsuarios/Usuario.
        cbRol.setModel(new DefaultComboBoxModel<>(new String[] {"Residente", "Brigadista"})); 
        cbRol.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cbRol.setBounds(xField, yStart + 4 * yOffset - 5, fieldWidth, 35);
        cbRol.setBackground(COLOR_PANEL_BLANCO);
        contentPane.add(cbRol);

        // -------------------------------------------------------------
        // --- 3. BOTONES DE ACCIÓN ---
        // -------------------------------------------------------------
        
        // --- BOTÓN DE REGISTRAR ---
        JButton btnRegistrar = new JButton("REGISTRAR CUENTA"); 
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegistrar.setBackground(COLOR_ACCENTO_SUCCESS); 
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setFocusPainted(false);
        
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                procesarRegistro();
            }
        });
        btnRegistrar.setBounds(50, 480, 380, 50);
        contentPane.add(btnRegistrar);
        
        // --- BOTÓN VOLVER AL LOGIN ---
        JButton btnVolver = new JButton("¿Ya tienes una cuenta? Inicia Sesión aquí.");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
        btnVolver.setForeground(COLOR_ACCENTO_LINK); 
        btnVolver.setContentAreaFilled(false); 
        btnVolver.setBorderPainted(false); 
        
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Muestra la ventana de login (que ya existe y contiene el Gestor)
                loginFrame.setVisible(true); 
                dispose(); // Cierra esta ventana de registro
            }
        });
        btnVolver.setBounds(90, 540, 300, 20); 
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
             // Llama al método del modelo (GestorUsuarios) para guardar los datos
             gestorUsuarios.registrarUsuario(nuevoUsuario); 

             // 3. REGISTRO EXITOSO Y VUELTA AL LOGIN
             JOptionPane.showMessageDialog(this, 
                 "✅ ¡Registro Exitoso!\nUsuario: " + usuario + "\nAhora puede iniciar sesión.", 
                 "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            
             // Vuelve al Login
             loginFrame.setVisible(true); 
             this.dispose(); 
             
        } catch (IllegalArgumentException e) {
            // Manejo de error si el usuario ya existe (asumiendo que GestorUsuarios lanza esta excepción)
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
             // Manejo de otros errores no esperados (por ejemplo, problemas de I/O)
            JOptionPane.showMessageDialog(this, "Error inesperado al registrar: " + e.getMessage(), "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}