package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaRoles extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public VentanaRoles() {

        setTitle("Seleccionar Rol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 350);
        setLocationRelativeTo(null); // Centrar la ventana

        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Seleccione su Rol", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(25, 118, 210));
        lblTitulo.setBounds(50, 20, 330, 40);
        contentPane.add(lblTitulo);

        JButton btnCoord = new JButton("COORDINADOR");
        btnCoord.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCoord.setBackground(new Color(46, 204, 113));
        btnCoord.setForeground(Color.WHITE);
        btnCoord.setBounds(120, 90, 200, 40);
        btnCoord.addActionListener(e -> abrirPanelPrincipal("Coordinador"));
        contentPane.add(btnCoord);

        JButton btnBrigadista = new JButton("BRIGADISTA");
        btnBrigadista.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBrigadista.setBackground(new Color(52, 152, 219));
        btnBrigadista.setForeground(Color.WHITE);
        btnBrigadista.setBounds(120, 150, 200, 40);
        btnBrigadista.addActionListener(e -> abrirPanelPrincipal("Brigadista"));
        contentPane.add(btnBrigadista);

        JButton btnUsuario = new JButton("USUARIO / RESIDENTE");
        btnUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnUsuario.setBackground(new Color(241, 196, 15));
        btnUsuario.setForeground(Color.WHITE);
        btnUsuario.setBounds(120, 210, 200, 40);
        btnUsuario.addActionListener(e -> abrirPanelPrincipal("Usuario"));
        contentPane.add(btnUsuario);

        // ----------------------------
        // BOTÓN REGRESAR A INICIO
        // ----------------------------
        JButton btnVolver = new JButton("VOLVER");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnVolver.setBackground(new Color(231, 76, 60));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBounds(10, 265, 100, 30);
        btnVolver.addActionListener(e -> volverAInicio());
        contentPane.add(btnVolver);
    }

    private void abrirPanelPrincipal(String rol) {
        PanelPrincipal ventana = new PanelPrincipal(rol);
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        dispose();
    }

    private void volverAInicio() {
        VentanaInicio inicio = new VentanaInicio();
        inicio.setLocationRelativeTo(null);
        inicio.setVisible(true);
        dispose();
    }
}

