package principal;

import java.awt.EventQueue;
// Importamos la Ventana de Inicio que queremos mostrar primero
import vista.VentanaInicio; 
// Mantenemos estas importaciones por si se necesitan más adelante, 
// aunque inicialmente solo usaremos VentanaInicio

public class MainApp {
    public static void main(String[] args) {
        // En lugar de inicializar GestorUsuarios y LoginView aquí,
        // simplemente lanzamos la VentanaInicio.
        
       
        // El GestorUsuarios se inicializará y se pasará al LoginView
        // *después* de que el usuario haga clic en "INICIAR" 
        // o si usas la VentanaInicio como una simple splash screen.
        
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // ** 1. Creamos y mostramos la Ventana de Inicio (Splash Screen) **
                    VentanaInicio frame = new VentanaInicio(); 
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    
                    // Nota: Si VentanaInicio requiere el GestorUsuarios para la 
                    // lógica de inicio posterior, deberías inicializar GestorUsuarios 
                    // y pasarlo a VentanaInicio aquí.

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}