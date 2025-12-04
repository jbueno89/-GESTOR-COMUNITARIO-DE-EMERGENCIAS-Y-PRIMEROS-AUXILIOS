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

// IMPORTACIONES NECESARIAS PARA LA CONEXIÓN Y PERSISTENCIA
import modelo.GestorUsuarios;
import modelo.Usuario;

public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfUsuario;
    private JPasswordField pfContraseña;
    private JComboBox<String> cbRol;
    
    // --- COLORES DE ESTILO CONSISTENTE ---
    private final Color COLOR_PRIMARIO = new Color(26, 75, 140);   // Azul Profundo
    private final Color COLOR_ACCENTO = new Color(217, 0, 29);    // Rojo Brillante
    private final Color COLOR_FONDO_CLARO = new Color(245, 245, 245); // Gris Claro (fondo principal)
    private final Color COLOR_PANEL_BLANCO = Color.WHITE; // Panel de formulario
    // --- FIN COLORES ---
    
    // Campo para la CONEXIÓN: Almacena la referencia al gestor de datos.
    private GestorUsuarios gestorUsuarios;

    /**
     * CONSTRUCTOR MODIFICADO: Ahora requiere el GestorUsuarios y aplica el nuevo estilo.
     */
    public LoginView(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios; 

        setTitle("Sistema de Gestión de Emergencias - Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        // Aplicamos el color de fondo claro
        contentPane.setBackground(COLOR_FONDO_CLARO);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // ************************************************************
        // 1. CARGA DEL LOGO (RUTA CORREGIDA)
        // ************************************************************
        JLabel lblLogo = new JLabel("");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        // Aumentamos la altura del logo
        lblLogo.setBounds(10, 10, 444, 120); 

        // RUTA CORREGIDA: Accede al recurso desde la raíz del classpath (/bin/recursos)
        final String RUTA_CLASSPATH = "/recursos/Logo.png"; 

        try {
            URL logoURL = getClass().getResource(RUTA_CLASSPATH);
            ImageIcon originalIcon = null;

            if (logoURL != null) {
                originalIcon = new ImageIcon(logoURL);
            } 
            
            if (originalIcon != null) {
                Image img = originalIcon.getImage(); 
                int width = lblLogo.getWidth();
                int height = lblLogo.getHeight();
                Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImg);
                lblLogo.setIcon(scaledIcon);
            } else {
                lblLogo.setText("⚠️ IMAGEN NO ENCONTRADA: " + RUTA_CLASSPATH);
                lblLogo.setFont(new Font("SansSerif", Font.BOLD, 10));
                lblLogo.setForeground(COLOR_ACCENTO); // Usamos el color de acento
            }

        } catch (Exception e) {
            lblLogo.setText("🚨 ERROR DE CARGA DE IMAGEN");
            e.printStackTrace();
        }
        contentPane.add(lblLogo);


        // --- TÍTULO DE BIENVENIDA ---
        JLabel lblTitulo = new JLabel("INICIO DE SESIÓN EN EL SISTEMA DE GESTIÓN");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(COLOR_PRIMARIO); // Aplicamos el color primario
        lblTitulo.setBounds(10, 140, 444, 25);
        contentPane.add(lblTitulo);

        // --- MENSAJE DE ROL ---
        JLabel lblMensajeRol = new JLabel("Por favor, selecciona tu rol e ingresa tus credenciales.");
        lblMensajeRol.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensajeRol.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblMensajeRol.setBounds(10, 170, 444, 20);
        contentPane.add(lblMensajeRol);

        // --- SELECCIÓN DE ROL ---
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("SansSerif", Font.BOLD, 14)); // Fuente un poco más grande
        lblRol.setBounds(50, 230, 100, 20);
        lblRol.setForeground(COLOR_PRIMARIO);
        contentPane.add(lblRol);

        cbRol = new JComboBox<>();
        cbRol.setModel(new DefaultComboBoxModel<>(new String[] {"Coordinador", "Brigadista", "Residente"}));
        cbRol.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Fuente un poco más grande
        cbRol.setBounds(160, 225, 250, 35);
        cbRol.setBackground(COLOR_PANEL_BLANCO);
        contentPane.add(cbRol);

        // --- USUARIO ---
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblUsuario.setBounds(50, 285, 100, 20);
        lblUsuario.setForeground(COLOR_PRIMARIO);
        contentPane.add(lblUsuario);

        tfUsuario = new JTextField();
        tfUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tfUsuario.setBounds(160, 280, 250, 35);
        // Borde simple y limpio
        tfUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(tfUsuario);
        tfUsuario.setColumns(10);

        // --- CONTRASEÑA ---
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblContraseña.setBounds(50, 340, 100, 20);
        lblContraseña.setForeground(COLOR_PRIMARIO);
        contentPane.add(lblContraseña);

        pfContraseña = new JPasswordField();
        pfContraseña.setBounds(160, 335, 250, 35);
        // Borde simple y limpio
        pfContraseña.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1));
        contentPane.add(pfContraseña);

        // --- BOTÓN DE INICIAR SESIÓN ---
        JButton btnIniciarSesion = new JButton("INICIAR SESIÓN");
        btnIniciarSesion.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fuente más grande
        btnIniciarSesion.setBackground(COLOR_PRIMARIO); // Azul Profundo
        btnIniciarSesion.setForeground(Color.WHITE);
        // Quitamos el borde para un look plano
        btnIniciarSesion.setBorderPainted(false);
        btnIniciarSesion.setFocusPainted(false);
        
        btnIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });
        btnIniciarSesion.setBounds(50, 410, 360, 50); // Botón más grande
        contentPane.add(btnIniciarSesion);

        // --- BOTÓN DE CREAR CUENTA (Regístrate) ---
        JButton btnCrearCuenta = new JButton("Crear Cuenta / Registrarse");
        btnCrearCuenta.setFont(new Font("SansSerif", Font.BOLD, 12)); // Texto en negrita
        btnCrearCuenta.setForeground(COLOR_ACCENTO); // Color de acento (Rojo)
        btnCrearCuenta.setContentAreaFilled(false);
        btnCrearCuenta.setBorderPainted(false);
        btnCrearCuenta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Aquí va la llamada a RegistroView. Se mantiene la lógica original.
                // Asegúrate de que la clase RegistroView exista
                // RegistroView registro = new RegistroView(LoginView.this, gestorUsuarios);
                // registro.setVisible(true);
                // LoginView.this.setVisible(false);
                JOptionPane.showMessageDialog(LoginView.this, "La funcionalidad de registro no está implementada aún.", "En Desarrollo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        btnCrearCuenta.setBounds(120, 475, 220, 20);
        contentPane.add(btnCrearCuenta);
    } // Fin del constructor

    private void autenticarUsuario() {
        String usuario = tfUsuario.getText().trim();
        String contrasena = new String(pfContraseña.getPassword());
        String rolSeleccionado = (String) cbRol.getSelectedItem();
        
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y Contraseña no pueden estar vacíos.", "Error de Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogueado = gestorUsuarios.validarCredenciales(usuario, contrasena);

        if (usuarioLogueado != null) {
            if (usuarioLogueado.getRol().equals(rolSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                    "✅ Acceso Exitoso! Bienvenido " + usuarioLogueado.getNombreUsuario() + " (" + usuarioLogueado.getRol() + ")",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

                abrirPanelPrincipal(usuarioLogueado.getRol()); // Pasa el rol real
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Credenciales válidas, pero el usuario no tiene el rol de '" + rolSeleccionado + "'. Su rol es: " + usuarioLogueado.getRol(),
                    "Error de Rol", JOptionPane.WARNING_MESSAGE);
                pfContraseña.setText("");
            }
        } else {
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
                    // La clase PanelPrincipal debe estar definida e implementada con el constructor PanelPrincipal(String)
                    PanelPrincipal frame = new PanelPrincipal(rol);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Mensaje de error mejorado para indicar la causa del problema
                    JOptionPane.showMessageDialog(null, "Error al abrir el Panel Principal. Asegúrese de que la clase PanelPrincipal exista en el paquete 'vista' y tenga un constructor público que reciba un String (el rol).", "Error Crítico", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}