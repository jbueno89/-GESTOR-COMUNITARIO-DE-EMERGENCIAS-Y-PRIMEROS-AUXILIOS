/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la colección de objetos Usuario, incluyendo su
 * persistencia (guardado y carga) mediante serialización.
 * Forma parte de la **Capa de Lógica (Modelo)** y demuestra el **Manejo de Archivos**
 * y el **Manejo de Errores** (IOException, Exception).
 * 
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class GestorUsuarios implements Serializable {

    // Se recomienda siempre incluir un serialVersionUID para clases serializables.
    private static final long serialVersionUID = 1L; 
    
    // Nombre del archivo de persistencia
    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    
    /**
     * Lista donde se almacenan los objetos Usuario. Demuestra Manejo de Objetos (List).
     */
    private List<Usuario> listaUsuarios;

    /**
     * Constructor de la clase GestorUsuarios.
     * Inicializa la lista de usuarios cargando datos si existen,
     * o creando usuarios por defecto si es la primera ejecución.
     */
    public GestorUsuarios() {
        // Carga los usuarios guardados o inicializa la lista vacía
        this.listaUsuarios = cargarUsuarios();
        
        // Inicializa usuarios por defecto si la lista está vacía.
        if (listaUsuarios.isEmpty()) {
            inicializarUsuariosPorDefecto();
        }
    }

    /**
     * Registra un nuevo objeto Usuario en la lista y persiste la colección completa.
     * Implementa la validación para asegurar la unicidad del nombre de usuario.
     * 
     * @param nuevoUsuario El objeto Usuario a registrar.
     * @throws Exception Si el nombre de usuario ya existe.
     */
    public void registrarUsuario(Usuario nuevoUsuario) throws Exception {
        // Uso de stream para una búsqueda eficiente e insensible a mayúsculas/minúsculas.
        String nuevoUsuarioLower = nuevoUsuario.getNombreUsuario().toLowerCase();
        
        // Manejo de Errores: Verifica unicidad del nombre de usuario
        if (listaUsuarios.stream().anyMatch(u -> u.getNombreUsuario().toLowerCase().equals(nuevoUsuarioLower))) {
            throw new Exception("El nombre de usuario '" + nuevoUsuario.getNombreUsuario() + "' ya existe. Por favor, elige otro.");
        }
        
        // Creación de Objetos: El objeto 'nuevoUsuario' se añade a la colección.
        listaUsuarios.add(nuevoUsuario);
        guardarUsuarios(); // Persiste la lista actualizada (Manejo de Archivos)
    }

    /**
     * Busca un usuario y valida las credenciales para el inicio de sesión.
     * 
     * @param usuario Nombre de usuario ingresado.
     * @param contrasena Contraseña ingresada.
     * @return El objeto Usuario autenticado si las credenciales son correctas; de lo contrario, retorna null.
     */
    public Usuario validarCredenciales(String usuario, String contrasena) {
        // Itera sobre la lista para buscar la coincidencia exacta de nombre y contraseña.
        for (Usuario u : listaUsuarios) {
            // Se valida que coincida exactamente con lo almacenado (Encapsulamiento vía getters)
            if (u.getNombreUsuario().equals(usuario) && u.getContrasena().equals(contrasena)) {
                return u; // Retorna el objeto Usuario autenticado
            }
        }
        return null; // Credenciales incorrectas o usuario no encontrado
    }
    
    /**
     * Inicializa un conjunto de usuarios con roles predefinidos para facilitar la demostración.
     * Se invoca únicamente si el archivo de datos no existe.
     */
    private void inicializarUsuariosPorDefecto() {
        System.out.println("\n--- [INIT] Creando usuarios por defecto... ---");
        
        // Creación de Objetos: Instanciación de diferentes roles.
        listaUsuarios.add(new Usuario("Admin Coordinador", "admin", "12345", "Coordinador"));
        listaUsuarios.add(new Usuario("Brigadista de Prueba", "brigada", "12345", "Brigadista"));
        listaUsuarios.add(new Usuario("Residente Comunitario", "residente", "12345", "Residente"));
        
        guardarUsuarios(); // Guarda inmediatamente los usuarios iniciales.
        System.out.println("Usuarios por defecto cargados. Contraseña general: 12345.");
        System.out.println("----------------------------------------------\n");
    }

    /**
     * Guarda la lista de usuarios en un archivo mediante Serialización.
     * (Cumple con el requisito de Manejo de Archivos)
     */
    private void guardarUsuarios() {
        // Uso de try-with-resources para asegurar el cierre del ObjectOutputStream
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(listaUsuarios);
        } catch (IOException e) {
            // Manejo de Errores: Captura y notifica un error crítico de E/S.
            System.err.println("ERROR CRÍTICO: No se pudo guardar el archivo de usuarios. Causa: " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace para depuración
        }
    }

    /**
     * Carga la lista de usuarios desde un archivo mediante Deserialización.
     * (Cumple con el requisito de Manejo de Archivos)
     * 
     * @return La lista de objetos Usuario cargada o una lista vacía si el archivo no existe o falla la carga.
     */
    private List<Usuario> cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);
        // Si el archivo no existe o tiene 0 bytes, retorna una lista vacía
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>(); 
        }
        
        // Uso de try-with-resources para asegurar el cierre del ObjectInputStream
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = ois.readObject();
            
            // Manejo de Errores y Seguridad: Valida el tipo de objeto cargado.
            if (obj instanceof List<?>) {
                // Se suprime la advertencia porque el método guardarUsuarios solo escribe List<Usuario>
                @SuppressWarnings("unchecked") 
                List<Usuario> loadedList = (List<Usuario>) obj;
                return loadedList;
            } else {
                 // Si el archivo contiene datos corruptos o de un tipo incorrecto
                 throw new IOException("El contenido del archivo no es una lista de usuarios válida.");
            }
        } catch (IOException | ClassNotFoundException e) {
            // Manejo de Errores: Captura errores de lectura/deserialización.
            System.err.println("ADVERTENCIA: Error al cargar el archivo de usuarios. Se iniciará con lista vacía. Causa: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene la lista completa de usuarios.
     * 
     * @return La lista actual de usuarios en el sistema.
     */
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }
}