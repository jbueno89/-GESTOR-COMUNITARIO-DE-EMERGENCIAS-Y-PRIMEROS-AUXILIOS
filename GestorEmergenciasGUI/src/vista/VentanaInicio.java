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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL; 

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.SwingConstants; 
import javax.swing.BorderFactory;

// IMPORTACIONES REQUERIDAS para iniciar la lógica de negocio
import modelo.GestorUsuarios; 

/**
 * Ventana de inicio o bienvenida (Splash Screen) de la aplicación.
 * Es el primer punto de contacto visual y se encarga de iniciar la 
 * **Capa de Lógica (GestorUsuarios)** antes de pasar al login.
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    
    // --- COLORES DE ESTILO CONSISTENTE (Atributos finales) ---
    private static final Color COLOR_PRIMARIO = new Color(26, 75, 140);   
    private static final Color COLOR_ACCENTO = new Color(217, 0, 29);    
    private static final Color COLOR_FONDO_CLARO = new Color(245, 245, 245); 
    private static final Color COLOR_PANEL_BLANCO = Color.WHITE; 
    
    // Método main mantenido solo para pruebas iniciales, la AppPrincipal lo invoca
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicio frame = new VentanaInicio();
                    frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor principal de la ventana.
	 */
	public VentanaInicio() {
        inicializarComponentes();
    }
    
    /**
     * Inicializa y configura todos los componentes de la interfaz gráfica (GUI).
     */
    private void inicializarComponentes() {
        setTitle("Sistema de Gestión de Emergencias - Inicio");
        
		setMaximumSize(new Dimension(550, 500)); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 500);
        setResizable(false);
        
		contentPane = new JPanel();
        contentPane.setBackground(COLOR_FONDO_CLARO); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelVentanaInicio = new JPanel();
		panelVentanaInicio.setBounds(0, 0, 534, 461);
        panelVentanaInicio.setBackground(COLOR_FONDO_CLARO); 
		contentPane.add(panelVentanaInicio);
		panelVentanaInicio.setLayout(null);
		
        // -------------------------------------------------------------
        // --- 1. TÍTULO Y LOGO ---
        // -------------------------------------------------------------
        
		JLabel lblTituloVentanaInicio = new JLabel("GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS");
		
		lblTituloVentanaInicio.setHorizontalAlignment(SwingConstants.CENTER);
		lblTituloVentanaInicio.setFont(new Font("SansSerif", Font.BOLD, 17)); 
        lblTituloVentanaInicio.setForeground(COLOR_PRIMARIO); 
		lblTituloVentanaInicio.setBounds(10, 19, 500, 30);
		panelVentanaInicio.add(lblTituloVentanaInicio);
		
		// LÓGICA DEL LOGO
		JLabel lblLogoVentanaInicio = new JLabel(); 
        
        final int LOGO_WIDTH = 150; 
        final int LOGO_HEIGHT = 150;

		lblLogoVentanaInicio.setBounds(192, 60, LOGO_WIDTH, LOGO_HEIGHT); 
		
        // RUTA DEL LOGO
        String logoPath = "/recursos/Logo.png";
        
        try {
            // Manejo de Archivos: Uso de getResource para cargar recursos.
            URL logoURL = VentanaInicio.class.getResource(logoPath); 
            
            if (logoURL != null) {
                ImageIcon originalIcon = new ImageIcon(logoURL);
                Image image = originalIcon.getImage();
                Image scaledImage = image.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
                lblLogoVentanaInicio.setIcon(new ImageIcon(scaledImage));
            } else {
                // Manejo de Errores: Si la URL es nula
                throw new Exception("URL del logo no encontrada. Ruta buscada en Classpath: " + logoPath);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el logo: " + e.getMessage());
            // Fallback
            lblLogoVentanaInicio.setText("[Logo No Encontrado]"); 
            lblLogoVentanaInicio.setFont(new Font("SansSerif", Font.BOLD, 14)); // Ajuste de fuente para el fallback
            lblLogoVentanaInicio.setForeground(COLOR_ACCENTO); 
            lblLogoVentanaInicio.setHorizontalAlignment(JLabel.CENTER);
        }
        
		panelVentanaInicio.add(lblLogoVentanaInicio);
		
        // -------------------------------------------------------------
        // --- 2. PANEL DE NOMBRES ---
        // -------------------------------------------------------------
		JPanel panelNombresVentanaInicio = new JPanel();
		panelNombresVentanaInicio.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
            "Desarrollado por:", 
            TitledBorder.LEADING, TitledBorder.TOP, 
            new Font("SansSerif", Font.BOLD, 12), 
            COLOR_PRIMARIO
        ));
        panelNombresVentanaInicio.setBackground(COLOR_PANEL_BLANCO); 
		panelNombresVentanaInicio.setBounds(65, 230, 400, 150); 
		panelNombresVentanaInicio.setLayout(null); 
		panelVentanaInicio.add(panelNombresVentanaInicio);
		
		JLabel lblNombre1VenInicio = new JLabel("Jose Miguel Bueno Martinez - 20251020093", JLabel.CENTER);
		lblNombre1VenInicio.setFont(new Font("SansSerif", Font.PLAIN, 15)); 
        lblNombre1VenInicio.setForeground(Color.DARK_GRAY);
		lblNombre1VenInicio.setBounds(10, 30, 380, 40); 
		panelNombresVentanaInicio.add(lblNombre1VenInicio);
		
		JLabel lblNombre2VenInicio = new JLabel("Anyelo Esteban Casas Zapata - 20251020106", JLabel.CENTER);
		lblNombre2VenInicio.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblNombre2VenInicio.setForeground(Color.DARK_GRAY);
		lblNombre2VenInicio.setBounds(10, 75, 380, 40); 
		panelNombresVentanaInicio.add(lblNombre2VenInicio);
		
        // -------------------------------------------------------------
        // --- 3. BOTONES ---
        // -------------------------------------------------------------
		
		JButton btnIniciarVenInicio = new JButton("INICIAR APLICACIÓN"); 
		btnIniciarVenInicio.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIniciarVenInicio.setBackground(COLOR_PRIMARIO); 
        btnIniciarVenInicio.setForeground(Color.WHITE); 
        btnIniciarVenInicio.setBorderPainted(false);
        btnIniciarVenInicio.setFocusPainted(false);

		btnIniciarVenInicio.setBounds(50, 400, 210, 45); 
        
        btnIniciarVenInicio.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic del botón Iniciar.
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirAplicacionPrincipal();
            }
        });
        
		panelVentanaInicio.add(btnIniciarVenInicio);
		
		JButton btnCerrarVenInicio = new JButton("CERRAR");
		btnCerrarVenInicio.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrarVenInicio.setBackground(COLOR_ACCENTO); 
        btnCerrarVenInicio.setForeground(Color.WHITE); 
        btnCerrarVenInicio.setBorderPainted(false);
        btnCerrarVenInicio.setFocusPainted(false);
        
		btnCerrarVenInicio.setBounds(275, 400, 210, 45); 
        
        btnCerrarVenInicio.addActionListener(new ActionListener() {
            /**
             * Maneja el evento de clic del botón Cerrar.
             * @param e El evento de acción.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cierra la aplicación de forma segura
                System.exit(0);
            }
        });
        
		panelVentanaInicio.add(btnCerrarVenInicio);
    }
    
    // -------------------------------------------------------------------------
    // MÉTODO QUE LANZA LoginView (Demostración de Arquitectura)
    // -------------------------------------------------------------------------

    /**
     * Inicializa la Capa Lógica (GestorUsuarios) y lanza la vista de Login (LoginView).
     */
    private void abrirAplicacionPrincipal() {
        try {
            // 1. Inicializar la Capa Lógica (Arquitectura por Capas)
            GestorUsuarios gestor = new GestorUsuarios();
            
            // 2. Crear y mostrar la ventana de Login, pasándole el gestor
            LoginView loginFrame = new LoginView(gestor); 
            
            // 3. Control de la Vista (End del Programa para esta vista)
            loginFrame.setLocationRelativeTo(null); 
            loginFrame.setVisible(true);
            dispose(); // Cierra la VentanaInicio
            
        } catch (Exception e) {
            // Manejo de Errores: Error crítico al fallar la inicialización
            JOptionPane.showMessageDialog(this, 
                "Error grave al iniciar la aplicación. Revise la inicialización de GestorUsuarios y LoginView.", 
                "Error de Ejecución Crítico", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}