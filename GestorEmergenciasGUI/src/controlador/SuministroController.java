/*
 * ***************************************************************
 * GRUPO 10
 * GESTOR COMUNITARIO DE EMERGENCIAS Y PRIMEROS AUXILIOS
 * Jose Miguel Bueno Martinez - 20251020093
 * Anyelo Esteban Casas Zapata - 20251020106
 * ***************************************************************
 */
package controlador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import java.awt.Color;

import modelo.Suministro;
import servicio.SuministroService;

/**
 * Controlador para la gestión y manipulación de Suministros.
 * Es responsable de coordinar la vista (JTable) con la lógica de negocio (SuministroService)
 * para operaciones CRUD, filtrado y marcado visual de ítems críticos.
 * Demuestra el patrón **MVC (Controlador)**, **Arquitectura por Capas** y manipulación de la UI
 * mediante el uso de {@link DefaultTableCellRenderer}.
 * * @author Jose Miguel Bueno Martinez
 * @author Anyelo Esteban Casas Zapata
 */
public class SuministroController {

    /**
     * Servicio que maneja la persistencia y la lógica de negocio de los suministros.
     */
    private final SuministroService service;
    
    /**
     * Lista en memoria de todos los suministros para manipulación rápida y filtrado local.
     */
    private List<Suministro> suministros;

    /**
     * Constructor del controlador. Inicializa el servicio y carga la lista de suministros.
     * @param service La instancia de {@link SuministroService} inyectada.
     */
    public SuministroController(SuministroService service) {
        this.service = service;
        this.suministros = new ArrayList<>(service.getAll());
    }

    // ====================================================================
    // CONSULTAS Y PERSISTENCIA (CRUD)
    // ====================================================================

    /**
     * Obtiene una copia de la lista completa de suministros en memoria.
     * @return Una nueva {@link List} que contiene todos los suministros.
     */
    public List<Suministro> getAllSuministros() {
        return new ArrayList<>(suministros);
    }

    /**
     * Sincroniza la lista en memoria con el servicio y fuerza la persistencia de todos los datos.
     * Es un método de control interno para garantizar que los cambios locales se guarden en archivo.
     */
    private void guardarCambios() {
        service.setSuministros(suministros); // actualiza la lista completa en el servicio
        service.guardarSuministros();        // guarda en el archivo
    }

    /**
     * Agrega un nuevo suministro al sistema. Si el ID es 0, delega la creación al servicio.
     * Luego actualiza la lista local y la tabla de la UI.
     * @param s El objeto {@link Suministro} a agregar.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void agregarSuministro(Suministro s, JTable table) {
        // Si el ID es 0, se genera automáticamente usando el servicio
        if (s.getId() == 0) {
            service.addSuministro(s); // esto asigna un ID único y guarda en archivo
        } else {
            suministros.add(s);
            guardarCambios();
        }
        // Aseguramos que la lista local tenga todos los suministros actualizados
        this.suministros = new ArrayList<>(service.getAll());
        actualizarTabla(table);
    }

    /**
     * Actualiza los datos de un suministro existente en la lista, guarda los cambios
     * y refresca la tabla.
     * @param s El objeto {@link Suministro} con los datos actualizados.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void actualizarSuministro(Suministro s, JTable table) {
        for (int i = 0; i < suministros.size(); i++) {
            if (suministros.get(i).getId() == s.getId()) {
                suministros.set(i, s);
                break;
            }
        }
        guardarCambios();
        actualizarTabla(table);
        aplicarColorCritico(table);
    }

    /**
     * Elimina un suministro por su ID.
     * Luego actualiza la lista local y la tabla de la UI.
     * @param id El ID del suministro a eliminar.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void eliminarSuministro(int id, JTable table) {
        suministros.removeIf(s -> s.getId() == id);
        service.eliminarSuministro(id);
        // Refrescar lista local después de eliminar
        this.suministros = new ArrayList<>(service.getAll());
        actualizarTabla(table);
        aplicarColorCritico(table);
    }

    // ====================================================================
    // MANEJO DE VISTA (JTABLE)
    // ====================================================================

    /**
     * Limpia y rellena la tabla de la UI con la lista completa de suministros en memoria.
     * Aplica el renderizado de color crítico.
     * @param table El componente {@link JTable} a rellenar.
     */
    public void actualizarTabla(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpia la tabla

        for (Suministro s : suministros) {
            model.addRow(new Object[]{
                s.getId(),
                s.getNombre(),
                s.getStockActual(),
                s.getUnidad(),
                s.getMinimoCritico(),
                s.getUbicacion(),
                s.getFechaCaducidad(),
                s.isCritico() ? "CRÍTICO" : ""
            });
        }
        aplicarColorCritico(table);
    }

    /**
     * Limpia y rellena la tabla de la UI aplicando filtros de búsqueda por nombre y ubicación.
     * Aplica el renderizado de color crítico.
     * @param table El componente {@link JTable} a rellenar.
     * @param busqueda Cadena de texto para buscar en el nombre del suministro.
     * @param ubicacionFiltro Filtro exacto por ubicación.
     */
    public void actualizarTablaFiltrada(JTable table, String busqueda, String ubicacionFiltro) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Suministro s : suministros) {
            // Lógica de filtrado
            boolean coincideNombre = busqueda == null || s.getNombre().toLowerCase().contains(busqueda.toLowerCase());
            boolean coincideUbicacion = ubicacionFiltro == null || "Almacén".equals(ubicacionFiltro) || s.getUbicacion().equals(ubicacionFiltro);

            if (coincideNombre && coincideUbicacion) {
                model.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getStockActual(),
                    s.getUnidad(),
                    s.getMinimoCritico(),
                    s.getUbicacion(),
                    s.getFechaCaducidad(),
                    s.isCritico() ? "CRÍTICO" : ""
                });
            }
        }
        aplicarColorCritico(table);
    }

    /**
     * Aplica un renderizador a la tabla para colorear de naranja las filas
     * correspondientes a suministros marcados como críticos.
     * Demuestra **Lógica de Presentación** en el Controlador.
     * @param table El componente {@link JTable} al que se le aplicará el renderizador.
     */
    public void aplicarColorCritico(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Obtener el ID del suministro en la fila actual
                int id = (int) table.getValueAt(row, 0);
                Suministro s = suministros.stream().filter(su -> su.getId() == id).findFirst().orElse(null);

                // Aplicar coloración si es crítico
                if (s != null && s.isCritico()) {
                    c.setBackground(Color.ORANGE);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }
                return c;
            }
        });
    }

    // ====================================================================
    // UTILIDADES Y ESTADÍSTICAS
    // ====================================================================

    /**
     * Marca un suministro específico como crítico y persiste el cambio.
     * @param id El ID del suministro a marcar.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void marcarSuministroCritico(int id, JTable table) {
        Suministro s = suministros.stream().filter(su -> su.getId() == id).findFirst().orElse(null);
        if (s != null) {
            s.setCritico(true);
            guardarCambios();
            actualizarTabla(table);
        }
    }

    /**
     * Cuenta el número total de suministros que están marcados como críticos.
     * Demuestra el uso de la API Stream para estadísticas.
     * @return El número de suministros críticos.
     */
    public int contarSuministrosCriticos() {
        return (int) suministros.stream().filter(Suministro::isCritico).count();
    }

    /**
     * Obtiene un suministro específico de la lista en memoria por su ID.
     * @param id El ID del suministro a buscar.
     * @return El objeto {@link Suministro} encontrado, o {@code null} si no existe.
     */
    public Suministro getSuministroPorId(int id) {
        return suministros.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    /**
     * Ejecuta un reset completo: limpia la lista en memoria y en el servicio (archivo de persistencia),
     * y refresca la tabla de la UI.
     * @param table El componente {@link JTable} a actualizar.
     */
    public void resetSuministros(JTable table) {
        suministros.clear();
        service.resetSuministros();
        // Recargar la lista local para asegurar consistencia (estará vacía)
        this.suministros = new ArrayList<>(service.getAll());
        actualizarTabla(table);
    }
}