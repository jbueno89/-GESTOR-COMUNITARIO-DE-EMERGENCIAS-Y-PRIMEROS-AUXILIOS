/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package modelo;

import java.io.Serializable;

/**
 * Representa la clase base para cualquier usuario que acceda al sistema.
 * Implementa Serializable para permitir la persistencia de datos en archivos
 * (Manejo de Archivos) y demuestra el concepto de **Encapsulamiento**
 * al tener atributos privados accesibles solo por métodos públicos (Getters/Setters).
 * 
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class Usuario implements Serializable { 
    
    private static final long serialVersionUID = 1L; 
    
    /**
     * Nombre completo del usuario.
     */
    private String nombreCompleto;
    
    /**
     * Nombre de usuario único para el login.
     */
    private String nombreUsuario; 
    
    /**
     * Contraseña del usuario (texto plano solo para fines académicos).
     */
    private String contrasena; 
    
    /**
     * Rol que define los permisos del usuario.
     */
    private String rol; 

    /**
     * Constructor completo para crear un nuevo usuario.
     * 
     * @param nombreCompleto Nombre real del usuario.
     * @param nombreUsuario Nombre único para el login.
     * @param contrasena Contraseña del usuario (se asume en texto plano solo para fines académicos/simulación).
     * @param rol Rol que define los permisos (ej. "Coordinador", "Brigadista", "Residente").
     */
    public Usuario(String nombreCompleto, String nombreUsuario, String contrasena, String rol) {
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // -----------------------------------------------------------------
    // GETTERS (Lectura de propiedades) - Cumplimiento de Encapsulamiento
    // -----------------------------------------------------------------

    /**
     * Obtiene el nombre completo del usuario.
     * @return El nombre completo.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Obtiene el nombre de usuario (usado para el login).
     * @return El nombre de usuario.
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña.
     */
    public String getContrasena() {
        return contrasena;
    }
    
    /**
     * Obtiene el rol asignado al usuario.
     * @return El rol (ej. "Coordinador").
     */
    public String getRol() {
        return rol;
    }

    // -----------------------------------------------------------------
    // SETTERS (Modificación de propiedades) - Cumplimiento de Encapsulamiento
    // -----------------------------------------------------------------

    /**
     * Establece o actualiza el nombre completo del usuario.
     * @param nombreCompleto El nuevo nombre completo.
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Establece o actualiza el nombre de usuario.
     * @param nombreUsuario El nuevo nombre de usuario.
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Establece o actualiza la contraseña del usuario.
     * @param contrasena La nueva contraseña.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Establece o actualiza el rol del usuario.
     * @param rol El nuevo rol.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    // -----------------------------------------------------------------
    // MÉTODO TOSTRING (Útil para depuración y Muestra de Resultados)
    // -----------------------------------------------------------------
    
    /**
     * Devuelve una representación en cadena del objeto Usuario.
     * @return String con los detalles del usuario.
     */
    @Override
    public String toString() {
        return "Usuario [nombreCompleto=" + nombreCompleto + ", nombreUsuario=" + nombreUsuario + ", rol=" + rol + "]";
    }
}