package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    // Nombre del archivo de persistencia
    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private List<Usuario> listaUsuarios;

    public GestorUsuarios() {
        // Carga los usuarios guardados o crea la lista si es la primera vez
        listaUsuarios = cargarUsuarios();
        
        // Inicializa usuarios por defecto si la lista está vacía.
        // Esto permite probar la aplicación de inmediato.
        if (listaUsuarios.isEmpty()) {
            inicializarUsuariosPorDefecto();
        }
    }

    /**
     * Registra un nuevo usuario en la lista y guarda la lista.
     * @param nuevoUsuario El objeto Usuario a registrar.
     * @throws Exception Si el nombre de usuario ya existe.
     */
    public void registrarUsuario(Usuario nuevoUsuario) throws Exception {
        // Validación: Verifica si el nombre de usuario ya existe (insensible a mayús/minús para la búsqueda, aunque se almacena el original)
        String nuevoUsuarioLower = nuevoUsuario.getNombreUsuario().toLowerCase();
        
        if (listaUsuarios.stream().anyMatch(u -> u.getNombreUsuario().toLowerCase().equals(nuevoUsuarioLower))) {
            throw new Exception("El nombre de usuario '" + nuevoUsuario.getNombreUsuario() + "' ya existe. Por favor, elige otro.");
        }
        
        listaUsuarios.add(nuevoUsuario);
        guardarUsuarios(); // Guarda la lista actualizada
    }

    /**
     * Busca y valida las credenciales de un usuario.
     * @param usuario Nombre de usuario ingresado.
     * @param contrasena Contraseña ingresada.
     * @return El objeto Usuario si las credenciales son correctas, o null en caso contrario.
     */
    public Usuario validarCredenciales(String usuario, String contrasena) {
        // Busca un usuario que coincida con el nombre y la contraseña
        // Es mejor iterar para evitar conversiones innecesarias en el stream si la lista es grande.
        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equals(usuario) && u.getContrasena().equals(contrasena)) {
                return u; // Retorna el objeto Usuario si es correcto
            }
        }
        return null; // Retorna null si no se encuentra
    }
    
    /**
     * Inicializa un conjunto de usuarios para pruebas si la lista está vacía.
     */
    private void inicializarUsuariosPorDefecto() {
        System.out.println("No se encontró archivo de usuarios. Creando usuarios por defecto...");
        
        // Usuario 1: Coordinador (Para acceder a todas las funcionalidades)
        listaUsuarios.add(new Usuario("Admin Coordinador", "admin", "12345", "Coordinador"));
        
        // Usuario 2: Brigadista (Para acceder a la gestión de emergencias en campo)
        listaUsuarios.add(new Usuario("Brigadista de Prueba", "brigada", "12345", "Brigadista"));
        
        // Usuario 3: Residente (Para acceder a la interfaz de reportes)
        listaUsuarios.add(new Usuario("Residente Comunitario", "residente", "12345", "Residente"));
        
        guardarUsuarios(); // Guarda los usuarios iniciales
        System.out.println("Usuarios por defecto cargados. Contraseña general: 12345.");
    }

    /**
     * Guarda la lista de usuarios en un archivo (Serialización).
     */
    private void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(listaUsuarios);
            // Opcional: System.out.println("Usuarios guardados correctamente.");
        } catch (IOException e) {
            // Manejo más explícito del error de E/S
            System.err.println("CRÍTICO: No se pudo guardar el archivo de usuarios. Causa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de usuarios desde un archivo (Deserialización).
     * @return La lista de usuarios cargada o una lista vacía si falla o no existe el archivo.
     */
    private List<Usuario> cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>(); // Si no existe o está vacío, devuelve una lista nueva
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            // El casting es seguro ya que siempre guardamos una List<Usuario>
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                 // Usa el cast suprimido de forma segura
                @SuppressWarnings("unchecked")
                List<Usuario> loadedList = (List<Usuario>) obj;
                return loadedList;
            } else {
                 throw new IOException("El contenido del archivo no es una lista válida.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ADVERTENCIA: Error al cargar el archivo de usuarios. Se iniciará sin datos guardados. Causa: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}