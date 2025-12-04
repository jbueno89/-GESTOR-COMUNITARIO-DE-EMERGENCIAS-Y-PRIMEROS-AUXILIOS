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
import java.text.SimpleDateFormat;
import javax.swing.Timer;
import java.util.Date;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants; 
// ************************************************************
// NUEVAS IMPORTACIONES PARA IMAGEN
// ************************************************************
import javax.swing.ImageIcon; // Para cargar la imagen
import java.awt.Image; // Para escalar la imagen

public class PanelPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Variables funcionales
	private int mouseX, mouseY;
	private CardLayout cardLayout;
	
	// Variables para la lógica de roles (se mantiene para usar el rol en el contenido)
	private String rolUsuario; 
	
	// Variables del menú lateral (deben ser atributos de clase para el estilo)
	private JButton btnDashboard;
	private JButton btnIncidentesActivos;
	private JButton btnInventarioSuministros;
	private JButton btnBrigadistas;
	private JButton btnGuiasPAuxilios;
	private JButton btnReportes;
	private JButton btnCerrarSesion;
	
	// Variables de la vista
	private JTextField tfBuscarIncidentes;
	private JTable tableIncidentes;
	private JTextField tfBuscarNombreSuminis;
	private JTable tableSuministros;
	private JTextField tfBuscarBrigadistas;
	private JTable tableBrigadistas;
	private JTextField tfProtocolosPrimAux;
	private JFormattedTextField tfFechaInicioReporte;
	private JFormattedTextField tfFechaFinReporte;
	private JTable tableReportesResultPrevi;
	private JLabel lblEstadoFooter;
	private JLabel lblVersionFooter;

	// Definición de colores
	private final Color COLOR_PRIMARIO_AZUL = new Color(0, 153, 204); 
	private final Color COLOR_FONDO_OSCURO = new Color(40, 40, 40); 
	private final Color COLOR_BOTON_MENU = new Color(75, 75, 75); 
	private final Color COLOR_CERRAR_SESION = new Color(220, 20, 60); 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Usamos el rol "Coordinador" para pruebas, aunque el menú es completo
					PanelPrincipal frame = new PanelPrincipal(); 
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// ************************************************************
	// Constructor Principal (Usado por Login/Registro)
	// ************************************************************
	public PanelPrincipal(String rol) {
	    this.rolUsuario = rol;
	    inicializarComponentes();
	    // NOTA CLAVE: SE ELIMINA LA LLAMADA A filtrarBotonesPorRol()
	    aplicarEstiloBotones(); 
	}


	/**
	 * Create the frame. (Constructor sin argumentos, llama al principal)
	 */
	public PanelPrincipal() {
	    // Asigna el rol por defecto (ej. Coordinador)
	    this("Coordinador"); 
	}
	
	private void inicializarComponentes() {
		
		// 1. CONFIGURACIÓN DE LA VENTANA
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		this.setSize(1000, 700);
		this.setLocationRelativeTo(null);
		this.setResizable(false); 

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
        
        // ************************************************************
        // AÑADIR LOGO COMO FONDO DE PANTALLA
        // ************************************************************
        JLabel lblLogoFondo = new JLabel();
        lblLogoFondo.setBounds(200, 40, 800, 620); // Área del panel de contenido (sin menú lateral ni barra superior)
        lblLogoFondo.setOpaque(true);
        lblLogoFondo.setBackground(Color.WHITE); // Fondo blanco para el área de contenido
        panelFondo.add(lblLogoFondo); 
        panelFondo.setComponentZOrder(lblLogoFondo, 0); // Asegura que el logo esté detrás de todo

        try {
            // Carga la imagen (asegúrate de que la ruta sea correcta)
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/resources/Logo.png")); // Usa la ruta si está en un paquete 'resources'
            // Si el archivo está en el directorio del proyecto y no en un JAR/paquete, usa:
            // ImageIcon originalIcon = new ImageIcon("Logo.png");
            
            // Escala la imagen para que se ajuste al área de fondo (ej. 750x600) o déjalo a un tamaño fijo grande
            Image originalImage = originalIcon.getImage();
            // Tamaño objetivo: un poco más pequeño que el panel de contenido para que se vea bien centrado
            int targetWidth = 600; 
            int targetHeight = 600;
            
            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            
            lblLogoFondo.setIcon(scaledIcon);
            lblLogoFondo.setHorizontalAlignment(SwingConstants.CENTER);
            lblLogoFondo.setVerticalAlignment(SwingConstants.CENTER);
            
            // NOTA SOBRE TRANSPARENCIA: La parte blanca del logo (Logo.png)
            // debe ser transparente *en el archivo PNG* para que el fondo
            // blanco (o cualquier otro color) del JLabel se vea. 
            // Si el logo ya es un PNG con fondo transparente, este código funcionará.

        } catch (Exception e) {
            System.err.println("Error al cargar o escalar el logo: " + e.getMessage());
            // Si no carga, el fondo será simplemente blanco (por el lblLogoFondo.setBackground(Color.WHITE);)
        }
        
        // La instrucción `panelFondo.setComponentZOrder(lblLogoFondo, 0);` 
        // coloca el JLabel de fondo al inicio de la lista de componentes (detrás de todos).
        // ************************************************************
		// FIN AÑADIR LOGO COMO FONDO DE PANTALLA
        // ************************************************************


		// BARRA SUPERIOR (AZUL)
		JPanel panelBarra = new JPanel();
		panelBarra.setBounds(0, 0, 1000, 40);
		panelBarra.setBackground(COLOR_PRIMARIO_AZUL);
		panelFondo.add(panelBarra);
		panelFondo.setComponentZOrder(panelBarra, 0); // Lo ponemos al frente (z-order 0 = más al frente en Swing)

		JLabel lblTituloLogo = new JLabel("GESTOR COMUNITARIO DE EMERGENCIAS");
		lblTituloLogo.setBounds(10, 9, 350, 23);
		lblTituloLogo.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblTituloLogo.setForeground(Color.WHITE); 
		panelBarra.add(lblTituloLogo);

		// Lógica de Minimizar
		JButton btnMinimizar = new JButton("-");
		btnMinimizar.setBounds(885, 2, 50, 37);
		btnMinimizar.setFont(new Font("SansSerif", Font.BOLD, 20));
		btnMinimizar.setBackground(COLOR_PRIMARIO_AZUL);
		btnMinimizar.setForeground(Color.WHITE);
		btnMinimizar.setBorderPainted(false);
		btnMinimizar.setFocusPainted(false);
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
		btnCerrar.setBackground(COLOR_PRIMARIO_AZUL);
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setBorderPainted(false);
		btnCerrar.setFocusPainted(false);
		btnCerrar.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        btnCerrar.setBackground(COLOR_CERRAR_SESION);
		    }
		    @Override
		    public void mouseExited(MouseEvent e) {
		        btnCerrar.setBackground(COLOR_PRIMARIO_AZUL);
		    }
		});
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

		// PANEL DE BOTONES LATERAL (GRIS OSCURO)
		JPanel panelBotones = new JPanel();
		panelBotones.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelBotones.setBounds(0, 40, 200, 620);
        panelBotones.setBackground(COLOR_FONDO_OSCURO); 
		panelFondo.add(panelBotones);
		panelFondo.setComponentZOrder(panelBotones, 0); // Lo ponemos al frente

		panelBotones.setLayout(null);

		// 2. CONEXIÓN DE CARDLAYOUT
		JPanel panelContenido = new JPanel();
		panelContenido.setBounds(199, 39, 801, 620);
		// ************************************************************
		// IMPORTANTE: Hacemos el panel de contenido transparente para ver el fondo
		// ************************************************************
		panelContenido.setOpaque(false);
		panelFondo.add(panelContenido);
		panelFondo.setComponentZOrder(panelContenido, 0); // Lo ponemos al frente

		cardLayout = new CardLayout(0, 0);
		panelContenido.setLayout(cardLayout);

		// 3. CREACIÓN DE BOTONES DEL MENÚ LATERAL (SE CREAN TODOS VISIBLES)
		
		btnDashboard = new JButton("Tablero");
		btnDashboard.setBounds(10, 21, 180, 40);
		btnDashboard.addActionListener(e -> cardLayout.show(panelContenido, "Dashboard"));
		panelBotones.add(btnDashboard);

		btnIncidentesActivos = new JButton("Incidentes Activos");
		btnIncidentesActivos.setBounds(10, 72, 180, 40);
		btnIncidentesActivos.addActionListener(e -> cardLayout.show(panelContenido, "Incidentes"));
		panelBotones.add(btnIncidentesActivos);

		btnInventarioSuministros = new JButton("Inventario Suministros");
		btnInventarioSuministros.setBounds(10, 123, 180, 40);
		btnInventarioSuministros.addActionListener(e -> cardLayout.show(panelContenido, "Inventario"));
		panelBotones.add(btnInventarioSuministros);

		btnBrigadistas = new JButton("Brigadistas");
		btnBrigadistas.setBounds(10, 174, 180, 40);
		btnBrigadistas.addActionListener(e -> cardLayout.show(panelContenido, "Brigadistas"));
		panelBotones.add(btnBrigadistas);

		btnGuiasPAuxilios = new JButton("Guias Primeros Auxilios");
		btnGuiasPAuxilios.setBounds(10, 225, 180, 40);
		btnGuiasPAuxilios.addActionListener(e -> cardLayout.show(panelContenido, "GuiasPAuxilios"));
		panelBotones.add(btnGuiasPAuxilios);

		btnReportes = new JButton("Reportes");
		btnReportes.setBounds(10, 276, 180, 40);
		btnReportes.addActionListener(e -> cardLayout.show(panelContenido, "Reportes"));
		panelBotones.add(btnReportes);
		
        // Botón de Cerrar Sesión (Visible para todos)
        btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setBounds(10, 560, 180, 45); 
        btnCerrarSesion.addActionListener(e -> {
            System.out.println("Cerrando Sesión...");
            this.dispose(); 
            // Aquí se llamaría a la LoginView
        });
        panelBotones.add(btnCerrarSesion);
		
		
		// -------------------------------------------------------------------------
		// A partir de aquí sigue el código de todas las vistas (Incidentes, Inventario, etc.)
		// ************************************************************
		// IMPORTANTE: Hacemos los paneles de vista internos (Incidentes, Inventario, etc.) transparentes
		// si queremos ver el logo a través de ellos, o les ponemos un color de fondo si no.
		// En este caso, **todos los paneles de vista internos deben ser transparentes**
		// para que el logo se vea, **O** solo hacemos transparente el panel Dashboard.
		// A continuación, se deja el fondo blanco por defecto en las vistas para mejor legibilidad.
		// Solo se modifica el panelDashboard.
		// -------------------------------------------------------------------------
		
		JPanel panelIncidentes = new JPanel();
		panelContenido.add(panelIncidentes, "Incidentes");
		panelIncidentes.setLayout(null); // Dejar con fondo

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
		panelInventario.setLayout(null); // Dejar con fondo

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

		tableSuministros
				= new JTable();
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
		panelBrigadistas.setLayout(null); // Dejar con fondo

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

		JPanel panelDetallesBrigadista = new JPanel();
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
		panelGuiasPAuxilios.setLayout(null); // Dejar con fondo

		JLabel lblTituloPrimerosAux = new JLabel("Biblioteca de Guías y Protocolos de  Auxilios");
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
		panelReportes.setLayout(null); // Dejar con fondo

		JLabel lblTituloReportes = new JLabel("Biblioteca de Guías y Protocolos de  Auxilios");
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

		// JFormattedTextFields con máscara para fechas
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
		// ************************************************************
		// Hacemos el Dashboard transparente para ver el logo
		// ************************************************************
		panelDashboard.setOpaque(false); 

		cardLayout.show(panelContenido, "Dashboard"); 

		// Footer
		JPanel panelFooter = new JPanel();
		panelFooter.setBounds(0, 660, 1000, 40);
		panelFondo.add(panelFooter);
		panelFondo.setComponentZOrder(panelFooter, 0); // Lo ponemos al frente
		panelFooter.setLayout(null);

		lblEstadoFooter = new JLabel("Inicializando Sistema..."); 
		lblEstadoFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEstadoFooter.setBounds(10, 11, 500, 18);
		panelFooter.add(lblEstadoFooter);

		lblVersionFooter = new JLabel("Gestión de Emergencias v1.0.1 © 2025"); 
		lblVersionFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblVersionFooter.setBounds(750, 11, 240, 18);
		panelFooter.add(lblVersionFooter);

		// Timer para simular sincronización en el Footer
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Timer timer = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String horaActual = timeFormat.format(new Date());
				lblEstadoFooter.setText("✅ Sistema Listo | Última sincronización: " + horaActual);
			}
		});

		lblEstadoFooter.setText("✅ Sistema Listo | Última sincronización: " + timeFormat.format(new Date()));
		timer.start();

		// --- Componentes del Dashboard (Se mantienen) ---
		// NOTA: Para que el logo se vea, todos los paneles en Dashboard 
		// que deberían ser transparentes, deben ser transparentes (setOpaque(false)).
		// En este caso, dejamos los paneles de información opacos para mejor lectura.
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
	
	
	// ************************************************************
	// MÉTODO DE FILTRADO ELIMINADO. SOLO MANTENEMOS EL ESTILO.
	// ************************************************************
	
	private void aplicarEstiloBotones() {
	    // Estilo Común para todos los botones del menú (excepto Cerrar Sesión)
	    JButton[] menuButtons = {
	        btnDashboard, btnIncidentesActivos, btnInventarioSuministros,
	        btnBrigadistas, btnGuiasPAuxilios, btnReportes
	    };
	    
	    Color menuBgColor = COLOR_BOTON_MENU; 
	    Color menuFgColor = Color.WHITE;
	    Font menuFont = new Font("SansSerif", Font.BOLD, 12);
	    Color hoverColor = COLOR_PRIMARIO_AZUL; 
	    
	    for (JButton btn : menuButtons) {
	        btn.setFont(menuFont);
	        btn.setBackground(menuBgColor);
	        btn.setForeground(menuFgColor);
	        btn.setBorderPainted(false); 
	        btn.setFocusPainted(false);
	        btn.setHorizontalAlignment(SwingConstants.LEFT); 
	        btn.setOpaque(true); 
	        
	        // Listener de Mouse para un efecto de "Hover"
	        btn.addMouseListener(new MouseAdapter() {
	            Color original = menuBgColor;
	            
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                btn.setBackground(hoverColor);
	            }
	            
	            @Override
	            public void mouseExited(MouseEvent e) {
	                btn.setBackground(original); 
	            }
	        });
	    }
	    
	    // Estilo Específico para el Botón de CERRAR SESIÓN (Rojo)
	    btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 14));
	    btnCerrarSesion.setBackground(COLOR_CERRAR_SESION); 
	    btnCerrarSesion.setForeground(Color.WHITE);
	    btnCerrarSesion.setBorderPainted(false);
	    btnCerrarSesion.setFocusPainted(false);
	    btnCerrarSesion.setOpaque(true); 
	}
}