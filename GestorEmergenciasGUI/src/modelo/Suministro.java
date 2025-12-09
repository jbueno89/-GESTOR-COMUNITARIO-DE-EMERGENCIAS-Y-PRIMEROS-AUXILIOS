/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package modelo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa un artículo o recurso de suministro gestionado por el sistema.
 * Contiene información sobre la cantidad, ubicación, fecha de caducidad y estado crítico.
 * Implementa {@link Serializable} para permitir la **persistencia** (guardado en archivos).
 * Demuestra **Encapsulamiento** y **Manejo de Estados**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 * @version 2.0
 * @since 2024-10-01
 */
public class Suministro implements Serializable {
    
    /**
     * Identificador único para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del suministro.
     */
    private int id;                 
    
    /**
     * Nombre del artículo o recurso (ej. "Botiquín", "Linterna").
     */
    private String nombre;          
    
    /**
     * Cantidad actual disponible en stock.
     */
    private int stockActual;        
    
    /**
     * Unidad de medida (ej. "unidades", "litros", "paquetes").
     */
    private String unidad;          
    
    /**
     * Nivel mínimo de stock para considerar el suministro como crítico.
     */
    private int minimoCritico;      
    
    /**
     * Ubicación de almacenamiento (ej. "Almacén Central", "Vehículo 1").
     */
    private String ubicacion;       
    
    /**
     * Fecha en la que el suministro caduca. Se utiliza {@link LocalDate} para manejar fechas sin hora.
     */
    private LocalDate fechaCaducidad;
    
    /**
     * Bandera que indica si el suministro está actualmente marcado como crítico,
     * usualmente si el stock actual es menor o igual al mínimo crítico.
     */
    private boolean critico;

    // ====================================================================
    // CONSTRUCTORES
    // ====================================================================

    /**
     * Constructor completo para inicializar un suministro con un ID predefinido.
     * @param id Identificador único.
     * @param nombre Nombre del suministro.
     * @param stockActual Cantidad en stock.
     * @param unidad Unidad de medida.
     * @param minimoCritico Cantidad mínima para el estado crítico.
     * @param ubicacion Ubicación de almacenamiento.
     * @param fechaCaducidad Fecha de caducidad.
     */
    public Suministro(int id, String nombre, int stockActual, String unidad,
                      int minimoCritico, String ubicacion, LocalDate fechaCaducidad) {
        this.id = id;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.unidad = unidad;
        this.minimoCritico = minimoCritico;
        this.ubicacion = ubicacion;
        this.fechaCaducidad = fechaCaducidad;
        this.critico = false; // Estado inicial no crítico por defecto
    }

    /**
     * Constructor para crear un suministro nuevo, donde el ID será asignado automáticamente
     * por el servicio de persistencia (típicamente usado al crear un objeto en la UI).
     * @param nombre Nombre del suministro.
     * @param stock Cantidad en stock.
     * @param unidad Unidad de medida.
     * @param minCritico Cantidad mínima para el estado crítico.
     * @param ubicacion Ubicación de almacenamiento.
     * @param fechaCaducidad Fecha de caducidad.
     */
    public Suministro(String nombre, int stock, String unidad, int minCritico, String ubicacion, LocalDate fechaCaducidad) {
        this.nombre = nombre;
        this.stockActual = stock;
        this.unidad = unidad;
        this.minimoCritico = minCritico;
        this.ubicacion = ubicacion;
        this.fechaCaducidad = fechaCaducidad;
        this.critico = false; // Estado inicial no crítico por defecto
    }

    // ====================================================================
    // GETTERS (Encapsulamiento)
    // ====================================================================
    
    /**
     * Obtiene el ID del suministro.
     * @return El ID.
     */
    public int getId() { return id; }
    
    /**
     * Obtiene el nombre del suministro.
     * @return El nombre.
     */
    public String getNombre() { return nombre; }
    
    /**
     * Obtiene la cantidad actual en stock.
     * @return La cantidad de stock.
     */
    public int getStockActual() { return stockActual; }
    
    /**
     * Obtiene la unidad de medida.
     * @return La unidad (ej. "unidades").
     */
    public String getUnidad() { return unidad; }
    
    /**
     * Obtiene el nivel de stock mínimo para considerarse crítico.
     * @return El mínimo crítico.
     */
    public int getMinimoCritico() { return minimoCritico; }
    
    /**
     * Obtiene la ubicación de almacenamiento.
     * @return La ubicación.
     */
    public String getUbicacion() { return ubicacion; }
    
    /**
     * Obtiene la fecha de caducidad.
     * @return La fecha de caducidad como {@link LocalDate}.
     */
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    
    /**
     * Verifica si el suministro está marcado como crítico.
     * @return {@code true} si está marcado como crítico.
     */
    public boolean isCritico() { return critico; }

    // ====================================================================
    // SETTERS (Encapsulamiento)
    // ====================================================================
    
    /**
     * Establece el ID del suministro.
     * @param id El nuevo ID.
     */
    public void setId(int id) { this.id = id; }
    
    /**
     * Establece el nombre del suministro.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    /**
     * Establece la cantidad actual en stock.
     * @param stockActual La nueva cantidad.
     */
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    
    /**
     * Establece la unidad de medida.
     * @param unidad La nueva unidad.
     */
    public void setUnidad(String unidad) { this.unidad = unidad; }
    
    /**
     * Establece el nivel de stock mínimo para considerarse crítico.
     * @param minimoCritico El nuevo mínimo.
     */
    public void setMinimoCritico(int minimoCritico) { this.minimoCritico = minimoCritico; }
    
    /**
     * Establece la ubicación de almacenamiento.
     * @param ubicacion La nueva ubicación.
     */
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    /**
     * Establece la fecha de caducidad.
     * @param fechaCaducidad La nueva fecha de caducidad.
     */
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
    
    /**
     * Establece si el suministro debe ser marcado como crítico.
     * @param critico {@code true} para marcar como crítico, {@code false} en caso contrario.
     */
    public void setCritico(boolean critico) { this.critico = critico; }
}