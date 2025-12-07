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
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;

// LIBRERÍAS AÑADIDAS PARA LAS FUNCIONALIDADES:
import java.text.SimpleDateFormat;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import javax.swing.border.LineBorder;
import java.net.URL; // Necesario para cargar recursos
import javax.swing.ImageIcon; // Necesario para el logo
import java.awt.Image; // Necesario para escalar el logo

import servicio.BrigadistaService;
import servicio.GuiaService;
import servicio.IncidenteService;
import controlador.BrigadistaController;
import controlador.CrearIncidenteController;
import controlador.IncidenteController;
import controlador.SuministroController;
import modelo.Brigadista;
import modelo.Incidente;
import modelo.Suministro;
import servicio.SuministroService;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.awt.Desktop;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.UIManager;
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
	private JButton btnEliminarBrigadista;
	private JComboBox cbEditarEstadoBrig; 
	private JPanel panelDetallesBrigadista;
	private IncidenteService incidenteService;
	private JComboBox<String> cbFiltroPrioridad;

	private IncidenteController incidenteController;
	private SuministroController suministroController;
	private BrigadistaController brigadistaController;
	private JTextField tfNombreEditPanel;
	private JTextField tfEspecialidadEditPanel;
	private JTextField tfIdeditpanel;
	private JTextField tfTelefonoEditPanel;
	private JLabel lblEstadoActualPanel;
	private JTextField tfEstadoActualPanel;
	private JButton btnAsignarBrigadista;
	private JButton btnDesasignarBrigadista;
	private JButton btnAddGuiaPAuxili;
	private JPanel panelGuiasPAuxilios;
	private JButton btnQuitarGuiaPAuxili;

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
		panelBarra.setBackground(new Color(204, 204, 204));
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
		btnMinimizar.setBackground(new Color(255, 255, 255));
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
		btnCerrar.setBackground(new Color(255, 255, 255));
		btnCerrar.setBounds(937, 2, 53, 37);
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panelBarra.add(btnCerrar);

		JLabel lblTitulo = new JLabel("Selfsecurity\r\n");
		lblTitulo.setForeground(new Color(0, 0, 0));
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
		panelBotones.setBackground(new Color(204, 204, 204));
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
		// ============================
		// PANEL DE INCIDENTES
		// ============================
		JPanel panelIncidentes = new JPanel();
		panelContenido.add(panelIncidentes, "Incidentes");
		panelIncidentes.setLayout(null);

		// ----------------------------
		// TÍTULO
		// ----------------------------
		JLabel lblTituloIncidentesActCurso = new JLabel("Incidentes Activos en Curso");
		lblTituloIncidentesActCurso.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloIncidentesActCurso.setBounds(20, 11, 301, 25);
		panelIncidentes.add(lblTituloIncidentesActCurso);

		// ----------------------------
		// BUSCADOR Y FILTRO
		// ----------------------------
		JLabel lblFiltroIncidentes = new JLabel("Filtrar :");
		lblFiltroIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblFiltroIncidentes.setBounds(20, 50, 51, 25);
		panelIncidentes.add(lblFiltroIncidentes);

		tfBuscarIncidentes = new JTextField();
		tfBuscarIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tfBuscarIncidentes.setBounds(75, 50, 198, 25);
		tfBuscarIncidentes.setColumns(10);
		panelIncidentes.add(tfBuscarIncidentes);

		cbFiltroPrioridad = new JComboBox<>();
		cbFiltroPrioridad.setModel(new DefaultComboBoxModel<>(new String[] {
		        "Prioridad", "Prioridad Alta", "Prioridad Media", "Prioridad Baja"
		}));
		cbFiltroPrioridad.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroPrioridad.setBounds(291, 50, 120, 25);
		panelIncidentes.add(cbFiltroPrioridad);

		// ----------------------------
		// TABLA DE INCIDENTES
		// ----------------------------
		tableIncidentes = new JTable();
		tableIncidentes.setModel(new DefaultTableModel(
		        new Object[][] {},
		        new String[] { "ID", "Tipo", "Prioridad", "Ubicación", "Tiempo Activo", "Brigadista Asignado" }
		));
		tableIncidentes.getColumnModel().getColumn(4).setPreferredWidth(87);
		tableIncidentes.getColumnModel().getColumn(5).setPreferredWidth(117);
		tableIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 12));

		JScrollPane spIncidentes = new JScrollPane(tableIncidentes);
		spIncidentes.setBounds(20, 90, 771, 480);
		panelIncidentes.add(spIncidentes);

		// ----------------------------
		// BOTONES
		// ----------------------------
		btnCrearIncidente = new JButton("Crear Incidente");
		btnCrearIncidente.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnCrearIncidente.setBounds(670, 50, 121, 25);
		panelIncidentes.add(btnCrearIncidente);

		btnVerDetallesInciden = new JButton("Ver Detalles");
		btnVerDetallesInciden.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnVerDetallesInciden.setBounds(20, 581, 150, 28);
		panelIncidentes.add(btnVerDetallesInciden);

		btnMarcaResueltoIncide = new JButton("Marcar como Resuelto");
		btnMarcaResueltoIncide.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnMarcaResueltoIncide.setBounds(180, 581, 180, 28);
		panelIncidentes.add(btnMarcaResueltoIncide);
		


		// ----------------------------
		// INICIALIZACIÓN DE SERVICIOS Y CONTROLADORE
		// ----------------------------
		// Inicialización
		BrigadistaService brigadistaService = new BrigadistaService();
		IncidenteService incidenteService = new IncidenteService(brigadistaService);

		// ASIGNACIÓN AL ATRIBUTO, NO A UNA VARIABLE LOCAL
		this.incidenteController = new IncidenteController(incidenteService, brigadistaService);

		// Cargar datos iniciales en la tabla (al iniciar la app)
		incidenteController.actualizarTabla(tableIncidentes, "", "");

		// ----------------------------
		// LISTENERS Y ACCIONES
		// ----------------------------

		// Filtro de prioridad
		cbFiltroPrioridad.addActionListener(e -> actualizarTabla());

		// Búsqueda dinámica
		tfBuscarIncidentes.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
		    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla(); }
		    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla(); }
		    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarTabla(); }
		});

		// Botón Crear Incidente
		CrearIncidenteController crearController = new CrearIncidenteController(incidenteService);
		btnCrearIncidente.addActionListener(e -> {
		    crearController.mostrarDialogoCrearIncidente(
		        SwingUtilities.getWindowAncestor(this),
		        this::actualizarTabla // callback para refrescar tabla después de crear
		    );
		});

		// Botón Ver Detalles
		btnVerDetallesInciden.addActionListener(e -> {
		    incidenteController.mostrarDetallesIncidente(this, tableIncidentes);
		});
		
		// boton marcar como resuelto

		btnMarcaResueltoIncide.addActionListener(e -> {
		    incidenteController.marcarIncidenteResuelto(this, tableIncidentes);
		    actualizarTabla(); // refrescar tabla
		});
		
		// ------------------ PANEL INVENTARIO -----------------------
		JPanel panelInventario = new JPanel();
		panelContenido.add(panelInventario, "Inventario");
		panelInventario.setLayout(null);

		// Título
		JLabel lblTituloInventario = new JLabel("Gestión de Inventario y Suministros");
		lblTituloInventario.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloInventario.setBounds(20, 11, 350, 25);
		panelInventario.add(lblTituloInventario);

		// Buscar
		JLabel lblBuscarInventario = new JLabel("Buscar :");
		lblBuscarInventario.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblBuscarInventario.setBounds(20, 53, 75, 25);
		panelInventario.add(lblBuscarInventario);

		tfBuscarNombreSuminis = new JTextField();
		tfBuscarNombreSuminis.setBounds(95, 53, 250, 25);
		panelInventario.add(tfBuscarNombreSuminis);
		tfBuscarNombreSuminis.setColumns(10);

		// Filtro por ubicación
		JComboBox cbFiltroUbi = new JComboBox();
		cbFiltroUbi.setModel(new DefaultComboBoxModel(new String[] {"Almacén", "Almacén Principal", "Almacén A", "Almacén B", "Almacén C"}));
		cbFiltroUbi.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroUbi.setBounds(355, 53, 127, 25);
		panelInventario.add(cbFiltroUbi);

		// Botones
		btnAñadirSuministro = new JButton("Añadir Stock");
		btnAñadirSuministro.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAñadirSuministro.setBounds(671, 53, 120, 25);
		panelInventario.add(btnAñadirSuministro);

		btnReporteSuministro = new JButton("Generar Reporte");
		btnReporteSuministro.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnReporteSuministro.setBounds(520, 53, 141, 25);
		panelInventario.add(btnReporteSuministro);

		btnEditStockSuminis = new JButton("Editar Stock");
		btnEditStockSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnEditStockSuminis.setBounds(20, 580, 150, 29);
		panelInventario.add(btnEditStockSuminis);

		btnMarcarCriticSuminis = new JButton("Marcar Crítico");
		btnMarcarCriticSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnMarcarCriticSuminis.setBounds(180, 580, 150, 29);
		panelInventario.add(btnMarcarCriticSuminis);

		// Scroll y tabla
		JScrollPane spSuministros = new JScrollPane();
		spSuministros.setBounds(20, 89, 771, 480);
		panelInventario.add(spSuministros);

		tableSuministros = new JTable();
		tableSuministros.setModel(new DefaultTableModel(
		    new Object[][] {},
		    new String[] { "ID", "Suministro", "Stock Actual", "Unidad (Kg/Uni)", "Mínimo Crítico", "Ubicación", "Fecha de Caducidad" }
		));
		tableSuministros.getColumnModel().getColumn(3).setPreferredWidth(90);
		tableSuministros.getColumnModel().getColumn(6).setPreferredWidth(116);
		spSuministros.setViewportView(tableSuministros);

		// Inicializar controlador y servicio
		suministroController = new SuministroController(new SuministroService());

		// Actualizar tabla al iniciar la app
		suministroController.actualizarTabla(tableSuministros);

		// Listeners del buscador y filtros
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

		cbFiltroUbi.addActionListener(e -> {
		    String texto = tfBuscarNombreSuminis.getText();
		    String ubicacion = (String) cbFiltroUbi.getSelectedItem();
		    suministroController.actualizarTablaFiltrada(tableSuministros, texto, ubicacion);
		});

		// Botón Añadir Suministro
		btnAñadirSuministro.addActionListener(e -> {
		    new CrearSuministroDialog(this, suministroController, tableSuministros).setVisible(true);
		});

		btnReporteSuministro.addActionListener(e -> {
		    try {
		        // Obtener los suministros actuales desde el controlador
		        List<Suministro> suministros = suministroController.getAllSuministros();

		        if (suministros.isEmpty()) {
		            JOptionPane.showMessageDialog(this, "No hay suministros para generar el reporte.", "Aviso", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        // Ordenar por Ubicación y luego por Nombre
		        suministros.sort((s1, s2) -> {
		            int cmp = s1.getUbicacion().compareToIgnoreCase(s2.getUbicacion());
		            return cmp != 0 ? cmp : s1.getNombre().compareToIgnoreCase(s2.getNombre());
		        });

		        // Seleccionar archivo CSV a guardar
		        JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setSelectedFile(new java.io.File("ReporteSuministrosActivos.csv"));
		        int seleccion = fileChooser.showSaveDialog(this);

		        if (seleccion == JFileChooser.APPROVE_OPTION) {
		            File archivo = fileChooser.getSelectedFile();

		            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
		                // Encabezado CSV
		                bw.write("ID,Nombre,Stock Actual,Unidad,Mínimo Crítico,Ubicación,Fecha Caducidad,Crítico\n");

		                // Escribir cada suministro
		                for (Suministro s : suministros) {
		                    bw.write(String.format("%d,%s,%d,%s,%d,%s,%s,%s\n",
		                            s.getId(),
		                            s.getNombre(),
		                            s.getStockActual(),
		                            s.getUnidad(),
		                            s.getMinimoCritico(),
		                            s.getUbicacion(),
		                            s.getFechaCaducidad(),
		                            s.isCritico() ? "Sí" : "No"
		                    ));
		                }
		            }

		            JOptionPane.showMessageDialog(this, "Reporte de suministros generado correctamente en: " 
		                                                + archivo.getAbsolutePath());
		        }

		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});


		// Botón Editar Stock
		btnEditStockSuminis.addActionListener(e -> abrirEditarSuministro());

		// Botón Marcar Crítico
		btnMarcarCriticSuminis.addActionListener(e -> marcarSuministroCritico());

		// ------------------ PANEL BRIGADISTAS -----------------------
		JPanel panelBrigadistas = new JPanel();
		panelContenido.add(panelBrigadistas, "Brigadistas");
		panelBrigadistas.setLayout(null);

		// Título
		JLabel lblTituloBrigadista = new JLabel("Gestión de Brigadistas y Personal ");
		lblTituloBrigadista.setFont(new Font("SansSerif", Font.BOLD, 15));
		lblTituloBrigadista.setBounds(20, 11, 350, 25);
		panelBrigadistas.add(lblTituloBrigadista);

		// Buscar
		JLabel lblBuscarBrigadistas = new JLabel("Buscar :");
		lblBuscarBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblBuscarBrigadistas.setBounds(20, 50, 75, 25);
		panelBrigadistas.add(lblBuscarBrigadistas);

		tfBuscarBrigadistas = new JTextField();
		tfBuscarBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tfBuscarBrigadistas.setColumns(10);
		tfBuscarBrigadistas.setBounds(95, 51, 250, 25);
		panelBrigadistas.add(tfBuscarBrigadistas);

		// Filtro por estado
		JComboBox cbFiltroEstadoBrig = new JComboBox();
		cbFiltroEstadoBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbFiltroEstadoBrig.setModel(new DefaultComboBoxModel(new String[] {"Estado", "Libre", "En Servicio", "Descanso"}));
		cbFiltroEstadoBrig.setBounds(355, 51, 120, 25);
		panelBrigadistas.add(cbFiltroEstadoBrig);
		tfBuscarBrigadistas.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
		    private void actualizar() {
		        String texto = tfBuscarBrigadistas.getText().trim().toLowerCase();
		        String estado = cbFiltroEstadoBrig.getSelectedItem().toString();
		        
		        // Filtrar brigadistas
		        List<Brigadista> filtrados = brigadistaController.getAllBrigadistas()
		                .stream()
		                .filter(b -> b.getNombre().toLowerCase().contains(texto))
		                .filter(b -> estado.equals("Estado") || b.getEstado().equals(estado))
		                .toList();
		        
		        // Actualizar tabla con los filtrados
		        brigadistaController.actualizarTablaFiltrada(tableBrigadistas, filtrados);
		    }

		    @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
		    @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
		    @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizar(); }
		});

		// Listener del combo de estados
		cbFiltroEstadoBrig.addActionListener(e -> {
		    String texto = tfBuscarBrigadistas.getText().trim().toLowerCase();
		    String estado = cbFiltroEstadoBrig.getSelectedItem().toString();
		    
		    List<Brigadista> filtrados = brigadistaController.getAllBrigadistas()
		            .stream()
		            .filter(b -> b.getNombre().toLowerCase().contains(texto))
		            .filter(b -> estado.equals("Estado") || b.getEstado().equals(estado))
		            .toList();
		    
		    brigadistaController.actualizarTablaFiltrada(tableBrigadistas, filtrados);
		});

		// Botón añadir brigadista
		btnAddBrigadista = new JButton("Añadir Brigadista");
		btnAddBrigadista.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAddBrigadista.setBounds(651, 51, 140, 25);
		panelBrigadistas.add(btnAddBrigadista);

		// Tabla y scroll
		JScrollPane spBrigadistas = new JScrollPane();
		spBrigadistas.setBounds(20, 90, 455, 500);
		panelBrigadistas.add(spBrigadistas);

		tableBrigadistas = new JTable();
		tableBrigadistas.setModel(new DefaultTableModel(
		    new Object[][] {},
		    new String[] { "ID", "Nombre", "Estado", "Especialidad", "Teléfono" }
		));
		tableBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		spBrigadistas.setViewportView(tableBrigadistas);

		// Inicializar controlador
		brigadistaController = new BrigadistaController();
		brigadistaController.actualizarTabla(tableBrigadistas); // Cargar datos al iniciar

		// Panel de detalles
		panelDetallesBrigadista = new JPanel();
		panelDetallesBrigadista.setBounds(490, 90, 290, 500);
		panelBrigadistas.add(panelDetallesBrigadista);
		panelDetallesBrigadista.setLayout(null);
		panelDetallesBrigadista.setBorder(new LineBorder(new Color(190, 190, 190), 1));

		// Campos y etiquetas
		JLabel lblIdBrigadistas = new JLabel("ID:");
		lblIdBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblIdBrigadistas.setBounds(10, 21, 23, 20);
		panelDetallesBrigadista.add(lblIdBrigadistas);

		tfIdeditpanel = new JTextField();
		tfIdeditpanel.setColumns(10);
		tfIdeditpanel.setBounds(30, 22, 180, 20);
		panelDetallesBrigadista.add(tfIdeditpanel);

		JLabel lblNombreBrigadista = new JLabel("Nombre:");
		lblNombreBrigadista.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblNombreBrigadista.setBounds(10, 51, 60, 20);
		panelDetallesBrigadista.add(lblNombreBrigadista);

		tfNombreEditPanel = new JTextField();
		tfNombreEditPanel.setBounds(63, 52, 217, 20);
		tfNombreEditPanel.setColumns(10);
		panelDetallesBrigadista.add(tfNombreEditPanel);

		JLabel lblEstadoActualBrig = new JLabel("Estado Actual:");
		lblEstadoActualBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEstadoActualBrig.setBounds(10, 91, 82, 27);
		panelDetallesBrigadista.add(lblEstadoActualBrig);

		tfEstadoActualPanel = new JTextField();
		tfEstadoActualPanel.setBounds(89, 95, 71, 20);
		tfEstadoActualPanel.setColumns(10);
		tfEstadoActualPanel.setEditable(false); // Mostrar solo, no editar directamente
		panelDetallesBrigadista.add(tfEstadoActualPanel);

		JLabel lblEditarEstadoBrig = new JLabel("Editar:");
		lblEditarEstadoBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEditarEstadoBrig.setBounds(170, 91, 40, 27);
		panelDetallesBrigadista.add(lblEditarEstadoBrig);

		cbEditarEstadoBrig = new JComboBox();
		cbEditarEstadoBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbEditarEstadoBrig.setModel(new DefaultComboBoxModel(new String[] {"Estado", "Libre", "En Servicio", "Descanso"}));
		cbEditarEstadoBrig.setBounds(170, 115, 110, 27);
		panelDetallesBrigadista.add(cbEditarEstadoBrig);

		JLabel lblEspacialidadBrigadista = new JLabel("Especialidad:");
		lblEspacialidadBrigadista.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEspacialidadBrigadista.setBounds(10, 151, 82, 27);
		panelDetallesBrigadista.add(lblEspacialidadBrigadista);

		tfEspecialidadEditPanel = new JTextField();
		tfEspecialidadEditPanel.setColumns(10);
		tfEspecialidadEditPanel.setBounds(97, 153, 183, 25);
		panelDetallesBrigadista.add(tfEspecialidadEditPanel);

		JLabel lblTelefonoBrigadista = new JLabel("Teléfono:");
		lblTelefonoBrigadista.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblTelefonoBrigadista.setBounds(10, 190, 60, 27);
		panelDetallesBrigadista.add(lblTelefonoBrigadista);

		tfTelefonoEditPanel = new JTextField();
		tfTelefonoEditPanel.setColumns(10);
		tfTelefonoEditPanel.setBounds(63, 192, 183, 25);
		panelDetallesBrigadista.add(tfTelefonoEditPanel);

		// Botones de acción
		JButton btnGuardarCambiosBrig = new JButton("Guardar Cambios");
		btnGuardarCambiosBrig.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuardarCambiosBrig.setBounds(10, 406, 270, 38);
		panelDetallesBrigadista.add(btnGuardarCambiosBrig);

		btnEliminarBrigadista = new JButton("Eliminar");
		btnEliminarBrigadista.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnEliminarBrigadista.setBounds(10, 451, 270, 38);
		panelDetallesBrigadista.add(btnEliminarBrigadista);

		// -------------------- Listeners --------------------

		// Selección de fila en la tabla
		tableBrigadistas.getSelectionModel().addListSelectionListener(e -> {
		    int fila = tableBrigadistas.getSelectedRow();
		    if (fila >= 0) {
		        tfIdeditpanel.setText(tableBrigadistas.getValueAt(fila, 0).toString());
		        tfNombreEditPanel.setText(tableBrigadistas.getValueAt(fila, 1).toString());
		        tfEstadoActualPanel.setText(tableBrigadistas.getValueAt(fila, 2).toString());
		        cbEditarEstadoBrig.setSelectedItem(tableBrigadistas.getValueAt(fila, 2).toString());
		        tfEspecialidadEditPanel.setText(tableBrigadistas.getValueAt(fila, 3).toString());
		        tfTelefonoEditPanel.setText(tableBrigadistas.getValueAt(fila, 4).toString());
		    }
		});

		// Botón Guardar Cambios
		btnGuardarCambiosBrig.addActionListener(e -> {
		    int fila = tableBrigadistas.getSelectedRow();
		    if (fila >= 0) {
		        int id = Integer.parseInt(tfIdeditpanel.getText().trim());
		        Brigadista b = brigadistaController.getAllBrigadistas().stream()
		                .filter(br -> br.getId() == id)
		                .findFirst().orElse(null);

		        if (b != null) {
		            b.setNombre(tfNombreEditPanel.getText().trim());
		            b.setEspecialidad(tfEspecialidadEditPanel.getText().trim());
		            b.setTelefono(tfTelefonoEditPanel.getText().trim());
		            b.setEstado(cbEditarEstadoBrig.getSelectedItem().toString());

		            brigadistaController.actualizarBrigadista(b, tableBrigadistas);
		        }
		    } else {
		        JOptionPane.showMessageDialog(panelBrigadistas, "Seleccione un brigadista para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
		    }
		});

		// Botón Eliminar
		btnEliminarBrigadista.addActionListener(e -> {
		    int fila = tableBrigadistas.getSelectedRow();
		    if (fila >= 0) {
		        int id = Integer.parseInt(tfIdeditpanel.getText().trim());
		        int confirm = JOptionPane.showConfirmDialog(panelBrigadistas,
		                "¿Desea eliminar este brigadista?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
		        if (confirm == JOptionPane.YES_OPTION) {
		            brigadistaController.eliminarBrigadista(id, tableBrigadistas);
		        }
		    } else {
		        JOptionPane.showMessageDialog(panelBrigadistas, "Seleccione un brigadista para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
		    }
		});

		// Botón añadir brigadista - abrir diálogo
		btnAddBrigadista.addActionListener(e -> {
		    // Crear el diálogo y pasarle el controlador y la tabla para que se actualice al añadir
		    new CrearBrigadistaDialog(this, brigadistaController, tableBrigadistas).setVisible(true);
		});
		
		btnAsignarBrigadista = new JButton("Asignar Brigadista");
		btnAsignarBrigadista.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAsignarBrigadista.setBounds(374, 581, 150, 28);
		panelIncidentes.add(btnAsignarBrigadista);
		
		btnDesasignarBrigadista = new JButton("Desasignar Brigadista");
		btnDesasignarBrigadista.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnDesasignarBrigadista.setBounds(534, 581, 168, 28);
		panelIncidentes.add(btnDesasignarBrigadista);
		
	
		// ==========================================
		// PANEL PRINCIPAL – BOTONES DE INCIDENTES
		// ==========================================

		// Cargar datos iniciales en la tabla de incidentes
		incidenteController.actualizarTabla(tableIncidentes, "", "");

		// ========================
		// BOTÓN ASIGNAR BRIGADISTA
		// ========================
		btnAsignarBrigadista.addActionListener(e -> {
		    int filaInc = tableIncidentes.getSelectedRow();
		    if (filaInc < 0) {
		        JOptionPane.showMessageDialog(this, "Seleccione un incidente primero.");
		        return;
		    }

		    int idIncidente = Integer.parseInt(tableIncidentes.getValueAt(filaInc, 0).toString());
		    Incidente incidente = incidenteService.buscarPorId(idIncidente);
		    if (incidente == null) return;

		    // Filtrar brigadistas libres
		    List<Brigadista> libres = brigadistaController.getAllBrigadistas()
		            .stream().filter(b -> b.getEstado().equals("Libre"))
		            .toList();

		    if (libres.isEmpty()) {
		        JOptionPane.showMessageDialog(this, "No hay brigadistas libres disponibles.");
		        return;
		    }

		    // Mostrar diálogo para elegir brigadista
		    String[] nombres = libres.stream().map(Brigadista::getNombre).toArray(String[]::new);
		    String seleccionado = (String) JOptionPane.showInputDialog(
		            this,
		            "Seleccione un brigadista:",
		            "Asignar Brigadista",
		            JOptionPane.PLAIN_MESSAGE,
		            null,
		            nombres,
		            nombres[0]
		    );

		    if (seleccionado == null) return;

		    Brigadista b = libres.stream().filter(x -> x.getNombre().equals(seleccionado)).findFirst().orElse(null);
		    if (b == null) return;

		    // Asignar brigadista al incidente
		    incidente.setIdBrigadista(b.getId());
		    incidente.setNombreBrigadista(b.getNombre());
		    incidenteService.actualizarIncidente(incidente);

		    // Cambiar estado del brigadista
		    b.setEstado("En Servicio");
		    brigadistaController.actualizarBrigadista(b, tableBrigadistas);

		    // Refrescar tabla de incidentes
		    incidenteController.actualizarTabla(tableIncidentes, "", "");
		});

		// =========================
		// BOTÓN DESASIGNAR BRIGADISTA
		// =========================
		btnDesasignarBrigadista.addActionListener(e -> {
		    int filaInc = tableIncidentes.getSelectedRow();
		    if (filaInc < 0) {
		        JOptionPane.showMessageDialog(this, "Seleccione un incidente primero.");
		        return;
		    }

		    int idIncidente = Integer.parseInt(tableIncidentes.getValueAt(filaInc, 0).toString());
		    Incidente incidente = incidenteService.buscarPorId(idIncidente);
		    if (incidente == null || incidente.getIdBrigadista() == 0) {
		        JOptionPane.showMessageDialog(this, "Este incidente no tiene brigadista asignado.");
		        return;
		    }

		    // Obtener brigadista asignado
		    Brigadista b = brigadistaController.getAllBrigadistas()
		            .stream().filter(x -> x.getId() == incidente.getIdBrigadista())
		            .findFirst().orElse(null);

		    if (b != null) {
		        b.setEstado("Libre");
		        brigadistaController.actualizarBrigadista(b, tableBrigadistas);
		    }

		    // Limpiar asignación en el incidente
		    incidente.setIdBrigadista(0);
		    incidente.setNombreBrigadista("");
		    incidenteService.actualizarIncidente(incidente);

		    // Refrescar tabla de incidentes
		    incidenteController.actualizarTabla(tableIncidentes, "", "");

		    JOptionPane.showMessageDialog(this, "Brigadista desasignado correctamente.");
		});


		// ------------------ PANEL GUIAS -----------------------
		panelGuiasPAuxilios = new JPanel();
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

		JScrollPane spPrimerosAux = new JScrollPane();
		spPrimerosAux.setBounds(20, 91, 614, 500);
		panelGuiasPAuxilios.add(spPrimerosAux);
		
		// Servicio para persistencia
		GuiaService guiaService = new GuiaService();

		// Lista maestra que siempre contiene todos los PDFs
		List<String> listaCompletaGuias = new ArrayList<>(guiaService.cargarLista());

		// Modelo dinámico para el JList
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listaCompletaGuias.forEach(listModel::addElement);
		
		// Cargar guías persistentes al iniciar
		guiaService.cargarLista().forEach(listModel::addElement);

		// ---------------------- JList con el modelo dinámico ----------------------
		JList<String> listProtocolosPrimAux = new JList<>(listModel);
		listProtocolosPrimAux.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 12));
		spPrimerosAux.setViewportView(listProtocolosPrimAux);

		// ---------------------- Acción para abrir PDF al hacer doble clic ----------------------
		listProtocolosPrimAux.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        if (evt.getClickCount() == 2) { // Detecta doble clic
		            int index = listProtocolosPrimAux.getSelectedIndex();
		            if (index >= 0) {
		                String nombrePDF = listModel.getElementAt(index);
		                File pdf = new File("guias/" + nombrePDF); // Carpeta donde guardas los PDFs
		                if (pdf.exists()) {
		                    try {
		                        java.awt.Desktop.getDesktop().open(pdf); // Abre el PDF con el visor predeterminado
		                    } catch (Exception ex) {
		                        JOptionPane.showMessageDialog(null, "No se pudo abrir el PDF: " + ex.getMessage());
		                    }
		                } else {
		                    JOptionPane.showMessageDialog(null, "El archivo no existe: " + nombrePDF);
		                }
		            }
		        }
		    }
		});
		
		
		btnAddGuiaPAuxili = new JButton("Añadir Guía");
		btnAddGuiaPAuxili.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAddGuiaPAuxili.setBounds(664, 51, 127, 25);
		panelGuiasPAuxilios.add(btnAddGuiaPAuxili);
		
		btnAddGuiaPAuxili.addActionListener(e -> {
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Seleccionar guía PDF");
		    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    fileChooser.setAcceptAllFileFilterUsed(false);
		    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos PDF", "pdf"));

		    int seleccion = fileChooser.showOpenDialog(this); // Centrar en la ventana actual
		    if (seleccion == JFileChooser.APPROVE_OPTION) {
		        File archivoSeleccionado = fileChooser.getSelectedFile();

		        // Validar extensión .pdf
		        if (!archivoSeleccionado.getName().toLowerCase().endsWith(".pdf")) {
		            JOptionPane.showMessageDialog(this, "El archivo seleccionado no es un PDF.", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        try {
		            // Crear carpeta "guias" si no existe
		            File carpetaGuias = new File("guias");
		            if (!carpetaGuias.exists()) {
		                carpetaGuias.mkdirs();
		            }

		            // Copiar archivo seleccionado a la carpeta "guias"
		            File destino = new File(carpetaGuias, archivoSeleccionado.getName());

		            // Evitar duplicados en la lista
		            if (listModel.contains(destino.getName())) {
		                JOptionPane.showMessageDialog(this, "La guía ya existe en la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
		                return;
		            }

		            java.nio.file.Files.copy(archivoSeleccionado.toPath(), destino.toPath(),
		                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

		            // Añadir nombre al modelo de la lista para que aparezca en el JList
		            listModel.addElement(destino.getName());

		            // Persistir la lista usando GuiaService
		            GuiaService service = new GuiaService();
		            service.guardarLista(Collections.list(listModel.elements()));

		            JOptionPane.showMessageDialog(this, "Guía añadida correctamente: " + destino.getName());

		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(this, "Error al añadir la guía: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		
		btnQuitarGuiaPAuxili = new JButton("Quitar Guía");
		btnQuitarGuiaPAuxili.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnQuitarGuiaPAuxili.setBounds(664, 89, 127, 25);
		panelGuiasPAuxilios.add(btnQuitarGuiaPAuxili);
		
		btnQuitarGuiaPAuxili.addActionListener(e -> {
		    int index = listProtocolosPrimAux.getSelectedIndex(); // Obtener índice seleccionado
		    if (index < 0) {
		        JOptionPane.showMessageDialog(this, "Seleccione una guía para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
		        return;
		    }

		    String nombrePDF = listModel.getElementAt(index);

		    // Confirmar eliminación
		    int confirm = JOptionPane.showConfirmDialog(this,
		            "¿Desea eliminar la guía: " + nombrePDF + "?",
		            "Confirmar eliminación",
		            JOptionPane.YES_NO_OPTION);

		    if (confirm != JOptionPane.YES_OPTION) return; // Canceló la eliminación

		    // Eliminar del modelo para actualizar el JList
		    listModel.remove(index);

		    // Intentar eliminar archivo físico
		    File pdf = new File("guias/" + nombrePDF);
		    if (pdf.exists()) {
		        try {
		            if (!pdf.delete()) {
		                JOptionPane.showMessageDialog(this, "No se pudo eliminar el archivo físico: " + nombrePDF,
		                        "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (SecurityException ex) {
		            JOptionPane.showMessageDialog(this, "Error de seguridad al eliminar el archivo: " + ex.getMessage(),
		                    "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }

		    // Actualizar persistencia con GuiaService
		    GuiaService service = new GuiaService();
		    service.guardarLista(Collections.list(listModel.elements()));
		});
		
		// ---------------------- Filtrado en la barra de búsqueda ----------------------
		tfProtocolosPrimAux.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
		    private void filtrar() {
		        String texto = tfProtocolosPrimAux.getText().trim().toLowerCase();
		        listModel.clear();
		        if (texto.isEmpty()) {
		            // Mostrar todas las guías si no hay filtro
		            listaCompletaGuias.forEach(listModel::addElement);
		        } else {
		            // Mostrar solo las guías que contienen el texto
		            listaCompletaGuias.stream()
		                    .filter(s -> s.toLowerCase().contains(texto))
		                    .forEach(listModel::addElement);
		        }
		    }

		    @Override
		    public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
		    @Override
		    public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
		    @Override
		    public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
		});

		cardLayout.show(panelContenido, "Dashboard");

		JButton btnIncidentesActivos = new JButton("Incidentes Activos");
		btnIncidentesActivos.setBackground(new Color(255, 255, 255));
		btnIncidentesActivos.setForeground(new Color(0, 0, 0));
		btnIncidentesActivos.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnIncidentesActivos.addActionListener(e -> cardLayout.show(panelContenido, "Incidentes"));
		btnIncidentesActivos.setBounds(10, 11, 180, 40);
		panelBotones.add(btnIncidentesActivos);

		JButton btnInventarioSuministros = new JButton("Inventario Suministros");
		btnInventarioSuministros.setBackground(new Color(255, 255, 255));
		btnInventarioSuministros.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnInventarioSuministros.setBounds(10, 62, 180, 40);
		btnInventarioSuministros.addActionListener(e -> cardLayout.show(panelContenido, "Inventario"));
		panelBotones.add(btnInventarioSuministros);

		JButton btnBrigadistas = new JButton("Brigadistas");
		btnBrigadistas.setBackground(new Color(255, 255, 255));
		btnBrigadistas.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnBrigadistas.setBounds(10, 113, 180, 40);
		btnBrigadistas.addActionListener(e -> cardLayout.show(panelContenido, "Brigadistas"));
		panelBotones.add(btnBrigadistas);

		JButton btnGuiasPAuxilios = new JButton("Guias Primeros Auxilios");
		btnGuiasPAuxilios.setBackground(new Color(255, 255, 255));
		btnGuiasPAuxilios.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGuiasPAuxilios.setBounds(10, 164, 180, 40);
		btnGuiasPAuxilios.addActionListener(e -> cardLayout.show(panelContenido, "GuiasPAuxilios"));
		panelBotones.add(btnGuiasPAuxilios);

		// Footer (Añadido para completar la estructura 1000x700)
		JPanel panelFooter = new JPanel();
		panelFooter.setBackground(new Color(204, 204, 204));
		panelFooter.setBounds(0, 660, 1000, 40);
		panelFondo.add(panelFooter);
		panelFooter.setLayout(null);

		lblEstadoFooter = new JLabel("Inicializando Sistema...");
		lblEstadoFooter.setForeground(new Color(0, 0, 0));
		lblEstadoFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEstadoFooter.setBounds(10, 11, 500, 18);
		panelFooter.add(lblEstadoFooter);

		lblVersionFooter = new JLabel("Gestión de Emergencias v1.0.1 © 2025"); // Usamos la variable declarada
		lblVersionFooter.setForeground(new Color(0, 0, 0));
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
		
		JButton btnGenerarReporteBrigad = new JButton("Generar Reporte");
		btnGenerarReporteBrigad.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGenerarReporteBrigad.setBounds(501, 52, 140, 25);
		panelBrigadistas.add(btnGenerarReporteBrigad);
		
		btnGenerarReporteBrigad.addActionListener(e -> {
		    try {
		        DefaultTableModel model = (DefaultTableModel) tableBrigadistas.getModel();
		        int rowCount = model.getRowCount();

		        if (rowCount == 0) {
		            JOptionPane.showMessageDialog(this, "No hay brigadistas activos en la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        // Construir lista de brigadistas desde la tabla
		        List<Brigadista> brigadistas = new ArrayList<>();
		        for (int i = 0; i < rowCount; i++) {
		            Brigadista b = new Brigadista();
		            b.setId((int) model.getValueAt(i, 0));
		            b.setNombre((String) model.getValueAt(i, 1));
		            b.setEstado((String) model.getValueAt(i, 2));
		            b.setEspecialidad((String) model.getValueAt(i, 3));
		            b.setTelefono((String) model.getValueAt(i, 4));
		            brigadistas.add(b);
		        }

		        // Ordenar primero por Estado (Libre → En Servicio → Descanso) y luego por Nombre
		        brigadistas.sort((b1, b2) -> {
		            List<String> ordenEstados = List.of("Libre", "En Servicio", "Descanso");
		            int cmp = Integer.compare(
		                    ordenEstados.indexOf(b1.getEstado()),
		                    ordenEstados.indexOf(b2.getEstado())
		            );
		            return cmp != 0 ? cmp : b1.getNombre().compareToIgnoreCase(b2.getNombre());
		        });

		        // Elegir ubicación y nombre del archivo CSV
		        JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setSelectedFile(new java.io.File("ReporteBrigadistasActivos.csv"));
		        int seleccion = fileChooser.showSaveDialog(this);

		        if (seleccion == JFileChooser.APPROVE_OPTION) {
		            File archivo = fileChooser.getSelectedFile();

		            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
		                // Encabezado CSV
		                bw.write("ID,Nombre,Estado,Especialidad,Teléfono\n");

		                // Escribir cada brigadista
		                for (Brigadista b : brigadistas) {
		                    bw.write(String.format("%d,%s,%s,%s,%s\n",
		                            b.getId(),
		                            b.getNombre(),
		                            b.getEstado(),
		                            b.getEspecialidad(),
		                            b.getTelefono()
		                    ));
		                }
		            }

		            JOptionPane.showMessageDialog(this, "Reporte de brigadistas activos generado correctamente en: " 
		                                                + archivo.getAbsolutePath());
		        }

		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});

		JButton btnGenerarReporteIciden = new JButton("Generar Reporte");
		btnGenerarReporteIciden.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnGenerarReporteIciden.setBounds(516, 52, 144, 25);
		panelIncidentes.add(btnGenerarReporteIciden);
		
		btnGenerarReporteIciden.addActionListener(e -> {
		    try {
		        // Obtener todos los incidentes del archivo
		        List<Incidente> incidentes = incidenteService.getAll(); // todo el .dat

		        if (incidentes.isEmpty()) {
		            JOptionPane.showMessageDialog(this, "No hay incidentes en el historial.", "Aviso", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        // Elegir ubicación y nombre del archivo a guardar
		        JFileChooser fileChooser = new JFileChooser();
		        fileChooser.setSelectedFile(new java.io.File("ReporteHistoricoIncidentes.csv"));
		        int seleccion = fileChooser.showSaveDialog(this);

		        if (seleccion == JFileChooser.APPROVE_OPTION) {
		            File archivo = fileChooser.getSelectedFile();

		            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
		                // Encabezado CSV
		                bw.write("ID,Tipo,Prioridad,Ubicación,Responsable,TiempoActivo,Resuelto\n");

		                // Función para escapar campos CSV
		                java.util.function.Function<String, String> esc = campo -> {
		                    if (campo == null) return "";
		                    String valor = campo.replace("\"", "\"\""); // duplicar comillas internas
		                    if (valor.contains(",") || valor.contains("\n") || valor.contains("\"")) {
		                        valor = "\"" + valor + "\""; // envolver en comillas si contiene comas, saltos o comillas
		                    }
		                    return valor;
		                };

		                // Escribir cada incidente
		                for (Incidente i : incidentes) {
		                    bw.write(
		                        i.getId() + "," +
		                        esc.apply(i.getTipo()) + "," +
		                        esc.apply(i.getPrioridad()) + "," +
		                        esc.apply(i.getUbicacion()) + "," +
		                        esc.apply(i.getNombreBrigadista()) + "," +
		                        esc.apply(i.getTiempoActivo()) + "," +
		                        (i.isResuelto() ? "Sí" : "No") + "\n"
		                    );
		                }
		            }

		            JOptionPane.showMessageDialog(this, "Reporte histórico generado correctamente en: " + archivo.getAbsolutePath());
		        }

		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});

		
		
		// fin constructor
	}

	/**
	 * Aplica las reglas de visibilidad/eliminación por rol.  
	 * - Elimina físicamente (panel.remove) los botones que pediste quitar.
	 * - No cambia posición ni estilo de los elementos restantes.
	 */
	private void applyRoleRules(String rol, JPanel panelIncidentes, JPanel panelInventario, JPanel panelBrigadistas) {
	    rol = (rol == null) ? "Coordinador" : rol;

	    // COORDINADOR -> todo activo
	    if (rol.equalsIgnoreCase("Coordinador")) {
	        return;
	    }

	    // ============================
	    // BRIGADISTA
	    // ============================
	    if (rol.equalsIgnoreCase("Brigadista")) {

	        // Ocultar botón Asignar Brigadista
	        if (btnAsignarBrigadista != null) {
	            panelIncidentes.remove(btnAsignarBrigadista);
	        }
	        if (btnDesasignarBrigadista != null) {
	            panelIncidentes.remove(btnDesasignarBrigadista);
	        }

	        if (btnAddBrigadista != null) {
	            panelBrigadistas.remove(btnAddBrigadista);
	        }
	        if (btnEliminarBrigadista != null) {
	            panelBrigadistas.remove(btnEliminarBrigadista);
	        }
	        if (panelDetallesBrigadista != null) {
	            panelBrigadistas.remove(panelDetallesBrigadista);
	        }
	    }

	    // ============================
	    // USUARIO O RESIDENTE
	    // ============================
	    if (rol.equalsIgnoreCase("Usuario") || rol.equalsIgnoreCase("Residente")) {

	        // Ocultar botón Asignar Brigadista
	        if (btnAsignarBrigadista != null) {
	            panelIncidentes.remove(btnAsignarBrigadista);
	            
	        }
	        
	        if (btnDesasignarBrigadista != null) {
	            panelIncidentes.remove(btnDesasignarBrigadista);
	        }


	        if (btnMarcaResueltoIncide != null) {
	            panelIncidentes.remove(btnMarcaResueltoIncide);
	        }

	        if (btnAñadirSuministro != null) {
	            panelInventario.remove(btnAñadirSuministro);
	        }
	        if (btnEditStockSuminis != null) {
	            panelInventario.remove(btnEditStockSuminis);
	        }
	        if (btnMarcarCriticSuminis != null) {
	            panelInventario.remove(btnMarcarCriticSuminis);
	        }
	        if (btnAddBrigadista != null) {
	            panelBrigadistas.remove(btnAddBrigadista);
	        }
	        if (btnEliminarBrigadista != null) {
	            panelBrigadistas.remove(btnEliminarBrigadista);
	        }
	        if (panelDetallesBrigadista != null) {
	            panelBrigadistas.remove(panelDetallesBrigadista);
	        }
	        if (btnAddGuiaPAuxili != null) {
				panelGuiasPAuxilios.remove(btnAddGuiaPAuxili);
	        }
	        if (btnQuitarGuiaPAuxili != null) {
	        	panelGuiasPAuxilios.remove(btnQuitarGuiaPAuxili);
	        }
	    }

	    this.revalidate();
	    this.repaint();
	}
	private void actualizarTabla() {
	    String busqueda = tfBuscarIncidentes.getText();
	    String filtro = (String) cbFiltroPrioridad.getSelectedItem();
	    incidenteController.actualizarTabla(tableIncidentes, busqueda, filtro);
	}
	// Abrir el diálogo de edición de suministro
	private void abrirEditarSuministro() {
	    int fila = tableSuministros.getSelectedRow();
	    if (fila >= 0) {
	        int id = (int) tableSuministros.getValueAt(fila, 0);
	        Suministro s = suministroController.getAllSuministros().stream()
	                .filter(su -> su.getId() == id)
	                .findFirst()
	                .orElse(null);
	        if (s != null) {
	            // Ahora pasamos 'false' porque estamos editando un suministro existente
	            new EditarSuministroDialog(this, suministroController, tableSuministros, s, false).setVisible(true);
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "Seleccione un suministro para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
	    }
	}

	// Aplicar color a los suministros críticos
	private void aplicarColorCritico() {
	    tableSuministros.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value,
	                                                       boolean isSelected, boolean hasFocus,
	                                                       int row, int column) {
	            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	            int id = (int) table.getValueAt(row, 0);
	            Suministro s = suministroController.getAllSuministros().stream()
	                    .filter(su -> su.getId() == id)
	                    .findFirst().orElse(null);

	            if (s != null && s.isCritico()) {
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
	            s.setCritico(true); // marcar como crítico

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

	// Clase para generar el reporte de suministros
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
	                         (s.isCritico() ? "CRÍTICO" : "") + "\n");
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
	
	private void abrirCrearBrigadista() {
	    // Abrimos el diálogo de creación, pasando el controlador y la tabla
	    CrearBrigadistaDialog dialog = new CrearBrigadistaDialog(this, brigadistaController, tableBrigadistas);
	    dialog.setVisible(true);
	}
	
}
