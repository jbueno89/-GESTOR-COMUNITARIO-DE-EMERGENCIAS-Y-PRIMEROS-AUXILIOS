package vista;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL; // Nueva importación necesaria para la carga robusta de recursos

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.border.TitledBorder;

public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    
    // ** Constante: Define la clase principal a abrir (debe existir en el paquete 'vista') **
    private static final String NOMBRE_CLASE_PRINCIPAL = "vista.PanelPrincipal"; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicio frame = new VentanaInicio();
                    // Centrar la ventana al iniciar
                    frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaInicio() {
        // Ajuste del título de la ventana
        setTitle("Inicio - Gestor Comunitario");
        
		setMaximumSize(new Dimension(500, 450));
		setBackground(new Color(240, 240, 240));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 245, 245)); // Color de fondo uniforme
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelVentanaInicio = new JPanel();
		panelVentanaInicio.setBounds(0, 0, 534, 461);
        panelVentanaInicio.setBackground(contentPane.getBackground());
		contentPane.add(panelVentanaInicio);
		panelVentanaInicio.setLayout(null);
		
		JLabel lblTituloVentanaInicio = new JLabel("GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS", JLabel.CENTER);
		lblTituloVentanaInicio.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloVentanaInicio.setForeground(new Color(25, 118, 210)); // Color distintivo para el título
		lblTituloVentanaInicio.setBounds(10, 19, 500, 30);
		panelVentanaInicio.add(lblTituloVentanaInicio);
		
		// -------------------------------------------------------------------------
		// LÓGICA DEL LOGO: Carga Logo.png (usando ruta relativa al paquete)
        // -------------------------------------------------------------------------
		JLabel lblLogoVentanaInicio = new JLabel(); // Etiqueta base
        
        // ** Modificación: Eliminamos logoSize=80 y definimos el tamaño del JLabel directamente **
        final int LOGO_WIDTH = 135;
        final int LOGO_HEIGHT = 134;

		lblLogoVentanaInicio.setBounds(200, 60, LOGO_WIDTH, LOGO_HEIGHT); // Usando 135x134
		
        // La ruta es relativa a la clase VentanaInicio, por lo que busca en vista/recursos/Logo.png
        String logoPath = "recursos/Logo.png";
        
        try {
            // Intentamos cargar el recurso usando la ruta relativa a la clase.
            URL logoURL = VentanaInicio.class.getResource(logoPath);
            
            if (logoURL != null) {
                // Si la URL se encuentra, cargamos y escalamos la imagen
                ImageIcon originalIcon = new ImageIcon(logoURL);
                Image image = originalIcon.getImage();
                // ** CORRECCIÓN: Usamos LOGO_WIDTH y LOGO_HEIGHT para escalar la imagen al tamaño correcto (135x134) **
                Image scaledImage = image.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
                lblLogoVentanaInicio.setIcon(new ImageIcon(scaledImage));
            } else {
                // Si ninguna URL fue encontrada, lanzamos una excepción para activar el fallback
                throw new Exception("URL del logo no encontrada. Ruta buscada: vista/" + logoPath);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el logo. Verifique que 'vista/recursos/Logo.png' exista.");
            // Fallback (si la imagen no se carga)
            lblLogoVentanaInicio.setText("\u26A1");
            lblLogoVentanaInicio.setFont(new Font("SansSerif", Font.PLAIN, 72));
            lblLogoVentanaInicio.setForeground(new Color(255, 165, 0));
            lblLogoVentanaInicio.setHorizontalAlignment(JLabel.CENTER);
        }
        
		panelVentanaInicio.add(lblLogoVentanaInicio);
		
		// -------------------------------------------------------------------------
		// PANEL DE NOMBRES (TitledBorder ya aplicado) - POSICIÓN MANTENIDA
        // -------------------------------------------------------------------------
		JPanel panelNombresVentanaInicio = new JPanel();
		panelNombresVentanaInicio.setBorder(new TitledBorder(null, "Desarrollado por :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelNombresVentanaInicio.setBounds(62, 205, 400, 150);
		panelNombresVentanaInicio.setLayout(null); // Mantener el layout nulo para las posiciones internas
		panelVentanaInicio.add(panelNombresVentanaInicio);
		
		JLabel lblNombre1VenInicio = new JLabel("Jose Miguel Bueno Martinez - 20251020093", JLabel.CENTER);
		lblNombre1VenInicio.setFont(new Font("SansSerif", Font.ITALIC, 15));
		lblNombre1VenInicio.setBounds(10, 30, 380, 40); // Posición dentro del panel ajustada para centrado
		panelNombresVentanaInicio.add(lblNombre1VenInicio);
		
		JLabel lblNombre2VenInicio = new JLabel("Anyelo Esteban Casas Zapata - 20251020106", JLabel.CENTER);
		lblNombre2VenInicio.setFont(new Font("SansSerif", Font.ITALIC, 15));
		lblNombre2VenInicio.setBounds(10, 75, 380, 40); // Posición dentro del panel ajustada para centrado
		panelNombresVentanaInicio.add(lblNombre2VenInicio);
		
		// -------------------------------------------------------------------------
		// BOTONES Y LÓGICA DE EVENTOS - POSICIÓN MANTENIDA
        // -------------------------------------------------------------------------
		JButton btnIniciarVenInicio = new JButton("INICIAR");
		btnIniciarVenInicio.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIniciarVenInicio.setBackground(new Color(46, 204, 113)); // Estilo moderno
        btnIniciarVenInicio.setForeground(Color.WHITE); 
		btnIniciarVenInicio.setBounds(50, 380, 180, 40);
        
        // ** Acción INICIAR: Llama al método para abrir la AppPrincipal **
        btnIniciarVenInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirAplicacionPrincipal();
            }
        });
        
		panelVentanaInicio.add(btnIniciarVenInicio);
		
		JButton btnCerrarVenInicio = new JButton("CERRAR");
		btnCerrarVenInicio.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrarVenInicio.setBackground(new Color(231, 76, 60)); // Estilo moderno
        btnCerrarVenInicio.setForeground(Color.WHITE); 
		btnCerrarVenInicio.setBounds(297, 380, 180, 40);
        
        // ** Acción CERRAR: Cierra la aplicación **
        btnCerrarVenInicio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
		panelVentanaInicio.add(btnCerrarVenInicio);
	}
    
    /**
     * Método que utiliza Reflection para cargar dinámicamente la ventana principal 
     * (PanelPrincipal) y cerrar esta ventana de inicio (Splash Screen).
     */
    private void abrirAplicacionPrincipal() {
        try {
            // 1. Cargar la clase principal por su nombre completo (paquete.clase)
            Class<?> mainClass = Class.forName(NOMBRE_CLASE_PRINCIPAL);
            
            // 2. Obtener el constructor y crear una nueva instancia de la ventana principal
            Constructor<?> constructor = mainClass.getDeclaredConstructor();
            JFrame mainFrame = (JFrame) constructor.newInstance();
            
            // 3. Mostrar la nueva ventana y cerrar la ventana actual
            mainFrame.setLocationRelativeTo(null); 
            mainFrame.setVisible(true);
            dispose(); 

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, 
                "Error: La clase principal '" + NOMBRE_CLASE_PRINCIPAL + "' no fue encontrada. Por favor, crea la clase 'PanelPrincipal' en el paquete 'vista'.", 
                "Error de Carga", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al iniciar la aplicación principal: " + e.getMessage(), 
                "Error de Ejecución", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}