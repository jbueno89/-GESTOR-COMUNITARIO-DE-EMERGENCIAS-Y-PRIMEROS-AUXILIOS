/**
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package modelo;

/**
 * Clase que representa un suministro de emergencia en el inventario.
 * Demuestra **HERENCIA** al extender de RecursoEmergencia y **POLIMORFISMO**
 * al implementar los métodos abstractos de la clase padre.
 * 
 * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class Suministro extends RecursoEmergencia {

    private static final long serialVersionUID = 1L;
    
    // Contador estático para IDs únicos
    private static int contadorId = 1;
    
    /**
     * Cantidad actual en stock.
     */
    private int stockActual;
    
    /**
     * Cantidad mínima antes de alertar (stock crítico).
     */
    private int stockMinimo;
    
    /**
     * Unidad de medida (Ej: "Unidades", "Kg", "Litros").
     */
    private String unidad;
    
    /**
     * Ubicación física del suministro.
     */
    private String ubicacion;
    
    /**
     * Fecha de caducidad (opcional, puede ser null).
     */
    private String fechaCaducidad;

    /**
     * Constructor completo para crear un nuevo suministro.
     * 
     * @param nombre Nombre del suministro
     * @param stockActual Cantidad inicial
     * @param stockMinimo Stock mínimo crítico
     * @param unidad Unidad de medida
     * @param ubicacion Ubicación física
     */
    public Suministro(String nombre, int stockActual, int stockMinimo, 
                     String unidad, String ubicacion) {
        super(contadorId++, nombre); // Llama al constructor de la clase padre
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.unidad = unidad;
        this.ubicacion = ubicacion;
        this.fechaCaducidad = "N/A";
        
        // Actualiza la disponibilidad según el stock
        actualizarDisponibilidad();
    }

    // =========================================================================
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (POLIMORFISMO)
    // =========================================================================

    /**
     * Verifica si el suministro está disponible (tiene stock suficiente).
     * Implementa el método abstracto de RecursoEmergencia.
     * 
     * @return true si hay stock, false en caso contrario
     */
    @Override
    public boolean estaDisponible() {
        return stockActual > 0;
    }

    /**
     * Asigna una cantidad del suministro a una emergencia.
     * Implementa el método abstracto de RecursoEmergencia.
     * 
     * @param cantidad Cantidad a asignar (se pasa como idEmergencia por compatibilidad)
     * @return true si la asignación fue exitosa
     */
    @Override
    public boolean asignarAEmergencia(int cantidad) {
        if (cantidad > stockActual) {
            System.err.println("ERROR: Stock insuficiente. Disponible: " + stockActual);
            return false;
        }
        
        stockActual -= cantidad;
        actualizarDisponibilidad();
        
        System.out.println("[Suministro #" + id + "] " + cantidad + " " + unidad + 
                         " asignadas. Stock actual: " + stockActual);
        return true;
    }

    /**
     * Libera el suministro (en este caso, repone stock).
     * Implementa el método abstracto de RecursoEmergencia.
     */
    @Override
    public void liberar() {
        // Para suministros, "liberar" no aplica de la misma forma
        // pero se implementa por compatibilidad
        System.out.println("[Suministro #" + id + "] Estado actualizado.");
    }

    /**
     * Obtiene información detallada del suministro.
     * Implementa el método abstracto de RecursoEmergencia.
     * 
     * @return String con los detalles
     */
    @Override
    public String obtenerDetalles() {
        return String.format("Suministro: %s | Stock: %d %s | Mínimo: %d | Ubicación: %s | Caducidad: %s",
                           nombre, stockActual, unidad, stockMinimo, ubicacion, fechaCaducidad);
    }

    // =========================================================================
    // MÉTODOS ESPECÍFICOS DE SUMINISTRO
    // =========================================================================

    /**
     * Agrega stock al suministro (entrada de inventario).
     * Demuestra **Lógica de Negocio** para control de inventario.
     * 
     * @param cantidad Cantidad a agregar
     * @throws IllegalArgumentException Si la cantidad es negativa
     */
    public void agregarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        
        stockActual += cantidad;
        actualizarDisponibilidad();
        
        System.out.println("[Suministro #" + id + "] Se agregaron " + cantidad + 
                         " " + unidad + ". Stock actual: " + stockActual);
    }

    /**
     * Retira stock del suministro (salida de inventario).
     * 
     * @param cantidad Cantidad a retirar
     * @return true si se retiró exitosamente, false si no hay suficiente stock
     */
    public boolean retirarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
        
        if (cantidad > stockActual) {
            System.err.println("ERROR: No hay suficiente stock. Disponible: " + stockActual);
            return false;
        }
        
        stockActual -= cantidad;
        actualizarDisponibilidad();
        
        System.out.println("[Suministro #" + id + "] Se retiraron " + cantidad + 
                         " " + unidad + ". Stock actual: " + stockActual);
        return true;
    }

    /**
     * Verifica si el stock está en nivel crítico.
     * Implementa **Reglas de Negocio** para alertas.
     * 
     * @return true si el stock está por debajo del mínimo
     */
    public boolean estaCritico() {
        return stockActual <= stockMinimo;
    }

    /**
     * Verifica si el suministro está por vencer (simulado).
     * @return true si está cerca de vencer
     */
    public boolean estaPorVencer() {
        // Implementación simplificada - en un sistema real compararía fechas
        return fechaCaducidad != null && !fechaCaducidad.equals("N/A");
    }

    /**
     * Actualiza el estado de disponibilidad según el stock.
     */
    private void actualizarDisponibilidad() {
        setDisponible(stockActual > 0);
    }

    // =========================================================================
    // GETTERS Y SETTERS
    // =========================================================================

    /**
     * Obtiene el stock actual.
     * @return Stock actual
     */
    public int getStockActual() {
        return stockActual;
    }

    /**
     * Obtiene el stock mínimo.
     * @return Stock mínimo
     */
    public int getStockMinimo() {
        return stockMinimo;
    }

    /**
     * Establece el stock mínimo.
     * @param stockMinimo Nuevo stock mínimo
     */
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /**
     * Obtiene la unidad de medida.
     * @return Unidad
     */
    public String getUnidad() {
        return unidad;
    }

    /**
     * Obtiene la ubicación.
     * @return Ubicación
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Establece la ubicación.
     * @param ubicacion Nueva ubicación
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Obtiene la fecha de caducidad.
     * @return Fecha de caducidad
     */
    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    /**
     * Establece la fecha de caducidad.
     * @param fechaCaducidad Nueva fecha (formato: "DD/MM/YYYY")
     */
    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * Establece el ID del suministro (usado al cargar desde archivo).
     * @param id Nuevo ID
     */
    public void setId(int id) {
        this.id = id;
        if (id >= contadorId) {
            contadorId = id + 1;
        }
    }

    /**
     * Reinicia el contador de IDs.
     * @param nuevoContador Nuevo valor
     */
    public static void reiniciarContador(int nuevoContador) {
        contadorId = nuevoContador;
    }

    /**
     * Obtiene el contador actual.
     * @return Contador actual
     */
    public static int getContadorActual() {
        return contadorId;
    }

    // =========================================================================
    // MÉTODO TOSTRING (POLIMORFISMO - sobrescribe el de RecursoEmergencia)
    // =========================================================================

    /**
     * Representación en cadena del suministro.
     * Sobrescribe el toString() de RecursoEmergencia (POLIMORFISMO).
     * 
     * @return String con información del suministro
     */
    @Override
    public String toString() {
        String estado = estaCritico() ? " ⚠️ CRÍTICO" : "";
        return "Suministro #" + id + ": " + nombre + 
               " | Stock: " + stockActual + " " + unidad + estado;
    }
}