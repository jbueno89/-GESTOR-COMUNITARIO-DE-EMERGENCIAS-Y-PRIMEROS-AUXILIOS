package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// Variables funcionales
	private int mouseX, mouseY;
    private CardLayout cardLayout;
    private JTextField tfBuscarIncidentes;
    private JTable tableIncidentes;
    private JTextField tfBuscarNombreSuminis;
    private JTable tableSuministros;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PanelPrincipal frame = new PanelPrincipal();
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
	public PanelPrincipal() {
        
        // 1. CONFIGURACIÓN DE LA VENTANA
        setUndecorated(true); // Anula la barra de título de Windows
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(1000, 700));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelFondo = new JPanel();
		panelFondo.setPreferredSize(new Dimension(1000, 700));
		panelFondo.setBounds(0, 0, 1000, 700);
		contentPane.add(panelFondo);
		panelFondo.setLayout(null);
		
		JPanel panelBarra = new JPanel();
		panelBarra.setBounds(0, 0, 1000, 40);
		panelFondo.add(panelBarra);
		panelBarra.setLayout(null);
		
		JLabel lblTituloLogo = new JLabel("New label");
		lblTituloLogo.setBounds(10, 9, 277, 23);
        lblTituloLogo.setFont(new Font("SansSerif", Font.BOLD, 14));
		panelBarra.add(lblTituloLogo);
        
        // Lógica de Minimizar
		JButton btnMinimizar = new JButton("-");
		btnMinimizar.setBounds(905, 3, 41, 31);
		btnMinimizar.setFont(new Font("SansSerif", Font.BOLD, 20));
		btnMinimizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                setState(JFrame.ICONIFIED);
			}
		});
		panelBarra.add(btnMinimizar);
		
        // Lógica de Cerrar
		JButton btnCerrar = new JButton("x");
		btnCerrar.setBounds(949, 3, 41, 31);
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                System.exit(0);
			}
		});
		
		JLabel label_1 = new JLabel("");
		label_1.setBounds(360, 0, 106, 13);
		panelBarra.add(label_1);
		panelBarra.add(btnCerrar);
        
        // Lógica de Arrastre (Mover la ventana)
        panelBarra.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        panelBarra.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
            }
        });
		
		JPanel panelBotones = new JPanel();
		panelBotones.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelBotones.setBounds(0, 40, 200, 620);
		panelFondo.add(panelBotones);
		panelBotones.setLayout(null);
		
        // ----------------------------------------------------
        // 2. CONEXIÓN DE CARDLAYOUT
        // ----------------------------------------------------
		JPanel panelContenido = new JPanel();
		panelContenido.setBounds(199, 39, 801, 620);
		panelFondo.add(panelContenido);
		
        cardLayout = new CardLayout(0, 0); 
        panelContenido.setLayout(cardLayout); 
		
        // ************************************************************
        // NOTA: Los nombres clave (String) de las tarjetas son: 
        // "Dashboard", "Incidentes", "Inventario", "Brigadistas", "GuiasPAuxilios", "Reportes"
        // ************************************************************
        
		JPanel panelIncidentes = new JPanel();
		panelContenido.add(panelIncidentes, "Incidentes"); 
		panelIncidentes.setLayout(null); 
		
		JLabel lblTituloIncidentesActCurso = new JLabel("Incidentes Activos en Curso");
		lblTituloIncidentesActCurso.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloIncidentesActCurso.setBounds(20, 11, 301, 25);
		panelIncidentes.add(lblTituloIncidentesActCurso);
		
		JLabel lblfiltroIncidentes = new JLabel("Filtrar :");
		lblfiltroIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblfiltroIncidentes.setBounds(20, 50, 51, 25);
		panelIncidentes.add(lblfiltroIncidentes);
		
		tfBuscarIncidentes = new JTextField();
		tfBuscarIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tfBuscarIncidentes.setBounds(75, 50, 198, 25);
		panelIncidentes.add(tfBuscarIncidentes);
		tfBuscarIncidentes.setColumns(10);
		
		JComboBox cbFiltroPrioridad = new JComboBox();
		cbFiltroPrioridad.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroPrioridad.setBounds(291, 50, 120, 25);
		panelIncidentes.add(cbFiltroPrioridad);
		
		JButton btnCrearIncidente = new JButton("Crear Incidente");
		btnCrearIncidente.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnCrearIncidente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCrearIncidente.setBounds(670, 50, 121, 25);
		panelIncidentes.add(btnCrearIncidente);
		
		JScrollPane spIncidentes = new JScrollPane();
		spIncidentes.setBounds(20, 90, 771, 480);
		panelIncidentes.add(spIncidentes);
		
		tableIncidentes = new JTable();
		tableIncidentes.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Tipo", "Prioridad", "Ubicaci\u00F3n", "Tiempo Activo", "Brigadista Asignado"
			}
		));
		tableIncidentes.getColumnModel().getColumn(4).setPreferredWidth(87);
		tableIncidentes.getColumnModel().getColumn(5).setPreferredWidth(117);
		tableIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		spIncidentes.setViewportView(tableIncidentes);
		
		JButton btnVerDetallesInciden = new JButton("Ver Detalles");
		btnVerDetallesInciden.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnVerDetallesInciden.setBounds(20, 581, 150, 28);
		panelIncidentes.add(btnVerDetallesInciden);
		
		JButton btnMarcaResueltoIncide = new JButton("Marcar como Resuelto");
		btnMarcaResueltoIncide.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnMarcaResueltoIncide.setBounds(180, 581, 180, 28);
		panelIncidentes.add(btnMarcaResueltoIncide);
		
		JPanel panelInventario = new JPanel();
		panelContenido.add(panelInventario, "Inventario"); 
		panelInventario.setLayout(null);
		
		JLabel lblTituloInventario = new JLabel("Gestión de Inventario y Suministros");
		lblTituloInventario.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloInventario.setBounds(20, 11, 350, 25);
		panelInventario.add(lblTituloInventario);
		
		JLabel lblBuscarInventario = new JLabel("Buscar :");
		lblBuscarInventario.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblBuscarInventario.setBounds(20, 53, 75, 25);
		panelInventario.add(lblBuscarInventario);
		
		tfBuscarNombreSuminis = new JTextField();
		tfBuscarNombreSuminis.setBounds(95, 53, 250, 25);
		panelInventario.add(tfBuscarNombreSuminis);
		tfBuscarNombreSuminis.setColumns(10);
		
		JComboBox cbFiltroUbi = new JComboBox();
		cbFiltroUbi.setBounds(355, 53, 120, 25);
		panelInventario.add(cbFiltroUbi);
		
		JButton btnAñadirSuministro = new JButton("Añadir Stock");
		btnAñadirSuministro.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAñadirSuministro.setBounds(520, 53, 120, 25);
		panelInventario.add(btnAñadirSuministro);
		
		JButton btnReporteSuministro = new JButton("Generar Reporte");
		btnReporteSuministro.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnReporteSuministro.setBounds(650, 53, 141, 25);
		panelInventario.add(btnReporteSuministro);
		
		JScrollPane spSuministros = new JScrollPane();
		spSuministros.setBounds(20, 89, 771, 480);
		panelInventario.add(spSuministros);
		
		tableSuministros = new JTable();
		tableSuministros.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Suministro", "Stock Actual", "Unidad (Kg/Uni)", "M\u00EDnimo Cr\u00EDtico", "Ubicaci\u00F3n", "Fecha de Caducidad"
			}
		));
		tableSuministros.getColumnModel().getColumn(3).setPreferredWidth(90);
		tableSuministros.getColumnModel().getColumn(6).setPreferredWidth(116);
		spSuministros.setViewportView(tableSuministros);
		
		JButton btnEditStockSuminis = new JButton("Editar Stock");
		btnEditStockSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnEditStockSuminis.setBounds(20, 580, 150, 29);
		panelInventario.add(btnEditStockSuminis);
		
		JButton btnMarcarCriticSuminis = new JButton("Marcar Crítico");
		btnMarcarCriticSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnMarcarCriticSuminis.setBounds(180, 580, 150, 29);
		panelInventario.add(btnMarcarCriticSuminis);
		
		JPanel panelBrigadistas = new JPanel();
		panelContenido.add(panelBrigadistas, "Brigadistas"); 
		panelBrigadistas.setLayout(null);
		
		JPanel panelGuiasPAuxilios = new JPanel();
		panelContenido.add(panelGuiasPAuxilios, "GuiasPAuxilios"); 
		panelGuiasPAuxilios.setLayout(null);
		
		JPanel panelReportes = new JPanel();
		panelContenido.add(panelReportes, "Reportes"); 
		panelReportes.setLayout(null);
		
		JPanel panelDashboard = new JPanel();
		panelDashboard.setFont(new Font("SansSerif", Font.BOLD, 30));
		panelContenido.add(panelDashboard, "Dashboard"); 
		panelDashboard.setLayout(null);
        
        cardLayout.show(panelContenido, "Dashboard"); // Vista inicial
        
        // ----------------------------------------------------
        // 3. LISTENERS DE BOTONES (Conexión)
        // ----------------------------------------------------
		
		JButton btnDashboard = new JButton("Tablero");
		btnDashboard.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnDashboard.setBounds(10, 21, 180, 40);
        btnDashboard.addActionListener(e -> cardLayout.show(panelContenido, "Dashboard"));
		panelBotones.add(btnDashboard);
		
		JButton btnIncidentesActivos = new JButton("Incidentes Activos");
		btnIncidentesActivos.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnIncidentesActivos.addActionListener(e -> cardLayout.show(panelContenido, "Incidentes"));
		btnIncidentesActivos.setBounds(10, 72, 180, 40);
		panelBotones.add(btnIncidentesActivos);
		
		JButton btnInventarioSuministros = new JButton("Inventario Suministros");
		btnInventarioSuministros.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnInventarioSuministros.setBounds(10, 123, 180, 40);
        btnInventarioSuministros.addActionListener(e -> cardLayout.show(panelContenido, "Inventario"));
		panelBotones.add(btnInventarioSuministros);
		
		JButton btnBrigadistas = new JButton("Brigadistas");
		btnBrigadistas.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnBrigadistas.setBounds(10, 174, 180, 40);
        btnBrigadistas.addActionListener(e -> cardLayout.show(panelContenido, "Brigadistas"));
		panelBotones.add(btnBrigadistas);
		
		JButton btnGuiasPAuxilios = new JButton("Guias Primeros Auxilios");
		btnGuiasPAuxilios.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuiasPAuxilios.setBounds(10, 225, 180, 40);
        btnGuiasPAuxilios.addActionListener(e -> cardLayout.show(panelContenido, "GuiasPAuxilios"));
		panelBotones.add(btnGuiasPAuxilios);
		
		JButton btnReportes = new JButton("Reportes");
		btnReportes.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnReportes.setBounds(10, 276, 180, 40);
        btnReportes.addActionListener(e -> cardLayout.show(panelContenido, "Reportes"));
		panelBotones.add(btnReportes);
		
        // Footer (Añadido para completar la estructura 1000x700)
        JPanel panelFooter = new JPanel();
        panelFooter.setBounds(0, 660, 1000, 40);
        panelFondo.add(panelFooter);

        // --- Componentes del Dashboard (Se mantienen) ---
		JPanel panelIncidentesAbiertos = new JPanel();
		panelIncidentesAbiertos.setBounds(20, 70, 250, 100);
		panelDashboard.add(panelIncidentesAbiertos);
		panelIncidentesAbiertos.setLayout(null);
		
		JLabel lblNumIncidentes = new JLabel("12");
		lblNumIncidentes.setFont(new Font("SansSerif", Font.BOLD, 30));
		lblNumIncidentes.setBounds(20, 21, 100, 36);
		panelIncidentesAbiertos.add(lblNumIncidentes);
		
		JLabel lblIncidentesAbiertos = new JLabel("Incidentes Abiertos");
		lblIncidentesAbiertos.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblIncidentesAbiertos.setBounds(20, 60, 200, 16);
		panelIncidentesAbiertos.add(lblIncidentesAbiertos);
		
		JPanel panelBrigadistasLibres = new JPanel();
		panelBrigadistasLibres.setBounds(280, 70, 250, 100);
		panelDashboard.add(panelBrigadistasLibres);
		panelBrigadistasLibres.setLayout(null);
		
		JLabel lblNumBrigadistas = new JLabel("5");
		lblNumBrigadistas.setFont(new Font("SansSerif", Font.BOLD, 30));
		lblNumBrigadistas.setBounds(20, 21, 100, 40);
		panelBrigadistasLibres.add(lblNumBrigadistas);
		
		JLabel lblBrigadistasLibres = new JLabel("Brigadistas Libres");
		lblBrigadistasLibres.setBounds(20, 61, 200, 16);
		panelBrigadistasLibres.add(lblBrigadistasLibres);
		lblBrigadistasLibres.setFont(new Font("SansSerif", Font.PLAIN, 12));
		
		JPanel panelStockCritico = new JPanel();
		panelStockCritico.setLayout(null);
		panelStockCritico.setBounds(540, 70, 250, 100);
		panelDashboard.add(panelStockCritico);
		
		JLabel lblNumStock = new JLabel("5");
		lblNumStock.setFont(new Font("SansSerif", Font.BOLD, 30));
		lblNumStock.setBounds(20, 21, 100, 40);
		panelStockCritico.add(lblNumStock);
		
		JLabel lblStockCritico = new JLabel("Stock Crítico");
		lblStockCritico.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblStockCritico.setBounds(20, 61, 200, 16);
		panelStockCritico.add(lblStockCritico);
		
		JPanel panelIncidentesPrioridad = new JPanel();
		panelIncidentesPrioridad.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelIncidentesPrioridad.setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Incidentes por Prioridad", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)), "Incidentes por Prioridad", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelIncidentesPrioridad.setBounds(20, 190, 450, 200);
		panelDashboard.add(panelIncidentesPrioridad);
		
		JPanel panelBrigadistasEspecialidad = new JPanel();
		panelBrigadistasEspecialidad.setBorder(new TitledBorder(null, "Brigadistas por Especialidad", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelBrigadistasEspecialidad.setBounds(480, 190, 310, 200);
		panelDashboard.add(panelBrigadistasEspecialidad);

	}
}