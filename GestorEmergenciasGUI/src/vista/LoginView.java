package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.io.File;
import java.awt.Image; 

// IMPORTACIONES NECESARIAS PARA LA CONEXIÓN Y PERSISTENCIA
import modelo.GestorUsuarios;
import modelo.Usuario;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfUsuario;
    private JPasswordField pfContraseña;
    private JComboBox<String> cbRol;
    
    // Campo para la CONEXIÓN: Almacena la referencia al gestor de datos.
    private GestorUsuarios gestorUsuarios;

    // ELIMINADO: Se elimina el main() de esta clase. El punto de entrada será MainApp.java.
    // public static void main(String[] args) { ... }

    /**
     * CONSTRUCTOR MODIFICADO: Ahora requiere el GestorUsuarios.
     */
    public LoginView(GestorUsuarios gestorUsuarios) {
        // Almacena la referencia al gestor de usuarios que contendrá los datos guardados.
        this.gestorUsuarios = gestorUsuarios; 

        setTitle("Sistema de Gestión de Emergencias - Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // ************************************************************
        // 1. CARGA DEL LOGO (Mantenido)
        // ************************************************************
        JLabel lblLogo = new JLabel("");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBounds(10, 10, 444, 100); 

        // NOTA IMPORTANTE: La ruta absoluta siguiente es específica de tu sistema. 
        // Se recomienda usar rutas relativas o classpath para producción.
        final String RUTA_ABSOLUTA = "C:/Users/ANYELO/eclipse-workspace/-GESTOR-COMUNITARIO-DE-EMERGENCIAS-Y-PRIMEROS-AUXILIOS-main/GestorEmergenciasGUI/src/vista/Logo.jpg";

        try {
            // Lógica de carga y escalado... (mantenida de tu código)
            java.net.URL location = null;
            ImageIcon originalIcon = null;
            File logoFile = new File(RUTA_ABSOLUTA);

            if (logoFile.exists() && logoFile.isFile()) {
                originalIcon = new ImageIcon(logoFile.getAbsolutePath());
            } else {
                location = getClass().getResource("Logo.jpg"); 
                if (location == null) {
                    location = getClass().getResource("/Logo.jpg"); 
                }
                if (location != null) {
                    originalIcon = new ImageIcon(location);
                }
            }

            if (originalIcon != null) {
                Image img = originalIcon.getImage(); 
                int width = lblLogo.getWidth();
                int height = lblLogo.getHeight();
                Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImg);
                lblLogo.setIcon(scaledIcon);
            } else {
                lblLogo.setText("⚠️ IMAGEN NO ENCONTRADA: Logo.jpg");
                lblLogo.setFont(new Font("SansSerif", Font.BOLD, 10));
                lblLogo.setForeground(Color.RED);
            }

        } catch (Exception e) {
            lblLogo.setText("🚨 ERROR DE CARGA DE IMAGEN");
            e.printStackTrace();
        }
        contentPane.add(lblLogo);


        // --- TÍTULO DE BIENVENIDA ---
        JLabel lblTitulo = new JLabel("BIENVENIDO AL SISTEMA DE GESTIÓN DE EMERGENCIA");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitulo.setBounds(10, 120, 444, 25);
        contentPane.add(lblTitulo);

        // --- MENSAJE DE ROL ---
        JLabel lblMensajeRol = new JLabel("Por favor, selecciona tu rol e inicia sesión.");
        lblMensajeRol.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensajeRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMensajeRol.setBounds(10, 150, 444, 20);
        contentPane.add(lblMensajeRol);

        // --- SELECCIÓN DE ROL ---
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRol.setBounds(50, 210, 100, 20);
        contentPane.add(lblRol);

        cbRol = new JComboBox<>();
        cbRol.setModel(new DefaultComboBoxModel<>(new String[] {"Coordinador", "Brigadista", "Residente"}));
        cbRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
        cbRol.setBounds(160, 210, 250, 30);
        contentPane.add(cbRol);

        // --- USUARIO ---
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUsuario.setBounds(50, 260, 100, 20);
        contentPane.add(lblUsuario);

        tfUsuario = new JTextField();
        tfUsuario.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tfUsuario.setBounds(160, 260, 250, 30);
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);

        // --- CONTRASEÑA ---
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblContraseña.setBounds(50, 310, 100, 20);
        contentPane.add(lblContraseña);

        pfContraseña = new JPasswordField();
        pfContraseña.setBounds(160, 310, 250, 30);
        contentPane.add(pfContraseña);

        // --- BOTÓN DE INICIAR SESIÓN ---
        JButton btnIniciarSesion = new JButton("INICIAR SESIÓN");
        btnIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIniciarSesion.setBackground(new Color(0, 153, 204));
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });
        btnIniciarSesion.setBounds(50, 390, 360, 45);
        contentPane.add(btnIniciarSesion);

        // --- BOTÓN DE CREAR CUENTA (Regístrate) ---
        JButton btnCrearCuenta = new JButton("Crear Cuenta / Registrarse");
        btnCrearCuenta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnCrearCuenta.setForeground(new Color(0, 102, 204));
        btnCrearCuenta.setContentAreaFilled(false);
        btnCrearCuenta.setBorderPainted(false);
        btnCrearCuenta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // **CONEXIÓN A REGISTROVIEW: Se pasa 'this' (la referencia al Login) y el gestor**
                RegistroView registro = new RegistroView(LoginView.this, gestorUsuarios);
                registro.setVisible(true);
                LoginView.this.setVisible(false); // Oculta la ventana de login
            }
        });
        btnCrearCuenta.setBounds(120, 450, 220, 20);
        contentPane.add(btnCrearCuenta);
    } // Fin del constructor

    /**
     * Autentica al usuario usando el GestorUsuarios, no la lógica simulada.
     */
    private void autenticarUsuario() {
        String usuario = tfUsuario.getText().trim();
        String contrasena = new String(pfContraseña.getPassword());
        String rolSeleccionado = (String) cbRol.getSelectedItem();
        
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y Contraseña no pueden estar vacíos.", "Error de Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. LLAMADA AL GESTOR: Intenta validar las credenciales
        Usuario usuarioLogueado = gestorUsuarios.validarCredenciales(usuario, contrasena);

        if (usuarioLogueado != null) {
            // 2. ÉXITO: Verifica que el rol coincida con el rol seleccionado en el ComboBox (opcional, pero buena práctica)
            if (usuarioLogueado.getRol().equals(rolSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                    "✅ Acceso Exitoso! Bienvenido " + usuarioLogueado.getNombreUsuario() + " (" + usuarioLogueado.getRol() + ")",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

                abrirPanelPrincipal(usuarioLogueado.getRol()); // Pasa el rol real
                this.dispose();
            } else {
                // Las credenciales son válidas, pero el rol seleccionado no coincide con el rol guardado.
                JOptionPane.showMessageDialog(this,
                    "Credenciales válidas, pero el usuario no tiene el rol de '" + rolSeleccionado + "'. Su rol es: " + usuarioLogueado.getRol(),
                    "Error de Rol", JOptionPane.WARNING_MESSAGE);
                pfContraseña.setText("");
            }
        } else {
            // 3. FALLO: Usuario/Contraseña incorrectos
            JOptionPane.showMessageDialog(this,
                "Credenciales incorrectas para el Rol seleccionado.",
                "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            pfContraseña.setText("");
        }
    }

    /**
     * Lanza la ventana principal (PanelPrincipal) pasando el rol del usuario.
     */
    private void abrirPanelPrincipal(String rol) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // La clase PanelPrincipal debe estar definida e implementada
                    PanelPrincipal frame = new PanelPrincipal(rol);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Esto manejaría el caso si PanelPrincipal no existe aún
                    JOptionPane.showMessageDialog(null, "Error al abrir el Panel Principal. Asegúrese de que la clase PanelPrincipal exista.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}