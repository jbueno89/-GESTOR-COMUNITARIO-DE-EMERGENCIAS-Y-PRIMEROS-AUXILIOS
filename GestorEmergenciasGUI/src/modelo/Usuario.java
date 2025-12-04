package modelo;

import java.io.Serializable;

public class Usuario implements Serializable { 
    
    private static final long serialVersionUID = 1L; 
    
    private String nombreCompleto;
    private String nombreUsuario; 
    private String contrasena; 
    private String rol; 

    // Constructor
    public Usuario(String nombreCompleto, String nombreUsuario, String contrasena, String rol) {
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters necesarios para la autenticación y el Panel Principal
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }
    
    public String getRol() {
        return rol;
    }

    // Puedes añadir más getters si los necesitas...
}