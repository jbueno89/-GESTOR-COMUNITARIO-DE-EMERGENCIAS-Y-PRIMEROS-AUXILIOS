package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Suministro implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;                 
    private String nombre;          
    private int stockActual;        
    private String unidad;          
    private int minimoCritico;      
    private String ubicacion;       
    private LocalDate fechaCaducidad;

    // Constructor completo con ID
    public Suministro(int id, String nombre, int stockActual, String unidad,
                      int minimoCritico, String ubicacion, LocalDate fechaCaducidad) {
        this.id = id;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.unidad = unidad;
        this.minimoCritico = minimoCritico;
        this.ubicacion = ubicacion;
        this.fechaCaducidad = fechaCaducidad;
    }

    // Constructor sin ID (para crear un suministro nuevo; el ID se asigna automáticamente)
    public Suministro(String nombre, int stock, String unidad, int minCritico, String ubicacion, LocalDate fechaCaducidad) {
        this.nombre = nombre;
        this.stockActual = stock;
        this.unidad = unidad;
        this.minimoCritico = minCritico;
        this.ubicacion = ubicacion;
        this.fechaCaducidad = fechaCaducidad;
    }

    // ---------- Getters ----------
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getStockActual() { return stockActual; }
    public String getUnidad() { return unidad; }
    public int getMinimoCritico() { return minimoCritico; }
    public String getUbicacion() { return ubicacion; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }

    // ---------- Setters ----------
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public void setMinimoCritico(int minimoCritico) { this.minimoCritico = minimoCritico; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}

