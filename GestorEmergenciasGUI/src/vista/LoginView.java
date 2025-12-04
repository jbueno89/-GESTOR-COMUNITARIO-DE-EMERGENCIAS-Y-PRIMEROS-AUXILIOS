/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
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
import java.awt.Image; 
import java.net.URL; 

// IMPORTACIONES DE LA CAPA MODELO (LÓGICA)
import modelo.GestorUsuarios;
import modelo.Usuario;

/**
 * Ventana de inicio de sesión de la aplicación.
 * Representa la **Capa de Interfaz Gráfica (GUI)** y demuestra la **Arquitectura por Capas**
 * al comunicarse directamente con el GestorUsuarios (Capa Lógica).
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class LoginView extends JFrame { // Mantengo JFrame como nombre estándar

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    // Componentes de entrada de datos
    private JTextField tfUsuario;
    private JPasswordField pfContrasena; // Se corrigió la tilde por convención de Java
    private JComboBox<String> cbRol;
    
    // --- COLORES DE ESTILO CONSISTENTE (Atributos finales) ---
    private final Color COLOR_PRIMARIO = new Color(26, 75, 140);   
    private final Color COLOR_ACCENTO = new Color(217, 0, 29);    
    private final Color COLOR_FONDO_CLARO = new Color(245, 245, 245); 
    private final Color COLOR_PANEL_BLANCO = Color.WHITE; 
    
    // Campo para la CONEXIÓN: Referencia a la capa de Lógica
    private GestorUsuarios gestorUsuarios;

    /**
     * Constructor de la ventana de login. Inicializa la interfaz gráfica
     * y establece la conexión con la capa de lógica.
     * @param gestorUsuarios La instancia del GestorUsuarios para la autenticación.
     */
    public LoginView(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios; 

        // Configuración básica de la ventana
        setTitle("Sistema de Gestión de Emergencias - Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(COLOR_FONDO_CLARO);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // ************************************************************
        // 1. CARGA DEL LOGO (Manejo de Errores mejorado)
        // ************************************************************
        JLabel lblLogo = new JLabel("");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBounds(10, 10, 444, 120); 

        final String RUTA_CLASSPATH = "/recursos/Logo.png"; 

        try {
            URL logoURL = getClass().getResource(RUTA_CLASSPATH);
            ImageIcon originalIcon = null;

            if (logoURL != null) {
                originalIcon = new ImageIcon(logoURL);
                Image img = originalIcon.getImage(); 
                int width = lblLogo.getWidth();
                int height = lblLogo.getHeight();
                Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImg);
                lblLogo.setIcon(scaledIcon);
            } else {
                // Manejo de Error: Si el recurso no se encuentra
                lblLogo.setText("⚠️ IMAGEN NO ENCONTRADA: " + RUTA_CLASSPATH);
                lblLogo.setFont(new Font("SansSerif", Font.BOLD, 10));
                lblLogo.setForeground(COLOR_ACCENTO); 
            }

        } catch (Exception e) {
            // Manejo de Error: Si hay un error de carga
            lblLogo.setText("🚨 ERROR DE CARGA DE IMAGEN");
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
        }
        contentPane.add(lblLogo);


        // --- ETIQUETAS Y CAMPOS DE FORMULARIO ---
        
        JLabel lblTitulo = new JLabel("INICIO DE SESIÓN EN EL SISTEMA DE GESTIÓN");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_PRIMARIO); 
        lblTitulo.setBounds(10, 140, 444, 25);
        contentPane.add(lblTitulo);

        JLabel lblMensajeRol = new JLabel("Por favor, selecciona tu rol e ingresa tus credenciales.");
        lblMensajeRol.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensajeRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMensajeRol.setBounds(10, 170, 444, 20);
        contentPane.add(lblMensajeRol);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 14)); 
        lblRol.setBounds(50, 230, 100, 20);
        lblRol.setForeground(COLOR_PRIMARIO);
        contentPane.add(lblRol);

        cbRol = new JComboBox<>();
        cbRol.setModel(new DefaultComboBoxModel<>(new String[] {"Coordinador", "Brigadista", "Residente"}));
        cbRol.setFont(new Font("SansSerif", Font.PLAIN, 14)); 
        cbRol.setBounds(160, 225, 250, 35);
        cbRol.setBackground(COLOR_PANEL_BLANCO);
        contentPane.add(cbRol);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblUsuario.setBounds(50, 285, 100, 20);
        lblUsuario.setForeground(COLOR_PRIMARIO);
        contentPane.add(lblUsuario);

        tfUsuario = new JTextField();
        tfUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tfUsuario.setBounds(160, 280, 250, 35);
        tfUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblContrasena.setBounds(50, 340, 100, 20);
        lblContrasena.setForeground(COLOR_PRIMARIO);
        contentPane.add(lblContrasena);

        pfContrasena = new JPasswordField();
        pfContrasena.setBounds(160, 335, 250, 35);
        pfContrasena.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(pfContrasena);

        // --- BOTÓN DE INICIAR SESIÓN ---
        JButton btnIniciarSesion = new JButton("INICIAR SESIÓN");
        btnIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 16)); 
        btnIniciarSesion.setBackground(COLOR_PRIMARIO); 
        btnIniciarSesion.setForeground(Color.WHITE);
        btnIniciarSesion.setBorderPainted(false);
        btnIniciarSesion.setFocusPainted(false);
        
        btnIniciarSesion.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic del botón de Iniciar Sesión.
             * @param e El evento de acción.
             */
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });
        btnIniciarSesion.setBounds(50, 410, 360, 50); 
        contentPane.add(btnIniciarSesion);

        // --- BOTÓN DE CREAR CUENTA (Registro) ---
        JButton btnCrearCuenta = new JButton("Crear Cuenta / Registrarse");
        btnCrearCuenta.setFont(new Font("SansSerif", Font.BOLD, 12)); 
        btnCrearCuenta.setForeground(COLOR_ACCENTO); 
        btnCrearCuenta.setContentAreaFilled(false);
        btnCrearCuenta.setBorderPainted(false);
        btnCrearCuenta.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic del botón de Registro.
             * @param e El evento de acción.
             */
            public void actionPerformed(ActionEvent e) {
                // Mensaje de funcionalidad no implementada (se deja la lógica comentada)
                JOptionPane.showMessageDialog(LoginView.this, "La funcionalidad de registro no está implementada aún. Use las credenciales por defecto (admin/12345).", "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnCrearCuenta.setBounds(120, 475, 220, 20);
        contentPane.add(btnCrearCuenta);
    } // Fin del constructor

    /**
     * Realiza la validación de las credenciales y el rol contra la capa de lógica.
     */
    private void autenticarUsuario() {
        String usuario = tfUsuario.getText().trim();
        String contrasena = new String(pfContrasena.getPassword());
        String rolSeleccionado = (String) cbRol.getSelectedItem();
        
        // 1. Validación Robusta de Entradas (Manejo de Errores)
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y Contraseña no pueden estar vacíos.", "Error de Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Comunicación con la Capa Modelo (Arquitectura por Capas)
        Usuario usuarioLogueado = gestorUsuarios.validarCredenciales(usuario, contrasena);

        if (usuarioLogueado != null) {
            // 3. Verificación de Rol
            if (usuarioLogueado.getRol().equals(rolSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                    "✅ Acceso Exitoso! Bienvenido " + usuarioLogueado.getNombreUsuario() + " (" + usuarioLogueado.getRol() + ")",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

                abrirPanelPrincipal(usuarioLogueado.getRol());
                this.dispose(); // Cierra la ventana de login
            } else {
                JOptionPane.showMessageDialog(this,
                    "Credenciales válidas, pero el usuario no tiene el rol de '" + rolSeleccionado + "'. Su rol real es: " + usuarioLogueado.getRol(),
                    "Error de Rol", JOptionPane.WARNING_MESSAGE);
                pfContrasena.setText("");
            }
        } else {
            // 4. Credenciales Fallidas
            JOptionPane.showMessageDialog(this,
                "Credenciales incorrectas. Usuario no encontrado o contraseña inválida.",
                "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            pfContrasena.setText("");
        }
    }

    /**
     * Abre la ventana principal de la aplicación, pasando el rol para configurar el acceso
     * a las distintas funcionalidades (simulando permisos).
     * @param rol El rol del usuario autenticado ("Coordinador", "Brigadista", etc.).
     */
    private void abrirPanelPrincipal(String rol) {
        EventQueue.invokeLater(new Runnable() {
            /**
             * Lanza el PanelPrincipal en el EDT.
             */
            public void run() {
                try {
                    // Nota: Se asume que la clase PanelPrincipal está implementada en el paquete 'vista'
                    // y tiene un constructor que recibe el rol.
                    JFrame frame = new PanelPrincipal(rol); // Suponiendo PanelPrincipal extiende JFrame
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al abrir el Panel Principal. Asegúrese de que la clase PanelPrincipal exista y sea accesible.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
