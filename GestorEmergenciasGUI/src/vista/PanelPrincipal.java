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
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
import servicio.IncidenteService;
import controlador.CrearIncidenteController;
import controlador.IncidenteController;
import controlador.SuministroController;
import modelo.Suministro;
import servicio.SuministroService;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.FileWriter;
import java.awt.Desktop;
import java.util.List;
//Paquete de UI/Dialogs si creaste un diálogo para crear incidentes

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

	// Cambiamos JTextField por JFormattedTextField para las fechas (Ver Constructor)
	private JFormattedTextField tfFechaInicioReporte;
	private JFormattedTextField tfFechaFinReporte;

	private JTable tableReportesResultPrevi;

	// Declaramos los JLabels del Footer para poder actualizarlos
	private JLabel lblEstadoFooter;
	private JLabel lblVersionFooter;

	// BOTONES (declarados a nivel de clase para poder manipularlos según rol)
	private JButton btnCrearIncidente;
	private JButton btnVerDetallesInciden;
	private JButton btnMarcaResueltoIncide;

	private JButton btnAñadirSuministro;
	private JButton btnReporteSuministro;
	private JButton btnEditStockSuminis;
	private JButton btnMarcarCriticSuminis;

	private JButton btnAddBrigadista;
	private JButton btnGuardarCamEstadoBrig;
	private JComboBox cbEditarEstadoBrig; 
	private JPanel panelDetallesBrigadista;
	private IncidenteService incidenteService;
	private JComboBox<String> cbFiltroPrioridad;

	private IncidenteController incidenteController;
	private SuministroController suministroController;
	// si quieres ocultar también la caja (no requerida ahora)

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// arranca como Coordinador para pruebas
					PanelPrincipal frame = new PanelPrincipal("Coordinador");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor por defecto -> Coordinador (útil para pruebas)
	 */
	public PanelPrincipal() {
		this("Coordinador");
	}

	/**
	 * Create the frame.
	 */
	public PanelPrincipal(String rol) {

		// 1. CONFIGURACIÓN DE LA VENTANA
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		this.setSize(1000, 700);
		this.setLocationRelativeTo(null);
		this.setResizable(false); // <--- Bloquea la redimensión

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
		final int LOGO_WIDTH = 50;
		final int LOGO_HEIGHT = 37;

		lblLogo.setBounds(6, 2, LOGO_WIDTH, LOGO_HEIGHT);
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

		// ------------------ PANEL INCIDENTES -----------------------
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
		
		// Inicializar servicio
		incidenteService = new IncidenteService();
		this.incidenteController = new IncidenteController(incidenteService);
		
		cbFiltroPrioridad = new JComboBox<>();
		cbFiltroPrioridad.setModel(new DefaultComboBoxModel(new String[] {"Prioridad", "Prioridad Alta", "Prioridad Media", "Prioridad Baja"}));
		cbFiltroPrioridad.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroPrioridad.setBounds(291, 50, 120, 25);
		panelIncidentes.add(cbFiltroPrioridad);

		// Actualizar tabla al cambiar filtro
				cbFiltroPrioridad.addActionListener(e -> actualizarTabla());
				tfBuscarIncidentes.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
				    @Override
				    public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla(); }
				    @Override
				    public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla(); }
				    @Override
				    public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla(); }
				});
				
		btnCrearIncidente = new JButton("Crear Incidente");
		btnCrearIncidente.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnCrearIncidente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// implementación futura
				
			}
		});
		btnCrearIncidente.setBounds(670, 50, 121, 25);
		panelIncidentes.add(btnCrearIncidente);
		
		CrearIncidenteController crearController = new CrearIncidenteController(incidenteService);

		// Listener del botón
		btnCrearIncidente.addActionListener(e -> {
		    crearController.mostrarDialogoCrearIncidente(
		        SwingUtilities.getWindowAncestor(this),
		        this::actualizarTabla // callback para refrescar tabla
		    );
		});


		JScrollPane spIncidentes = new JScrollPane();
		spIncidentes.setBounds(20, 90, 771, 480);
		panelIncidentes.add(spIncidentes);

		tableIncidentes = new JTable();
		tableIncidentes.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Tipo", "Prioridad", "Ubicación", "Tiempo Activo", "Brigadista Asignado" }));
		tableIncidentes.getColumnModel().getColumn(4).setPreferredWidth(87);
		tableIncidentes.getColumnModel().getColumn(5).setPreferredWidth(117);
		tableIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		spIncidentes.setViewportView(tableIncidentes);

		btnVerDetallesInciden = new JButton("Ver Detalles");
		btnVerDetallesInciden.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnVerDetallesInciden.setBounds(20, 581, 150, 28);
		panelIncidentes.add(btnVerDetallesInciden);

		btnVerDetallesInciden.addActionListener(e -> {
		    incidenteController.mostrarDetallesIncidente(this, tableIncidentes);
		});
		
		btnMarcaResueltoIncide = new JButton("Marcar como Resuelto");
		btnMarcaResueltoIncide.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnMarcaResueltoIncide.setBounds(180, 581, 180, 28);
		panelIncidentes.add(btnMarcaResueltoIncide);
		
		btnMarcaResueltoIncide.addActionListener(e -> {
		    incidenteController.marcarIncidenteResuelto(this, tableIncidentes);
		    actualizarTabla(); // para reflejar cambios en la tabla
		});
		// ------------------ PANEL INVENTARIO -----------------------
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
				new DefaultComboBoxModel(new String[] {"Almacén", "Almacén Principal", "Almacén A", "Almacén B", "Almacén C"}));
		cbFiltroUbi.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroUbi.setBounds(355, 53, 127, 25);
		panelInventario.add(cbFiltroUbi);

		// Buscador por nombre
		tfBuscarNombreSuminis.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
		    private void update() {
		        String texto = tfBuscarNombreSuminis.getText();
		        String ubicacion = (String) cbFiltroUbi.getSelectedItem();
		        suministroController.actualizarTablaFiltrada(tableSuministros, texto, ubicacion);
		    }
		    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
		    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
		    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
		});
		
		// Filtro por ubicación
		cbFiltroUbi.addActionListener(e -> {
		    String texto = tfBuscarNombreSuminis.getText();
		    String ubicacion = (String) cbFiltroUbi.getSelectedItem();
		    suministroController.actualizarTablaFiltrada(tableSuministros, texto, ubicacion);
		});
		btnAñadirSuministro = new JButton("Añadir Stock");
		btnAñadirSuministro.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAñadirSuministro.setBounds(520, 53, 120, 25);
		panelInventario.add(btnAñadirSuministro);
		
		suministroController = new SuministroController(new SuministroService());
		
		btnAñadirSuministro.addActionListener(e -> {
		    new CrearSuministroDialog(this, suministroController, tableSuministros).setVisible(true);
		});
		
		btnReporteSuministro = new JButton("Generar Reporte");
		btnReporteSuministro.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnReporteSuministro.setBounds(650, 53, 141, 25);
		panelInventario.add(btnReporteSuministro);
		
		btnReporteSuministro.addActionListener(e -> {
		    List<Suministro> suministros = suministroController.getAllSuministros();
		    ReporteService.generarYAbrirReporte(suministros, this);
		});

		JScrollPane spSuministros = new JScrollPane();
		spSuministros.setBounds(20, 89, 771, 480);
		panelInventario.add(spSuministros);

		tableSuministros = new JTable();
		tableSuministros
				.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Suministro", "Stock Actual",
						"Unidad (Kg/Uni)", "Mínimo Crítico", "Ubicación", "Fecha de Caducidad" }));
		tableSuministros.getColumnModel().getColumn(3).setPreferredWidth(90);
		tableSuministros.getColumnModel().getColumn(6).setPreferredWidth(116);
		spSuministros.setViewportView(tableSuministros);

		btnEditStockSuminis = new JButton("Editar Stock");
		btnEditStockSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnEditStockSuminis.setBounds(20, 580, 150, 29);
		panelInventario.add(btnEditStockSuminis);
		
		btnEditStockSuminis.addActionListener(e -> abrirEditarSuministro());

		btnMarcarCriticSuminis = new JButton("Marcar Crítico");
		btnMarcarCriticSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnMarcarCriticSuminis.setBounds(180, 580, 150, 29);
		panelInventario.add(btnMarcarCriticSuminis);

		btnMarcarCriticSuminis.addActionListener(e -> marcarSuministroCritico());
		// ------------------ PANEL BRIGADISTAS -----------------------
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

		btnAddBrigadista = new JButton("Añadir Brigadista");
		btnAddBrigadista.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAddBrigadista.setBounds(651, 51, 140, 25);
		panelBrigadistas.add(btnAddBrigadista);

		JScrollPane spBrigadistas = new JScrollPane();
		spBrigadistas.setBounds(20, 90, 455, 500);
		panelBrigadistas.add(spBrigadistas);

		tableBrigadistas = new JTable();
		tableBrigadistas.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Nombre", "Estado", "Especialidad", "Teléfono" }));
		tableBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		spBrigadistas.setViewportView(tableBrigadistas);

		panelDetallesBrigadista = new JPanel();
		panelDetallesBrigadista.setBounds(490, 90, 290, 500);
		panelBrigadistas.add(panelDetallesBrigadista);
		panelDetallesBrigadista.setLayout(null);
		panelDetallesBrigadista.setBorder(new LineBorder(new Color(190, 190, 190), 1));

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

		cbEditarEstadoBrig = new JComboBox();
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

		btnGuardarCamEstadoBrig = new JButton("Guardar Cambios de Estado");
		btnGuardarCamEstadoBrig.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuardarCamEstadoBrig.setBounds(10, 451, 270, 38);
		panelDetallesBrigadista.add(btnGuardarCamEstadoBrig);

		// ------------------ PANEL GUIAS -----------------------
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

		// ------------------ PANEL REPORTES -----------------------
		JPanel panelReportes = new JPanel();
		panelContenido.add(panelReportes, "Reportes");
		panelReportes.setLayout(null);

		JLabel lblTituloReportes = new JLabel("Reportes");
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
		// JFormattedTextFields con máscara para fechas
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
		panelResultPreviReporte.setBorder(new TitledBorder(null, "Resultados / Previsualización",
				TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panelResultPreviReporte.setBounds(410, 50, 381, 460);
		panelReportes.add(panelResultPreviReporte);
		panelResultPreviReporte.setLayout(null);

		JScrollPane spResultPreviReportes = new JScrollPane();
		spResultPreviReportes.setBounds(10, 30, 361, 419);
		panelResultPreviReporte.add(spResultPreviReportes);

		tableReportesResultPrevi = new JTable();
		tableReportesResultPrevi.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Nombre del Reporte", "Fecha de Creación", "Tipo", "Usuario" }));
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

		cardLayout.show(panelContenido, "Dashboard");

		JButton btnIncidentesActivos = new JButton("Incidentes Activos");
		btnIncidentesActivos.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnIncidentesActivos.addActionListener(e -> cardLayout.show(panelContenido, "Incidentes"));
		btnIncidentesActivos.setBounds(10, 11, 180, 40);
		panelBotones.add(btnIncidentesActivos);

		JButton btnInventarioSuministros = new JButton("Inventario Suministros");
		btnInventarioSuministros.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnInventarioSuministros.setBounds(10, 62, 180, 40);
		btnInventarioSuministros.addActionListener(e -> cardLayout.show(panelContenido, "Inventario"));
		panelBotones.add(btnInventarioSuministros);

		JButton btnBrigadistas = new JButton("Brigadistas");
		btnBrigadistas.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnBrigadistas.setBounds(10, 113, 180, 40);
		btnBrigadistas.addActionListener(e -> cardLayout.show(panelContenido, "Brigadistas"));
		panelBotones.add(btnBrigadistas);

		JButton btnGuiasPAuxilios = new JButton("Guias Primeros Auxilios");
		btnGuiasPAuxilios.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuiasPAuxilios.setBounds(10, 164, 180, 40);
		btnGuiasPAuxilios.addActionListener(e -> cardLayout.show(panelContenido, "GuiasPAuxilios"));
		panelBotones.add(btnGuiasPAuxilios);

		JButton btnReportes = new JButton("Reportes");
		btnReportes.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnReportes.setBounds(10, 215, 180, 40);
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
		// Timer para simular sincronización en el Footer
		// ************************************************************
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		// El Timer se ejecutará cada 60,000 milisegundos (1 minuto)
		Timer timer = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String horaActual = timeFormat.format(new Date());
				lblEstadoFooter.setText("✅ Sistema Listo | Última sincronización: " + horaActual);
			}
		});

		// Ejecutar inmediatamente al inicio
		lblEstadoFooter.setText("✅ Sistema Listo | Última sincronización: " + timeFormat.format(new Date()));
		timer.start();

		// ========================= APLICAR REGLAS POR ROL ==========================
		// Rol viene de VentanaRoles: "Coordinador", "Brigadista", "Usuario"
		applyRoleRules(rol, panelIncidentes, panelInventario, panelBrigadistas);

		// fin constructor
	}

	/**
	 * Aplica las reglas de visibilidad/eliminación por rol.  
	 * - Elimina físicamente (panel.remove) los botones que pediste quitar.
	 * - No cambia posición ni estilo de los elementos restantes.
	 */
	private void applyRoleRules(String rol, JPanel panelIncidentes, JPanel panelInventario, JPanel panelBrigadistas) {
		rol = (rol == null) ? "Coordinador" : rol;

		// COORDINADOR -> todo activo (no hacemos nada)
		if (rol.equalsIgnoreCase("Coordinador")) {
			// nada que cambiar
			return;
		}

		// BRIGADISTA
		if (rol.equalsIgnoreCase("Brigadista")) {
			// Panel Brigadistas: NO puede añadir brigadistas, NO puede cambiar estado.
			if (btnAddBrigadista != null) {
				panelBrigadistas.remove(btnAddBrigadista);
			}
			if (btnGuardarCamEstadoBrig != null) {
				panelBrigadistas.remove(btnGuardarCamEstadoBrig);
			}
			 if (rol.equalsIgnoreCase("Brigadista")) {
			        // Elimina el panelDetallesBrigadista
			        if (panelDetallesBrigadista != null) {
			            panelBrigadistas.remove(panelDetallesBrigadista);
			        }
			    }
			
			// opcional: ocultar también el combo de edición de estado (si prefieres)
			// pero pediste eliminar botones, así que dejo el combo (solo visual)
			// INVENTARIO: tiene acceso total -> no removemos nada
			// INCIDENTES: brigadista PUEDE marcar como resuelto y ver detalles -> no removemos nada
		}

		// USUARIO / RESIDENTE
		if (rol.equalsIgnoreCase("Usuario") || rol.equalsIgnoreCase("Residente")) {
			// INCIDENTES: NO puede marcar como resuelto -> eliminar el botón
			if (btnMarcaResueltoIncide != null) {
				panelIncidentes.remove(btnMarcaResueltoIncide);
			}
			// INVENTARIO: NO puede añadir, editar, ni marcar crítico -> eliminar esos botones
			if (btnAñadirSuministro != null) {
				panelInventario.remove(btnAñadirSuministro);
			}
			if (btnEditStockSuminis != null) {
				panelInventario.remove(btnEditStockSuminis);
			}
			if (btnMarcarCriticSuminis != null) {
				panelInventario.remove(btnMarcarCriticSuminis);
			}
			// BRIGADISTAS: NO puede añadir ni cambiar estado -> eliminar botones
			if (btnAddBrigadista != null) {
				panelBrigadistas.remove(btnAddBrigadista);
			}
			if (btnGuardarCamEstadoBrig != null) {
				panelBrigadistas.remove(btnGuardarCamEstadoBrig);
			}
			if (panelDetallesBrigadista != null) {
	            panelBrigadistas.remove(panelDetallesBrigadista);
	        }
			 if (rol.equalsIgnoreCase("Usuario") || rol.equalsIgnoreCase("Residente")) {
			        // Elimina el panelDetallesBrigadista
			        if (panelDetallesBrigadista != null) {
			            panelBrigadistas.remove(panelDetallesBrigadista);
			        }
			    }
		}

		// después de quitar componentes actualizamos la UI
		this.revalidate();
		this.repaint();
	}
	private void actualizarTabla() {
	    String busqueda = tfBuscarIncidentes.getText();
	    String filtro = (String) cbFiltroPrioridad.getSelectedItem();
	    incidenteController.actualizarTabla(tableIncidentes, busqueda, filtro);
	}
	private void abrirEditarSuministro() {
	    int fila = tableSuministros.getSelectedRow();
	    if (fila >= 0) {
	        int id = (int) tableSuministros.getValueAt(fila, 0);
	        Suministro s = suministroController.getAllSuministros().stream()
	                .filter(su -> su.getId() == id)
	                .findFirst()
	                .orElse(null);
	        if (s != null) {
	            new EditarSuministroDialog(this, suministroController, tableSuministros, s).setVisible(true);
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "Seleccione un suministro para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
	    }
	}
	private void aplicarColorCritico() {
	    tableSuministros.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value,
	                                                       boolean isSelected, boolean hasFocus,
	                                                       int row, int column) {
	            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	            int stock = (int) table.getValueAt(row, 2);        // columna 2 = Stock Actual
	            int minCritico = (int) table.getValueAt(row, 4);   // columna 4 = Mínimo Crítico

	            if (stock <= minCritico) {
	                c.setBackground(Color.ORANGE); // crítico = naranja
	            } else {
	                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
	            }

	            return c;
	        }
	    });
	}

	// Lógica para marcar un suministro como crítico
	private void marcarSuministroCritico() {
	    int fila = tableSuministros.getSelectedRow();
	    if (fila >= 0) {
	        int id = (int) tableSuministros.getValueAt(fila, 0);
	        Suministro s = suministroController.getAllSuministros().stream()
	                .filter(su -> su.getId() == id)
	                .findFirst().orElse(null);

	        if (s != null) {
	            // Forzamos a que sea crítico ajustando stock si quieres
	            s.setStockActual(s.getMinimoCritico());

	            // Guardar cambios
	            suministroController.actualizarTabla(tableSuministros);

	            // Aplicar color a toda la tabla
	            aplicarColorCritico();

	            JOptionPane.showMessageDialog(this, "Suministro marcado como crítico.", 
	                                          "Éxito", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "Seleccione un suministro para marcar como crítico.", 
	                                      "Aviso", JOptionPane.WARNING_MESSAGE);
	    }
	}
	public class ReporteService {

	    public static void generarYAbrirReporte(List<Suministro> suministros, java.awt.Component parent) {
	        String nombreArchivo = "reporte_suministros.csv";

	        try (FileWriter fw = new FileWriter(nombreArchivo)) {
	            fw.write("ID,Suministro,Stock Actual,Unidad,Mínimo Crítico,Ubicación,Fecha Caducidad,Critico\n");
	            for (Suministro s : suministros) {
	                fw.write(s.getId() + "," +
	                         s.getNombre() + "," +
	                         s.getStockActual() + "," +
	                         s.getUnidad() + "," +
	                         s.getMinimoCritico() + "," +
	                         s.getUbicacion() + "," +
	                         s.getFechaCaducidad() + "," +
	                         (s.getStockActual() <= s.getMinimoCritico() ? "CRÍTICO" : "") + "\n");
	            }
	            fw.flush();

	            JOptionPane.showMessageDialog(parent, "Reporte generado exitosamente: " + nombreArchivo);

	            // Abrir el archivo automáticamente
	            if (Desktop.isDesktopSupported()) {
	                Desktop.getDesktop().open(new java.io.File(nombreArchivo));
	            }

	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(parent, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
	        }
	    }
	}
	
}
