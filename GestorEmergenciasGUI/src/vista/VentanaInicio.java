package vista;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
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

public class VentanaInicio extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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

		JLabel lblTituloVentanaInicio = new JLabel("GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS", JLabel.CENTER);
		lblTituloVentanaInicio.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloVentanaInicio.setForeground(new Color(25, 118, 210));
		lblTituloVentanaInicio.setBounds(10, 19, 500, 30);
		panelVentanaInicio.add(lblTituloVentanaInicio);

		// LOGO
		JLabel lblLogoVentanaInicio = new JLabel();
		final int LOGO_WIDTH = 135;
		final int LOGO_HEIGHT = 134;
		lblLogoVentanaInicio.setBounds(200, 60, LOGO_WIDTH, LOGO_HEIGHT);

		String logoPath = "recursos/Logo.png";
		try {
			URL logoURL = VentanaInicio.class.getResource(logoPath);
			if (logoURL != null) {
				ImageIcon originalIcon = new ImageIcon(logoURL);
				Image scaledImage = originalIcon.getImage().getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
				lblLogoVentanaInicio.setIcon(new ImageIcon(scaledImage));
			} else {
				throw new Exception("No se encontró Logo.png");
			}
		} catch (Exception e) {
			lblLogoVentanaInicio.setText("\u26A1");
			lblLogoVentanaInicio.setFont(new Font("SansSerif", Font.PLAIN, 72));
			lblLogoVentanaInicio.setForeground(Color.ORANGE);
			lblLogoVentanaInicio.setHorizontalAlignment(JLabel.CENTER);
		}

		panelVentanaInicio.add(lblLogoVentanaInicio);

		// PANEL NOMBRES
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

		// BOTONES
		JButton btnIniciar = new JButton("INICIAR");
		btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnIniciar.setBackground(new Color(46, 204, 113));
		btnIniciar.setForeground(Color.WHITE);
		btnIniciar.setBounds(50, 380, 180, 40);
		btnIniciar.addActionListener(e -> abrirVentanaRoles());
		panelVentanaInicio.add(btnIniciar);

		JButton btnCerrar = new JButton("CERRAR");
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnCerrar.setBackground(new Color(231, 76, 60));
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setBounds(297, 380, 180, 40);
		btnCerrar.addActionListener(e -> System.exit(0));
		panelVentanaInicio.add(btnCerrar);
	}

	private void abrirVentanaRoles() {
		VentanaRoles roles = new VentanaRoles();
		roles.setLocationRelativeTo(null);
		roles.setVisible(true);
		dispose();
	}
}
