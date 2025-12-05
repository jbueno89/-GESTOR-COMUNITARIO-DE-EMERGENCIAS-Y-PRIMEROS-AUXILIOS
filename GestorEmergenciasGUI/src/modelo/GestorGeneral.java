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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gestor UNIFICADO que maneja inventario, reportes, bitácoras y coordinación general.
 * Simplifica la arquitectura al centralizar funcionalidades relacionadas.
 * Demuestra **Arquitectura por Capas**, **Manejo de Archivos** y **Reglas de Negocio**.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class GestorGeneral implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final String ARCHIVO_SUMINISTROS = "suministros.dat";
    private static final String ARCHIVO_BITACORAS = "bitacoras.dat";
    
    /**
     * Lista de suministros del inventario.
     */
    private List<Suministro> inventario;
    
    /**
     * Lista de bitácoras (registro de acciones para auditoría).
     */
    private List<String> bitacoras;
    
    /**
     * Referencia al gestor de incidentes para coordinación.
     */
    private GestorIncidentes gestorIncidentes;
    
    /**
     * Referencia al gestor de brigadistas para coordinación.
     */
    private GestorBrigadistas gestorBrigadistas;

    /**
     * Constructor del gestor general.
     * Inicializa todos los componentes y carga datos persistidos.
     */
    public GestorGeneral() {
        this.inventario = cargarInventario();
        this.bitacoras = cargarBitacoras();
        this.gestorIncidentes = new GestorIncidentes();
        this.gestorBrigadistas = new GestorBrigadistas();
        
        // Inicializa inventario por defecto si está vacío
        if (inventario.isEmpty()) {
            inicializarInventarioDefault();
        }
        
        registrarBitacora("Sistema", "GestorGeneral inicializado correctamente");
        System.out.println("[GestorGeneral] Sistema iniciado - Inventario: " + inventario.size() + 
                         " items | Bitácoras: " + bitacoras.size());
    }

    // =========================================================================
    // GESTIÓN DE INVENTARIO
    // =========================================================================

    /**
     * Registra un nuevo suministro en el inventario.
     * * @param nombre Nombre del suministro
     * @param stockInicial Cantidad inicial
     * @param stockMinimo Stock mínimo crítico
     * @param unidad Unidad de medida
     * @param ubicacion Ubicación física
     * @return El suministro creado
     */
    public Suministro agregarSuministro(String nombre, int stockInicial, int stockMinimo,
                                       String unidad, String ubicacion) {
        Suministro nuevo = new Suministro(nombre, stockInicial, stockMinimo, unidad, ubicacion);
        inventario.add(nuevo);
        guardarInventario();
        
        registrarBitacora("Admin", "Suministro agregado: " + nombre + " (Stock: " + stockInicial + ")");
        return nuevo;
    }

    /**
     * Registra una entrada de stock (aumenta cantidad).
     * * @param idSuministro ID del suministro
     * @param cantidad Cantidad a agregar
     */
    public void registrarEntrada(int idSuministro, int cantidad) {
        Suministro suministro = buscarSuministroPorId(idSuministro);
        if (suministro == null) {
            throw new IllegalArgumentException("Suministro #" + idSuministro + " no encontrado.");
        }
        
        suministro.agregarStock(cantidad);
        guardarInventario();
        
        registrarBitacora("Inventario", "Entrada: " + cantidad + " " + suministro.getUnidad() + 
                        " de " + suministro.getNombre());
    }

    /**
     * Registra una salida de stock (disminuye cantidad).
     * * @param idSuministro ID del suministro
     * @param cantidad Cantidad a retirar
     * @return true si se retiró exitosamente
     */
    public boolean registrarSalida(int idSuministro, int cantidad) {
        Suministro suministro = buscarSuministroPorId(idSuministro);
        if (suministro == null) {
            throw new IllegalArgumentException("Suministro #" + idSuministro + " no encontrado.");
        }
        
        boolean exito = suministro.retirarStock(cantidad);
        if (exito) {
            guardarInventario();
            registrarBitacora("Inventario", "Salida: " + cantidad + " " + suministro.getUnidad() + 
                            " de " + suministro.getNombre());
        }
        
        return exito;
    }

    /**
     * Obtiene todos los suministros del inventario.
     * @return Lista de suministros
     */
    public List<Suministro> obtenerTodosSuministros() {
        return new ArrayList<>(inventario);
    }

    /**
     * Busca un suministro por su ID.
     * @param id ID del suministro
     * @return El suministro encontrado o null
     */
    public Suministro buscarSuministroPorId(int id) {
        return inventario.stream()
            .filter(s -> s.getId() == id)
            .findFirst()
            .orElse(null);
    }

    /**
     * Busca suministros por nombre (búsqueda parcial).
     * @param nombre Nombre o parte del nombre
     * @return Lista de suministros que coinciden
     */
    public List<Suministro> buscarSuministrosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return obtenerTodosSuministros();
        }
        
        String nombreBusqueda = nombre.toLowerCase();
        return inventario.stream()
            .filter(s -> s.getNombre().toLowerCase().contains(nombreBusqueda))
            .collect(Collectors.toList());
    }

    /**
     * Obtiene suministros en estado crítico (stock bajo).
     * Implementa **Reglas de Negocio** para alertas automáticas.
     * * @return Lista de suministros críticos
     */
    public List<Suministro> obtenerSuministrosCriticos() {
        return inventario.stream()
            .filter(Suministro::estaCritico)
            .collect(Collectors.toList());
    }

    /**
     * Genera alerta de stock bajo.
     * @return String con el mensaje de alerta o null si no hay alertas
     */
    public String generarAlertaStock() {
        List<Suministro> criticos = obtenerSuministrosCriticos();
        
        if (criticos.isEmpty()) {
            return null;
        }
        
        StringBuilder alerta = new StringBuilder("⚠️ ALERTA DE STOCK CRÍTICO:\n\n");
        for (Suministro s : criticos) {
            alerta.append("• ").append(s.getNombre())
                  .append(": ").append(s.getStockActual()).append(" ").append(s.getUnidad())
                  .append(" (Mínimo: ").append(s.getStockMinimo()).append(")\n");
        }
        
        return alerta.toString();
    }

    // =========================================================================
    // GESTIÓN DE BITÁCORAS (Auditoría)
    // =========================================================================

    /**
     * Registra una acción en la bitácora del sistema.
     * Implementa **Auditoría** para cumplimiento de requisitos.
     * * @param usuario Usuario que realiza la acción
     * @param accion Descripción de la acción
     */
    public void registrarBitacora(String usuario, String accion) {
        String timestamp = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        String registro = "[" + timestamp + "] " + usuario + ": " + accion;
        bitacoras.add(registro);
        guardarBitacoras();
        
        System.out.println("[BITÁCORA] " + registro);
    }
    
    /**
     * Método necesario para la compatibilidad con el código anterior.
     * Llama a la función de registro.
     * * @param usuario Usuario que realiza la acción
     * @param accion Descripción de la acción
     */
    public void reporteBitacora(String usuario, String accion) {
        registrarBitacora(usuario, accion);
    }
    
    /**
     * *** MÉTODO AÑADIDO PARA SOLUCIONAR EL ERROR DE COMPILACIÓN ***
     * Genera un reporte de la bitácora como una cadena de texto para ser mostrada en la interfaz.
     * * @return String con todas las entradas de la bitácora, separadas por nueva línea.
     */
    public String generarReporteBitacora() {
        if (bitacoras.isEmpty()) {
            return "No hay registros de bitácora disponibles.";
        }
        // Utiliza String.join para concatenar la lista de entradas con saltos de línea.
        return String.join("\n", bitacoras);
    }

    /**
     * Obtiene todas las bitácoras registradas.
     * @return Lista de registros de auditoría
     */
    public List<String> obtenerTodasBitacoras() {
        return new ArrayList<>(bitacoras);
    }

    /**
     * Obtiene las últimas N bitácoras.
     * @param cantidad Número de registros a obtener
     * @return Lista con las últimas bitácoras
     */
    public List<String> obtenerUltimasBitacoras(int cantidad) {
        int size = bitacoras.size();
        if (size == 0) return new ArrayList<>();
        
        int inicio = Math.max(0, size - cantidad);
        return new ArrayList<>(bitacoras.subList(inicio, size));
    }

    /**
     * Busca bitácoras por usuario.
     * @param usuario Usuario a buscar
     * @return Lista de bitácoras del usuario
     */
    public List<String> buscarBitacorasPorUsuario(String usuario) {
        return bitacoras.stream()
            .filter(b -> b.contains(usuario + ":"))
            .collect(Collectors.toList());
    }

    // =========================================================================
    // COORDINACIÓN Y REPORTES
    // =========================================================================

    /**
     * Genera un reporte completo del estado del sistema.
     * Demuestra **Generación de Reportes** según requisitos.
     * * @return String con el reporte completo
     */
    public String generarReporteCompleto() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("═══════════════════════════════════════════════\n");
        reporte.append("   REPORTE GENERAL DEL SISTEMA\n");
        reporte.append("═══════════════════════════════════════════════\n\n");
        
        // Sección de Incidentes
        reporte.append("📋 INCIDENTES:\n");
        reporte.append("  • Total: ").append(gestorIncidentes.getTotalIncidentes()).append("\n");
        reporte.append("  • Activos: ").append(gestorIncidentes.contarIncidentesActivos()).append("\n");
        reporte.append("  • Prioridad Alta: ").append(gestorIncidentes.contarIncidentesPrioridadAlta()).append("\n\n");
        
        // Sección de Brigadistas
        int[] estadosBrigadistas = gestorBrigadistas.contarPorEstado();
        reporte.append("👥 BRIGADISTAS:\n");
        reporte.append("  • Total: ").append(gestorBrigadistas.getTotalBrigadistas()).append("\n");
        reporte.append("  • Libres: ").append(estadosBrigadistas[0]).append("\n");
        reporte.append("  • En Servicio: ").append(estadosBrigadistas[1]).append("\n");
        reporte.append("  • Descanso: ").append(estadosBrigadistas[2]).append("\n\n");
        
        // Sección de Inventario
        int criticos = obtenerSuministrosCriticos().size();
        reporte.append("📦 INVENTARIO:\n");
        reporte.append("  • Total Suministros: ").append(inventario.size()).append("\n");
        reporte.append("  • Stock Crítico: ").append(criticos).append("\n\n");
        
        // Últimas bitácoras
        reporte.append("📝 ÚLTIMAS ACCIONES:\n");
        List<String> ultimas = obtenerUltimasBitacoras(5);
        for (String bitacora : ultimas) {
            reporte.append("  ").append(bitacora).append("\n");
        }
        
        reporte.append("\n═══════════════════════════════════════════════\n");
        
        registrarBitacora("Sistema", "Reporte general generado");
        return reporte.toString();
    }

    /**
     * Genera estadísticas de incidentes por tipo.
     * @return Mapa con tipo y cantidad de incidentes
     */
    public Map<String, Integer> obtenerEstadisticasIncidentesPorTipo() {
        List<Incidente> incidentes = gestorIncidentes.obtenerTodosIncidentes();
        Map<String, Integer> stats = new HashMap<>();
        
        for (Incidente inc : incidentes) {
            stats.put(inc.getTipo(), stats.getOrDefault(inc.getTipo(), 0) + 1);
        }
        
        return stats;
    }

    // =========================================================================
    // ACCESO A GESTORES ESPECÍFICOS
    // =========================================================================

    /**
     * Obtiene el gestor de incidentes.
     * @return GestorIncidentes
     */
    public GestorIncidentes getGestorIncidentes() {
        return gestorIncidentes;
    }

    /**
     * Obtiene el gestor de brigadistas.
     * @return GestorBrigadistas
     */
    public GestorBrigadistas getGestorBrigadistas() {
        return gestorBrigadistas;
    }

    // =========================================================================
    // PERSISTENCIA DE DATOS (Manejo de Archivos)
    // =========================================================================

    /**
     * Guarda el inventario en archivo.
     */
    private void guardarInventario() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_SUMINISTROS))) {
            oos.writeObject(inventario);
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo guardar el inventario.");
            e.printStackTrace();
        }
    }

    /**
     * Carga el inventario desde archivo.
     * @return Lista cargada o vacía
     */
    @SuppressWarnings("unchecked")
    private List<Suministro> cargarInventario() {
        File archivo = new File(ARCHIVO_SUMINISTROS);
        if (!archivo.exists()) return new ArrayList<>();
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            List<Suministro> loadedList = (List<Suministro>) ois.readObject();
            
            if (!loadedList.isEmpty()) {
                int maxId = loadedList.stream().mapToInt(Suministro::getId).max().orElse(0);
                Suministro.reiniciarContador(maxId + 1);
            }
            
            return loadedList;
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Guarda las bitácoras en archivo.
     */
    private void guardarBitacoras() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_BITACORAS))) {
            oos.writeObject(bitacoras);
        } catch (IOException e) {
            System.err.println("ERROR: No se pudieron guardar las bitácoras.");
        }
    }

    /**
     * Carga las bitácoras desde archivo.
     * @return Lista cargada o vacía
     */
    @SuppressWarnings("unchecked")
    private List<String> cargarBitacoras() {
        File archivo = new File(ARCHIVO_BITACORAS);
        if (!archivo.exists()) return new ArrayList<>();
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Inicializa suministros por defecto.
     */
    private void inicializarInventarioDefault() {
        inventario.add(new Suministro("Botiquín Primeros Auxilios", 15, 5, "Unidades", "Almacén A"));
        inventario.add(new Suministro("Vendas", 100, 20, "Unidades", "Almacén A"));
        inventario.add(new Suministro("Agua Potable", 200, 50, "Litros", "Almacén B"));
        inventario.add(new Suministro("Linternas", 25, 10, "Unidades", "Almacén C"));
        inventario.add(new Suministro("Mantas Térmicas", 8, 15, "Unidades", "Almacén A"));
        
        guardarInventario();
        System.out.println("[GestorGeneral] Inventario por defecto inicializado.");
    }
}