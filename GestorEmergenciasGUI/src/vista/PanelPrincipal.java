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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JEditorPane;
// LIBRERÍAS AÑADIDAS PARA LAS FUNCIONALIDADES:
import java.text.SimpleDateFormat;
import javax.swing.Timer;
import java.util.Date;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import javax.swing.border.LineBorder;
import java.net.URL; // Necesario para cargar recursos
import javax.swing.ImageIcon; // Necesario para el logo
import java.awt.Image; // Necesario para escalar el logo

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
	private JTextField tfBuscarBrigadistas;
	private JTable tableBrigadistas;
	private JTextField tfProtocolosPrimAux;

	// Cambiamos JTextField por JFormattedTextField para las fechas (Ver
	// Constructor)
	private JFormattedTextField tfFechaInicioReporte;
	private JFormattedTextField tfFechaFinReporte;

	private JTable tableReportesResultPrevi;

	// Declaramos los JLabels del Footer para poder actualizarlos
	private JLabel lblEstadoFooter;
	private JLabel lblVersionFooter;

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
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		this.setSize(1000, 700);
		this.setLocationRelativeTo(null);
		this.setResizable(false); // <--- [FUNCIONALIDAD AÑADIDA] Bloquea la redimensión

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

		// -------------------------------------------------------------------------
		// LÓGICA DEL LOGO: Carga Logo.png y escalado dinámico al tamaño de lblLogo
        // -------------------------------------------------------------------------
		JLabel lblLogo = new JLabel("");
		final int LOGO_X = 6;
		final int LOGO_Y = 2;
		final int LOGO_WIDTH = 50;
		final int LOGO_HEIGHT = 37;
		
		lblLogo.setBounds(6, 2, 50, 37);
		lblLogo.setFont(new Font("SansSerif", Font.BOLD, 14));
		panelBarra.add(lblLogo);
		
		String logoPath = "recursos/Logo.png";
		try {
		    URL logoURL = VentanaInicio.class.getResource(logoPath);
		    if (logoURL != null) {
		        ImageIcon originalIcon = new ImageIcon(logoURL);
		        Image image = originalIcon.getImage();
		        // Escalamos la imagen al tamaño actual del JLabel
		        Image scaledImage = image.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
		        lblLogo.setIcon(new ImageIcon(scaledImage));
		    } else {
		        // Fallback si la imagen no se carga
		        lblLogo.setText("I"); 
		        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 20));
		    }
		} catch (Exception e) {
		    System.err.println("Error al cargar el logo en PanelPrincipal: vista/" + logoPath);
		    lblLogo.setText("I");
		}
		// -------------------------------------------------------------------------
		// FIN LÓGICA DEL LOGO
        // -------------------------------------------------------------------------

		// Lógica de Minimizar
		JButton btnMinimizar = new JButton("-");
		btnMinimizar.setBounds(885, 2, 50, 37);
		btnMinimizar.setFont(new Font("SansSerif", Font.BOLD, 20));
		btnMinimizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(JFrame.ICONIFIED);
			}
		});
		panelBarra.add(btnMinimizar);

		// Lógica de Cerrar
		JButton btnCerrar = new JButton("x");
		btnCerrar.setBounds(937, 2, 53, 37);
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panelBarra.add(btnCerrar);
		
		JLabel lblTitulo = new JLabel("Selfsecurity\r\n");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 20));
		lblTitulo.setBounds(49, 9, 128, 23);
		panelBarra.add(lblTitulo);

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
		// "Dashboard", "Incidentes", "Inventario", "Brigadistas", "GuiasPAuxilios",
		// "Reportes"
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
		cbFiltroPrioridad.setModel(
				new DefaultComboBoxModel(new String[] { "Prioridad Alta", "Prioridad Media", "Prioridad Baja" }));
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
		tableIncidentes.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Tipo", "Prioridad", "Ubicaci\u00F3n", "Tiempo Activo", "Brigadista Asignado" }));
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
		cbFiltroUbi.setModel(
				new DefaultComboBoxModel(new String[] { "Almacén Principal", "Almacén A", "Almacén B", "Almacén C" }));
		cbFiltroUbi.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroUbi.setBounds(355, 53, 127, 25);
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
		tableSuministros
				.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Suministro", "Stock Actual",
						"Unidad (Kg/Uni)", "M\u00EDnimo Cr\u00EDtico", "Ubicaci\u00F3n", "Fecha de Caducidad" }));
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

		JLabel lblTituloBrigadista = new JLabel("Gestión de Brigadistas y Personal ");
		lblTituloBrigadista.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloBrigadista.setBounds(20, 11, 350, 25);
		panelBrigadistas.add(lblTituloBrigadista);

		JLabel lblBuscarBrigadistas = new JLabel("Buscar :");
		lblBuscarBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblBuscarBrigadistas.setBounds(20, 50, 75, 25);
		panelBrigadistas.add(lblBuscarBrigadistas);

		tfBuscarBrigadistas = new JTextField();
		tfBuscarBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tfBuscarBrigadistas.setColumns(10);
		tfBuscarBrigadistas.setBounds(95, 51, 250, 25);
		panelBrigadistas.add(tfBuscarBrigadistas);

		JComboBox cbFiltroEstadoBrig = new JComboBox();
		cbFiltroEstadoBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroEstadoBrig.setModel(new DefaultComboBoxModel(new String[] { "Libre", "En Servicio", "Descanso" }));
		cbFiltroEstadoBrig.setToolTipText("");
		cbFiltroEstadoBrig.setBounds(355, 51, 120, 25);
		panelBrigadistas.add(cbFiltroEstadoBrig);

		JButton btnAddBrigadista = new JButton("Añadir Brigadista");
		btnAddBrigadista.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAddBrigadista.setBounds(651, 51, 140, 25);
		panelBrigadistas.add(btnAddBrigadista);

		JScrollPane spBrigadistas = new JScrollPane();
		spBrigadistas.setBounds(20, 90, 455, 500);
		panelBrigadistas.add(spBrigadistas);

		tableBrigadistas = new JTable();
		tableBrigadistas.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Nombre", "Estado", "Especialidad", "Tel\u00E9fono" }));
		tableBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		spBrigadistas.setViewportView(tableBrigadistas);

		JPanel panelDetallesBrigadista = new JPanel(); // Cambiado de 'panel' a 'panelDetallesBrigadista' para mejor
														// claridad
		panelDetallesBrigadista.setBounds(490, 90, 290, 500);
		panelBrigadistas.add(panelDetallesBrigadista);
		panelDetallesBrigadista.setLayout(null);
		panelDetallesBrigadista.setBorder(new LineBorder(new Color(190, 190, 190), 1)); // <--- [FUNCIONALIDAD AÑADIDA]
																						// Borde para estructurar

		JLabel lblIdBrigadistas = new JLabel("ID: [ ID seleccionado ]");
		lblIdBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblIdBrigadistas.setBounds(10, 21, 270, 20);
		panelDetallesBrigadista.add(lblIdBrigadistas);

		JLabel lblNombreBrigadista = new JLabel("Nombre: [ Nombre ]");
		lblNombreBrigadista.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblNombreBrigadista.setBounds(10, 51, 270, 20);
		panelDetallesBrigadista.add(lblNombreBrigadista);

		JLabel lblEstadoActualBrig = new JLabel("Estado Actual: [ Estado ]");
		lblEstadoActualBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEstadoActualBrig.setBounds(10, 91, 155, 27);
		panelDetallesBrigadista.add(lblEstadoActualBrig);

		JComboBox cbEditarEstadoBrig = new JComboBox();
		cbEditarEstadoBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbEditarEstadoBrig.setModel(new DefaultComboBoxModel(new String[] { "Libre", "En Servicio", "Descanso" }));
		cbEditarEstadoBrig.setBounds(170, 115, 110, 27);
		panelDetallesBrigadista.add(cbEditarEstadoBrig);

		JLabel lblEditarEstadoBrig = new JLabel("Editar:");
		lblEditarEstadoBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEditarEstadoBrig.setBounds(170, 91, 40, 27);
		panelDetallesBrigadista.add(lblEditarEstadoBrig);

		JLabel lblEspacialidadBrigadista = new JLabel("Especialidad: [ Tipo ]");
		lblEspacialidadBrigadista.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEspacialidadBrigadista.setBounds(10, 151, 155, 27);
		panelDetallesBrigadista.add(lblEspacialidadBrigadista);

		JButton btnGuardarCamEstadoBrig = new JButton("Guardar Cambios de Estado");
		btnGuardarCamEstadoBrig.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuardarCamEstadoBrig.setBounds(10, 451, 270, 38);
		panelDetallesBrigadista.add(btnGuardarCamEstadoBrig);

		JPanel panelGuiasPAuxilios = new JPanel();
		panelContenido.add(panelGuiasPAuxilios, "GuiasPAuxilios");
		panelGuiasPAuxilios.setLayout(null);

		JLabel lblTituloPrimerosAux = new JLabel("Biblioteca de Guías y Protocolos de  Auxilios");
		lblTituloPrimerosAux.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloPrimerosAux.setBounds(20, 11, 350, 25);
		panelGuiasPAuxilios.add(lblTituloPrimerosAux);

		JLabel lblBuscarPrimerosAux = new JLabel("Buscar Guía :");
		lblBuscarPrimerosAux.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblBuscarPrimerosAux.setBounds(20, 51, 100, 25);
		panelGuiasPAuxilios.add(lblBuscarPrimerosAux);

		tfProtocolosPrimAux = new JTextField();
		tfProtocolosPrimAux.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tfProtocolosPrimAux.setColumns(10);
		tfProtocolosPrimAux.setBounds(120, 51, 300, 25);
		panelGuiasPAuxilios.add(tfProtocolosPrimAux);

		JComboBox cbFiltroCategPrimAux = new JComboBox();
		cbFiltroCategPrimAux.setModel(new DefaultComboBoxModel(new String[] { "Trauma", "RCP", "Quemaduras" }));
		cbFiltroCategPrimAux.setToolTipText("");
		cbFiltroCategPrimAux.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroCategPrimAux.setBounds(450, 51, 120, 25);
		panelGuiasPAuxilios.add(cbFiltroCategPrimAux);

		JScrollPane spPrimerosAux = new JScrollPane();
		spPrimerosAux.setBounds(20, 91, 250, 500);
		panelGuiasPAuxilios.add(spPrimerosAux);

		JList listProtocolosPrimAux = new JList();
		listProtocolosPrimAux.setFont(new Font("SansSerif", Font.PLAIN, 12));
		listProtocolosPrimAux.setModel(new AbstractListModel() {
			String[] values = new String[] { "Protocolo de Quemaduras", "Manejo de Fracturas" };

			public int getSize() {
				return values.length;
			}

			public Object getElementAt(int index) {
				return values[index];
			}
		});
		spPrimerosAux.setViewportView(listProtocolosPrimAux);

		JPanel panelVisualGuiaPrimAux = new JPanel();
		panelVisualGuiaPrimAux.setBounds(280, 91, 511, 500);
		panelGuiasPAuxilios.add(panelVisualGuiaPrimAux);
		panelVisualGuiaPrimAux.setLayout(null);

		JLabel lblTituloGuiaPrimAux = new JLabel("Título del Documento\r\n");
		lblTituloGuiaPrimAux.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloGuiaPrimAux.setBounds(10, 11, 491, 30);
		panelVisualGuiaPrimAux.add(lblTituloGuiaPrimAux);

		JScrollPane spVisualGuiasPrimAux = new JScrollPane();
		spVisualGuiasPrimAux.setBounds(10, 50, 491, 439);
		panelVisualGuiaPrimAux.add(spVisualGuiasPrimAux);

		JEditorPane epVisualGuiasPrimAux = new JEditorPane();
		spVisualGuiasPrimAux.setViewportView(epVisualGuiasPrimAux);

		JPanel panelReportes = new JPanel();
		panelContenido.add(panelReportes, "Reportes");
		panelReportes.setLayout(null);

		JLabel lblTituloReportes = new JLabel("Biblioteca de Guías y Protocolos de  Auxilios");
		lblTituloReportes.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloReportes.setBounds(20, 11, 400, 25);
		panelReportes.add(lblTituloReportes);

		JPanel panelPeriodoTipoReportes = new JPanel();
		panelPeriodoTipoReportes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		panelPeriodoTipoReportes.setBorder(
				new TitledBorder(null, "Definir Periodo y Tipo", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelPeriodoTipoReportes.setBounds(20, 50, 370, 200);
		panelReportes.add(panelPeriodoTipoReportes);
		panelPeriodoTipoReportes.setLayout(null);

		JLabel lblTipoReporte = new JLabel("Tipo de Reporte :");
		lblTipoReporte.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblTipoReporte.setBounds(30, 21, 100, 25);
		panelPeriodoTipoReportes.add(lblTipoReporte);

		JComboBox cbTipoReporte = new JComboBox();
		cbTipoReporte.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbTipoReporte.setModel(new DefaultComboBoxModel(new String[] { "Incidentes", "Inventario", "Brigadistas" }));
		cbTipoReporte.setBounds(140, 21, 220, 25);
		panelPeriodoTipoReportes.add(cbTipoReporte);

		JLabel lblReporteFechaInicio = new JLabel("Fecha Inicio :");
		lblReporteFechaInicio.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblReporteFechaInicio.setBounds(30, 61, 100, 25);
		panelPeriodoTipoReportes.add(lblReporteFechaInicio);

		JLabel lblReporteFechaFin = new JLabel("Fecha Fin :");
		lblReporteFechaFin.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblReporteFechaFin.setBounds(30, 91, 100, 25);
		panelPeriodoTipoReportes.add(lblReporteFechaFin);

		// ************************************************************
		// [CÓDIGO MANTENIDO] JFormattedTextFields con máscara para fechas
		// ************************************************************
		try {
			MaskFormatter mask = new MaskFormatter("##/##/####"); // DD/MM/AAAA
			mask.setPlaceholderCharacter('_');

			tfFechaInicioReporte = new JFormattedTextField(mask);
			tfFechaInicioReporte.setBounds(140, 61, 220, 25);
			panelPeriodoTipoReportes.add(tfFechaInicioReporte);

			tfFechaFinReporte = new JFormattedTextField(mask);
			tfFechaFinReporte.setBounds(140, 91, 220, 25);
			panelPeriodoTipoReportes.add(tfFechaFinReporte);

		} catch (java.text.ParseException e) {
			// FALLBACK CORREGIDO: Usamos JFormattedTextField() simple, manteniendo el tipo
			// de dato.
			tfFechaInicioReporte = new JFormattedTextField();
			tfFechaInicioReporte.setBounds(140, 61, 220, 25);
			panelPeriodoTipoReportes.add(tfFechaInicioReporte);

			tfFechaFinReporte = new JFormattedTextField();
			tfFechaFinReporte.setBounds(140, 91, 220, 25);
			panelPeriodoTipoReportes.add(tfFechaFinReporte);

			System.err.println("Error al aplicar la máscara de fecha. Usando JFormattedTextFields planos.");
		}

		JButton btnGenerarInfReporte = new JButton("Generar Informe");
		btnGenerarInfReporte.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGenerarInfReporte.setBounds(20, 261, 370, 40);
		panelReportes.add(btnGenerarInfReporte);

		JPanel panelResultPreviReporte = new JPanel();
		panelResultPreviReporte.setBorder(new TitledBorder(null, "Resultados / Previsualizaci\u00F3n",
				TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelResultPreviReporte.setBounds(410, 50, 381, 460);
		panelReportes.add(panelResultPreviReporte);
		panelResultPreviReporte.setLayout(null);

		JScrollPane spResultPreviReportes = new JScrollPane();
		spResultPreviReportes.setBounds(10, 30, 361, 419);
		panelResultPreviReporte.add(spResultPreviReportes);

		tableReportesResultPrevi = new JTable();
		tableReportesResultPrevi.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Nombre del Reporte", "Fecha de Creaci\u00F3n", "Tipo", "Usuario" }));
		tableReportesResultPrevi.getColumnModel().getColumn(0).setPreferredWidth(110);
		tableReportesResultPrevi.getColumnModel().getColumn(1).setPreferredWidth(105);
		spResultPreviReportes.setViewportView(tableReportesResultPrevi);

		JButton btnDescargaReporte = new JButton("Descargar CSV");
		btnDescargaReporte.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnDescargaReporte.setBounds(410, 520, 179, 40);
		panelReportes.add(btnDescargaReporte);

		JButton btnVerDetallesReporte = new JButton("Ver Detalles");
		btnVerDetallesReporte.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnVerDetallesReporte.setBounds(610, 520, 181, 40);
		panelReportes.add(btnVerDetallesReporte);

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
		panelFooter.setLayout(null);

		lblEstadoFooter = new JLabel("Inicializando Sistema..."); // Usamos la variable declarada
		lblEstadoFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEstadoFooter.setBounds(10, 11, 500, 18);
		panelFooter.add(lblEstadoFooter);

		lblVersionFooter = new JLabel("Gestión de Emergencias v1.0.1 © 2025"); // Usamos la variable declarada
		lblVersionFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblVersionFooter.setBounds(750, 11, 240, 18);
		panelFooter.add(lblVersionFooter);

		// ************************************************************
		// [FUNCIONALIDAD MANTENIDA] Timer para simular sincronización en el Footer
		// ************************************************************
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		// El Timer se ejecutará cada 60,000 milisegundos (1 minuto)
		Timer timer = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Aquí se llamaría al método del Controlador para obtener la hora real
				String horaActual = timeFormat.format(new Date());
				// En la Vista, solo actualizamos el JLabel:
				lblEstadoFooter.setText("✅ Sistema Listo | Última sincronización: " + horaActual);

				// NOTA: Para actualizar 'Datos Cargados: X', se necesitaría un método público
				// en esta Vista
				// que el Controlador llamaría. Ejemplo: actualizarContadorIncidentes(int
				// count);
			}
		});

		// Ejecutar inmediatamente al inicio
		lblEstadoFooter.setText("✅ Sistema Listo | Última sincronización: " + timeFormat.format(new Date()));
		timer.start();

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
		panelIncidentesPrioridad
				.setBorder(new TitledBorder(
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"Incidentes por Prioridad", TitledBorder.LEFT, TitledBorder.TOP, null,
								new Color(0, 0, 0)),
						"Incidentes por Prioridad", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelIncidentesPrioridad.setBounds(20, 190, 450, 200);
		panelDashboard.add(panelIncidentesPrioridad);

		JPanel panelBrigadistasEspecialidad = new JPanel();
		panelBrigadistasEspecialidad.setBorder(new TitledBorder(null, "Brigadistas por Especialidad", TitledBorder.LEFT,
				TitledBorder.TOP, null, null));
		panelBrigadistasEspecialidad.setBounds(480, 190, 310, 200);
		panelDashboard.add(panelBrigadistasEspecialidad);

	}
}