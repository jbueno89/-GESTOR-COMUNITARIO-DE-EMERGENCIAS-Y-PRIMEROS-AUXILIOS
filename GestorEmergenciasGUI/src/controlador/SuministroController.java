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

public class SuministroController {

    private final SuministroService service;
    private List<Suministro> suministros; // lista en memoria para manipulación rápida

    public SuministroController(SuministroService service) {
        this.service = service;
        this.suministros = new ArrayList<>(service.getAll());
    }

    // ---------------------- Obtener lista completa ----------------------
    public List<Suministro> getAllSuministros() {
        return new ArrayList<>(suministros);
    }

    // ---------------------- Guardar cambios de toda la lista ----------------------
    private void guardarCambios() {
        service.setSuministros(suministros); // actualiza la lista completa en el servicio
        service.guardarSuministros();        // guarda en el archivo
    }

    // ---------------------- Agregar suministro ----------------------
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

    // ---------------------- Actualizar suministro ----------------------
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

    // ---------------------- Eliminar suministro ----------------------
    public void eliminarSuministro(int id, JTable table) {
        suministros.removeIf(s -> s.getId() == id);
        service.eliminarSuministro(id);
        // Refrescar lista local después de eliminar
        this.suministros = new ArrayList<>(service.getAll());
        actualizarTabla(table);
        aplicarColorCritico(table);
    }

    // ---------------------- Actualizar tabla completa ----------------------
    public void actualizarTabla(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

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

    // ---------------------- Actualizar tabla filtrada ----------------------
    public void actualizarTablaFiltrada(JTable table, String busqueda, String ubicacionFiltro) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Suministro s : suministros) {
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

    // ---------------------- Aplicar color a los críticos ----------------------
    public void aplicarColorCritico(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                int id = (int) table.getValueAt(row, 0);
                Suministro s = suministros.stream().filter(su -> su.getId() == id).findFirst().orElse(null);

                if (s != null && s.isCritico()) {
                    c.setBackground(Color.ORANGE);
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                }
                return c;
            }
        });
    }

    // ---------------------- Marcar suministro como crítico ----------------------
    public void marcarSuministroCritico(int id, JTable table) {
        Suministro s = suministros.stream().filter(su -> su.getId() == id).findFirst().orElse(null);
        if (s != null) {
            s.setCritico(true);
            guardarCambios();
            actualizarTabla(table);
        }
    }

    // ---------------------- Contar suministros críticos ----------------------
    public int contarSuministrosCriticos() {
        return (int) suministros.stream().filter(Suministro::isCritico).count();
    }

    // ---------------------- Obtener suministro por ID ----------------------
    public Suministro getSuministroPorId(int id) {
        return suministros.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
    }

    // ---------------------- Reset completo ----------------------
    public void resetSuministros(JTable table) {
        suministros.clear();
        service.resetSuministros();
        this.suministros = new ArrayList<>(service.getAll());
        actualizarTabla(table);
    }
}



