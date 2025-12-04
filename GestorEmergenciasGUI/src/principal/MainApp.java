/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package principal;

import javax.swing.SwingUtilities;
// Importamos la ventana de inicio que debe ser la primera en mostrarse
import vista.VentanaInicio;
// Nota: GestorUsuarios y LoginView se inicializan desde VentanaInicio.

/**
 * Clase principal que contiene el método main para iniciar la aplicación.
 * El punto de entrada es la **VentanaInicio** (Splash Screen), orquestando
 * el **Run del Programa** en el Event Dispatch Thread (EDT).
 * 
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class MainApp {

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     * Esta clase solo debe ser usada a través de su método main estático.
     */
    private MainApp() {
        // Constructor privado para clase utilitaria
    }

    /**
     * El método principal de ejecución de la aplicación.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Aseguramos que todas las operaciones de la interfaz gráfica se ejecuten
        // en el Event Dispatch Thread (EDT) de Swing.
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * El método run donde se inicializan los componentes de la aplicación.
             */
            public void run() {
                try {
                    // -------------------------------------------------------------
                    // 1. INICIALIZACIÓN DE LA VISTA (Punto de entrada: VentanaInicio)
                    // Iniciamos la VentanaInicio, la cual contendrá la lógica para
                    // iniciar el GestorUsuarios y pasar al LoginView.
                    // -------------------------------------------------------------
                    VentanaInicio inicio = new VentanaInicio();
                    inicio.setLocationRelativeTo(null); // Centra la ventana
                    inicio.setVisible(true);

                } catch (Exception e) {
                    System.err.println("Error FATAL al iniciar la aplicación.");
                    e.printStackTrace();
                }
            }
        });
    }
}