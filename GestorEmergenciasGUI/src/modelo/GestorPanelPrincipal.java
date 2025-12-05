/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package modelo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// IMPORTACIONES DE LA CAPA MODELO (Se requiere el archivo GestorContexto.java en el paquete modelo)
import modelo.*;

/**
 * Panel principal funcional con gestión completa de incidentes, brigadistas e inventario.
 * Demuestra **Arquitectura por Capas** conectando la Vista con la Lógica.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class GestorPanelPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // COLORES
    private final Color COLOR_PRIMARIO = new Color(26, 75, 140);
    private final Color COLOR_ACCENTO = new Color(217, 0, 29);
    private final Color COLOR_FONDO_MENU = new Color(50, 50, 50);
    private final Color COLOR_FONDO_CLARO = new Color(245, 245, 245);
    
    // GESTORES (Capa de Lógica)
    private GestorGeneral gestorGeneral;
    private GestorIncidentes gestorIncidentes;
    private GestorBrigadistas gestorBrigadistas;
    
    private String rolActual;
    private int mouseX, mouseY;
    private CardLayout cardLayout;
    
    // COMPONENTES DE LA INTERFAZ
    private JTable tableIncidentes;
    private JTable tableSuministros;
    private JTable tableBrigadistas;
    private DefaultTableModel modeloIncidentes;
    private DefaultTableModel modeloSuministros;
    private DefaultTableModel modeloBrigadistas;
    
    private JLabel lblTitulo;
    private JLabel lblNumIncidentes;
    private JLabel lblNumBrigadistas;
    private JLabel lblNumStock;
    
    private JButton btnActivo;

    /**
     * Constructor principal con rol del usuario.
     * @param rol Rol del usuario logueado
     */
    public GestorPanelPrincipal(String rol) {
        this.rolActual = rol;
        
        // INICIALIZAR GESTORES (Conexión con la Capa de Lógica)
        // Se asume que GestorGeneral está implementado en el paquete 'modelo'
        this.gestorGeneral = new GestorGeneral();
        this.gestorIncidentes = gestorGeneral.getGestorIncidentes();
        this.gestorBrigadistas = gestorGeneral.getGestorBrigadistas();
        
        inicializarComponentes();
        cargarDatosIniciales(); // Carga datos en las tablas
        
        // Registrar acción en bitácora
        gestorGeneral.registrarBitacora(rolActual, "Usuario ingresó al sistema");
    }

    /**
     * Inicializa todos los componentes de la interfaz.
     */
    private void inicializarComponentes() {
        // Configuración básica
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panelFondo = new JPanel();
        panelFondo.setBounds(0, 0, 1000, 700);
        panelFondo.setLayout(null);
        panelFondo.setBackground(COLOR_FONDO_CLARO);
        contentPane.add(panelFondo);

        // BARRA SUPERIOR
        crearBarraSuperior(panelFondo);
        
        // MENÚ LATERAL
        crearMenuLateral(panelFondo);
        
        // PANEL DE CONTENIDO CON CARDLAYOUT
        JPanel panelContenido = new JPanel();
        panelContenido.setBounds(200, 40, 800, 620);
        panelContenido.setBackground(COLOR_FONDO_CLARO);
        cardLayout = new CardLayout(0, 0);
        panelContenido.setLayout(cardLayout);
        panelFondo.add(panelContenido);

        // CREAR TODOS LOS PANELES
        crearPanelDashboard(panelContenido);
        crearPanelIncidentes(panelContenido);
        crearPanelInventario(panelContenido);
        crearPanelBrigadistas(panelContenido);
        crearPanelReportes(panelContenido);

        // FOOTER
        crearFooter(panelFondo);
    }

    /**
     * Crea la barra superior con título y controles.
     */
    private void crearBarraSuperior(JPanel panelFondo) {
        JPanel panelBarra = new JPanel();
        panelBarra.setBounds(0, 0, 1000, 40);
        panelBarra.setLayout(null);
        panelBarra.setBackground(COLOR_FONDO_MENU);
        panelFondo.add(panelBarra);

        // Título
        lblTitulo = new JLabel("GESTOR COMUNITARIO - " + rolActual.toUpperCase());
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBounds(50, 9, 450, 23);
        panelBarra.add(lblTitulo);

        // Botón Minimizar
        JButton btnMinimizar = new JButton("-");
        btnMinimizar.setBounds(885, 0, 50, 40);
        btnMinimizar.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnMinimizar.setForeground(Color.WHITE);
        btnMinimizar.setBackground(COLOR_FONDO_MENU);
        btnMinimizar.setBorderPainted(false);
        btnMinimizar.addActionListener(e -> setState(JFrame.ICONIFIED));
        panelBarra.add(btnMinimizar);

        // Botón Cerrar
        JButton btnCerrar = new JButton("×");
        btnCerrar.setBounds(937, 0, 53, 40);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(COLOR_ACCENTO);
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(e -> System.exit(0));
        panelBarra.add(btnCerrar);

        // Arrastre de ventana
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
    }

    /**
     * Crea el menú lateral de navegación.
     */
    private void crearMenuLateral(JPanel panelFondo) {
        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(0, 40, 200, 620);
        panelBotones.setBackground(COLOR_FONDO_MENU);
        panelBotones.setLayout(null);
        panelFondo.add(panelBotones);

        // Botones del menú
        JButton btnDashboard = crearBotonMenu("  📊 Tablero", 21);
        btnDashboard.addActionListener(e -> {
            cardLayout.show(contentPane.getComponent(0).getParent(), "Dashboard");
            activarBoton(btnDashboard);
            actualizarDashboard();
        });
        panelBotones.add(btnDashboard);
        btnActivo = btnDashboard;
        btnActivo.setBackground(COLOR_PRIMARIO);

        JButton btnIncidentes = crearBotonMenu("  🚨 Incidentes", 75);
        btnIncidentes.addActionListener(e -> {
            cardLayout.show(contentPane.getComponent(0).getParent(), "Incidentes");
            activarBoton(btnIncidentes);
            cargarIncidentes();
        });
        panelBotones.add(btnIncidentes);

        JButton btnInventario = crearBotonMenu("  📦 Inventario", 129);
        btnInventario.addActionListener(e -> {
            cardLayout.show(contentPane.getComponent(0).getParent(), "Inventario");
            activarBoton(btnInventario);
            cargarInventario();
        });
        panelBotones.add(btnInventario);

        JButton btnBrigadistas = crearBotonMenu("  👥 Brigadistas", 183);
        btnBrigadistas.addActionListener(e -> {
            cardLayout.show(contentPane.getComponent(0).getParent(), "Brigadistas");
            activarBoton(btnBrigadistas);
            cargarBrigadistas();
        });
        panelBotones.add(btnBrigadistas);

        JButton btnReportes = crearBotonMenu("  📋 Reportes", 237);
        btnReportes.addActionListener(e -> {
            cardLayout.show(contentPane.getComponent(0).getParent(), "Reportes");
            activarBoton(btnReportes);
        });
        panelBotones.add(btnReportes);

        // Botón Cerrar Sesión
        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCerrarSesion.setBounds(10, 560, 180, 45);
        btnCerrarSesion.setBackground(COLOR_ACCENTO);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.addActionListener(e -> {
            gestorGeneral.registrarBitacora(rolActual, "Usuario cerró sesión");
            dispose();
        });
        panelBotones.add(btnCerrarSesion);
    }

    /**
     * Crea un botón del menú con estilo consistente.
     */
    private JButton crearBotonMenu(String texto, int y) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(COLOR_FONDO_MENU);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBounds(0, y, 200, 45);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARIO);
            }
            public void mouseExited(MouseEvent e) {
                if (btn != btnActivo) {
                    btn.setBackground(COLOR_FONDO_MENU);
                }
            }
        });
        
        return btn;
    }

    /**
     * Activa un botón del menú visualmente.
     */
    private void activarBoton(JButton nuevoBoton) {
        if (btnActivo != null && btnActivo != nuevoBoton) {
            btnActivo.setBackground(COLOR_FONDO_MENU);
        }
        btnActivo = nuevoBoton;
        btnActivo.setBackground(COLOR_PRIMARIO);
    }

    /**
     * Crea el panel Dashboard con estadísticas.
     */
    private void crearPanelDashboard(JPanel panelContenido) {
        JPanel panelDashboard = new JPanel();
        panelDashboard.setBackground(COLOR_FONDO_CLARO);
        panelDashboard.setLayout(null);
        panelContenido.add(panelDashboard, "Dashboard");

        JLabel lblTituloDash = new JLabel("Resumen de Actividad");
        lblTituloDash.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTituloDash.setBounds(20, 20, 300, 25);
        panelDashboard.add(lblTituloDash);

        // Paneles de estadísticas
        JPanel panelIncidentes = crearPanelEstadistica("Incidentes Abiertos", 20, 70);
        // El primer componente del panel de estadística es el JLabel del número
        lblNumIncidentes = (JLabel) panelIncidentes.getComponent(0); 
        panelDashboard.add(panelIncidentes);

        JPanel panelBrigadistas = crearPanelEstadistica("Brigadistas Libres", 280, 70);
        lblNumBrigadistas = (JLabel) panelBrigadistas.getComponent(0);
        panelDashboard.add(panelBrigadistas);

        JPanel panelStock = crearPanelEstadistica("Stock Crítico", 540, 70);
        lblNumStock = (JLabel) panelStock.getComponent(0);
        panelDashboard.add(panelStock);
        
        // Sección para la bitácora o incidentes recientes
        JLabel lblIncidentesRecientes = new JLabel("Última Bitácora");
        lblIncidentesRecientes.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblIncidentesRecientes.setBounds(20, 200, 300, 25);
        panelDashboard.add(lblIncidentesRecientes);
        
        JTextArea areaBitacora = new JTextArea(gestorGeneral.generarReporteBitacora());
        areaBitacora.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaBitacora.setEditable(false);
        
        JScrollPane scrollBitacora = new JScrollPane(areaBitacora);
        scrollBitacora.setBounds(20, 240, 760, 350);
        panelDashboard.add(scrollBitacora);
    }

    /**
     * Crea un panel de estadística individual.
     */
    private JPanel crearPanelEstadistica(String titulo, int x, int y) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, 250, 100);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setLayout(null);

        JLabel lblNumero = new JLabel("0");
        lblNumero.setFont(new Font("SansSerif", Font.BOLD, 30));
        lblNumero.setForeground(COLOR_PRIMARIO);
        lblNumero.setBounds(20, 21, 100, 36);
        panel.add(lblNumero);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTitulo.setBounds(20, 60, 200, 16);
        panel.add(lblTitulo);

        return panel;
    }

    /**
     * Crea el panel de gestión de incidentes.
     */
    private void crearPanelIncidentes(JPanel panelContenido) {
        JPanel panelIncidentes = new JPanel();
        panelIncidentes.setBackground(COLOR_FONDO_CLARO);
        panelIncidentes.setLayout(null);
        panelContenido.add(panelIncidentes, "Incidentes");

        JLabel lblTitulo = new JLabel("Gestión de Incidentes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setBounds(20, 11, 300, 25);
        panelIncidentes.add(lblTitulo);

        // Botón Crear Incidente
        JButton btnCrear = new JButton("+ Crear Incidente");
        btnCrear.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCrear.setBackground(COLOR_PRIMARIO);
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setBounds(620, 50, 171, 25);
        btnCrear.addActionListener(e -> mostrarDialogoCrearIncidente());
        panelIncidentes.add(btnCrear);

        // Tabla de incidentes
        String[] columnas = {"ID", "Tipo", "Prioridad", "Ubicación", "Estado", "Brigadista"};
        modeloIncidentes = new DefaultTableModel(columnas, 0);
        tableIncidentes = new JTable(modeloIncidentes);
        tableIncidentes.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        JScrollPane scroll = new JScrollPane(tableIncidentes);
        scroll.setBounds(20, 90, 771, 450);
        panelIncidentes.add(scroll);

        // Botones de acción
        JButton btnResolver = new JButton("Resolver");
        btnResolver.setBounds(20, 560, 150, 28);
        btnResolver.setBackground(COLOR_ACCENTO);
        btnResolver.setForeground(Color.WHITE);
        btnResolver.addActionListener(e -> resolverIncidenteSeleccionado());
        panelIncidentes.add(btnResolver);
    }

    /**
     * Muestra diálogo para crear un nuevo incidente.
     */
    private void mostrarDialogoCrearIncidente() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        JTextField tfTipo = new JTextField();
        JTextField tfDescripcion = new JTextField();
        JTextField tfUbicacion = new JTextField();
        JComboBox<String> cbPrioridad = new JComboBox<>(new String[]{"Alta", "Media", "Baja"});
        
        panel.add(new JLabel("Tipo de Emergencia:"));
        panel.add(tfTipo);
        panel.add(new JLabel("Descripción:"));
        panel.add(tfDescripcion);
        panel.add(new JLabel("Ubicación:"));
        panel.add(tfUbicacion);
        panel.add(new JLabel("Prioridad:"));
        panel.add(cbPrioridad);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Crear Incidente", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                Incidente nuevo = gestorIncidentes.crearIncidente(
                    tfTipo.getText(),
                    tfDescripcion.getText(),
                    tfUbicacion.getText(),
                    (String) cbPrioridad.getSelectedItem()
                );
                
                gestorGeneral.registrarBitacora(rolActual, 
                    "Incidente creado: " + nuevo.getTipo());
                
                JOptionPane.showMessageDialog(this, 
                    "✅ Incidente #" + nuevo.getId() + " creado exitosamente");
                
                cargarIncidentes();
                actualizarDashboard();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Resuelve el incidente seleccionado.
     */
    private void resolverIncidenteSeleccionado() {
        int fila = tableIncidentes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un incidente primero");
            return;
        }
        
        // Asumiendo que la columna 0 es el ID del incidente
        int id = (int) tableIncidentes.getValueAt(fila, 0); 
        
        try {
            gestorIncidentes.cambiarEstado(id, "Resuelto");
            gestorGeneral.registrarBitacora(rolActual, "Incidente #" + id + " resuelto");
            JOptionPane.showMessageDialog(this, "✅ Incidente resuelto");
            cargarIncidentes();
            actualizarDashboard();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    /**
     * Crea el panel de gestión de inventario.
     */
    private void crearPanelInventario(JPanel panelContenido) {
        JPanel panelInventario = new JPanel();
        panelInventario.setBackground(COLOR_FONDO_CLARO);
        panelInventario.setLayout(null);
        panelContenido.add(panelInventario, "Inventario");

        JLabel lblTitulo = new JLabel("Gestión de Inventario");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setBounds(20, 11, 300, 25);
        panelInventario.add(lblTitulo);

        // Tabla de suministros
        String[] columnas = {"ID", "Suministro", "Stock", "Unidad", "Mínimo", "Ubicación"};
        modeloSuministros = new DefaultTableModel(columnas, 0);
        tableSuministros = new JTable(modeloSuministros);
        
        JScrollPane scroll = new JScrollPane(tableSuministros);
        scroll.setBounds(20, 89, 771, 480);
        panelInventario.add(scroll);

        // Botón Agregar Stock
        JButton btnAgregar = new JButton("+ Agregar Stock");
        btnAgregar.setBounds(20, 580, 150, 29);
        btnAgregar.setBackground(COLOR_PRIMARIO);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addActionListener(e -> agregarStockSeleccionado());
        panelInventario.add(btnAgregar);
    }

    /**
     * Agrega stock al suministro seleccionado.
     */
    private void agregarStockSeleccionado() {
        int fila = tableSuministros.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un suministro");
            return;
        }
        
        int id = (int) tableSuministros.getValueAt(fila, 0);
        String cantidadStr = JOptionPane.showInputDialog(this, "Cantidad a agregar:");
        
        if (cantidadStr != null && !cantidadStr.isEmpty()) {
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                gestorGeneral.registrarEntrada(id, cantidad);
                JOptionPane.showMessageDialog(this, "✅ Stock actualizado");
                cargarInventario();
                actualizarDashboard();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    /**
     * Crea el panel de gestión de brigadistas.
     */
    private void crearPanelBrigadistas(JPanel panelContenido) {
        JPanel panelBrigadistas = new JPanel();
        panelBrigadistas.setBackground(COLOR_FONDO_CLARO);
        panelBrigadistas.setLayout(null);
        panelContenido.add(panelBrigadistas, "Brigadistas");

        JLabel lblTitulo = new JLabel("Gestión de Brigadistas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setBounds(20, 11, 300, 25);
        panelBrigadistas.add(lblTitulo);

        // Tabla de brigadistas
        String[] columnas = {"ID", "Nombre", "Teléfono", "Especialidad", "Estado"};
        modeloBrigadistas = new DefaultTableModel(columnas, 0);
        tableBrigadistas = new JTable(modeloBrigadistas);
        
        JScrollPane scroll = new JScrollPane(tableBrigadistas);
        scroll.setBounds(20, 90, 771, 500);
        panelBrigadistas.add(scroll);
    }

    /**
     * Crea el panel de reportes.
     */
    private void crearPanelReportes(JPanel panelContenido) {
        JPanel panelReportes = new JPanel();
        panelReportes.setBackground(COLOR_FONDO_CLARO);
        panelReportes.setLayout(null);
        panelContenido.add(panelReportes, "Reportes");

        JLabel lblTitulo = new JLabel("Generación de Reportes");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(COLOR_PRIMARIO);
        lblTitulo.setBounds(20, 11, 300, 25);
        panelReportes.add(lblTitulo);

        JButton btnGenerar = new JButton("GENERAR REPORTE COMPLETO");
        btnGenerar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGenerar.setBackground(COLOR_ACCENTO);
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setBounds(20, 60, 300, 40);
        btnGenerar.addActionListener(e -> mostrarReporteCompleto());
        panelReportes.add(btnGenerar);

        JTextArea areaReporte = new JTextArea(gestorGeneral.generarReporteCompleto());
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaReporte.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(areaReporte);
        scroll.setBounds(20, 120, 760, 480);
        panelReportes.add(scroll);
    }

    /**
     * Muestra el reporte completo del sistema.
     */
    private void mostrarReporteCompleto() {
        String reporte = gestorGeneral.generarReporteCompleto();
        
        JTextArea area = new JTextArea(reporte);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scroll, "Reporte del Sistema", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Crea el footer con información del sistema.
     */
    private void crearFooter(JPanel panelFondo) {
        JPanel panelFooter = new JPanel();
        panelFooter.setBounds(0, 660, 1000, 40);
        panelFooter.setLayout(null);
        panelFooter.setBackground(new Color(230, 230, 230));
        panelFondo.add(panelFooter);

        JLabel lblEstado = new JLabel("Estado: Conectado como " + rolActual);
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEstado.setBounds(10, 10, 300, 20);
        panelFooter.add(lblEstado);

        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
        lblVersion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblVersion.setBounds(900, 10, 90, 20);
        panelFooter.add(lblVersion);
    }

    // =========================================================================
    // MÉTODOS DE CARGA DE DATOS
    // =========================================================================

    /**
     * Carga todos los datos iniciales.
     */
    private void cargarDatosIniciales() {
        actualizarDashboard();
        cargarIncidentes();
        cargarInventario();
        cargarBrigadistas();
    }

    /**
     * Actualiza las estadísticas del dashboard.
     */
    private void actualizarDashboard() {
        lblNumIncidentes.setText(String.valueOf(gestorIncidentes.contarIncidentesActivos()));
        
        int[] estadosBrig = gestorBrigadistas.contarPorEstado();
        // Asumiendo que la posición 0 es brigadistas libres
        lblNumBrigadistas.setText(String.valueOf(estadosBrig[0])); 
        
        lblNumStock.setText(String.valueOf(gestorGeneral.obtenerSuministrosCriticos().size()));
    }

    /**
     * Carga los incidentes en la tabla.
     */
    private void cargarIncidentes() {
        modeloIncidentes.setRowCount(0);
        List<Incidente> incidentes = gestorIncidentes.obtenerIncidentesActivos();
        
        for (Incidente inc : incidentes) {
            modeloIncidentes.addRow(new Object[]{
                inc.getId(),
                inc.getTipo(),
                inc.getPrioridad(),
                inc.getUbicacion(),
                inc.getEstado(),
                inc.getNombreBrigadista()
            });
        }
    }

    /**
     * Carga el inventario en la tabla.
     */
    private void cargarInventario() {
        modeloSuministros.setRowCount(0);
        List<Suministro> suministros = gestorGeneral.obtenerTodosSuministros();
        
        for (Suministro s : suministros) {
            modeloSuministros.addRow(new Object[]{
                s.getId(),
                s.getNombre(),
                s.getStockActual(),
                s.getUnidad(),
                s.getStockMinimo(),
                s.getUbicacion()
            });
        }
        
        // Mostrar alerta si hay stock crítico (parte completada)
        String alerta = gestorGeneral.generarAlertaStock();
        if (alerta != null && !alerta.isEmpty()) {
            // Se muestra la alerta en la pestaña de Inventario al cargar
            // Nota: Aquí se podría mostrar un JLabel de advertencia en el panel.
            System.out.println("ALERTA DE STOCK CRÍTICO: " + alerta);
        }
    }
    
    /**
     * Carga los brigadistas en la tabla.
     */
    private void cargarBrigadistas() {
        modeloBrigadistas.setRowCount(0);
        List<Brigadista> brigadistas = gestorBrigadistas.obtenerTodos();
        
        for (Brigadista b : brigadistas) {
            modeloBrigadistas.addRow(new Object[]{
                b.getId(),
                b.getNombre(),
                b.getTelefono(),
                b.getEspecialidad(),
                b.getEstado()
            });
        }
    }
    
    /**
     * Método principal para ejecutar la interfaz.
     * @param args Argumentos de la línea de comandos (no usados)
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Simulamos un usuario con rol "Admin"
                GestorPanelPrincipal frame = new GestorPanelPrincipal("Administrador");
                frame.setLocationRelativeTo(null); // Centrar
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}