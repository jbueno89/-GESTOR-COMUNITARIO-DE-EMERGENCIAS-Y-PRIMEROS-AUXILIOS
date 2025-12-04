/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
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

// IMPORTACIONES DE LA CAPA MODELO (LÓGICA)
import modelo.GestorUsuarios;
import modelo.Usuario;

/**
 * Ventana para el registro de nuevos usuarios en el sistema.
 * Forma parte de la **Capa de Interfaz Gráfica (GUI)** y demuestra
 * la **Arquitectura por Capas** al delegar la persistencia de datos
 * al GestorUsuarios (Capa Lógica).
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class RegistroView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Atributos de la GUI (se renombró pfContrasena para convención Java)
    private JTextField tfNombre;
    private JTextField tfUsuario;
    private JPasswordField pfContrasena;
    private JPasswordField pfConfirmarContrasena;
    private JComboBox<String> cbRol;
    
    // --- COLORES DE ESTILO CONSISTENTE ---
    private static final Color COLOR_PRIMARIO = new Color(26, 75, 140);   
    private static final Color COLOR_ACCENTO_SUCCESS = new Color(34, 139, 34); 
    private static final Color COLOR_ACCENTO_LINK = new Color(0, 102, 204); 
    private static final Color COLOR_FONDO_CLARO = new Color(245, 245, 245); 
    private static final Color COLOR_PANEL_BLANCO = Color.WHITE; 
    
    // Dependencias de la Capa de Lógica y Vista
    private GestorUsuarios gestorUsuarios; 
    private JFrame loginFrame; // Uso JFrame para generalizar, aunque se espera VentanaLogin

    /**
     * Constructor principal de la vista de registro.
     * @param loginFrame Referencia a la ventana de Login para poder volver.
     * @param gestorUsuarios Instancia del GestorUsuarios para registrar los datos.
     */
    public RegistroView(JFrame loginFrame, GestorUsuarios gestorUsuarios) {
        // Se cambió el tipo del parámetro a JFrame para ser más flexible, 
        // ya que LoginView extiende JFrame.
        this.loginFrame = loginFrame; 
        this.gestorUsuarios = gestorUsuarios; 

        inicializarComponentes();
    }
    
    /**
     * Inicializa y configura todos los componentes de la interfaz gráfica (GUI).
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
        JLabel lblContrasenaLabel = new JLabel("Contraseña:");
        lblContrasenaLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblContrasenaLabel.setForeground(COLOR_PRIMARIO);
        lblContrasenaLabel.setBounds(xLabel, yStart + 2 * yOffset, 150, 20);
        contentPane.add(lblContrasenaLabel);

        pfContrasena = new JPasswordField();
        pfContrasena.setBounds(xField, yStart + 2 * yOffset - 5, fieldWidth, 35);
        pfContrasena.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(pfContrasena);
        
        // --- Confirmar Contraseña ---
        JLabel lblConfirmarContrasena = new JLabel("Confirmar Contraseña:");
        lblConfirmarContrasena.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblConfirmarContrasena.setForeground(COLOR_PRIMARIO);
        lblConfirmarContrasena.setBounds(xLabel, yStart + 3 * yOffset, 150, 20);
        contentPane.add(lblConfirmarContrasena);

        pfConfirmarContrasena = new JPasswordField();
        pfConfirmarContrasena.setBounds(xField, yStart + 3 * yOffset - 5, fieldWidth, 35);
        pfConfirmarContrasena.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(pfConfirmarContrasena);
        
        // --- Rol (Tipo de Usuario) ---
        JLabel lblRol = new JLabel("Rol (Tipo de Usuario):");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRol.setForeground(COLOR_PRIMARIO);
        lblRol.setBounds(xLabel, yStart + 4 * yOffset, 150, 20);
        contentPane.add(lblRol);

        cbRol = new JComboBox<>();
        // Los roles deben coincidir exactamente con los que maneja GestorUsuarios/Usuario
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
            /**
             * Maneja el evento de clic del botón Registrar.
             * @param e El evento de acción.
             */
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
            /**
             * Maneja el evento de clic del botón Volver.
             * @param e El evento de acción.
             */
            public void actionPerformed(ActionEvent e) {
                // Vuelve a mostrar el Login y cierra la ventana actual
                if (loginFrame != null) {
                    loginFrame.setVisible(true); 
                }
                dispose(); 
            }
        });
        btnVolver.setBounds(90, 540, 300, 20); 
        contentPane.add(btnVolver);
    }
    
    /**
     * Valida los datos del formulario y llama a la capa de lógica para registrar
     * el nuevo objeto Usuario. Demuestra el **Manejo de Errores** y la **Creación de Objetos**.
     */
    private void procesarRegistro() {
        String nombre = tfNombre.getText().trim();
        String usuario = tfUsuario.getText().trim();
        String contrasena = new String(pfContrasena.getPassword());
        String confirmar = new String(pfConfirmarContrasena.getPassword());
        String rol = (String) cbRol.getSelectedItem();
        
        // 1. VALIDACIONES DE LA VISTA (Validación Robusta de Entradas)
        if (nombre.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || confirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser completados.", "Error de Registro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!contrasena.equals(confirmar)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE);
            pfContrasena.setText("");
            pfConfirmarContrasena.setText("");
            return;
        }
        
        if (contrasena.length() < 5) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 5 caracteres.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. LLAMADA A LA CAPA DE LÓGICA (Manejo de Errores)
        try {
             // Creación de Objetos: Se crea la instancia de la clase modelo.
             Usuario nuevoUsuario = new Usuario(nombre, usuario, contrasena, rol);
             
             // Llama al método del GestorUsuarios para persistir los datos
             gestorUsuarios.registrarUsuario(nuevoUsuario); 

             // 3. REGISTRO EXITOSO Y VUELTA AL LOGIN
             JOptionPane.showMessageDialog(this, 
                 "✅ ¡Registro Exitoso!\nUsuario: " + usuario + "\nAhora puede iniciar sesión.", 
                 "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            
             // Vuelve al Login y cierra esta ventana (End del Programa para esta vista)
             if (loginFrame != null) {
                 loginFrame.setVisible(true); 
             }
             this.dispose(); 
             
        } catch (Exception e) {
            // Captura la excepción lanzada por GestorUsuarios (ej. usuario ya existe, IOException)
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(), "Error Grave", JOptionPane.ERROR_MESSAGE);
        }
    }
}