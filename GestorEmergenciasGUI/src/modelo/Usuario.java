package modelo;

import java.io.Serializable;

public class Usuario implements Serializable { 
    
    private static final long serialVersionUID = 1L; 
    
    private String nombreCompleto;
    private String nombreUsuario; 
    private String contrasena; // NOTA: En una aplicación real, esta debería ser hasheada y no almacenada en texto plano.
    private String rol; 

    /**
     * Constructor completo para crear un nuevo usuario.
     */
    public Usuario(String nombreCompleto, String nombreUsuario, String contrasena, String rol) {
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // -----------------------------------------------------------------
    // GETTERS (Lectura de propiedades)
    // -----------------------------------------------------------------

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        // Se mantiene para el método validarCredenciales en GestorUsuarios.
        return contrasena;
    }
    
    public String getRol() {
        return rol;
    }

    // -----------------------------------------------------------------
    // SETTERS (Modificación de propiedades)
    // -----------------------------------------------------------------

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        // Se permite el cambio de contraseña.
        this.contrasena = contrasena;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    // -----------------------------------------------------------------
    // MÉTODO TOSTRING (Útil para depuración)
    // -----------------------------------------------------------------
    
    @Override
    public String toString() {
        return "Usuario [nombreCompleto=" + nombreCompleto + ", nombreUsuario=" + nombreUsuario + ", rol=" + rol + "]";
    }
}