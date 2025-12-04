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
import java.net.URL; 
import javax.swing.ImageIcon; 
import java.awt.Image; 
import javax.swing.SwingConstants; 

public class PanelPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

    // ====================================================
    // 🎨 DEFINICIÓN DE COLORES DEL LOGO 🎨
    // ====================================================
    private final Color COLOR_PRIMARIO = new Color(26, 75, 140);   // #1A4B8C (Azul Profundo)
    private final Color COLOR_ACCENTO = new Color(217, 0, 29);    // #D9001D (Rojo Brillante)
    private final Color COLOR_FONDO_MENU = new Color(50, 50, 50); // Gris Oscuro para el menú
    private final Color COLOR_FONDO_CLARO = new Color(245, 245, 245); // Gris Claro para el fondo principal
	
    // Campo para almacenar el rol 
    private String rolActual; 

	// Variables funcionales y de componentes
	private int mouseX, mouseY;
	private CardLayout cardLayout;
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
    private JLabel lblTitulo;
    
    // Componentes del Menú Lateral 
    private JButton btnDashboard;
    private JButton btnIncidentesActivos;
    private JButton btnInventarioSuministros;
    private JButton btnBrigadistas;
    private JButton btnGuiasPAuxilios;
    private JButton btnReportes;
    
    // 💡 NUEVA VARIABLE PARA RASTREAR EL BOTÓN ACTIVO
    private JButton btnActivo; 
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Usamos el constructor con un rol de prueba
					PanelPrincipal frame = new PanelPrincipal("Coordinador"); 
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
    
    // ====================================================
    // 💡 CLASE INTERNA PARA EL ESTILO DE BOTÓN DEL MENÚ (MODIFICADA)
    // ====================================================
    private class MenuButton extends JButton {
        public MenuButton(String text) {
            super(text);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setBackground(COLOR_FONDO_MENU); // Fondo del menú por defecto
            setForeground(Color.WHITE); 
            setFocusPainted(false);
            setBorderPainted(false);
            setHorizontalAlignment(SwingConstants.LEFT);
            setOpaque(true);
            // Padding/Margen
            setBorder(new EmptyBorder(0, 20, 0, 0)); 
            
            // Efecto hover (cambia el color de fondo al pasar el mouse)
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(COLOR_PRIMARIO); // Siempre Azul Profundo al pasar el mouse
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Si el botón NO es el activo, vuelve a Gris Oscuro
                    if (MenuButton.this != btnActivo) { 
                         setBackground(COLOR_FONDO_MENU);
                    }
                    // Si es el activo (btnActivo), se queda en COLOR_PRIMARIO
                }
            });
        }
    }


	// ----------------------------------------------------
	// 🔨 CONSTRUCTORES 🔨
	// ----------------------------------------------------
	public PanelPrincipal(String rol) {
        this.rolActual = rol;
		inicializarComponentes();
        
        // Actualizamos el título usando el rol
        lblTitulo.setText("GESTOR COMUNITARIO - " + rol.toUpperCase());
	}
    
	public PanelPrincipal() {
        this("Desconocido"); // Llama al constructor principal con un rol predeterminado
	}

	// ----------------------------------------------------
	// 🛠️ INICIALIZACIÓN DE COMPONENTES 🛠️
	// ----------------------------------------------------

    /**
     * Gestiona la selección visual de los botones del menú.
     * Desactiva el botón previamente activo y activa el nuevo.
     * @param nuevoBoton El JButton que se acaba de presionar.
     */
    private void activarBoton(JButton nuevoBoton) {
        // Si hay un botón activo y no es el que acabamos de presionar, desactívalo
        if (btnActivo != null && btnActivo != nuevoBoton) {
            btnActivo.setBackground(COLOR_FONDO_MENU);
        }
        
        // Asigna el nuevo botón activo y coloréalo
        btnActivo = nuevoBoton;
        btnActivo.setBackground(COLOR_PRIMARIO);
    }
    
	/**
	 * Inicializa todos los componentes de la interfaz.
	 */
	private void inicializarComponentes() {

		// 1. CONFIGURACIÓN DE LA VENTANA
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		this.setSize(1000, 700);
		this.setLocationRelativeTo(null);
		this.setResizable(false); 

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); 

		JPanel panelFondo = new JPanel();
		panelFondo.setBounds(0, 0, 1000, 700);
		contentPane.add(panelFondo);
		panelFondo.setLayout(null);
        panelFondo.setBackground(COLOR_FONDO_CLARO); // Fondo global de la aplicación

		// --- 2. Barra de Título (Arrastrable y Controles) ---
		JPanel panelBarra = new JPanel();
		panelBarra.setBounds(0, 0, 1000, 40);
		panelBarra.setLayout(null);
        panelBarra.setBackground(COLOR_FONDO_MENU); 
		panelFondo.add(panelBarra);

		// LÓGICA DEL LOGO
		JLabel lblLogo = new JLabel("");
		final int LOGO_WIDTH = 37; 
		final int LOGO_HEIGHT = 37;
		
		lblLogo.setBounds(6, 2, LOGO_WIDTH, LOGO_HEIGHT);
		lblLogo.setFont(new Font("SansSerif", Font.BOLD, 14));
		panelBarra.add(lblLogo);
		
		// Intento de cargar el logo desde recursos
		String logoPath = "/recursos/Logo.png"; 
		try {
		    URL logoURL = PanelPrincipal.class.getResource(logoPath); 
		    if (logoURL != null) {
		        ImageIcon originalIcon = new ImageIcon(logoURL);
		        Image image = originalIcon.getImage();
		        Image scaledImage = image.getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
		        lblLogo.setIcon(new ImageIcon(scaledImage));
		    } else {
		        lblLogo.setText("I"); 
		        lblLogo.setForeground(Color.WHITE); 
		        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 20));
		    }
		} catch (Exception e) {
		    System.err.println("Error al cargar el logo en PanelPrincipal: " + logoPath);
		    lblLogo.setText("I");
		}
		
		// Lógica de Minimizar y Cerrar (Usando COLOR_FONDO_MENU)
		JButton btnMinimizar = new JButton("-");
		btnMinimizar.setBounds(885, 0, 50, 40); 
		btnMinimizar.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnMinimizar.setForeground(Color.WHITE);
        btnMinimizar.setBackground(COLOR_FONDO_MENU);
        btnMinimizar.setBorderPainted(false);
        btnMinimizar.setFocusPainted(false);
		btnMinimizar.addActionListener(e -> setState(JFrame.ICONIFIED));
		panelBarra.add(btnMinimizar);

		JButton btnCerrar = new JButton("x");
		btnCerrar.setBounds(937, 0, 53, 40); 
		btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(COLOR_FONDO_MENU);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusPainted(false);
		btnCerrar.addActionListener(e -> System.exit(0));
		panelBarra.add(btnCerrar);
		
        // Inicialización del JLabel de título
		lblTitulo = new JLabel("GESTOR COMUNITARIO"); 
        lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 18));
		lblTitulo.setBounds(50, 9, 450, 23); 
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

		// --- 3. Panel de Botones (Menú Lateral) ---
		JPanel panelBotones = new JPanel();
		panelBotones.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelBotones.setBounds(0, 40, 200, 620);
        panelBotones.setBackground(COLOR_FONDO_MENU); 
		panelFondo.add(panelBotones);
		panelBotones.setLayout(null);

		// --- 4. Panel de Contenido (Vistas) ---
		JPanel panelContenido = new JPanel();
		panelContenido.setBounds(200, 40, 800, 620); // Ajuste de 1px a 200/800
        panelContenido.setBackground(COLOR_FONDO_CLARO);
		panelFondo.add(panelContenido);

		cardLayout = new CardLayout(0, 0);
		panelContenido.setLayout(cardLayout);

		// ----------------------------------------------------
		// 5. LISTENERS Y BOTONES DEL MENÚ (Usando MenuButton) - AHORA CON LÓGICA DE ACTIVACIÓN
		// ----------------------------------------------------
        
        // Botones de Navegación
		btnDashboard = new MenuButton("  Tablero");
		btnDashboard.setBounds(0, 21, 200, 45); 
		btnDashboard.addActionListener(e -> {
            cardLayout.show(panelContenido, "Dashboard");
            activarBoton(btnDashboard); 
        });
        
        // Inicializamos el Dashboard como el botón activo por defecto
        btnActivo = btnDashboard; 
        btnActivo.setBackground(COLOR_PRIMARIO);
		panelBotones.add(btnDashboard);

		btnIncidentesActivos = new MenuButton("  Incidentes Activos");
		btnIncidentesActivos.setBounds(0, 75, 200, 45);
		btnIncidentesActivos.addActionListener(e -> {
            cardLayout.show(panelContenido, "Incidentes");
            activarBoton(btnIncidentesActivos);
        });
		panelBotones.add(btnIncidentesActivos);

		btnInventarioSuministros = new MenuButton("  Inventario Suministros");
		btnInventarioSuministros.setBounds(0, 129, 200, 45);
		btnInventarioSuministros.addActionListener(e -> {
            cardLayout.show(panelContenido, "Inventario");
            activarBoton(btnInventarioSuministros);
        });
		panelBotones.add(btnInventarioSuministros);

		btnBrigadistas = new MenuButton("  Brigadistas");
		btnBrigadistas.setBounds(0, 183, 200, 45);
		btnBrigadistas.addActionListener(e -> {
            cardLayout.show(panelContenido, "Brigadistas");
            activarBoton(btnBrigadistas);
        });
		panelBotones.add(btnBrigadistas);

		btnGuiasPAuxilios = new MenuButton("  Guías P. Auxilios");
		btnGuiasPAuxilios.setBounds(0, 237, 200, 45);
		btnGuiasPAuxilios.addActionListener(e -> {
            cardLayout.show(panelContenido, "GuiasPAuxilios");
            activarBoton(btnGuiasPAuxilios);
        });
		panelBotones.add(btnGuiasPAuxilios);

		btnReportes = new MenuButton("  Reportes");
		btnReportes.setBounds(0, 291, 200, 45);
		btnReportes.addActionListener(e -> {
            cardLayout.show(panelContenido, "Reportes");
            activarBoton(btnReportes);
        });
		panelBotones.add(btnReportes);
        
        // --- Separador de secciones en el menú
        JPanel separadorMenu = new JPanel();
        separadorMenu.setBackground(new Color(100, 100, 100)); // Gris más claro sobre el fondo oscuro
        separadorMenu.setBounds(10, 350, 180, 1);
        panelBotones.add(separadorMenu);
        
        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrarSesion.setBounds(10, 560, 180, 45);
        btnCerrarSesion.setBackground(COLOR_ACCENTO); // ROJO de Énfasis/Emergencia
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.addActionListener(e -> {
            // Lógica para cerrar sesión (ej. volver a la pantalla de login)
            dispose(); 
        });
        
        // AÑADIR HOVER SÓLO A ESTE BOTÓN (NO DE NAVEGACIÓN PRINCIPAL)
        btnCerrarSesion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCerrarSesion.setBackground(new Color(255, 50, 70)); // Rojo más claro al pasar
            }

            @Override
            public void mouseExited(MouseEvent e) {
                 btnCerrarSesion.setBackground(COLOR_ACCENTO); // Vuelve al rojo original al salir
            }
        });
        
        panelBotones.add(btnCerrarSesion);


		// ************************************************************
		// Definición de Paneles de Contenido (Las vistas)
		// ************************************************************

		// --- Panel Dashboard --- 
		JPanel panelDashboard = new JPanel();
		panelDashboard.setFont(new Font("SansSerif", Font.BOLD, 30));
        panelDashboard.setBackground(COLOR_FONDO_CLARO);
		panelContenido.add(panelDashboard, "Dashboard");
		panelDashboard.setLayout(null);
        
        // Dashboard: Título
        JLabel lblTituloDashboard = new JLabel("Resumen de Actividad");
        lblTituloDashboard.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTituloDashboard.setBounds(20, 20, 300, 25);
        panelDashboard.add(lblTituloDashboard);
        
        // Dashboard: Contadores (Paneles más limpios)
		JPanel panelIncidentesAbiertos = new JPanel();
		panelIncidentesAbiertos.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        panelIncidentesAbiertos.setBackground(Color.WHITE);
		panelIncidentesAbiertos.setBounds(20, 70, 250, 100);
		panelDashboard.add(panelIncidentesAbiertos);
		panelIncidentesAbiertos.setLayout(null);

		JLabel lblNumIncidentes = new JLabel("12");
		lblNumIncidentes.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblNumIncidentes.setForeground(COLOR_PRIMARIO); // Color primario para métricas
		lblNumIncidentes.setBounds(20, 21, 100, 36);
		panelIncidentesAbiertos.add(lblNumIncidentes);

		JLabel lblIncidentesAbiertos = new JLabel("Incidentes Abiertos");
		lblIncidentesAbiertos.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblIncidentesAbiertos.setBounds(20, 60, 200, 16);
		panelIncidentesAbiertos.add(lblIncidentesAbiertos);

		JPanel panelBrigadistasLibres = new JPanel();
		panelBrigadistasLibres.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        panelBrigadistasLibres.setBackground(Color.WHITE);
		panelBrigadistasLibres.setBounds(280, 70, 250, 100);
		panelDashboard.add(panelBrigadistasLibres);
		panelBrigadistasLibres.setLayout(null);

		JLabel lblNumBrigadistas = new JLabel("5");
		lblNumBrigadistas.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblNumBrigadistas.setForeground(COLOR_PRIMARIO);
		lblNumBrigadistas.setBounds(20, 21, 100, 40);
		panelBrigadistasLibres.add(lblNumBrigadistas);

		JLabel lblBrigadistasLibres = new JLabel("Brigadistas Libres");
		lblBrigadistasLibres.setBounds(20, 61, 200, 16);
		panelBrigadistasLibres.add(lblBrigadistasLibres);
		lblBrigadistasLibres.setFont(new Font("SansSerif", Font.PLAIN, 12));

		JPanel panelStockCritico = new JPanel();
		panelStockCritico.setLayout(null);
		panelStockCritico.setBorder(new LineBorder(COLOR_ACCENTO, 1)); // Borde rojo
        panelStockCritico.setBackground(Color.WHITE);
		panelStockCritico.setBounds(540, 70, 250, 100);
		panelDashboard.add(panelStockCritico);

		JLabel lblNumStock = new JLabel("5");
		lblNumStock.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblNumStock.setForeground(COLOR_ACCENTO); // Número en rojo
		lblNumStock.setBounds(20, 21, 100, 40);
		panelStockCritico.add(lblNumStock);

		JLabel lblStockCritico = new JLabel("Stock Crítico");
		lblStockCritico.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblStockCritico.setBounds(20, 61, 200, 16);
		panelStockCritico.add(lblStockCritico);

        // Dashboard: Gráficos (Fondo blanco)
		JPanel panelIncidentesPrioridad = new JPanel();
		panelIncidentesPrioridad.setFont(new Font("SansSerif", Font.BOLD, 12));
		panelIncidentesPrioridad.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
								"Incidentes por Prioridad (Gráfico)", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelIncidentesPrioridad.setBackground(Color.WHITE);
		panelIncidentesPrioridad.setBounds(20, 190, 450, 200);
		panelDashboard.add(panelIncidentesPrioridad);

		JPanel panelBrigadistasEspecialidad = new JPanel();
		panelBrigadistasEspecialidad.setBorder(new TitledBorder(null, "Brigadistas por Especialidad (Gráfico)", TitledBorder.LEFT,
				TitledBorder.TOP, null, null));
        panelBrigadistasEspecialidad.setBackground(Color.WHITE);
		panelBrigadistasEspecialidad.setBounds(480, 190, 310, 200);
		panelDashboard.add(panelBrigadistasEspecialidad);

		// --- Panel Incidentes --- 
		JPanel panelIncidentes = new JPanel();
        panelIncidentes.setBackground(COLOR_FONDO_CLARO);
		panelContenido.add(panelIncidentes, "Incidentes");
		panelIncidentes.setLayout(null);
        
        // Títulos y botones con COLOR_PRIMARIO
        JLabel lblTituloIncidentesActCurso = new JLabel("Incidentes Activos en Curso");
		lblTituloIncidentesActCurso.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblTituloIncidentesActCurso.setBounds(20, 11, 301, 25);
        lblTituloIncidentesActCurso.setForeground(COLOR_PRIMARIO);
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
        btnCrearIncidente.setBackground(COLOR_PRIMARIO);
        btnCrearIncidente.setForeground(Color.WHITE);
		btnCrearIncidente.addActionListener(e -> {});
		btnCrearIncidente.setBounds(650, 50, 141, 25);
		panelIncidentes.add(btnCrearIncidente);

		JScrollPane spIncidentes = new JScrollPane();
        spIncidentes.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		spIncidentes.setBounds(20, 90, 771, 480);
		panelIncidentes.add(spIncidentes);

		tableIncidentes = new JTable();
		tableIncidentes.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Tipo", "Prioridad", "Ubicaci\u00F3n", "Tiempo Activo", "Brigadista Asignado" }));
		tableIncidentes.getColumnModel().getColumn(4).setPreferredWidth(87);
		tableIncidentes.getColumnModel().getColumn(5).setPreferredWidth(117);
		tableIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tableIncidentes.getTableHeader().setBackground(new Color(230, 230, 230)); // Cabecera gris claro
		spIncidentes.setViewportView(tableIncidentes);
        
        // Botones de acción
		JButton btnVerDetallesInciden = new JButton("Ver Detalles");
		btnVerDetallesInciden.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVerDetallesInciden.setBackground(new Color(190, 190, 190));
        btnVerDetallesInciden.setForeground(Color.BLACK);
		btnVerDetallesInciden.setBounds(20, 581, 150, 28);
		panelIncidentes.add(btnVerDetallesInciden);

		JButton btnMarcaResueltoIncide = new JButton("Marcar como Resuelto");
		btnMarcaResueltoIncide.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnMarcaResueltoIncide.setBackground(COLOR_ACCENTO);
        btnMarcaResueltoIncide.setForeground(Color.WHITE);
		btnMarcaResueltoIncide.setBounds(180, 581, 180, 28);
		panelIncidentes.add(btnMarcaResueltoIncide);

		// --- Panel Inventario --- 
		JPanel panelInventario = new JPanel();
        panelInventario.setBackground(COLOR_FONDO_CLARO);
		panelContenido.add(panelInventario, "Inventario");
		panelInventario.setLayout(null);

		JLabel lblTituloInventario = new JLabel("Gestión de Inventario y Suministros");
		lblTituloInventario.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblTituloInventario.setBounds(20, 11, 350, 25);
        lblTituloInventario.setForeground(COLOR_PRIMARIO);
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
        btnAñadirSuministro.setBackground(COLOR_PRIMARIO);
        btnAñadirSuministro.setForeground(Color.WHITE);
		btnAñadirSuministro.setBounds(520, 53, 120, 25);
		panelInventario.add(btnAñadirSuministro);

		JButton btnReporteSuministro = new JButton("Generar Reporte");
		btnReporteSuministro.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnReporteSuministro.setBackground(new Color(190, 190, 190));
        btnReporteSuministro.setForeground(Color.BLACK);
		btnReporteSuministro.setBounds(650, 53, 141, 25);
		panelInventario.add(btnReporteSuministro);

		JScrollPane spSuministros = new JScrollPane();
        spSuministros.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		spSuministros.setBounds(20, 89, 771, 480);
		panelInventario.add(spSuministros);

		tableSuministros = new JTable();
		tableSuministros
				.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Suministro", "Stock Actual",
						"Unidad (Kg/Uni)", "M\u00EDnimo Cr\u00EDtico", "Ubicaci\u00F3n", "Fecha de Caducidad" }));
		tableSuministros.getColumnModel().getColumn(3).setPreferredWidth(90);
		tableSuministros.getColumnModel().getColumn(6).setPreferredWidth(116);
		tableSuministros.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tableSuministros.getTableHeader().setBackground(new Color(230, 230, 230)); 
		spSuministros.setViewportView(tableSuministros);

		JButton btnEditStockSuminis = new JButton("Editar Stock");
		btnEditStockSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnEditStockSuminis.setBackground(COLOR_PRIMARIO);
        btnEditStockSuminis.setForeground(Color.WHITE);
		btnEditStockSuminis.setBounds(20, 580, 150, 29);
		panelInventario.add(btnEditStockSuminis);

		JButton btnMarcarCriticSuminis = new JButton("Marcar Crítico");
		btnMarcarCriticSuminis.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnMarcarCriticSuminis.setBackground(COLOR_ACCENTO);
        btnMarcarCriticSuminis.setForeground(Color.WHITE);
		btnMarcarCriticSuminis.setBounds(180, 580, 150, 29);
		panelInventario.add(btnMarcarCriticSuminis);

		// --- Panel Brigadistas --- 
		JPanel panelBrigadistas = new JPanel();
        panelBrigadistas.setBackground(COLOR_FONDO_CLARO);
		panelContenido.add(panelBrigadistas, "Brigadistas");
		panelBrigadistas.setLayout(null);

		JLabel lblTituloBrigadista = new JLabel("Gestión de Brigadistas y Personal ");
		lblTituloBrigadista.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblTituloBrigadista.setBounds(20, 11, 350, 25);
        lblTituloBrigadista.setForeground(COLOR_PRIMARIO);
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
        btnAddBrigadista.setBackground(COLOR_PRIMARIO);
        btnAddBrigadista.setForeground(Color.WHITE);
		btnAddBrigadista.setBounds(651, 51, 140, 25);
		panelBrigadistas.add(btnAddBrigadista);

		JScrollPane spBrigadistas = new JScrollPane();
        spBrigadistas.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		spBrigadistas.setBounds(20, 90, 455, 500);
		panelBrigadistas.add(spBrigadistas);

		tableBrigadistas = new JTable();
		tableBrigadistas.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Nombre", "Estado", "Especialidad", "Tel\u00E9fono" }));
		tableBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tableBrigadistas.getTableHeader().setBackground(new Color(230, 230, 230));
		spBrigadistas.setViewportView(tableBrigadistas);

		JPanel panelDetallesBrigadista = new JPanel(); 
        panelDetallesBrigadista.setBackground(Color.WHITE);
		panelDetallesBrigadista.setBounds(490, 90, 301, 500); // Ancho ajustado a 301
		panelBrigadistas.add(panelDetallesBrigadista);
		panelDetallesBrigadista.setLayout(null);
		panelDetallesBrigadista.setBorder(new TitledBorder(new LineBorder(COLOR_PRIMARIO, 1), "Detalles del Brigadista", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), COLOR_PRIMARIO)); 

		JLabel lblIdBrigadistas = new JLabel("ID: [ ID seleccionado ]");
		lblIdBrigadistas.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblIdBrigadistas.setBounds(10, 31, 270, 20);
		panelDetallesBrigadista.add(lblIdBrigadistas);

		JLabel lblNombreBrigadista = new JLabel("Nombre: [ Nombre ]");
		lblNombreBrigadista.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblNombreBrigadista.setBounds(10, 61, 270, 20);
		panelDetallesBrigadista.add(lblNombreBrigadista);

		JLabel lblEstadoActualBrig = new JLabel("Estado Actual:");
		lblEstadoActualBrig.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblEstadoActualBrig.setBounds(10, 101, 155, 27);
		panelDetallesBrigadista.add(lblEstadoActualBrig);

		JComboBox cbEditarEstadoBrig = new JComboBox();
		cbEditarEstadoBrig.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbEditarEstadoBrig.setModel(new DefaultComboBoxModel(new String[] { "Libre", "En Servicio", "Descanso" }));
		cbEditarEstadoBrig.setBounds(10, 130, 150, 27);
		panelDetallesBrigadista.add(cbEditarEstadoBrig);

		JLabel lblEspacialidadBrigadista = new JLabel("Especialidad: [ Tipo ]");
		lblEspacialidadBrigadista.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEspacialidadBrigadista.setBounds(10, 171, 155, 27);
		panelDetallesBrigadista.add(lblEspacialidadBrigadista);

		JButton btnGuardarCamEstadoBrig = new JButton("GUARDAR CAMBIOS DE ESTADO");
		btnGuardarCamEstadoBrig.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGuardarCamEstadoBrig.setBackground(COLOR_PRIMARIO);
        btnGuardarCamEstadoBrig.setForeground(Color.WHITE);
		btnGuardarCamEstadoBrig.setBounds(10, 451, 281, 38);
		panelDetallesBrigadista.add(btnGuardarCamEstadoBrig);

		// --- Panel Guías --- 
		JPanel panelGuiasPAuxilios = new JPanel();
        panelGuiasPAuxilios.setBackground(COLOR_FONDO_CLARO);
		panelContenido.add(panelGuiasPAuxilios, "GuiasPAuxilios");
		panelGuiasPAuxilios.setLayout(null);

		JLabel lblTituloPrimerosAux = new JLabel("Biblioteca de Guías y Protocolos de Auxilios");
		lblTituloPrimerosAux.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblTituloPrimerosAux.setBounds(20, 11, 400, 25);
        lblTituloPrimerosAux.setForeground(COLOR_PRIMARIO);
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
        spPrimerosAux.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		spPrimerosAux.setBounds(20, 91, 250, 500);
		panelGuiasPAuxilios.add(spPrimerosAux);

		JList listProtocolosPrimAux = new JList();
		listProtocolosPrimAux.setFont(new Font("SansSerif", Font.PLAIN, 12));
        listProtocolosPrimAux.setBorder(new EmptyBorder(5, 5, 5, 5));
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
        panelVisualGuiaPrimAux.setBackground(Color.WHITE);
		panelVisualGuiaPrimAux.setBounds(280, 91, 511, 500);
		panelGuiasPAuxilios.add(panelVisualGuiaPrimAux);
		panelVisualGuiaPrimAux.setLayout(null);

		JLabel lblTituloGuiaPrimAux = new JLabel("Título del Documento");
		lblTituloGuiaPrimAux.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTituloGuiaPrimAux.setForeground(COLOR_PRIMARIO);
		lblTituloGuiaPrimAux.setBounds(10, 11, 491, 30);
		panelVisualGuiaPrimAux.add(lblTituloGuiaPrimAux);

		JScrollPane spVisualGuiasPrimAux = new JScrollPane();
        spVisualGuiasPrimAux.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		spVisualGuiasPrimAux.setBounds(10, 50, 491, 439);
		panelVisualGuiaPrimAux.add(spVisualGuiasPrimAux);

		JEditorPane epVisualGuiasPrimAux = new JEditorPane();
		spVisualGuiasPrimAux.setViewportView(epVisualGuiasPrimAux);

		// --- Panel Reportes --- 
		JPanel panelReportes = new JPanel();
        panelReportes.setBackground(COLOR_FONDO_CLARO);
		panelContenido.add(panelReportes, "Reportes");
		panelReportes.setLayout(null);

		JLabel lblTituloReportes = new JLabel("Generación y Biblioteca de Reportes");
		lblTituloReportes.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblTituloReportes.setBounds(20, 11, 400, 25);
        lblTituloReportes.setForeground(COLOR_PRIMARIO);
		panelReportes.add(lblTituloReportes);

		JPanel panelPeriodoTipoReportes = new JPanel();
		panelPeriodoTipoReportes.setFont(new Font("SansSerif", Font.PLAIN, 12));
		panelPeriodoTipoReportes.setBorder(
				new TitledBorder(new LineBorder(COLOR_PRIMARIO, 1), "Definir Periodo y Tipo", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 12), COLOR_PRIMARIO));
        panelPeriodoTipoReportes.setBackground(Color.WHITE);
		panelPeriodoTipoReportes.setBounds(20, 50, 370, 200);
		panelReportes.add(panelPeriodoTipoReportes);
		panelPeriodoTipoReportes.setLayout(null);

		JLabel lblTipoReporte = new JLabel("Tipo de Reporte :");
		lblTipoReporte.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblTipoReporte.setBounds(20, 30, 150, 25);
		panelPeriodoTipoReportes.add(lblTipoReporte);
        
        JComboBox cbTipoReporte = new JComboBox();
		cbTipoReporte.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cbTipoReporte.setModel(new DefaultComboBoxModel(new String[] { "Incidente", "Inventario", "Brigadistas" }));
		cbTipoReporte.setBounds(130, 30, 200, 25);
		panelPeriodoTipoReportes.add(cbTipoReporte);
        
        JLabel lblFechaInicioReporte = new JLabel("Fecha Inicio :");
		lblFechaInicioReporte.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblFechaInicioReporte.setBounds(20, 70, 150, 25);
		panelPeriodoTipoReportes.add(lblFechaInicioReporte);
        
        try {
			MaskFormatter mfFecha = new MaskFormatter("##/##/####");
			tfFechaInicioReporte = new JFormattedTextField(mfFecha);
			tfFechaInicioReporte.setToolTipText("DD/MM/AAAA");
			tfFechaInicioReporte.setFont(new Font("SansSerif", Font.PLAIN, 12));
			tfFechaInicioReporte.setBounds(130, 70, 90, 25);
			panelPeriodoTipoReportes.add(tfFechaInicioReporte);
		} catch (java.text.ParseException e) {
			tfFechaInicioReporte = new JFormattedTextField();
			tfFechaInicioReporte.setBounds(130, 70, 90, 25);
			panelPeriodoTipoReportes.add(tfFechaInicioReporte);
		}
        
        JLabel lblFechaFinReporte = new JLabel("Fecha Fin :");
		lblFechaFinReporte.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblFechaFinReporte.setBounds(20, 110, 150, 25);
		panelPeriodoTipoReportes.add(lblFechaFinReporte);

        try {
			MaskFormatter mfFecha = new MaskFormatter("##/##/####");
			tfFechaFinReporte = new JFormattedTextField(mfFecha);
			tfFechaFinReporte.setToolTipText("DD/MM/AAAA");
			tfFechaFinReporte.setFont(new Font("SansSerif", Font.PLAIN, 12));
			tfFechaFinReporte.setBounds(130, 110, 90, 25);
			panelPeriodoTipoReportes.add(tfFechaFinReporte);
		} catch (java.text.ParseException e) {
			tfFechaFinReporte = new JFormattedTextField();
			tfFechaFinReporte.setBounds(130, 110, 90, 25);
			panelPeriodoTipoReportes.add(tfFechaFinReporte);
		}
        
        JButton btnGenerarReporte = new JButton("GENERAR REPORTE");
		btnGenerarReporte.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGenerarReporte.setBackground(COLOR_ACCENTO);
        btnGenerarReporte.setForeground(Color.WHITE);
		btnGenerarReporte.setBounds(20, 150, 330, 30);
		panelPeriodoTipoReportes.add(btnGenerarReporte);
        
        // Tabla de Reportes
        JLabel lblReportesPrevios = new JLabel("Reportes Previos Generados");
		lblReportesPrevios.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblReportesPrevios.setBounds(20, 270, 300, 25);
        lblReportesPrevios.setForeground(COLOR_PRIMARIO);
		panelReportes.add(lblReportesPrevios);
        
        JScrollPane spReportesResultPrevi = new JScrollPane();
        spReportesResultPrevi.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		spReportesResultPrevi.setBounds(20, 300, 771, 280);
		panelReportes.add(spReportesResultPrevi);

		tableReportesResultPrevi = new JTable();
		tableReportesResultPrevi.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Tipo", "Periodo", "Fecha Creación", "Generado Por", "Acción" }));
		tableReportesResultPrevi.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tableReportesResultPrevi.getTableHeader().setBackground(new Color(230, 230, 230));
		spReportesResultPrevi.setViewportView(tableReportesResultPrevi);
        
        JButton btnDescargarReporte = new JButton("Descargar");
		btnDescargarReporte.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnDescargarReporte.setBackground(COLOR_PRIMARIO);
        btnDescargarReporte.setForeground(Color.WHITE);
		btnDescargarReporte.setBounds(20, 591, 150, 25);
		panelReportes.add(btnDescargarReporte);


		// --- 6. Panel Footer (Estado y Hora) ---
		JPanel panelFooter = new JPanel();
		panelFooter.setBounds(0, 660, 1000, 40);
		panelFondo.add(panelFooter);
		panelFooter.setLayout(null);
        panelFooter.setBackground(new Color(230, 230, 230)); // Gris claro para el footer

		lblEstadoFooter = new JLabel("Estado: Conectado como " + rolActual);
		lblEstadoFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblEstadoFooter.setBounds(10, 10, 200, 20);
		panelFooter.add(lblEstadoFooter);

		lblVersionFooter = new JLabel("v1.0.0");
		lblVersionFooter.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersionFooter.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblVersionFooter.setBounds(900, 10, 90, 20);
		panelFooter.add(lblVersionFooter);

		JLabel lblFechaHora = new JLabel();
		lblFechaHora.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblFechaHora.setBounds(700, 10, 200, 20);
		lblFechaHora.setHorizontalAlignment(SwingConstants.RIGHT);
		panelFooter.add(lblFechaHora);

		// Timer para actualizar la hora
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblFechaHora.setText(dateFormat.format(new Date()));
			}
		});
		timer.start();
	}
}