/*
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
import java.awt.Image;
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

/**
 * Clase principal de la **Vista** que representa la ventana de inicio de la aplicación.
 * Muestra el título del proyecto, un logo (o un placeholder si no se encuentra),
 * los nombres de los desarrolladores y botones para iniciar o cerrar la aplicación.
 * <p>
 * Implementa el método {@code main} para lanzar la interfaz gráfica.
 * </p>
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	/** Panel de contenido principal de la ventana. */
	private final JPanel contentPane;

	/**
	 * Método de entrada principal de la aplicación.
	 * Lanza la ventana de inicio en el Event Dispatch Thread (EDT).
	 * @param args Argumentos de la línea de comandos (no utilizados).
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicio frame = new VentanaInicio();
					frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor de la ventana de inicio.
	 * Configura la apariencia, el contenido y los manejadores de eventos.
	 */
	public VentanaInicio() {
		setTitle("Inicio - Gestor Comunitario");
		setMaximumSize(new Dimension(500, 450));
		setBackground(new Color(240, 240, 240));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 500);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(245, 245, 245));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panelVentanaInicio = new JPanel();
		panelVentanaInicio.setBounds(0, 0, 534, 461);
		panelVentanaInicio.setBackground(contentPane.getBackground());
		contentPane.add(panelVentanaInicio);
		panelVentanaInicio.setLayout(null);

		// Título de la Aplicación
		JLabel lblTituloVentanaInicio = new JLabel("GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS", JLabel.CENTER);
		lblTituloVentanaInicio.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloVentanaInicio.setForeground(new Color(25, 118, 210));
		lblTituloVentanaInicio.setBounds(10, 19, 500, 30);
		panelVentanaInicio.add(lblTituloVentanaInicio);

		// --- LOGO (Manejo de Archivos y Excepciones) ---
		JLabel lblLogoVentanaInicio = new JLabel();
		final int LOGO_WIDTH = 135;
		final int LOGO_HEIGHT = 134;
		lblLogoVentanaInicio.setBounds(200, 60, LOGO_WIDTH, LOGO_HEIGHT);

		String logoPath = "recursos/Logo.png";
		try {
			// Intenta cargar el recurso del logo usando el ClassLoader
			URL logoURL = VentanaInicio.class.getResource(logoPath);
			if (logoURL != null) {
				ImageIcon originalIcon = new ImageIcon(logoURL);
				// Escalar la imagen para ajustarla al tamaño del JLabel
				Image scaledImage = originalIcon.getImage().getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
				lblLogoVentanaInicio.setIcon(new ImageIcon(scaledImage));
			} else {
				// Si no se encuentra, lanza una excepción para usar el placeholder
				throw new Exception("No se encontró Logo.png");
			}
		} catch (Exception e) {
			// Placeholder si falla la carga del logo
			lblLogoVentanaInicio.setText("\u26A1"); // Icono de rayo (Emergencia)
			lblLogoVentanaInicio.setFont(new Font("SansSerif", Font.PLAIN, 72));
			lblLogoVentanaInicio.setForeground(Color.ORANGE);
			lblLogoVentanaInicio.setHorizontalAlignment(JLabel.CENTER);
			System.err.println("Advertencia: No se pudo cargar el archivo " + logoPath + ".");
		}

		// CORRECCIÓN: el nombre de la variable era incorrecto en el código original.
		// El código original decía: panelVentanaInicio.add(lblLogoVentanoInicio);
		// Y la declaración es: JLabel lblLogoVentanaInicio = new JLabel();
		// Asumo que fue un error tipográfico y uso la variable correcta.
		panelVentanaInicio.add(lblLogoVentanaInicio); 

		// --- PANEL NOMBRES (Información de Desarrolladores) ---
		JPanel panelNombres = new JPanel();
		panelNombres.setBorder(new TitledBorder(null, "Desarrollado por :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelNombres.setBounds(62, 205, 400, 150);
		panelNombres.setLayout(null);
		panelVentanaInicio.add(panelNombres);

		JLabel lblNombre1 = new JLabel("Jose Miguel Bueno Martinez - 20251020093", JLabel.CENTER);
		lblNombre1.setFont(new Font("SansSerif", Font.ITALIC, 15));
		lblNombre1.setBounds(10, 30, 380, 40);
		panelNombres.add(lblNombre1);

		JLabel lblNombre2 = new JLabel("Anyelo Esteban Casas Zapata - 20251020106", JLabel.CENTER);
		lblNombre2.setFont(new Font("SansSerif", Font.ITALIC, 15));
		lblNombre2.setBounds(10, 75, 380, 40);
		panelNombres.add(lblNombre2);

		// --- BOTONES ---
		
		// Botón INICIAR (Delegación para abrir la siguiente ventana)
		JButton btnIniciar = new JButton("INICIAR");
		btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnIniciar.setBackground(new Color(46, 204, 113)); // Verde
		btnIniciar.setForeground(Color.WHITE);
		btnIniciar.setBounds(50, 380, 180, 40);
		btnIniciar.addActionListener(e -> abrirVentanaRoles()); // Llama al método de navegación
		panelVentanaInicio.add(btnIniciar);

		// Botón CERRAR (Salida del sistema)
		JButton btnCerrar = new JButton("CERRAR");
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnCerrar.setBackground(new Color(231, 76, 60)); // Rojo
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setBounds(297, 380, 180, 40);
		btnCerrar.addActionListener(e -> System.exit(0));
		panelVentanaInicio.add(btnCerrar);
	}

	/**
	 * Método de navegación que abre la ventana de selección de roles
	 * y cierra la ventana actual.
	 */
	private void abrirVentanaRoles() {
		// Asumiendo la existencia de una clase VentanaRoles
		VentanaRoles roles = new VentanaRoles();
		roles.setLocationRelativeTo(null);
		roles.setVisible(true);
		this.dispose(); // Cierra la ventana de inicio
	}
}