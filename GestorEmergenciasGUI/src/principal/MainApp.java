package principal;

import java.awt.EventQueue;
import modelo.GestorUsuarios;
import vista.LoginView;

public class MainApp {
    public static void main(String[] args) {
        // 1. Inicializar el GestorUsuarios (Carga los datos guardados)
        GestorUsuarios gestor = new GestorUsuarios();
        
        // 2. Iniciar la interfaz de Login, pasándole el gestor
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // El constructor de LoginView debe recibir el gestor
                    LoginView frame = new LoginView(gestor); 
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}