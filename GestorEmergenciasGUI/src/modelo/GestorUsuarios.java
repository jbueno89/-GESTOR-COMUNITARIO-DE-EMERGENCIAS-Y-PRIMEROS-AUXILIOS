package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private List<Usuario> listaUsuarios;

    public GestorUsuarios() {
        // Carga los usuarios guardados al iniciar la aplicación
        listaUsuarios = cargarUsuarios();
    }

    public void registrarUsuario(Usuario nuevoUsuario) throws Exception {
        // Validación: Verifica si el nombre de usuario ya existe
        if (listaUsuarios.stream().anyMatch(u -> u.getNombreUsuario().equals(nuevoUsuario.getNombreUsuario()))) {
            throw new Exception("El nombre de usuario '" + nuevoUsuario.getNombreUsuario() + "' ya existe.");
        }
        
        listaUsuarios.add(nuevoUsuario);
        guardarUsuarios(); // Guarda la lista actualizada
    }

    public Usuario validarCredenciales(String usuario, String contrasena) {
        // Busca un usuario que coincida con el nombre y la contraseña
        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equals(usuario) && u.getContrasena().equals(contrasena)) {
                return u; // Retorna el objeto Usuario si es correcto
            }
        }
        return null; // Retorna null si no se encuentra
    }
    
    // Método privado para guardar la lista en un archivo (Serialización)
    private void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(listaUsuarios);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    // Método privado para cargar la lista desde un archivo (Deserialización)
    private List<Usuario> cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>(); // Si no existe o está vacío, devuelve una lista nueva
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            // El casting es seguro ya que siempre guardamos una List<Usuario>
            return (List<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuarios. Creando nueva lista: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}