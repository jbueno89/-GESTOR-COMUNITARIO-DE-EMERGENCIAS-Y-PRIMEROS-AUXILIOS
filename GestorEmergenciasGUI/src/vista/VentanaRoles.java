/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase de la **Vista** que permite al usuario seleccionar el rol
 * con el que desea ingresar al sistema (Coordinador, Brigadista o Usuario/Residente).
 * Actúa como un punto de navegación, delegando la apertura de la ventana principal
 * con permisos específicos.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class VentanaRoles extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPane;

    /**
     * Constructor de la ventana de selección de roles.
     * Configura la interfaz de usuario con botones para cada rol y botones de navegación.
     */
    public VentanaRoles() {

        setTitle("Seleccionar Rol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        setLocationRelativeTo(null); // Centrar la ventana

        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Título
        JLabel lblTitulo = new JLabel("Seleccione su Rol", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(25, 118, 210));
        lblTitulo.setBounds(50, 20, 330, 40);
        contentPane.add(lblTitulo);

        // Botón COORDINADOR
        JButton btnCoord = new JButton("COORDINADOR");
        btnCoord.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCoord.setBackground(new Color(46, 204, 113)); // Verde
        btnCoord.setForeground(Color.WHITE);
        btnCoord.setBounds(120, 90, 200, 40);
        // Delega la apertura del Panel Principal con el rol "Coordinador"
        btnCoord.addActionListener(e -> abrirPanelPrincipal("Coordinador"));
        contentPane.add(btnCoord);

        // Botón BRIGADISTA
        JButton btnBrigadista = new JButton("BRIGADISTA");
        btnBrigadista.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBrigadista.setBackground(new Color(52, 152, 219)); // Azul
        btnBrigadista.setForeground(Color.WHITE);
        btnBrigadista.setBounds(120, 150, 200, 40);
        // Delega la apertura del Panel Principal con el rol "Brigadista"
        btnBrigadista.addActionListener(e -> abrirPanelPrincipal("Brigadista"));
        contentPane.add(btnBrigadista);

        // Botón USUARIO / RESIDENTE
        JButton btnUsuario = new JButton("USUARIO / RESIDENTE");
        btnUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUsuario.setBackground(new Color(241, 196, 15)); // Amarillo/Naranja
        btnUsuario.setForeground(Color.WHITE);
        btnUsuario.setBounds(120, 210, 200, 40);
        // Delega la apertura del Panel Principal con el rol "Usuario"
        btnUsuario.addActionListener(e -> abrirPanelPrincipal("Usuario"));
        contentPane.add(btnUsuario);

        // BOTÓN REGRESAR A INICIO
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVolver.setBackground(new Color(231, 76, 60)); // Rojo
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBounds(10, 265, 100, 30);
        btnVolver.addActionListener(e -> volverAInicio());
        contentPane.add(btnVolver);
    }

    /**
     * Abre la ventana principal de la aplicación ({@link PanelPrincipal}), 
     * pasando el rol seleccionado como argumento para configurar permisos,
     * y cierra la ventana actual.
     * @param rol El rol seleccionado por el usuario ("Coordinador", "Brigadista", "Usuario").
     */
    private void abrirPanelPrincipal(String rol) {
        // Asumiendo la existencia de una clase PanelPrincipal
        PanelPrincipal ventana = new PanelPrincipal(rol);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        this.dispose();
    }

    /**
     * Abre la ventana de inicio ({@link VentanaInicio}) y cierra la ventana actual.
     */
    private void volverAInicio() {
        // Asumiendo la existencia de una clase VentanaInicio
        VentanaInicio inicio = new VentanaInicio();
        inicio.setLocationRelativeTo(null);
        inicio.setVisible(true);
        this.dispose();
    }
}