package controlador;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Suministro;
import servicio.SuministroService;

public class SuministroController {

    private final SuministroService service;

    public SuministroController(SuministroService service) {
        this.service = service;
    }

    public List<Suministro> getAllSuministros() {
        return service.getAll();
    }

    public void agregarSuministro(Suministro s) {
        service.addSuministro(s);
    }

    public void actualizarSuministro(Suministro s) {
        service.actualizarSuministro(s);
    }

    public void eliminarSuministro(int id) {
        service.eliminarSuministro(id);
    }

    // Actualizar tabla completa
    public void actualizarTabla(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Suministro s : service.getAll()) {
            model.addRow(new Object[]{
                s.getId(),
                s.getNombre(),
                s.getStockActual(),
                s.getUnidad(),
                s.getMinimoCritico(),
                s.getUbicacion(),
                s.getFechaCaducidad()
            });
        }
    }

    // Obtener suministro por ID
    public Suministro getSuministroPorId(int id) {
        return service.getAll().stream()
            .filter(s -> s.getId() == id)
            .findFirst().orElse(null);
    }
    
    public void agregarSuministro(Suministro s, JTable table) {
        service.addSuministro(s);
        actualizarTabla(table); // actualiza la tabla automáticamente
    }
    public void actualizarTablaFiltrada(JTable table, String busqueda, String ubicacionFiltro) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Suministro s : service.getAll()) {
            boolean coincideNombre = busqueda == null || s.getNombre().toLowerCase().contains(busqueda.toLowerCase());
            boolean coincideUbicacion = ubicacionFiltro == null || "Almacén".equals(ubicacionFiltro) || s.getUbicacion().equals(ubicacionFiltro);

            if (coincideNombre && coincideUbicacion) {
                model.addRow(new Object[] {
                    s.getId(),
                    s.getNombre(),
                    s.getStockActual(),
                    s.getUnidad(),
                    s.getMinimoCritico(),
                    s.getUbicacion(),
                    s.getFechaCaducidad()
                });
            }
        }
    }
    public int contarSuministrosCriticos() {
        int count = 0;
        for (Suministro s : service.getAll()) {
            if (s.getStockActual() <= s.getMinimoCritico()) { // criterio de crítico
                count++;
            }
        }
        return count;
    }
    }

